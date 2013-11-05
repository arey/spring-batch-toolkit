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

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test of the {@link DeleteElasticIndexTasklet}
 * 
 * @author Antoine
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:com/javaetmoi/core/batch/tasklet/applicationContext-elasticsearch.xml"})
public class TestDeleteElasticIndexTasklet {


    private static final String INDEX = "bank";

    @Autowired
    private Client                           client;

    private DeleteElasticIndexTasklet tasklet;

    @Before
    public void setUp()  {
        tasklet = new DeleteElasticIndexTasklet();
        tasklet.setEsClient(client);
    }


    @Test
    public void executeTaskletWithExistingIndex() throws Exception {
        IndicesAdminClient admin = client.admin().indices();
        assertTrue(client.admin().indices().create(new CreateIndexRequest(INDEX)).get().isAcknowledged());

        tasklet.setIndexName(INDEX);
        tasklet.execute(null, null);
        
        assertFalse(admin.exists(new IndicesExistsRequest(INDEX)).get().isExists());
    }
    
    @Test
    public void executeTaskletWithUnexistingIndex() throws Exception {
        tasklet.setIndexName(INDEX);
        tasklet.execute(null, null);
    }

}
