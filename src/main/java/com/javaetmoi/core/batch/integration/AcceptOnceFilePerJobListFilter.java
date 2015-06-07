package com.javaetmoi.core.batch.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.integration.file.filters.AbstractFileListFilter;
import org.springframework.integration.file.filters.ReversibleFileListFilter;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Blocks launch of the same job twice.
 * <p>
 * This {@see FileListFilter} could be use in a use case where a file triggers the execution of a Spring Batch job.<br>
 * When a received file has launched a job, all other files which triggers the same job are keep in a waiting list until the job is finished.<br>
 * Do not forget to call the {@see RollbackProcessedFileServiceActivator} at the end of the job execution.
 * </p>
 */
public class AcceptOnceFilePerJobListFilter extends AbstractFileListFilter<File> implements ReversibleFileListFilter<File> {

    private static Logger LOGGER = LoggerFactory.getLogger(JobExitStatusRouter.class);

    private final Set<String> acceptedJobSet = new HashSet<String>();

    private final Object monitor = new Object();

    private FileToJobNameConverter fileToJobNameConverter;

    @Override
    public boolean accept(File file) {
        String jobName;
        try {
            jobName = getJobName(file);
        } catch (NoSuchJobException ex) {
            LOGGER.warn(ex.getMessage(), ex);
            return false;
        }
        synchronized (this.monitor) {
            if (acceptedJobSet.contains(jobName)) {
                return false;
            }
            acceptedJobSet.add(jobName);
            return true;
        }
    }

    public void rollback(File file) throws NoSuchJobException {
        synchronized (this.monitor) {
            this.acceptedJobSet.remove(getJobName(file));
        }
    }

    @Override
    public void rollback(File file, List<File> files) {
        try {
            rollback(file);
        } catch (NoSuchJobException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private String getJobName(File file) throws NoSuchJobException {
        return fileToJobNameConverter.getJobNameFromFile(file);
    }

    public void setFileToJobNameConverter(FileToJobNameConverter fileToJobNameConverter) {
        this.fileToJobNameConverter = fileToJobNameConverter;
    }
}
