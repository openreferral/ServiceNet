package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.ServiceArea;

public final class ServiceAreaMother {

    public static final String SERVICE_AREA = "Service Area";

    public static ServiceArea createDefault() {
        return new ServiceArea()
            .description(SERVICE_AREA);
    }

    private ServiceAreaMother() {
    }
}
