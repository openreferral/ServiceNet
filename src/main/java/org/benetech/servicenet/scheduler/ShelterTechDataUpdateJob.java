package org.benetech.servicenet.scheduler;

import static org.benetech.servicenet.config.Constants.SHELTER_TECH_PROVIDER;

import java.time.ZonedDateTime;
import org.benetech.servicenet.adapter.sheltertech.ShelterTechCollector;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.DocumentUploadService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShelterTechDataUpdateJob extends BaseJob {

    private final Logger log = LoggerFactory.getLogger(ShelterTechDataUpdateJob.class);

    private static final String NAME = "ShelterTech Data Update Job";

    private static final String DESCRIPTION = "Collect ShelterTech Data through the API, map it to the common structure"
        + " and persist it to the database";

    @Value("${scheduler.shelter-tech-data.interval}")
    private int intervalInSeconds;

    @Autowired
    private DocumentUploadService documentUploadService;

    @Autowired
    private DataImportReportService dataImportReportService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(getFullName() + " is being executed");

        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now()).jobName(getFullName());
        String response = ShelterTechCollector.getData();

        documentUploadService.uploadApiData(response, getSystemAccount(), report);
    }

    protected String getSystemAccount() {
        return SHELTER_TECH_PROVIDER;
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
