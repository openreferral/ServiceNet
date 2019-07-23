package org.benetech.servicenet.matching;

import com.google.maps.model.LatLng;
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

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class LocationSimilarityCounterIntTest {

    private static final float PRECISION = 0.001f;
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
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result, PRECISION);
    }

    @Test
    public void shouldReturnMinRatioForAddressesWith1001MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344", 0.009, 0);
        assertEquals(1001, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.009, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith1000MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344", 0.00899, 0);
        assertEquals(1000, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.00899, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0.4, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith501MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344", 0.00451, 0);
        assertEquals(501, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.00451, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0.4, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith500MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344", 0.0044966, 0);
        assertEquals(500, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.0044966, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith499MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344", 0.00449, 0);
        assertEquals(499, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.00449, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith101MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344", 0.00091, 0);
        assertEquals(101, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.00091, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith100MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344",0.0008993, 0);
        assertEquals(100, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.0008993, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(1, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith99MetresDistance() {
        Location location1 = locationOf("Super Street 356", 0, 0);
        Location location2 = locationOf("Example Street 344", 0.00089, 0);
        assertEquals(99, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0.00089, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(1, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForTheSameAddress() {
        Location location1 = locationOf("494 Super Street", 0, 0);
        Location location2 = locationOf("Super Street 494", 0, 0);
        assertEquals(0, GeocodeUtils.getStraightLineDistanceInMeters(new LatLng(0, 0), new LatLng(0, 0)), DISTANCE_PRECISION);
        mockGeoService(location1, location2);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2, matchingContext);
        assertEquals(1, result, PRECISION);
    }

    private void mockGeoService(Location location1, Location location2) {
        PhysicalAddress physicalAddress1 = location1.getPhysicalAddress();
        PhysicalAddress physicalAddress2 = location2.getPhysicalAddress();
        when(geoApiMock.extract255AddressChars(physicalAddress1)).thenReturn(location1.getName());
        when(geoApiMock.extract255AddressChars(physicalAddress2)).thenReturn(location2
            .getName());

        when(geocodingResultService.findAllForAddressOrFetchIfEmpty(physicalAddress1, matchingContext))
            .thenReturn(Collections.singletonList(new org.benetech.servicenet.domain.GeocodingResult(location1.getName(), location1.getLatitude(), location1.getLongitude())));
        when(geocodingResultService.findAllForAddressOrFetchIfEmpty(physicalAddress2, matchingContext))
            .thenReturn(Collections.singletonList(new org.benetech.servicenet.domain.GeocodingResult(location2.getName(), location2.getLatitude(), location2.getLongitude())));
    }

    private Location locationOf(String address, double lat, double lng) {
        PhysicalAddress physicalAddress = new PhysicalAddress().address1(address);
        GeocodingResult geocodingResult = new GeocodingResult().address(address).latitude(lat).longitude(lng);
        return new Location().name(address).physicalAddress(physicalAddress).latitude(lat).longitude(lng)
            .geocodingResults(Collections.singletonList(geocodingResult));
    }
}
