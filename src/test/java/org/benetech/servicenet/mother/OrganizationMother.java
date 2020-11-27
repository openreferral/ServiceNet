package org.benetech.servicenet.mother;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import org.benetech.servicenet.domain.OrganizationError;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.UserProfile;

public class OrganizationMother {

    public static final String DEFAULT_NAME = "AAAAAAAAAA";
    public static final String UPDATED_NAME = "BBBBBBBBBB";

    public static final String DEFAULT_ALTERNATE_NAME = "AAAAAAAAAA";
    public static final String UPDATED_ALTERNATE_NAME = "BBBBBBBBBB";

    public static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    public static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    public static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    public static final String UPDATED_EMAIL = "BBBBBBBBBB";

    public static final String DEFAULT_URL = "AAAAAAAAAA";
    public static final String UPDATED_URL = "BBBBBBBBBB";

    public static final String DEFAULT_TAX_STATUS = "AAAAAAAAAA";
    public static final String UPDATED_TAX_STATUS = "BBBBBBBBBB";

    public static final String DEFAULT_TAX_ID = "AAAAAAAAAA";
    public static final String UPDATED_TAX_ID = "BBBBBBBBBB";

    public static final LocalDate DEFAULT_YEAR_INCORPORATED = LocalDate.ofEpochDay(0L);
    public static final LocalDate UPDATED_YEAR_INCORPORATED = LocalDate.now(ZoneId.systemDefault());

    public static final String DEFAULT_LEGAL_STATUS = "AAAAAAAAAA";
    public static final String UPDATED_LEGAL_STATUS = "BBBBBBBBBB";

    public static final Boolean DEFAULT_ACTIVE = true;
    public static final Boolean DEFAULT_INACTIVE = false;
    public static final Boolean UPDATED_ACTIVE = true;

