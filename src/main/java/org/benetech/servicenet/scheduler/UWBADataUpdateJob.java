package org.benetech.servicenet.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UWBADataUpdateJob extends AbstractICarolUpdateJob {

    private static final String NAME = "UWBA Data Update Job";
    private static final String SYSTEM_ACCOUNT = "UWBA";
    private static final String DESCRIPTION = "Collect UWBA Data thought iCarol API, map it to the common structure and " +
        "persist it to the database";

    @Value("${scheduler.uwba-data.interval}")
    private int intervalInSeconds;

    @Value("${scheduler.uwba-data.api-key}")
    private String uwbaApiKey;

    @Override
    protected String getApiKey() {
        return uwbaApiKey;
    }

    @Override
    protected String getSystemAccount() {
        return SYSTEM_ACCOUNT;
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
