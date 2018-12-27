package org.benetech.servicenet.scheduler;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.eden.model.TakeAllRequest;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.util.HttpUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class EdenDataUpdateJob extends BaseJob {

    private final Logger log = LoggerFactory.getLogger(EdenDataUpdateJob.class);

    private static final String NAME = "Eden Data Update Job";
    private static final String DESCRIPTION = "Collect Data thought iCarol API, map it to the common structure and " +
        "persist it to the database";
    private static final String URL = "https://api.icarol.com/v1/Resource/Search";
    private static final String SYSTEM_ACCOUNT = "Eden";

    @Value("${scheduler.interval.eden-data-update}")
    private int intervalInSeconds;

    @Value("${scheduler.interval.eden-api-key}")
    private String edenApiKey;

    @Autowired
    private DocumentUploadService documentUploadService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(NAME + " is being executed");

        Map<String, String> headers = HttpUtils.getStandardHeaders(edenApiKey);

        TakeAllRequest takeAllRequest = new TakeAllRequest(getLastJobExecutionDate());
        String body = new Gson().toJson(takeAllRequest);

        String response;
        try {
            response = HttpUtils.executePOST(URL, body, headers);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with iCarol API");
        }

        documentUploadService.uploadApiData(response, SYSTEM_ACCOUNT);
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

    private String getLastJobExecutionDate() {
        //TODO: return real date
        return "2018-12-19";
    }
}
