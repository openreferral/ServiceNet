package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Funding;

public final class FundingMother {

    public static final String FUNDING_SOURCE = "Funding source";

    public static Funding createDefault() {
        return new Funding()
            .source(FUNDING_SOURCE);
    }
}
