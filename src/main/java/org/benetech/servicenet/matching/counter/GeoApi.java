package org.benetech.servicenet.matching.counter;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GeoApi {

    private static final String CONNECTION_ERROR = "Cannot connect with Google Maps API";

    @Value("${similarity-ratio.credentials.google-api}")
    private String googleApiKey;

    public DistanceMatrix getDistanceMatrix(String[] address1, String[] address2) {
        try {
            return DistanceMatrixApi
                .getDistanceMatrix(getGeoApiContext(), address1, address2)
                .mode(TravelMode.WALKING)
                .await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(CONNECTION_ERROR);
        }
    }

    public GeocodingResult[] geocode(String address) {
        try {
            return GeocodingApi.geocode(getGeoApiContext(),
                address).await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(CONNECTION_ERROR);
        }
    }

    private GeoApiContext getGeoApiContext() {
        return new GeoApiContext.Builder()
            .apiKey(googleApiKey)
            .build();
    }
}
