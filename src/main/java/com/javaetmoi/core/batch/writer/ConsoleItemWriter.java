package com.javaetmoi.core.batch.writer;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

/**
 * Logs each content item by reflection with the {@link ToStringBuilder#reflectionToString(Object)}
 * method.
 * 
 * @author Antoine
 * 
 * @param <T>
 */
public class ConsoleItemWriter<T> implements ItemWriter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsoleItemWriter.class);

    @Override
    public void write(List<? extends T> items) throws Exception {
        LOG.trace("Console item writer starts");
        for (T item : items) {
            LOG.info(ToStringBuilder.reflectionToString(item));
        }
        LOG.trace("Console item writer ends");

    }
}
