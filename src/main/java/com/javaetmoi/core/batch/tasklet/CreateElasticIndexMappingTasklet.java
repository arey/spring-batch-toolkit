/**
 * Copyright 2013 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.javaetmoi.core.batch.tasklet;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;


/**
 * Add an Elasticsearch mapping definition for a type into one index.
 *
 * <p>
 * Mapping definition should be provided as JSON or YAML file / resource.
 * </p>
 *
 */
public class CreateElasticIndexMappingTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(CreateElasticIndexMappingTasklet.class);

    private Client esClient;

    private String indexName;

    private Resource indexMapping;

    private String mappingType;

    private XContentType contentType = XContentType.JSON;

    @PostConstruct
    public void afterPropertiesSet() {
        Assert.notNull(esClient, "esClient must not be null");
        Assert.notNull(indexName, "indexName must not be null");
        Assert.notNull(indexMapping, "indexMapping must not be null");
        Assert.notNull(mappingType, "mappingType must not be null");
        Assert.notNull(contentType, "contentType must not be null");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LOG.debug("Creating the {} mapping type for the index {}", mappingType, indexName);

        String source = IOUtils.toString(indexMapping.getInputStream(), "UTF-8");
        PutMappingRequestBuilder request = esClient.admin().indices().preparePutMapping(indexName);
        request.setSource(source, contentType).setType(mappingType);
        PutMappingResponse response = request.execute().actionGet();
        if (!response.isAcknowledged()) {
            throw new RuntimeException("The index mappings has not been acknowledged");
        }

        ElasticSearchHelper.refreshIndex(esClient, indexName);

        LOG.info("{} mapping type created for the index {}", StringUtils.capitalize(mappingType), indexName);
        return RepeatStatus.FINISHED;

    }

    /**
     * Sets the Elasticsearch client used to defined index mapping.
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
     * Sets the JSON resource defining index mapping.
     * <p>
     * File has to be encoded in UTF-8.
     *
     * @param indexMapping
     *            Spring resource descriptor, such as a file or class path resource.
     */
    public void setIndexMapping(Resource indexMapping) {
        this.indexMapping = indexMapping;
    }

    /**
     * Sets the type of the Elasticsearch mapping definition.
     *
     * @param mappingType
     *            type of the mapping definition
     */
    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    /**
     * Set the Content type of the Elasticsearch mapping file.
     *
     * @param contentType JSON (default) or YAML
     */
    public void setContentType(XContentType contentType) {
        this.contentType = contentType;
    }

}
