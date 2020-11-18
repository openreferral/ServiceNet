package org.benetech.servicenet.mother;

import java.util.HashSet;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;

import javax.persistence.EntityManager;

public class UserMother {
    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";
    private static final String DEFAULT_ORG_NAME = "Default Org Name";
    private static final String DEFAULT_ORG_URL = "organization.com";
    private static final String DEFAULT_PHONE_NR = "+12345678900";
    private static final String DIFFERENT_ORG_NAME = "Different Org name";
    private static final String DIFFERENT_ORG_URL = "different.org";
    private static final String DIFFERENT_PHONE_NR = "+12345678901";

    public static UserProfile admin() {
        UserProfile userProfile = new UserProfile();
        userProfile.setLogin("test");
        return userProfile;
    }

    public static UserProfile createDefault() {
        return UserProfile.builder()
            .login(DEFAULT_LOGIN)
            .organizationName(DEFAULT_ORG_NAME)
            .organizationUrl(DEFAULT_ORG_URL)
            .phoneNumber(DEFAULT_PHONE_NR)
            .systemAccount(SystemAccountMother.createDefault())
            .build();
    }

    public static UserProfile createDifferent() {
        return UserProfile.builder()
            .login(UPDATED_LOGIN)
            .organizationName(DIFFERENT_ORG_NAME)
            .organizationUrl(DIFFERENT_ORG_URL)
            .phoneNumber(DIFFERENT_PHONE_NR)
            .systemAccount(SystemAccountMother.createDefault())
            .build();
    }

    public static UserProfile createWithLogin(String login) {
        return UserProfile.builder()
            .login(login)
            .systemAccount(SystemAccountMother.createDefault())
            .build();
    }

    public static UserProfile createDefaultAndPersist(EntityManager em) {
        Silo silo = SiloMother.createDefault();
        SystemAccount account = SystemAccountMother.createDefaultAndPersist(em);
        UserProfile userProfile = createDefault();
        userProfile.setSystemAccount(account);
        userProfile.setOrganizations(new HashSet<>());
        userProfile.setShelters(new HashSet<>());
        userProfile.setUserGroups(new HashSet<>());
        userProfile.setSilo(silo);
        em.persist(silo);
        em.persist(userProfile);
        em.flush();
        return userProfile;
    }

    public static UserProfile createDifferentAndPersist(EntityManager em) {
        SystemAccount account = SystemAccountMother.createDefaultAndPersist(em);
        UserProfile userProfile = createDifferent();
        userProfile.setSystemAccount(account);
        em.persist(userProfile);
        em.flush();
        return userProfile;
    }

    public static UserProfile createForServiceProviderAndPersist(EntityManager em) {
        Silo silo = SiloMother.createDifferent();
        SystemAccount account = SystemAccountMother.createServiceProviderAndPersist(em);
        UserProfile userProfile = createDefault();
        userProfile.setSystemAccount(account);
        userProfile.setOrganizations(new HashSet<>());
        userProfile.setUserGroups(new HashSet<>());
        userProfile.setSilo(silo);
        em.persist(silo);
        em.persist(userProfile);
        em.flush();
        return userProfile;
    }

    public static UserProfile createWithLoginAndPersist(EntityManager em, String login) {
        SystemAccount account = SystemAccountMother.createDefaultAndPersist(em);
        UserProfile userProfile = createWithLogin(login);
        userProfile.setSystemAccount(account);
        em.persist(userProfile);
        em.flush();
        return userProfile;
    }

    private UserMother() {
    }
}
