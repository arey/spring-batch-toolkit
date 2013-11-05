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

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test of the {@link CreateElasticIndexMappingTasklet}
 * 
 * @author Antoine
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:com/javaetmoi/core/batch/tasklet/applicationContext-elasticsearch.xml"})
public class TestCreateElasticIndexMappingTasklet {

    private static final String              INDEX     = "bank";
    private static final String              TYPE = "customer";

    @Autowired
    private Client                           client;

    private CreateElasticIndexMappingTasklet tasklet;

    @Before
    public void setUp() throws InterruptedException, ExecutionException {
        tasklet = new CreateElasticIndexMappingTasklet();
        tasklet.setEsClient(client);

        assertTrue(client.admin().indices().create(new CreateIndexRequest(INDEX)).get().isAcknowledged());
    }

    @After
    public void tearDown() throws InterruptedException, ExecutionException {
        client.admin().indices().delete(new DeleteIndexRequest(INDEX)).get();
    }

    @Test
    public void executeTasklet() throws Exception {
        IndicesAdminClient admin = client.admin().indices();
        assertFalse(admin.typesExists(new TypesExistsRequest(new String[] { INDEX }, TYPE)).get().isExists());

        Resource resource = new ClassPathResource(
                "com/javaetmoi/core/batch/tasklet/TestCreateElasticIndexMappingTasklet-mapping.json");
        tasklet.setIndexMapping(resource);
        tasklet.setIndexName(INDEX);
        tasklet.setMappingType(TYPE);
        tasklet.execute(null, null);
        
        assertTrue(admin.typesExists(new TypesExistsRequest(new String[] { INDEX }, TYPE)).get().isExists());

    }

}
