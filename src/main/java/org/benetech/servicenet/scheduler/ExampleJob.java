package org.benetech.servicenet.scheduler;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExampleJob extends BaseJob {

    private static final String NAME = "Example job";

    private static final String DESCRIPTION = "Example job description";

    @Value("${scheduler.interval.example}")
    private int intervalInSeconds;

    private final Logger log = LoggerFactory.getLogger(ExampleJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info("Example job is executed");
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
