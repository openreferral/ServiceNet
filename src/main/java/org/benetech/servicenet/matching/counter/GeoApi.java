package org.benetech.servicenet.matching.counter;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GeoApi {

    private static final String CONNECTION_ERROR = "Cannot connect with Google Maps API";
    private static final String DELIMITER = ", ";

    @Value("${similarity-ratio.credentials.google-api}")
    private String googleApiKey;

    public GeocodingResult[] geocode(Location location) {
        return geocode(extractAddressString(location.getPhysicalAddress()));
    }

    public DistanceMatrix getDistanceMatrix(LatLng coordinates1, LatLng coordinates2) {
        try {
            return DistanceMatrixApi
                .getDistanceMatrix(
                    getGeoApiContext(), getCoordinatesString(coordinates1), getCoordinatesString(coordinates2))
                .mode(TravelMode.WALKING)
                .await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(CONNECTION_ERROR);
        }
    }

    public String extractAddressString(PhysicalAddress address) {
        return Stream.of(address.getAddress1(), address.getCity(), address.getCountry(), address.getPostalCode(),
            address.getRegion(), address.getStateProvince())
            .filter(StringUtils::isNotBlank).collect(Collectors.joining(DELIMITER));
    }

    private GeocodingResult[] geocode(String address) {
        try {
            return GeocodingApi.geocode(getGeoApiContext(),
                address).await();
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(CONNECTION_ERROR);
        }
    }

    private String[] getCoordinatesString(LatLng coordinates) {
        return new String[]{coordinates.lat + DELIMITER + coordinates.lng};
    }

    private GeoApiContext getGeoApiContext() {
        return new GeoApiContext.Builder()
            .apiKey(googleApiKey)
            .build();
    }
}
