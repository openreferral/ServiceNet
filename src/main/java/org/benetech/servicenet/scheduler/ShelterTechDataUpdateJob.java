package org.benetech.servicenet.scheduler;

import static org.benetech.servicenet.config.Constants.SHELTER_TECH_PROVIDER;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import org.benetech.servicenet.adapter.sheltertech.ShelterTechCollector;
import org.benetech.servicenet.domain.DataImportReport;
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

    @Override
    public Date getStartTime() {
        return getOffsetDate(60 * 60);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(getFullName() + " is being executed");

        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now())
            .jobName(getFullName())
            .systemAccount(getSystemAccount());
        String response = ShelterTechCollector.getData();

        try {
            documentUploadService.uploadApiData(response, getSystemAccount(), report);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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