    public static Organization createDefault(Boolean active) {
        Organization org = new Organization()
            .name(DEFAULT_NAME)
            .alternateName(DEFAULT_ALTERNATE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .email(DEFAULT_EMAIL)
            .url(DEFAULT_URL)
            .taxStatus(DEFAULT_TAX_STATUS)
            .taxId(DEFAULT_TAX_ID)
            .yearIncorporated(DEFAULT_YEAR_INCORPORATED)
            .legalStatus(DEFAULT_LEGAL_STATUS)
            .active(active);
        org.setAccount(SystemAccountMother.createDefault());
        org.setDailyUpdates(new HashSet<>());
        org.setUserProfiles(new HashSet<>());
        org.setAdditionalSilos(new HashSet<>());
        return org;
    }

    public static Organization createDifferent() {
        Organization org = new Organization()
            .name(UPDATED_NAME)
            .alternateName(UPDATED_ALTERNATE_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .url(UPDATED_URL)
            .taxStatus(UPDATED_TAX_STATUS)
            .taxId(UPDATED_TAX_ID)
            .yearIncorporated(UPDATED_YEAR_INCORPORATED)
            .legalStatus(UPDATED_LEGAL_STATUS)
            .active(UPDATED_ACTIVE);
        org.setAccount(SystemAccountMother.createDifferent());
        org.setDailyUpdates(new HashSet<>());
        org.setUserProfiles(new HashSet<>());
        org.setAdditionalSilos(new HashSet<>());
        return org;
    }

    public static Organization createDefaultForServiceProvider(Boolean active) {
        return new Organization()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .email(DEFAULT_EMAIL)
            .url(DEFAULT_URL)
            .active(active);
    }

    public static Organization createDefaultAndPersist(EntityManager em) {
        Organization org = createDefault(DEFAULT_ACTIVE);
        org.setAccount(SystemAccountMother.createDefaultAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createSimiliarAndPersist(EntityManager em) {
        Organization org = createDefault(DEFAULT_ACTIVE);
        org.setAccount(SystemAccountMother.createDifferentAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createDifferentAndPersist(EntityManager em) {
        Organization org = createDifferent();
        org.setAccount(SystemAccountMother.createDifferentAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createInactiveAndPersist(EntityManager em) {
        Organization org = createDefault(DEFAULT_INACTIVE);
        org.setAccount(SystemAccountMother.createDefaultAndPersist(em));
        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createDefaultWithAllRelationsAndPersist(EntityManager em) {
        Organization org = createDefault(DEFAULT_ACTIVE);

        org.setAccount(SystemAccountMother.createDefaultAndPersist(em));
        Set<UserProfile> userProfiles = new HashSet<>();
        userProfiles.add(UserMother.createDefaultAndPersist(em));
        org.setUserProfiles(userProfiles);

        Location loc = LocationMother.createDefaultWithAllRelationsAndPersist(em);
        org.setLocations(new HashSet<>());
        org.getLocations().add(loc);

        Service srv = ServiceMother.createDefaultWithAllRelationsAndPersist(em);
        org.setServices(new HashSet<>());
        org.getServices().add(srv);

        ServiceAtLocation srvAtLoc = new ServiceAtLocation().srvc(srv).location(loc);
        srv.setLocations(new HashSet<>());
        srv.getLocations().add(srvAtLoc);
        em.persist(srvAtLoc);

        Funding funding = FundingMother.createDefault();
        em.persist(funding);
        org.setFunding(funding);

        Program program = ProgramMother.createDefault();
        em.persist(program);
        org.setPrograms(new HashSet<>());
        org.getPrograms().add(program);

        Phone contactPhone = PhoneMother.createDefault();
        em.persist(contactPhone);

        Contact contact = ContactMother.createDefault();
        contact.setPhones(new HashSet<>());
        contact.getPhones().add(contactPhone);
        em.persist(contact);
        org.setContacts(new HashSet<>());
        org.getContacts().add(contact);

        Phone orgPhone = PhoneMother.createDefault();
        em.persist(orgPhone);
        org.setPhones(new HashSet<>());
        org.getPhones().add(orgPhone);

        DailyUpdate du = DailyUpdateMother.createDefault();
        em.persist(du);
        org.setDailyUpdates(new HashSet<>());
        org.getDailyUpdates().add(du);

        Silo silo = SiloMother.createAdditionalDefault();
        em.persist(silo);
        org.setAdditionalSilos(new HashSet<>());
        org.getAdditionalSilos().add(silo);

        OrganizationError organizationError = OrganizationErrorMother.createDefault();
        organizationError.organization(org);
        em.persist(organizationError);

        em.persist(org);
        em.flush();
        return org;
    }

    public static Organization createForServiceProviderAndPersist(EntityManager em) {
        Organization org = createDefaultForServiceProvider(DEFAULT_ACTIVE);

        org.setAccount(SystemAccountMother.createServiceProviderAndPersist(em));
        org.setUserProfiles(Collections.singleton(UserMother.createForServiceProviderAndPersist(em)));

        return populateServiceProviderRelations(org, em);
    }

    public static Organization createAnotherForServiceProviderAndPersist(EntityManager em) {
        Organization org = createDifferentAndPersist(em);

        org.setAccount(SystemAccountMother.createServiceProviderAndPersist(em));
        org.setUserProfiles(Collections.singleton(UserMother.createDifferentForServiceProviderAndPersist(em, org.getAccount())));

        return populateServiceProviderRelations(org, em);
    }

    private static Organization populateServiceProviderRelations(Organization org, EntityManager em) {
        Location loc = LocationMother.createForServiceProviderAndPersist(em);
        org.setLocations(Collections.singleton(loc));

        Service srv = ServiceMother.createForServiceProviderAndPersist(em);
        org.setServices(Collections.singleton(srv));

        ServiceAtLocation srvAtLoc = new ServiceAtLocation().srvc(srv).location(loc);
        srv.setLocations(Collections.singleton(srvAtLoc));
        em.persist(srvAtLoc);

        DailyUpdate du = DailyUpdateMother.createDefault();
        org.setDailyUpdates(Collections.singleton(du));
        em.persist(du);

        em.persist(org);
        em.flush();
        return org;
    }

    private OrganizationMother() {
    }
}
