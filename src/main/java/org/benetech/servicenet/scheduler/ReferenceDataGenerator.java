package org.benetech.servicenet.scheduler;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.generator.OrganizationMother;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.SystemAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@Transactional
public class ReferenceDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ReferenceDataGenerator.class);

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private EntityManager em;

    public void createReferenceData(String login, int numberOfOrganizationsToCreate) {
        SystemAccount serviceProviderAcc = systemAccountService.findByName(SERVICE_PROVIDER).get();
        if (login != null) {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findOneByLogin(login);
            if (userProfileOpt.isPresent()) {
                for (int i = 1; i <= numberOfOrganizationsToCreate; i++) {
                    LOG.info("Start creating " + i + "/" + numberOfOrganizationsToCreate + " organization");
                    this.createSingleDataSet(serviceProviderAcc, userProfileOpt.get());
                }
            }
        } else {
            List<UserProfile> userProfiles = userProfileRepository.findAll();
            for (UserProfile userProfile : userProfiles) {
                LOG.info("Start creating user for user " + userProfile.getLogin());
                for (int i = 1; i <= numberOfOrganizationsToCreate; i++) {
                    LOG.info("Start creating " + i + "/" + numberOfOrganizationsToCreate + " organization");
                    this.createSingleDataSet(serviceProviderAcc, userProfile);
                }
            }
        }
    }

    protected void createSingleDataSet(SystemAccount serviceProviderAcc, UserProfile userProfile) {
        OrganizationMother.INSTANCE.generate(em, serviceProviderAcc, userProfile);
    }
}
