package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Eligibility;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class EligibilityMother {

    public static final String ELIGIBILITY = "eligibility";

    public static Eligibility createDefault() {
        return new Eligibility()
            .eligibility(ELIGIBILITY);
    }
}
