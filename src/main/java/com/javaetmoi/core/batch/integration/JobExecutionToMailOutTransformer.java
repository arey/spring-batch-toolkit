package com.javaetmoi.core.batch.integration;

import org.springframework.batch.core.JobExecution;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;

import java.io.File;
import java.io.IOException;

/**
 * Transformer that accepts a Message whose payload is a JobExecution launched with an input.file parameter.
 * <p>
 * Mail body is build with an optional introduction string followed by JobExecution properties.
 * </p>
 */
@MessageEndpoint
public class JobExecutionToMailOutTransformer {

    public String mailIntroduction;

    public String lineSeparator = System.getProperty("line.separator");

    @Transformer
    public String toMailBody(JobExecution jobExecution) throws IOException {
        String inputFile = jobExecution.getJobParameters().getString("input.file");
        if (inputFile.contains("file://")) {
            inputFile = inputFile.replace("file://", "");
        }
        String filename = new File(inputFile).getName();

        StringBuilder body = new StringBuilder();
        if (mailIntroduction != null) {
            body.append(mailIntroduction).append(lineSeparator).append(lineSeparator);
        }
        body.append("Filename: ").append(filename).append("").append(lineSeparator);
        body.append("Cause: ").append(jobExecution.getAllFailureExceptions()).append(lineSeparator);
        body.append("Job Id: ").append(jobExecution.getJobId()).append(lineSeparator);
        body.append("Start time: ").append(jobExecution.getStartTime()).append(lineSeparator);
        body.append("End time: ").append(jobExecution.getEndTime()).append(lineSeparator);
        body.append(lineSeparator);
        return body.toString();
    }

    public void setMailIntroduction(String mailIntroduction) {
        this.mailIntroduction = mailIntroduction;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }
}
