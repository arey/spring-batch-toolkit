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
package com.javaetmoi.core.batch.processor;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.batch.item.ItemProcessor;

import com.javaetmoi.core.batch.writer.EsDocument;

/**
 * Process a Domain Object to an {@link EsDocument} in order it could be stored into Elasticsearch.
 * 
 * @param <T>
 *            Domain Object to index
 */
public abstract class EsDocumentProcessor<T> implements ItemProcessor<T, EsDocument> {

    @Override
    public EsDocument process(T item) throws Exception {
        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();
        fillContentBuilder(contentBuilder, item);
        contentBuilder.endObject();
        EsDocument document = new EsDocument(getDocumentType(item), contentBuilder);
        document.setId(getDocumentId(item));
        return document;
    }

    /**
     * Retrieves ID of the Document to store.
     * <p>
     * Could be overriden to provide a Document ID.
     * </p>
     * 
     * @param item
     *            object to store into Elasticsearch.
     * @return (may be <code>null</code> for auto-generated ID)
     */
    protected String getDocumentId(T item) {
        return null;
    }

    /**
     * Retrieves Type of the Document to store.
     * 
     * @param item
     *            object to store into Elasticsearch.
     * @return type of the document to index
     */
    abstract protected String getDocumentType(T item);

    /**
     * Map the Domain Object to store into an Elasticsearch structure.
     * 
     * @param contentBuilder
     *            Elasticsearch builder to complete with Domain Object properties to index
     * @param item
     *            object to index into Elasticsearch.
     * @throws IOException
     */
    abstract protected void fillContentBuilder(XContentBuilder contentBuilder, T item) throws IOException;

}
