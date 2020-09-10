package org.benetech.servicenet.generator;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.util.Random;
import java.util.Set;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrganizationMother implements BaseMother<Organization> {

    public static final OrganizationMother INSTANCE = new OrganizationMother();

    private final Logger log = LoggerFactory.getLogger(OrganizationMother.class);

    private static final int MAX_LOCATIONS_OR_SERVICES = 3;

    private static final Faker FAKER = FakerInstance.INSTANCE;

    private static final Random RANDOM = new Random();

    private OrganizationMother() {}

    public Organization generate(EntityManager em, SystemAccount systemAccount, UserProfile userProfile) {
        Organization org = this.generate(em);
        org.setAccount(systemAccount);
        org.setUserProfiles(Set.of(userProfile));

        em.persist(org);
        log.debug("Fake organization created: " + org.getName());

        return org;
    }

    @Override
    public Organization generate(EntityManager em) {
        Organization org = Organization.builder()
            .name(FAKER.company().name())
            .description(FAKER.company().industry())
            .email(FAKER.bothify("????##@????.com"))
            .url(FAKER.company().url())
            .yearIncorporated(LocalDate.now())
            .active(true)
            .build();

        this.addRandomNumberOfLocations(em, org);
        this.addRandomNumberOfServices(em, org);
        this.addRandomNumberOfDailyUpdates(em, org);

        return org;
    }

    private void addRandomNumberOfLocations(EntityManager em, Organization org) {
        int amount = RANDOM.nextInt(MAX_LOCATIONS_OR_SERVICES) + 1;
        for (int i = 0; i < amount; i++) {
            Location loc = LocationMother.INSTANCE.generate(em);
            loc.setOrganization(org);
            em.persist(loc);
            log.debug("Fake location created: " + loc.getName());
        }
    }

    private void addRandomNumberOfServices(EntityManager em, Organization org) {
        int amount = RANDOM.nextInt(MAX_LOCATIONS_OR_SERVICES) + 1;
        for (int i = 0; i < amount; i++) {
            Service service = ServiceMother.INSTANCE.generate(em);
            service.setOrganization(org);
            em.persist(service);
            log.debug("Fake service created: " + service.getName());
        }
    }

    private void addRandomNumberOfDailyUpdates(EntityManager em, Organization org) {
        int amount = RANDOM.nextInt(MAX_LOCATIONS_OR_SERVICES) + 1;
        for (int i = 0; i < amount; i++) {
            DailyUpdate update = DailyUpdateMother.INSTANCE.generate(em);
            update.setOrganization(org);
            em.persist(update);
            log.debug("Fake daily update created: " + update.getUpdate());
        }
    }
}
