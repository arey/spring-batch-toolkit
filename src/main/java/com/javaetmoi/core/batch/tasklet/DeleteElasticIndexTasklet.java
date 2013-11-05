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
package com.javaetmoi.core.batch.tasklet;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.indices.IndexMissingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;

/**
 * Delete an Elasticsearch index if exists.
 * 
 */
public class DeleteElasticIndexTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteElasticIndexTasklet.class);

    private Client              esClient;

    private String              indexName;

    @PostConstruct
    public void afterPropertiesSet() {
        Assert.notNull(esClient, "esClient must not be null");
        Assert.notNull(indexName, "indexName must not be null");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            DeleteIndexResponse response = esClient.admin().indices().prepareDelete(indexName).execute().actionGet();

            if (!response.isAcknowledged()) {
                throw new RuntimeException("Index deletion has not been acknowledged");
            }
            LOG.info("Index {} deleted", indexName);
        } catch (IndexMissingException e) {
            LOG.info("The index {} does not exist. Thus nothing to delete.", indexName);
        }
        return RepeatStatus.FINISHED;
    }

    /**
     * Sets the Elasticsearch client used to delete the index.
     * 
     * @param esClient
     *            Elasticsearch client
     */
    public void setEsClient(Client esClient) {
        this.esClient = esClient;
    }

    /**
     * Sets the name of the index to delete.
     * 
     * @param indexName
     *            name of the Elasticsearch index
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

}
