package org.benetech.servicenet.scheduler;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.generator.OrganizationMother;
import org.benetech.servicenet.repository.SiloRepository;
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

    private static final int BATCH_SIZE = 1000;

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private SiloRepository siloRepository;

    @Autowired
    private EntityManager em;


    public List<Organization> createReferenceData(String login, int numberOfOrganizationsToCreate) {
        return this.createReferenceData(login, numberOfOrganizationsToCreate, null);
    }

    @SuppressWarnings("PMD.CyclomaticComplexity")
    public List<Organization> createReferenceData(String login, int numberOfOrganizationsToCreate, String additionalSiloName) {
        SystemAccount serviceProviderAcc = systemAccountService.findByName(SERVICE_PROVIDER).get();
        List<Organization> createdOrganizations = new ArrayList<>();
        if (login != null) {
            Optional<UserProfile> userProfileOpt = userProfileRepository.findOneByLogin(login);
            if (userProfileOpt.isPresent()) {
                Silo silo = (additionalSiloName != null) ? siloRepository.getByName(additionalSiloName).get() : null;
                for (int i = 1; i <= numberOfOrganizationsToCreate; i++) {
                    LOG.info("Start creating " + i + "/" + numberOfOrganizationsToCreate + " organization");
                    createdOrganizations.add(
                        this.createSingleDataSet(serviceProviderAcc, userProfileOpt.get(), silo)
                    );
                    if (i > 0 && i % BATCH_SIZE == 0) {
                        em.flush();
                        em.clear();
                    }
                }
            }
        } else {
            List<UserProfile> userProfiles = userProfileRepository.findAll();
            for (UserProfile userProfile : userProfiles) {
                LOG.info("Start creating user for user " + userProfile.getLogin());
                Silo silo = (additionalSiloName != null) ? siloRepository.getByName(additionalSiloName).get() : null;
                for (int i = 1; i <= numberOfOrganizationsToCreate; i++) {
                    LOG.info("Start creating " + i + "/" + numberOfOrganizationsToCreate + " organization");
                    createdOrganizations.add(
                        this.createSingleDataSet(serviceProviderAcc, userProfile, silo)
                    );
                }
            }
        }
        return createdOrganizations;
    }

    protected Organization createSingleDataSet(SystemAccount serviceProviderAcc, UserProfile userProfile, Silo silo) {
        return OrganizationMother.INSTANCE.generate(em, serviceProviderAcc, userProfile, silo);
    }
}
