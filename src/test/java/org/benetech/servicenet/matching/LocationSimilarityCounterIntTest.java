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
import org.benetech.servicenet.matching.counter.GeocodeUtils;
import org.benetech.servicenet.matching.counter.LocationSimilarityCounter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class LocationSimilarityCounterIntTest {

    private static final float DISTANCE_PRECISION = 0.5f;

    private static final int NORTH_LAT_LNG = 0;
    private static final int SOUTH_LAT_LNG = 40;

    private static final int DISTANCE_1 = 6012092;
    private static final int DISTANCE_2 = 499;
    private static final int DISTANCE_3 = 500;
    private static final int DISTANCE_4 = 501;
    private static final int DISTANCE_5 = 101;
    private static final int DISTANCE_6 = 99;

    private static final double LAT_1 = 1.009;
    private static final double LAT_2 = 1.00899;
    private static final double LAT_3 = 1.00451;
    private static final double LAT_4 = 1.0044966;
    private static final double LAT_5 = 1.00449;
    private static final double LAT_6 = 1.00091;
    private static final double LAT_7 = 1.0008993;
    private static final double LAT_8 = 1.00089;

    private static final double LAT_TURK = 37.7814563;
    private static final double LNG_TURK = -122.4225255;
    private static final double LAT_WF = 37.781465;
    private static final double LNG_WF = -122.4225301;

    private static final int EXPECTED_1 = 1001;

    private static final BigDecimal SIMILARITY_04 = BigDecimal.valueOf(0.4);
    private static final BigDecimal SIMILARITY_08 = BigDecimal.valueOf(0.8);

    @Autowired
    @InjectMocks
    private LocationSimilarityCounter locationSimilarityCounter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnMinRatioForDistantAddresses() {
        Location location1 = locationOf("North Pole", NORTH_LAT_LNG, NORTH_LAT_LNG);
        Location location2 = locationOf("South Pole", SOUTH_LAT_LNG, SOUTH_LAT_LNG);

        assertEquals(
            DISTANCE_1,
            GeocodeUtils.getStraightLineDistanceInMeters(
                new LatLng(NORTH_LAT_LNG, NORTH_LAT_LNG),
                new LatLng(SOUTH_LAT_LNG, SOUTH_LAT_LNG)
            ),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnMinRatioForAddressesWith1001MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_1, 1);
        assertEquals(EXPECTED_1, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_1, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith1000MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_2, 1);
        assertEquals(1000, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_2, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(SIMILARITY_04));
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith501MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_3, 1);
        assertEquals(DISTANCE_4, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_3, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(SIMILARITY_04));
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith500MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_4, 1);
        assertEquals(DISTANCE_3, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_4, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(SIMILARITY_08));
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith499MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_5, 1);
        assertEquals(DISTANCE_2, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_5, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(SIMILARITY_08));
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith101MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_6, 1);
        assertEquals(DISTANCE_5, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_6, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(SIMILARITY_08));
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith100MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_7, 1);
        assertEquals(100, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_7, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith99MetresDistance() {
        Location location1 = locationOf("Super Street 356", 1, 1);
        Location location2 = locationOf("Example Street 344", LAT_8, 1);
        assertEquals(DISTANCE_6, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(LAT_8, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldReturnMaxRatioForTheSameAddress() {
        Location location1 = locationOf("494 Super Street", 1, 1);
        Location location2 = locationOf("Super Street 494", 1, 1);
        assertEquals(0, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(1, 1), new LatLng(1, 1)),
            DISTANCE_PRECISION);

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldMatchALocationWithDifferentNames() {
        Location location1 = locationOf("801 Turk Street San Francisco", LAT_TURK, LNG_TURK);
        location1.setName("801 Turk Street - San Francisco (CA)");
        Location location2 = locationOf("801 Turk Street San Francisco", LAT_WF, LNG_WF);
        location2.setName("EDD Workforce Service - San Francisco Civic Center");

        BigDecimal result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    private Location locationOf(String address, double lat, double lng) {
        PhysicalAddress physicalAddress = new PhysicalAddress().address1(address);
        GeocodingResult geocodingResult = new GeocodingResult().address(address).latitude(lat).longitude(lng);
        return new Location().name(address).physicalAddress(physicalAddress).latitude(lat).longitude(lng)
            .geocodingResults(Collections.singletonList(geocodingResult));
    }
}
