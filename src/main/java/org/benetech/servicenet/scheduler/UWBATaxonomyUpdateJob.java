package org.benetech.servicenet.scheduler;

import static org.benetech.servicenet.config.Constants.UWBA_PROVIDER;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UWBATaxonomyUpdateJob extends AbstractICarolTaxonomyUpdateJob {

    private static final String NAME = "UWBA Taxonomy Update Job";
    private static final String DESCRIPTION = "Collect UWBA Taxonomies through iCarol API,"
        + " map it to the common structure and persist it to the database";

    @Value("${scheduler.uwba-taxonomies.interval}")
    private int intervalInSeconds;

    @Value("${scheduler.uwba-data.api-key}")
    private String uwbaApiKey;

    @Override
    protected String getApiKey() {
        return uwbaApiKey;
    }

    @Override
    protected String getProviderName() {
        return UWBA_PROVIDER;
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
