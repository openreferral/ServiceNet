package org.benetech.servicenet.matching.counter;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LocationSimilarityCounter extends AbstractSimilarityCounter<Location> {

    private static final String DELIMITER = ", ";

    @Value("${similarity-ratio.config.location.level-1-distance}")
    private long level1meters;

    @Value("${similarity-ratio.config.location.level-2-distance}")
    private long level2meters;

    @Value("${similarity-ratio.config.location.level-3-distance}")
    private long level3meters;

    @Value("${similarity-ratio.weight.location.in-level-2-distance}")
    private float level2distanceRatio;

    @Value("${similarity-ratio.weight.location.in-level-3-distance}")
    private float level3distanceRatio;

    @Autowired
    private GeoApi geoApi;

    @Override
    public float countSimilarityRatio(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return NO_MATCH_RATIO;
        }
        float max = NO_MATCH_RATIO;
        for (GeocodingResult result1 : geocode(location1)) {
            for (GeocodingResult result2 : geocode(location2)) {
                max = Math.max(max, countSimilarityRatio(result1, result2));
            }
        }
        return max;
    }

    private float countSimilarityRatio(GeocodingResult result1, GeocodingResult result2) {
        return countSimilarityRatio(result1.geometry.location, result2.geometry.location);
    }

    private float countSimilarityRatio(LatLng coordinates1, LatLng coordinates2) {
        return countSimilarityRatio(geoApi.getDistanceMatrix(
            getCoordinatesString(coordinates1), getCoordinatesString(coordinates2)));
    }

    private String[] getCoordinatesString(LatLng coordinates) {
        return new String[]{coordinates.lat + DELIMITER + coordinates.lng};
    }

    private float countSimilarityRatio(DistanceMatrix matrix) {
        long distance = getShortestDistanceInMeters(matrix);

        if (distance > level3meters) {
            return NO_MATCH_RATIO;
        }

        if (distance > level2meters) {
            return level3distanceRatio;
        }

        if (distance > level1meters) {
            return level2distanceRatio;
        }

        return COMPLETE_MATCH_RATIO;
    }

    private long getShortestDistanceInMeters(DistanceMatrix matrix) {
        long min = Long.MAX_VALUE;
        for (DistanceMatrixRow row : matrix.rows) {
            for (DistanceMatrixElement element : row.elements) {
                min = Math.min(min, element.distance.inMeters);
            }
        }
        return min;
    }

    private String extractAddressString(PhysicalAddress address) {
        return Stream.of(address.getAddress1(), address.getCity(), address.getCountry(), address.getPostalCode(),
            address.getRegion(), address.getStateProvince())
            .filter(StringUtils::isNotBlank).collect(Collectors.joining(DELIMITER));
    }

    private GeocodingResult[] geocode(Location location) {
        return geoApi.geocode(extractAddressString(location.getPhysicalAddress()));
    }
}
