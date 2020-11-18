package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Silo;

public final class SiloMother {

    public static final String SILO_NAME = "SiloName";
    public static final String ADDITIONAL_SILO_NAME = "SiloNameA";
    public static final String DIFFERENT_SILO_NAME = "SiloNameD";

    public static Silo createDefault() {
        return new Silo()
            .name(SILO_NAME);
    }

    public static Silo createAdditionalDefault() {
        return new Silo()
            .name(ADDITIONAL_SILO_NAME);
    }

    public static Silo createDifferent() {
        return new Silo()
            .name(DIFFERENT_SILO_NAME);
    }

    private SiloMother() {
    }
}
