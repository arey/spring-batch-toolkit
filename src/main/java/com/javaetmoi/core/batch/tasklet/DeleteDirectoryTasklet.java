package com.javaetmoi.core.batch.tasklet;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

/**
 * Simple tasklet that deletes a directory recursively.
 * <p>
 * The directory to delete could be dynamically set from job context with an SpEL.<br/>
 * </p>
 */
public class DeleteDirectoryTasklet implements Tasklet {

    private String directoryPath;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {
        FileUtils.deleteDirectory(new File(directoryPath));
        contribution.incrementWriteCount(1);
        return RepeatStatus.FINISHED;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
