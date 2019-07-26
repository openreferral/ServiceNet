package org.benetech.servicenet.matching.counter;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;

public class GeoApi {

    private static final String CONNECTION_ERROR = "Cannot connect with Google Maps API. Using address: '%s'. " +
        "API Error: %s ";

    private GeoApiContext context;

    public GeoApi(String googleApiKey) {
        context = getGeoApiContext(googleApiKey);
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
