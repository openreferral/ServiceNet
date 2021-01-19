package org.benetech.servicenet.matching.counter;

import static java.util.Collections.singletonList;

import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationSimilarityCounter extends AbstractSimilarityCounter<Location> {

    @Value("${similarity-ratio.config.location.level-1-distance}")
    private long level1meters;

    @Value("${similarity-ratio.config.location.level-2-distance}")
    private long level2meters;

    @Value("${similarity-ratio.config.location.level-3-distance}")
    private long level3meters;

    @Value("${similarity-ratio.weight.location.in-level-2-distance}")
    private BigDecimal level2distanceRatio;

    @Value("${similarity-ratio.weight.location.in-level-3-distance}")
    private BigDecimal level3distanceRatio;

    @Value("${similarity-ratio.weight.location.in-same-city-zipcode}")
    private BigDecimal sameCityOrZipCodeRatio;

    @Override
    public BigDecimal countSimilarityRatio(Location location1, Location location2) {
        if (anyPhysicalAddressIsNull(location1, location2) || areLocations255AddressTextBlank(location1, location2)) {
            return NO_MATCH_RATIO;
        }

        List<GeocodingResult> baseGeocoding = location1.getGeocodingResults();
        List<GeocodingResult> partnerGeocoding = location2.getGeocodingResults();

        if (!areCoordsComparable(baseGeocoding, partnerGeocoding, location1, location2)) {
            if (areLocationsInSameCityOrZipcode(location1, location2)) {
                return sameCityOrZipCodeRatio;
            }
            return NO_MATCH_RATIO;
        }

        if (!areGeocodingResultsReliable(baseGeocoding)) {
            baseGeocoding = mockGeocodingResultsWithProviderData(location1);
        }
        if (!areGeocodingResultsReliable(partnerGeocoding)) {
            partnerGeocoding = mockGeocodingResultsWithProviderData(location2);
        }

        return countRatioBetweenGeocodingResults(baseGeocoding, partnerGeocoding);
    }

    private boolean areLocations255AddressTextBlank(Location location1, Location location2) {
        return StringUtils.isBlank(location1.getPhysicalAddress().extract255AddressChars()) ||
            StringUtils.isBlank(location2.getPhysicalAddress().extract255AddressChars());
    }

    private boolean areLocationsInSameCityOrZipcode(Location location1, Location location2) {
        if (location1.getPhysicalAddress().getPostalCode() == null && location2.getPhysicalAddress().getPostalCode() == null
            || location1.getPhysicalAddress().getCity() == null && location2.getPhysicalAddress().getCity() == null) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(
            location1.getPhysicalAddress().getPostalCode(), location2.getPhysicalAddress().getPostalCode())
            || StringUtils.equalsIgnoreCase(
                location1.getPhysicalAddress().getCity(), location2.getPhysicalAddress().getCity());
    }

    private BigDecimal countRatioBetweenGeocodingResults(List<GeocodingResult> baseGeocoding,
        List<GeocodingResult> partnerGeocoding) {
        BigDecimal max = NO_MATCH_RATIO;

        for (GeocodingResult result1 : baseGeocoding) {
            for (GeocodingResult result2 : partnerGeocoding) {
                BigDecimal similarityRatio = countSimilarityRatio(result1.getGoogleCoords(), result2.getGoogleCoords());
                if (similarityRatio.compareTo(max) > 0) {
                    max = similarityRatio;
                }
            }
        }

        return max;
    }

    private List<GeocodingResult> mockGeocodingResultsWithProviderData(Location location) {
        return singletonList(new GeocodingResult(location.getName(), location.getLatitude(), location.getLongitude()));
    }

    private boolean areCoordsComparable(List<GeocodingResult> baseGeocoding,
        List<GeocodingResult> partnerGeocoding, Location baseLocation, Location partnerLocation) {

        return (areGeocodingResultsReliable(baseGeocoding) || !areProviderCoordsEmpty(baseLocation))
                && (areGeocodingResultsReliable(partnerGeocoding) || !areProviderCoordsEmpty(partnerLocation));
    }

    private boolean areProviderCoordsEmpty(Location location) {
        return location.getLongitude() == null || location.getLatitude() == null
            || location.getLongitude().equals(0.0) || location.getLatitude().equals(0.0);
    }

    private boolean areGeocodingResultsReliable(List<GeocodingResult> geocodingResults) {
        return geocodingResults.stream()
            .anyMatch(result -> result != null && result.getPartialMatch() != null && !result.getPartialMatch()
                && Objects.equals(result.getLocationType(), LocationType.ROOFTOP));
    }

    private boolean anyPhysicalAddressIsNull(Location location1, Location location2) {
        return location1 == null || location1.getPhysicalAddress() == null
            || location2 == null || location2.getPhysicalAddress() == null;
    }

    private BigDecimal countSimilarityRatio(LatLng coordinates1, LatLng coordinates2) {
        return countSimilarityRatio(GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2));
    }

    private BigDecimal countSimilarityRatio(double distance) {
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
}
