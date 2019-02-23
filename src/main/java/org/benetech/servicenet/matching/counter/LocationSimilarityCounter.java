package org.benetech.servicenet.matching.counter;

import com.google.maps.model.LatLng;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.service.GeocodingResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private GeocodingResultService geocodingResultService;

    @Override
    public float countSimilarityRatio(Location location1, Location location2) {
        if (location1 == null || location1.getPhysicalAddress() == null
            || location2 == null || location2.getPhysicalAddress() == null) {
            return NO_MATCH_RATIO;
        }
        float max = NO_MATCH_RATIO;
        for (GeocodingResult result1 : geocodingResultService.findAllForLocationOrFetchIfEmpty(location1)) {
            for (GeocodingResult result2 : geocodingResultService.findAllForLocationOrFetchIfEmpty(location2)) {
                max = Math.max(max, countSimilarityRatio(result1, result2));
            }
        }
        return max;
    }

    private float countSimilarityRatio(GeocodingResult result1, GeocodingResult result2) {
        return countSimilarityRatio(result1.getGoogleCoords(), result2.getGoogleCoords());
    }

    private float countSimilarityRatio(LatLng coordinates1, LatLng coordinates2) {
        return countSimilarityRatio(getStraightLineDistanceInMeters(coordinates1, coordinates2));
    }

    private float countSimilarityRatio(double distance) {
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

    public double getStraightLineDistanceInMeters(LatLng coordinates1, LatLng coordinates2) {
        return GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2);
    }
}
