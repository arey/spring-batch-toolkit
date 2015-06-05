package com.javaetmoi.core.batch.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.io.File;

@MessageEndpoint
public class RollbackProcessedFileServiceActivator {

    private static Logger LOGGER = LoggerFactory.getLogger(RollbackProcessedFileServiceActivator.class);

    @Autowired
    private AcceptOnceFilePerJobListFilter acceptOnceCatalogListFilter;

    @ServiceActivator
    public void rollback(File file) throws NoSuchJobException {
        acceptOnceCatalogListFilter.rollback(file);
        LOGGER.info("File '{}' could be re-processed", file.getName());
    }

    public void setAcceptOnceCatalogListFilter(AcceptOnceFilePerJobListFilter acceptOnceCatalogListFilter) {
        this.acceptOnceCatalogListFilter = acceptOnceCatalogListFilter;
    }
}
