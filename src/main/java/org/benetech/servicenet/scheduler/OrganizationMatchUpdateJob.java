package org.benetech.servicenet.scheduler;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMatchUpdateJob extends BaseJob {

    private static final String NAME = "Organization Match Update Job";
    private static final String DESCRIPTION = "Update all Organization Matches";
    private final Logger log = LoggerFactory.getLogger(OrganizationMatchUpdateJob.class);

    @Value("${similarity-ratio.credentials.google-api}")
    private String googleApiKey;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(getFullName() + " is being executed");

        try {
            for (Organization org : organizationService.findAllWithEagerAssociations()) {
                organizationMatchService.createOrUpdateOrganizationMatchesSynchronously(
                    org, new MatchingContext(googleApiKey));
            }
        } catch (RuntimeException ex) {
            log.error(ex.getMessage(), ex);
        }
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
        return 0;
    }
}
