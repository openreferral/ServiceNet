package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Eligibility;

public final class EligibilityMother {

    public static final String ELIGIBILITY = "eligibility";

    public static Eligibility createDefault() {
        return new Eligibility()
            .eligibility(ELIGIBILITY);
    }

    private EligibilityMother() {
    }
}
