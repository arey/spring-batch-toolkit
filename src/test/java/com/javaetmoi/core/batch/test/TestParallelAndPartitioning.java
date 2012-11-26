/**
 * Copyright 2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.javaetmoi.core.batch.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.ExecutionContextTestUtils;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.javaetmoi.core.batch.partition.ColumnRangePartitioner;

/**
 * This test shows how to use partitioning steps into a parallel step.
 * <p>
 * The partitioning uses the {@link ColumnRangePartitioner} that finds the minimum and maximum
 * primary keys in the music_album table to obtain a count of rows and then calculates the number of
 * rows in the partition.
 * </p>
 * 
 * @author Antoine
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TestParallelAndPartitioning extends AbstractSpringBatchTest {

    @Autowired
    private JobLauncherTestUtils testUtils;

    @Test
    public void launchJob() throws Exception {
        // Launch the parallelAndPartitioningJob
        JobExecution execution = testUtils.launchJob();

        // Batch Status
        assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());

        // Movies
        assertEquals("8 movies", 8, getStepExecution(execution, "stepLogMovie").getWriteCount());

        // Music Albums
        StepExecution stepExecutionMusic = getStepExecution(execution, "stepLogMusicAlbum");
        assertEquals("11 music albums", 11, stepExecutionMusic.getWriteCount());
        Object gridSize = ExecutionContextTestUtils.getValueFromStep(stepExecutionMusic,
                "SimpleStepExecutionSplitter.GRID_SIZE");
        assertEquals("stepLogMusicAlbum divided into 2 partitions", 2L, gridSize);

        StepExecution stepExecPart0 = getStepExecution(execution,
                "stepLogMusicAlbumPartition:partition0");
        assertEquals("First partition processed 6 music albums", 6, stepExecPart0.getWriteCount());
        StepExecution stepExecPart1 = getStepExecution(execution,
                "stepLogMusicAlbumPartition:partition1");
        assertEquals("Second partition processed 5 music albums", 5, stepExecPart1.getWriteCount());
    }

    private StepExecution getStepExecution(JobExecution jobExecution, String stepName) {
        StepExecution stepExecution = null;
        List<String> stepNames = new ArrayList<String>();
        for (StepExecution candidate : jobExecution.getStepExecutions()) {
            String name = candidate.getStepName();
            stepNames.add(name);
            if (name.equals(stepName)) {
                stepExecution = candidate;
            }
        }
        if (stepExecution == null) {
            throw new IllegalArgumentException("No such step in this job execution: " + stepName
                    + " not in " + stepNames);
        }
        return stepExecution;
    }

}
