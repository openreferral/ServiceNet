package org.benetech.servicenet.scheduler;

import java.util.Date;
import java.util.List;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.counter.OrganizationSimilarityCounter;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMatchUpdateJob extends BaseJob {

    private static final String NAME = "Organization Match Update Job";
    private static final String DESCRIPTION = "Update existing Organization Matches";
    private final Logger log = LoggerFactory.getLogger(OrganizationMatchUpdateJob.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Autowired
    private OrganizationSimilarityCounter organizationSimilarityCounter;

    @Override
    public Date getStartTime() {
        return getOffsetDate(0);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.info(getFullName() + " is being executed");

        try {
            for (OrganizationMatchDTO match : organizationMatchService.findAll()) {
                Organization organization = organizationService
                    .findOneWithEagerAssociations(match.getOrganizationRecordId());
                Organization partner = organizationService
                    .findOneWithEagerAssociations(match.getPartnerVersionId());

                List<MatchSimilarityDTO> similarityDTOs = organizationSimilarityCounter.getMatchSimilarityDTOs(
                    organization, partner);
                organizationMatchService.createOrganizationMatches(organization, partner, similarityDTOs);
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
