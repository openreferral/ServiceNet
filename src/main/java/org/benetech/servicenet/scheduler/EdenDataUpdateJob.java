package org.benetech.servicenet.scheduler;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.eden.model.TakeAllRequest;
import org.benetech.servicenet.util.HttpUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class EdenDataUpdateJob extends BaseJob {

    private static final String NAME = "Eden Data Update Job";

    private static final String DESCRIPTION = "Collect Data thought iCarol API, map it to the common structure and " +
        "persist it to the database";
    private static final String URL = "https://api.icarol.com/v1/Resource/Search";

    @Value("${scheduler.interval.eden-data-update}")
    private int intervalInSeconds;

    @Value("${scheduler.interval.eden-api-key}")
    private String edenApiKey;

    private final Logger log = LoggerFactory.getLogger(EdenDataUpdateJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(NAME + " is being executed");

        Map<String, String> headers = HttpUtils.getStandardHeaders(edenApiKey);

        TakeAllRequest takeAllRequest = new TakeAllRequest(getLastJobExeutionDate());
        String body = new Gson().toJson(takeAllRequest);

        try {
            String response = HttpUtils.executePOST(URL, body, headers);
        } catch (IOException e ) {
            throw new IllegalStateException("Cannot connect with iCarol API");
        }

        //TODO persist the data from response
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

    private String getLastJobExeutionDate() {
        //TODO: handle this
        return "2018-12-19";
    }
}
