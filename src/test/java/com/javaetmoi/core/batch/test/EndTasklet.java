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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.support.DefaultStepExecutionAggregator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Display the masterpieces number that have been processed by unpartitioned steps.
 * 
 */
public class EndTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(EndTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        StepExecution resume = getUnpartitionedStepResume(jobExecution);
        LOG.info("{} masterpiece(s) have been processed", resume.getWriteCount());
        return RepeatStatus.FINISHED;
    }

    private StepExecution getUnpartitionedStepResume(JobExecution jobExecution) {
        StepExecution result = new StepExecution("resume", jobExecution);
        new DefaultStepExecutionAggregator().aggregate(result,
                getUnpartitionedStepExecution(jobExecution));
        return result;
    }

    private List<StepExecution> getUnpartitionedStepExecution(JobExecution jobExecution) {
        List<StepExecution> unpartitionedStepExecution = new ArrayList<StepExecution>();
        Collection<StepExecution> allStepExecutions = jobExecution.getStepExecutions();
        for (StepExecution stepExecution : allStepExecutions) {
            if (!stepExecution.getStepName().contains(":partition")) {
                unpartitionedStepExecution.add(stepExecution);
            }
        }
        return unpartitionedStepExecution;
    }

}
