/**
 * Copyright 2015 the original author or authors.
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
package com.javaetmoi.core.batch.integration;

import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;

import static org.junit.Assert.assertEquals;

public class TestJobExitStatusRouter {

    @Test
    public void routeToErrorChannel() {
        JobExitStatusRouter router = new JobExitStatusRouter();
        router.init();
        JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setExitStatus(ExitStatus.FAILED);
        jobExecution.setStatus(BatchStatus.FAILED);
        assertEquals("job-error", router.route(jobExecution));
    }

    @Test
    public void routeToSuccessChannel() {
        JobExitStatusRouter router = new JobExitStatusRouter();
        router.init();
        JobExecution jobExecution = new JobExecution(1L);
        jobExecution.setExitStatus(ExitStatus.COMPLETED);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        assertEquals("job-success", router.route(jobExecution));
    }


}
