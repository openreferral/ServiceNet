package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Location;

public final class LocationMother {

    public static final String NAME = "Location name";
    public static final String ALTERNATE_NAME = "Location alternate name";
    public static final String DESCRIPTION = "Location description";
    public static final String TRANSPORTATION = "Transportation";
    public static final double LATITUDE = 20.3443;
    public static final double LONGITUDE = -78.2323;

    public static Location createDefault() {
        return new Location()
            .name(NAME)
            .alternateName(ALTERNATE_NAME)
            .description(DESCRIPTION)
            .transportation(TRANSPORTATION)
            .latitude(LATITUDE)
            .longitude(LONGITUDE);
    }
}
