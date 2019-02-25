package org.benetech.servicenet.matching.counter;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeoApi {

    private static final String CONNECTION_ERROR = "Cannot connect with Google Maps API";
    private static final String DELIMITER = ", ";
    private static final int MAX_ADDRESS_LENGTH = 255;

    private GeoApiContext context;

    public GeoApi(String googleApiKey) {
        context = getGeoApiContext(googleApiKey);
    }

    public GeocodingResult[] geocode(Location location) {
        return geocode(extract255AddressChars(location.getPhysicalAddress()));
    }

    public String extract255AddressChars(PhysicalAddress address) {
        return Stream.of(address.getAddress1(), address.getCity(), address.getCountry(), address.getPostalCode(),
            address.getRegion(), address.getStateProvince())
            .filter(StringUtils::isNotBlank).collect(Collectors.joining(DELIMITER)).substring(0, MAX_ADDRESS_LENGTH);
    }

    private GeoApiContext getGeoApiContext(String googleApiKey) {
        return new GeoApiContext.Builder()
            .apiKey(googleApiKey)
            .build();
    }

    private GeocodingResult[] geocode(String address) {
        try {
            return GeocodingApi.geocode(context,
                address).await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(CONNECTION_ERROR);
        }
    }
}
