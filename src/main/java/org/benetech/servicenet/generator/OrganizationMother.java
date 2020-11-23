package org.benetech.servicenet.generator;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
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

    public Organization generate(EntityManager em, SystemAccount systemAccount, UserProfile userProfile, Silo silo) {
        Organization org = this.generate(em);
        org.setAccount(systemAccount);
        org.setUserProfiles(Set.of(userProfile));
        if (silo != null) {
            Set<Silo> silos = new HashSet<>();
            silos.add(silo);
            org.setAdditionalSilos(silos);
        }
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
        Set<Location> locations = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            Location loc = LocationMother.INSTANCE.generate(em);
            loc.setOrganization(org);
            em.persist(loc);
            locations.add(loc);
            log.debug("Fake location created: " + loc.getName());
        }
        org.setLocations(locations);
    }

    private void addRandomNumberOfServices(EntityManager em, Organization org) {
        int amount = RANDOM.nextInt(MAX_LOCATIONS_OR_SERVICES) + 1;
        Set<Service> services = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            Service service = ServiceMother.INSTANCE.generate(em);
            service.setOrganization(org);
            em.persist(service);
            Taxonomy taxonomy = new Taxonomy()
                .providerName(SERVICE_PROVIDER)
                .name(FAKER.commerce().department());
            em.persist(taxonomy);
            ServiceTaxonomy st = new ServiceTaxonomy();
            st.setSrvc(service);
            st.setTaxonomy(taxonomy);
            em.persist(st);
            services.add(service);
            log.debug("Fake service created: " + service.getName());
        }
        org.setServices(services);
    }

    private void addRandomNumberOfDailyUpdates(EntityManager em, Organization org) {
        int amount = RANDOM.nextInt(MAX_LOCATIONS_OR_SERVICES) + 1;
        Set<DailyUpdate> dailyUpdates = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            DailyUpdate update = DailyUpdateMother.INSTANCE.generate(em);
            update.setOrganization(org);
            em.persist(update);
            dailyUpdates.add(update);
            log.debug("Fake daily update created: " + update.getUpdate());
        }
        org.setDailyUpdates(dailyUpdates);
    }
}
