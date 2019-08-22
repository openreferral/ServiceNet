package org.benetech.servicenet.matching;

import static org.junit.Assert.assertEquals;

import com.google.maps.model.LatLng;
import java.math.BigDecimal;
import java.util.Collections;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.matching.counter.GeoApi;
import org.benetech.servicenet.matching.counter.GeocodeUtils;
import org.benetech.servicenet.matching.counter.LocationSimilarityCounter;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.service.GeocodingResultService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class LocationSimilarityCounterIntTest {

    private static final float DISTANCE_PRECISION = 0.5f;

    @Mock
    private GeoApi geoApiMock;

    @Mock
    private GeocodingResultService geocodingResultService;

    @Autowired
    @InjectMocks
    private LocationSimilarityCounter locationSimilarityCounter;

    private MatchingContext matchingContext;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        matchingContext = new MatchingContext(geoApiMock);
    }

    @Test
    public void shouldReturnMinRatioForDistantAddresses() {
        Location location1 = locationOf("North Pole", 0, 0);
        Location location2 = locationOf("South Pole", 40, 40);

        assertEquals(6012092, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(40, 40)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnMinRatioForAddressesWith1001MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", 1.009, 1);
        assertEquals(1001, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.009, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith1000MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", 1.00899, 1);
        assertEquals(1000, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.00899, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(0.4)));
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith501MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", 1.00451, 1);
        assertEquals(501, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.00451, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(0.4)));
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith500MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", 1.0044966, 1);
        assertEquals(500, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.0044966, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(0.8)));
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith499MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", 1.00449, 1);
        assertEquals(499, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.00449, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(0.8)));
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith101MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", 1.00091, 1);
        assertEquals(101, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.00091, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(0.8)));
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith100MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344",1.0008993, 1);
        assertEquals(100, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.0008993, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith99MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", 1.00089, 1);
        assertEquals(99, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1.00089, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldReturnMaxRatioForTheSameAddress() {
        Location location1 = locationOf("494 Super Street", 1, 1);
        Location location2 = locationOf("Super Street 494", 1, 1);
        assertEquals(0, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1, 1)), DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    private Location locationOf(String address, double lat, double lng) {
        PhysicalAddress physicalAddress = new PhysicalAddress().address1(address);
        GeocodingResult geocodingResult = new GeocodingResult().address(address).latitude(lat).longitude(lng);
        return new Location().name(address).physicalAddress(physicalAddress).latitude(lat).longitude(lng)
            .geocodingResults(Collections.singletonList(geocodingResult));
    }
}
