package org.benetech.servicenet.scheduler;

import static org.benetech.servicenet.config.Constants.EDEN_PROVIDER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EdenTaxonomyUpdateJob extends AbstractICarolTaxonomyUpdateJob {

    private static final String NAME = "Eden Taxonomy Update Job";
    private static final String DESCRIPTION = "Collect Eden Taxonomies through iCarol API,"
        + " map it to the common structure and persist it to the database";

    @Value("${scheduler.eden-taxonomies.interval}")
    private int intervalInSeconds;

    @Value("${scheduler.eden-data.api-key}")
    private String edenApiKey;

    @Override
    protected String getApiKey() {
        return edenApiKey;
    }

    @Override
    protected String getProviderName() {
        return EDEN_PROVIDER;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getFullName() {
        return NAME;
    }

    @Override
    public int getIntervalInSeconds() {
        return intervalInSeconds;
    }
}
