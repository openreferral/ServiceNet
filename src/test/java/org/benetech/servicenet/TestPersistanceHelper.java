package org.benetech.servicenet;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.benetech.servicenet.TestConstants.EXISTING_BOOLEAN;
import static org.benetech.servicenet.TestConstants.EXISTING_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.EXISTING_STRING;
import static org.benetech.servicenet.TestConstants.NEW_BOOLEAN;
import static org.benetech.servicenet.TestConstants.NEW_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.NEW_STRING;
import static org.benetech.servicenet.TestConstants.OTHER_STRING;
import static org.benetech.servicenet.TestConstants.PROVIDER;

@Component
public class TestPersistanceHelper {

    @Autowired
    public EntityManager em;

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    public <T> void persist(T obj) {
        em.persist(obj);
    }

    public <T> void flushAndRefresh(T obj) {
        em.flush();
        em.refresh(obj);
    }

    @Transactional
    public SystemAccount generateSystemAccount(String provider) {
        Optional<SystemAccount> accountFromDb = systemAccountRepository.findByName(provider);
        if (accountFromDb.isPresent()) {
            return accountFromDb.get();
        }
        SystemAccount account = new SystemAccount().name(provider);
        em.persist(account);
        em.flush();
        em.refresh(account);
        return account;
    }

    public Location generateNewLocation() {
        return new Location().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
    }

    public Location generateExistingLocation() {
        Location result = new Location().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }

    public Location generateExistingLocationDoNotPersist() {
        return new Location().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
    }

    public Service generateNewService() {
        return new Service().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
    }

    public Service generateExistingService() {
        Service result = new Service().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }

    public SystemAccount generateExistingAccount() {
        return generateSystemAccount(PROVIDER);
    }

    public Organization generateNewOrganization(SystemAccount account) {
        return new Organization().externalDbId(NEW_EXTERNAL_ID).account(account)
            .name(NEW_STRING).active(NEW_BOOLEAN);
    }

    public Service generateExistingServiceDoNotPersist() {
        return new Service().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
    }

    public Organization generateExistingOrganization(SystemAccount account) {
        Organization result = new Organization().externalDbId(EXISTING_EXTERNAL_ID).account(account)
            .name(EXISTING_STRING).active(EXISTING_BOOLEAN);
        em.persist(result);
        return result;
    }

    public Organization generateExistingOrganizationDoNotPersist() {
        return new Organization().externalDbId(EXISTING_EXTERNAL_ID).account(generateExistingAccount())
            .name(EXISTING_STRING).active(EXISTING_BOOLEAN);
    }

    public Taxonomy generateNewTaxonomy() {
        return new Taxonomy().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
    }

    public Taxonomy generateExistingTaxonomy() {
        Taxonomy result = new Taxonomy().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }

    public Taxonomy generateOtherTaxonomy() {
        Taxonomy result = new Taxonomy().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(OTHER_STRING);
        em.persist(result);
        return result;
    }

    @SafeVarargs
    public final <T> Set<T> mutableSet(T ... objects) {
        return new HashSet<T>(Arrays.asList(objects));
    }
}
