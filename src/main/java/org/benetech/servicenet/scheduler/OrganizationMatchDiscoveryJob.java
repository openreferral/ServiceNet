package org.benetech.servicenet.scheduler;

import java.util.Date;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMatchDiscoveryJob extends BaseJob {

    private static final String NAME = "Organization Match Discovery Job";
    private static final String DESCRIPTION = "Discover and update Matches for all Organizations";
    private final Logger log = LoggerFactory.getLogger(OrganizationMatchDiscoveryJob.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Override
    public Date getStartTime() {
        return getOffsetDate(Integer.MAX_VALUE);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(getFullName() + " is being executed");

        try {
            for (Organization org : organizationService.findAllWithEagerAssociations()) {
                organizationMatchService.createOrUpdateOrganizationMatchesSynchronously(
                    org);
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
        return Integer.MAX_VALUE;
    }
}
