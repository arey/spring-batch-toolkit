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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;

import javax.annotation.PostConstruct;

/**
 * Wait the end of of job execution before routing it.
 *
 * <p>
 * Exit status is used to choose the returned channel.<br>
 * Default error and success channel (ie "job-error" and "job-success")  are defined, but may be customized.<br>
 * Failed and Stopped jobs are routed to the "job-error" channel.<br/>
 * The router check the JobExecution statuts every 500 ms. This interval could be changed.
 * </p>
 */
@MessageEndpoint
public class JobExitStatusRouter {

    private static Logger LOGGER = LoggerFactory.getLogger(JobExitStatusRouter.class);


    private String errorChannel;

    private String successChannel;

    private Integer pollingInterval;

    @PostConstruct
    public void init() {
        if (errorChannel == null) {
            errorChannel = "job-error";
            LOGGER.debug("Default error channel has been set to {}", errorChannel);
        }
        if (successChannel == null) {
            successChannel = "job-success";
            LOGGER.debug("Default success channel has been set to {}", successChannel);
        }
        if (pollingInterval == null) {
            pollingInterval = 500;
            LOGGER.debug("Default polling interval has been set to {}", pollingInterval);
        }
    }

    @Router
    public String route(JobExecution jobExecution)  {
        while (jobExecution.getStatus().isRunning()) {
            LOGGER.debug("Still running job {} execution end ...", jobExecution.getJobId());
            try {
                Thread.sleep(pollingInterval);
            } catch (InterruptedException e) {
                LOGGER.warn("Router has been interrupted before job execution ending", e);
                return errorChannel;
            }
        }
        LOGGER.debug("Job {} exit status: {}", jobExecution.getJobId(), jobExecution.getExitStatus());
        if (jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            return successChannel;
        }
        return errorChannel;
    }

    public String getErrorChannel() {
        return errorChannel;
    }

    public void setErrorChannel(String errorChannel) {
        this.errorChannel = errorChannel;
    }

    public String getSuccessChannel() {
        return successChannel;
    }

    public void setSuccessChannel(String successChannel) {
        this.successChannel = successChannel;
    }


    public Integer getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(Integer pollingInterval) {
        this.pollingInterval = pollingInterval;
    }
}
