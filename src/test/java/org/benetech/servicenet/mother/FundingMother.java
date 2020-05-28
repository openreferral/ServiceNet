package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Funding;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class FundingMother {

    public static final String FUNDING_SOURCE = "Funding source";

    public static Funding createDefault() {
        return new Funding()
            .source(FUNDING_SOURCE);
    }
}
