package com.javaetmoi.core.batch.integration;

import org.springframework.batch.core.launch.NoSuchJobException;

import java.io.File;

/**
 * Retrieve a job name from a file.
 */
public interface FileToJobNameConverter {

    /**
     * Retrieve a job name from an input file.
     * <p>
     * Filename and a regex could be used to extract job name.
     * </p>
     *
     * @param file file that triggers the job
     * @return name of the job to launch
     * @throws NoSuchJobException when file does not match with any naming rules
     */
    String getJobNameFromFile(File file) throws NoSuchJobException;
}
