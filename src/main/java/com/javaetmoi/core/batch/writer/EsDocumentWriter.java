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
package com.javaetmoi.core.batch.writer;

import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.util.Assert;


/**
 * Index several documents in a single bulk request.
 */
public class EsDocumentWriter implements ItemWriter<EsDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(EsDocumentWriter.class);

    private Client              esClient;

    private String              indexName;

    private Long                timeout;

    @PostConstruct
    public void afterPropertiesSet() {
        Assert.notNull(esClient, "esClient must not be null");
        Assert.notNull(indexName, "indexName must not be null");
    }

    @Override
    public final void write(List<? extends EsDocument> documents) throws Exception {
        BulkRequestBuilder bulkRequest = esClient.prepareBulk();
        for (EsDocument doc : documents) {
            IndexRequestBuilder request = esClient.prepareIndex(indexName, doc.getType()).setSource(
                    doc.getContentBuilder());
            request.setId(doc.getId());
            if (doc.isVersioned()) {
                request.setVersion(doc.getVersion());
            }
            bulkRequest.add(request);
        }
        BulkResponse response;
        if (timeout != null) {
            response = bulkRequest.execute().actionGet(timeout);
        } else {
            response = bulkRequest.execute().actionGet();
        }
        processResponse(response);
    }

    private void processResponse(BulkResponse response) {
        if (response.hasFailures()) {
            String failureMessage = response.getItems()[0].getFailureMessage();
            throw new ElasticSearchException("Bulk request failed. First failure message: " + failureMessage);
        }
        LOG.info("{} documents indexed into ElasticSearch in {} ms", response.getItems().length,
                response.getTookInMillis());
    }

    /**
     * Sets the Elasticsearch client used for bulk request.
     * 
     * @param esClient
     *            Elasticsearch client
     */
    public void setEsClient(Client esClient) {
        this.esClient = esClient;
    }

    /**
     * Sets the name of the index where documents will be stored.
     * 
     * @param indexName
     *            name of the Elasticsearch index
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    /**
     * Waits if necessary for at most the given time for the computation to complete, and then
     * retrieves its result, if available.
     * 
     * @param timeout
     *            the maximum time in milliseconds to wait
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

}
