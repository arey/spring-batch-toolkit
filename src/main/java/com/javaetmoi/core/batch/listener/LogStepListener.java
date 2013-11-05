/**
 * Copyright 2013 the original author or authors.
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
package com.javaetmoi.core.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * Spring Batch listener logging several informations during step execution.
 * 
 * <p>
 * Logs are written through SLF4J.<br>
 * This bean should be declared with <strong>step</strong> scope in order to reference it's
 * {@link StepExecution}.
 * </p>
 * 
 */
public class LogStepListener<T, S> extends StepListenerSupport<T, S> {

    private static final Logger LOG = LoggerFactory.getLogger(LogStepListener.class);

    private StepExecution       stepExecution;

    private Integer             commitInterval;

    @Override
    public void beforeStep(StepExecution pStepExecution) {
        this.stepExecution = pStepExecution;
    }

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        if (commitInterval != null) {
            LOG.trace("Step {} - Reading next {} items", stepExecution.getStepName(), commitInterval);
        }
    }

    @Override
    public void beforeWrite(List<? extends S> items) {
        LOG.trace("Step {} - Writing {} items", stepExecution.getStepName(), items.size());
    }

    @Override
    public void afterWrite(List<? extends S> items) {
        if ((items != null) && !items.isEmpty()) {
            LOG.trace("Step {} - {} items writed", stepExecution.getStepName(), items.size());
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution pStepExecution) {
        if (LOG.isInfoEnabled()) {
            StringBuilder msg = new StringBuilder();
            msg.append("Step ").append(pStepExecution.getStepName());
            msg.append(" - Read count: ").append(pStepExecution.getReadCount());
            msg.append(" - Write count: ").append(pStepExecution.getWriteCount());
            msg.append(" - Commit count: ").append(pStepExecution.getCommitCount());
            LOG.info(msg.toString());
        }
        return super.afterStep(pStepExecution);
    }

    /**
     * Sets the chunk commit interval
     * 
     * @param commitInterval
     *            chunk commit interval (may be <code>null</code>)
     */
    public void setCommitInterval(Integer commitInterval) {
        this.commitInterval = commitInterval;
    }

}
