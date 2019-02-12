package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Authority;
import org.benetech.servicenet.security.AuthoritiesConstants;

public final class AuthorityMother {

    public static Authority getAdmin() {
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        return authority;
    }

    public static Authority getUser() {
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        return authority;
    }
}
