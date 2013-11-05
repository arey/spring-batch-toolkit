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

import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

/**
 * Elasticsearch helper class providing some methods hiding the Elasticsearch Java client API.
 */
public class ElasticSearchHelper {

    private ElasticSearchHelper() {
        // Utility class
    }

    /**
     * Execute a refresh request making all operations performed since the last refresh available
     * for search.
     * 
     * @param esClient
     *            Elasticsearch client
     * @param indices
     *            index list to refresh
     */
    public static void refreshIndex(Client esClient, String... indices) {
        AdminClient admin = esClient.admin();
        IndicesAdminClient indicesAdmin = admin.indices();
        RefreshRequest refreshRequest = new RefreshRequest(indices);
        try {
            indicesAdmin.refresh(refreshRequest).get();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected error while Elasticsearch refresh request", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Unexpected error while Elasticsearch refresh request", e);
        }
    }

}
