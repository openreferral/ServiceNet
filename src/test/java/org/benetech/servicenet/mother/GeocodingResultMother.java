package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.GeocodingResult;

public final class GeocodingResultMother {

    public static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    public static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    public static final Double DEFAULT_LATITUDE = 1D;
    public static final Double UPDATED_LATITUDE = 2D;

    public static final Double DEFAULT_LONGITUDE = 1D;
    public static final Double UPDATED_LONGITUDE = 2D;

    public static GeocodingResult generateDefault() {
        return new GeocodingResult()
            .address(DEFAULT_ADDRESS)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
    }

    private GeocodingResultMother() {
    }
}
