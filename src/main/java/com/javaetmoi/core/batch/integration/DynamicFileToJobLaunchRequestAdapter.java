package com.javaetmoi.core.batch.integration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.io.File;

/**
 * Adapt a {@link File} to a {@link JobLaunchRequest} with a job parameter
 * <code>input.file</code> equal to the path of the file.
 * <p>
 * The Job to launch is dynamic. It depends of the file attributes (directory, date, name, owner ...) according to your convention.
 * </p>
 */
@MessageEndpoint
public class DynamicFileToJobLaunchRequestAdapter {

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private FileToJobNameConverter fileToJobConverter;


    @ServiceActivator
    public JobLaunchRequest adapt(File file) throws NoSuchJobException {

        String jobName = fileToJobConverter.getJobNameFromFile(file);
        Job job = jobLocator.getJob(jobName);

        String fileName = file.getAbsolutePath();

        if (!fileName.startsWith("/")) {
            fileName = "/" + fileName;
        }

        fileName = "file://" + fileName;

        JobParameters jobParameters = new JobParametersBuilder().addString(
                "input.file", fileName).toJobParameters();

        if (job.getJobParametersIncrementer() != null) {
            jobParameters = job.getJobParametersIncrementer().getNext(jobParameters);
        }

        return new JobLaunchRequest(job, jobParameters);

    }
}
