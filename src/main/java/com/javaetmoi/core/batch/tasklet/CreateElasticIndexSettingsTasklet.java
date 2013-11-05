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

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Creates an Elasticsearch index from a JSON settings file.
 */
public class CreateElasticIndexSettingsTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(CreateElasticIndexSettingsTasklet.class);

    private Client              esClient;

    private String              indexName;

    private Resource            indexSettings;
    
    @PostConstruct
    public void afterPropertiesSet() {
        Assert.notNull(esClient, "esClient must not be null");
        Assert.notNull(indexName, "indexName must not be null");
        Assert.notNull(indexSettings, "indexSettings must not be null");
    }    

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LOG.debug("Creating the index {} settings", indexName);
        
        String source = IOUtils.toString(indexSettings.getInputStream(), "UTF-8");
        CreateIndexRequestBuilder createIndexReq = esClient.admin().indices().prepareCreate(indexName);
        createIndexReq.setSettings(source);
        CreateIndexResponse response = createIndexReq.execute().actionGet();
        if (!response.isAcknowledged()) {
            throw new RuntimeException("The index settings has not been acknowledged");
        }

        esClient.admin().indices().refresh(new RefreshRequest(indexName)).actionGet();

        LOG.info("Index {} settings created", indexName);
        return RepeatStatus.FINISHED;
    }

    /**
     * Sets the Elasticsearch client used to defined index settings.
     * 
     * @param esClient
     *            Elasticsearch client
     */
    public void setEsClient(Client esClient) {
        this.esClient = esClient;
    }

    /**
     * Sets the name of the index where documents will be stored
     * 
     * @param indexName
     *            name of the Elasticsearch index
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    
    /**
     * Sets the JSON resource defining index settings.
     * 
     * @param indexSettings
     *            Spring resource descriptor, such as a file or class path resource.
     */    
    public void setIndexSettings(Resource indexSettings) {
        this.indexSettings = indexSettings;
    }
}
