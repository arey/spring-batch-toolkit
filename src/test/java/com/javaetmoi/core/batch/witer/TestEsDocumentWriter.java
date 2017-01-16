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
package com.javaetmoi.core.batch.witer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.javaetmoi.core.batch.processor.EsDocumentProcessor;
import com.javaetmoi.core.batch.tasklet.ElasticSearchHelper;
import com.javaetmoi.core.batch.writer.EsDocument;
import com.javaetmoi.core.batch.writer.EsDocumentWriter;


/**
 * Unit test of the {@link EsDocumentWriter}
 * 
 * @author Antoine
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:com/javaetmoi/core/batch/tasklet/applicationContext-elasticsearch.xml"})
public class TestEsDocumentWriter {
    
    private static final String              INDEX     = "bank";
    
    private static final String              TYPE = "customer";

    @Autowired
    private Client                           client;

    private EsDocumentWriter writer;

    @Before
    public void setUp() throws InterruptedException, ExecutionException {
        writer = new EsDocumentWriter();
        writer.setEsClient(client);
        writer.setIndexName(INDEX);

        assertTrue(client.admin().indices().create(new CreateIndexRequest(INDEX)).get().isAcknowledged());
    }
    
    @After
    public void tearDown() throws InterruptedException, ExecutionException {
        client.admin().indices().delete(new DeleteIndexRequest(INDEX)).get();
    }    
    
    @Test
    public void write() throws Exception {
        CustomerProcessor processor = new CustomerProcessor();
        
        List<EsDocument> documents = new ArrayList<EsDocument>();
        documents.add(processor.process(new Customer("Max", 30)));
        documents.add(processor.process(new Customer("James", 52)));
        writer.write(documents);
        
        ElasticSearchHelper.refreshIndex(client, INDEX);
        IndicesStatsResponse response = client.admin().indices().stats(new IndicesStatsRequest().types(INDEX)).get();
        assertEquals(2, response.getIndex(INDEX).getTotal().getDocs().getCount());
    }
    
    static class Customer {
        String name;
        int age;
        Customer(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
    
    
    static class CustomerProcessor extends EsDocumentProcessor<Customer> {

        @Override
        protected String getDocumentType(Customer item) {
            return TYPE;
        }

        @Override
        protected void fillContentBuilder(XContentBuilder contentBuilder, Customer item) throws IOException {
            contentBuilder.field("name", item.name);
            contentBuilder.field("age", item.age);
        }
    }

}
