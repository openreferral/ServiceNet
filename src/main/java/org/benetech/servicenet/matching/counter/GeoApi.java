package org.benetech.servicenet.matching.counter;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.benetech.servicenet.domain.Address;

import java.io.IOException;

public class GeoApi {

    private static final String CONNECTION_ERROR = "Cannot connect with Google Maps API. Using address: '%s'. " +
        "API Error: %s ";
    private static final String DELIMITER = ", ";
    private static final int MAX_ADDRESS_LENGTH = 255;

    private GeoApiContext context;

    public GeoApi(String googleApiKey) {
        context = getGeoApiContext(googleApiKey);
    }

    public String extract255AddressChars(Address address) {
        return address.getAddress().length() <= MAX_ADDRESS_LENGTH
            ? address.getAddress() : address.getAddress().substring(0, MAX_ADDRESS_LENGTH);
    }

    private GeoApiContext getGeoApiContext(String googleApiKey) {
        return new GeoApiContext.Builder()
            .apiKey(googleApiKey)
            .build();
    }

    public GeocodingResult[] geocode(String address) {
        try {
            return GeocodingApi.geocode(context,
                address).await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(String.format(CONNECTION_ERROR, address, e.getMessage()));
        }
    }
}
