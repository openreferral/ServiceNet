package org.benetech.servicenet.matching;

import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.matching.counter.GeoApi;
import org.benetech.servicenet.matching.counter.LocationSimilarityCounter;
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

    @Mock
    private GeoApi geoApiMock;

    @Mock
    private GeocodingResultService geocodingResultService;

    @Autowired
    @InjectMocks
    private LocationSimilarityCounter locationSimilarityCounter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnMinRatioForDistantAddresses() {
        Location location1 = locationOf("North Pole");
        Location location2 = locationOf("South Pole");
        mockDistance(location1, location2, Long.MAX_VALUE);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result, PRECISION);
    }

    @Test
    public void shouldReturnMinRatioForAddressesWith1001MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 1001);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith1000MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 1000);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0.4, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith501MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 501);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0.4, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith500MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 500);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith499MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 499);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith101MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 101);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith100MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 100);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(1, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith99MetresDistance() {
        Location location1 = locationOf("Super Street 356");
        Location location2 = locationOf("Example Street 344");
        mockDistance(location1, location2, 99);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(1, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForTheSameAddress() {
        Location location1 = locationOf("494 Super Street");
        Location location2 = locationOf("Super Street 494");
        mockDistance(location1, location2, 0);

        float result = locationSimilarityCounter.countSimilarityRatio(location1, location2);
        assertEquals(1, result, PRECISION);
    }

    private void mockDistance(Location location1, Location location2, long distance) {
        double lat1 = 10.723364;
        double lat2 = 20.839202;
        double lng1 = -30.853043;
        double lng2 = -40.123454;

        PhysicalAddress physicalAddress1 = location1.getPhysicalAddress();
        PhysicalAddress physicalAddress2 = locationOf(location2.getName()).getPhysicalAddress();
        when(geoApiMock.extractAddressString(physicalAddress1)).thenReturn(location1.getName());
        when(geoApiMock.extractAddressString(physicalAddress2)).thenReturn(location2
            .getName());

        when(geoApiMock.geocode(location1)).thenReturn(geocodingOf(lat1, lng1));
        when(geoApiMock.geocode(locationOf(location2.getName()))).thenReturn(geocodingOf(lat2, lng2));

        LatLng latLng1 = new LatLng(lat1, lng1);
        LatLng latLng2 = new LatLng(lat2, lng2);

        when(geoApiMock.getDistanceMatrix(latLng1, latLng2)).thenReturn(distanceOf(new String[] {location1.getName()}, new String[] {location2.getName()}, distance));
        when(geoApiMock.getDistanceMatrix(latLng2, latLng1)).thenReturn(distanceOf(new String[] {location2.getName()}, new String[] {location1.getName()}, distance));

        when(geocodingResultService.findAllForLocationOrFetchIfEmpty(location1))
            .thenReturn(Collections.singletonList(new org.benetech.servicenet.domain.GeocodingResult(location1.getName(), lat1, lng1)));
        when(geocodingResultService.findAllForLocationOrFetchIfEmpty(location2))
            .thenReturn(Collections.singletonList(new org.benetech.servicenet.domain.GeocodingResult(location1.getName(), lat2, lng2)));
    }

    private GeocodingResult[] geocodingOf(double lat, double lng) {
        GeocodingResult result = new GeocodingResult();
        result.geometry = new Geometry();
        result.geometry.location = new LatLng();
        result.geometry.location.lng = lng;
        result.geometry.location.lat = lat;
        return new GeocodingResult[]{ result };
    }

    private DistanceMatrix distanceOf(String[] address1, String[] address2, long distance) {
        DistanceMatrixRow row = new DistanceMatrixRow();
        DistanceMatrixElement element = new DistanceMatrixElement();
        element.distance = new Distance();
        element.distance.inMeters = distance;
        row.elements = new DistanceMatrixElement[]{ element };
        return new DistanceMatrix(address1, address2, new DistanceMatrixRow[]{ row });
    }

    private Location locationOf(String address) {
        PhysicalAddress physicalAddress = new PhysicalAddress().address1(address);
        return new Location().name(address).physicalAddress(physicalAddress);
    }
}
