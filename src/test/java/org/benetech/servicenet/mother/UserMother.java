package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Authority;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.User;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

public class UserMother {
    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String UPDATED_LOGIN = "jhipster";

    private static final String DEFAULT_PASSWORD = "passjohndoe";

    private static final String DEFAULT_PASSWORD_HASH = "$2a$10$cSuZXRFEQh7DA8AmYvoAxu86k.Wrz8guEPtucj7N.RGJ3Ytda7B3C";

    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String UPDATED_PASSWORD_HASH = "$2a$10$qzaK4QISHfgTSwH.68GZkuPiEgrlVx8B9AgDDlHzKeiKxM48STQQa";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";

    private static final String UPDATED_LANGKEY = "fr";

    public static User admin() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(AuthorityMother.getAdmin());

        User user = new User();
        user.setLogin("test");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setEmail("john.doe@jhipster.com");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("en");
        user.setAuthorities(authorities);
        return user;
    }

    public static User createDefault() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(AuthorityMother.getUser());
        return User.builder()
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD_HASH)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRSTNAME)
            .lastName(DEFAULT_LASTNAME)
            .imageUrl(DEFAULT_IMAGEURL)
            .langKey(DEFAULT_LANGKEY)
            .systemAccount(SystemAccountMother.createDefault())
            .authorities(authorities)
            .build();
    }

    public static User createDifferent() {
        Set<Authority> authorities = new HashSet<>();
        authorities.add(AuthorityMother.getAdmin());
        return User.builder()
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD_HASH)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRSTNAME)
            .lastName(UPDATED_LASTNAME)
            .imageUrl(UPDATED_IMAGEURL)
            .langKey(UPDATED_LANGKEY)
            .systemAccount(SystemAccountMother.createDefault())
            .authorities(authorities)
            .build();
    }

    public static User createDefaultAndPersist(EntityManager em) {
        SystemAccount account = SystemAccountMother.createDefaultAndPersist(em);
        User user = createDefault();
        user.setSystemAccount(account);
        em.persist(user);
        em.flush();
        return user;
    }

    public static User createDifferentAndPersist(EntityManager em) {
        SystemAccount account = SystemAccountMother.createDefaultAndPersist(em);
        User user = createDifferent();
        user.setSystemAccount(account);
        em.persist(user);
        em.flush();
        return user;
    }
}
