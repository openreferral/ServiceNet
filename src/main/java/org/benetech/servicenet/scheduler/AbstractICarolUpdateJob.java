package org.benetech.servicenet.scheduler;

import com.google.gson.Gson;
import java.util.Date;
import org.apache.http.Header;
import org.benetech.servicenet.adapter.icarol.model.ICarolTakeAllRequest;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.util.HttpUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractICarolUpdateJob extends BaseJob {

    private final Logger log = LoggerFactory.getLogger(AbstractICarolUpdateJob.class);

    private static final String URL = "https://api.icarol.com/v1/Resource/Search";

    @Autowired
    private DocumentUploadService documentUploadService;

    @Autowired
    private DataImportReportService dataImportReportService;

    @Override
    public Date getStartTime() {
        return getOffsetDate(60 * 60);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(getFullName() + " is being executed");

        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now()).jobName(getFullName());
        Header[] headers = HttpUtils.getStandardAuthHeaders(getApiKey());

        ICarolTakeAllRequest takeAllRequest = new ICarolTakeAllRequest(getLastJobExecutionDate());
        String body = new Gson().toJson(takeAllRequest);

        String response;
        try {
            response = HttpUtils.executePOST(URL, body, headers);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot connect with iCarol API");
        }

        documentUploadService.uploadApiData(response, getSystemAccount(), report);
    }

    protected abstract String getApiKey();

    protected abstract String getSystemAccount();

    private String getLastJobExecutionDate() {
        DataImportReport lastReport = dataImportReportService.findLatestByJobName(getFullName());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (lastReport != null) {
            return lastReport.getStartDate().format(formatter);
        }
        return "";
    }
}
