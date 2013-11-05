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

import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * JSON document which is stored in Elasticsearch.
 * 
 * <p>
 * A document is stored in an <b>index</b> and has a <b>type</b> and an <b>id</b>.<br>
 * If no id is provided, Elasticsearch well auto-generated one.<br>
 * A document indexed in elasticsearch could be versioned.
 * </p>
 */
public class EsDocument {

    private String          id;

    private String          type;

    private Long            version;

    private XContentBuilder contentBuilder;

    /**
     * EsDocument constructor.
     * 
     * @param type
     *            type of the Elasticsearch document
     * @param contentBuilder
     *            Elasticsearch helper to generate JSON content.
     */
    public EsDocument(String type, XContentBuilder contentBuilder) {
        this.type = type;
        this.contentBuilder = contentBuilder;
    }

    protected String getId() {
        return id;
    }

    /**
     * Set the ID of a document which identifies a document.
     * 
     * @param id
     *            ID of a document (may be <code>null</code>)
     */
    public void setId(String id) {
        this.id = id;
    }

    protected XContentBuilder getContentBuilder() {
        return contentBuilder;
    }

    protected String getType() {
        return type;
    }

    /**
     * Sets the version, which will cause the index operation to only be performed if a matching
     * version exists and no changes happened on the doc since then.
     * 
     * @param version
     *            version of a document
     * @see http://www.elasticsearch.org/blog/versioning/
     */
    protected void setVersion(Long version) {
        this.version = version;
    }
    
    protected boolean isVersioned() {
        return version !=null;
    }

    public Long getVersion() {
        return version;
    }

}
