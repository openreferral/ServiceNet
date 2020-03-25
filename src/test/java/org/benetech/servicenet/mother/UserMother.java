package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.UserProfile;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

public class UserMother {
    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String UPDATED_LOGIN = "jhipster";

    public static UserProfile admin() {
        UserProfile userProfile = new UserProfile();
        userProfile.setLogin("test");
        return userProfile;
    }

    public static UserProfile createDefault() {
        return UserProfile.builder()
            .login(DEFAULT_LOGIN)
            .systemAccount(SystemAccountMother.createDefault())
            .build();
    }

    public static UserProfile createDifferent() {
        return UserProfile.builder()
            .login(UPDATED_LOGIN)
            .systemAccount(SystemAccountMother.createDefault())
            .build();
    }

    public static UserProfile createDefaultAndPersist(EntityManager em) {
        SystemAccount account = SystemAccountMother.createDefaultAndPersist(em);
        UserProfile userProfile = createDefault();
        userProfile.setSystemAccount(account);
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
}
