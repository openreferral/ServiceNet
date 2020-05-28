package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Service;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class ServiceMother {

    public static final String NAME = "Service name";
    public static final String ALTERNATE_NAME = "Service alternate name";
    public static final String DESCRIPTION = "Service description";
    public static final String TRANSPORTATION = "Transportation";
    public static final double LATITUDE = 20.3443;
    public static final double LONGITUDE = -78.2323;
    public static final String URL = "Service url";
    public static final String EMAIL = "Service email";
    public static final String STATUS = "Service status";
    public static final String INTERPRETATION_SERVICES = "Service interpretationServices";
    public static final String APPLICATION_PROCESS = "Service applicationProcess";
    public static final String WAIT_TIME = "Service waitTime";
    public static final String FEES = "Service fees";
    public static final String ACCREDITATIONS = "Service accreditations";
    public static final String LICENSES = "Service licenses";
    public static final String TYPE = "Service type";

    public static Service createDefault() {
        return new Service()
            .name(NAME)
            .alternateName(ALTERNATE_NAME)
            .description(DESCRIPTION)
            .url(URL)
            .email(EMAIL)
            .status(STATUS)
            .interpretationServices(INTERPRETATION_SERVICES)
            .applicationProcess(APPLICATION_PROCESS)
            .waitTime(WAIT_TIME)
            .fees(FEES)
            .accreditations(ACCREDITATIONS)
            .licenses(LICENSES)
            .type(TYPE);
    }
}
