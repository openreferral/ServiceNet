package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.SystemAccount;

public class SystemAccountMother {

    public static final String DEFAULT_NAME = "AAAAAAAAAA";
    public static final String UPDATED_NAME = "BBBBBBBBBB";

    public static SystemAccount createDefault() {
        return new SystemAccount().name(DEFAULT_NAME);
    }

    public static SystemAccount createDifferent() {
        return new SystemAccount().name(UPDATED_NAME);
    }
}
