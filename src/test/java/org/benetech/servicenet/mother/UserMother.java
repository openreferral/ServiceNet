package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Authority;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.security.AuthoritiesConstants;

import java.util.HashSet;
import java.util.Set;

public class UserMother {

    public static User admin() {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

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
}
