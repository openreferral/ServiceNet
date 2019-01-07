package org.benetech.servicenet.matching;

import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class LocationSimilarityCounterIntTest {

    private static final float PRECISION = 0.001f;
    private static final String DELIMITER = ", ";

    @Mock
    private GeoApi geoApiMock;

    @Autowired
    @InjectMocks
    private LocationSimilarityCounter locationSimilarityCounter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnMinRatioForDistantAddresses() {
        String address1 = "North Pole";
        String address2 = "South Pole";
        mockDistance(address1, address2, Long.MAX_VALUE);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(0, result, PRECISION);
    }

    @Test
    public void shouldReturnMinRatioForAddressesWith1001MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 1001);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(0, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith1000MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 1000);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(0.4, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel3RatioForAddressesWith501MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 501);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(0.4, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith500MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 500);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith499MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 499);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnLevel2RatioForAddressesWith101MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 101);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(0.8, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith100MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 100);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(1, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForAddressesWith99MetresDistance() {
        String address1 = "Super Street 356";
        String address2 = "Example Street 344";
        mockDistance(address1, address2, 99);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(1, result, PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForTheSameAddress() {
        String address1 = "494 Super Street";
        String address2 = "Super Street 494";
        mockDistance(address1, address2, 0);

        float result = locationSimilarityCounter.countSimilarityRatio(locationOf(address1), locationOf(address2));
        assertEquals(1, result, PRECISION);
    }

    private void mockDistance(String address1, String address2, long distance) {
        double lat1 = 10.723364;
        double lat2 = 20.839202;
        double lng1 = -30.853043;
        double lng2 = -40.123454;

        when(geoApiMock.geocode(address1)).thenReturn(geocodingOf(lat1, lng1));
        when(geoApiMock.geocode(address2)).thenReturn(geocodingOf(lat2, lng2));

        String[] array1 = new String[]{ lat1 + DELIMITER + lng1 };
        String[] array2 = new String[]{ lat2 + DELIMITER + lng2 };

        when(geoApiMock.getDistanceMatrix(array1, array2)).thenReturn(distanceOf(array1, array2, distance));
        when(geoApiMock.getDistanceMatrix(array2, array1)).thenReturn(distanceOf(array2, array1, distance));
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

    private Location locationOf(String address1) {
        PhysicalAddress physicalAddress1 = new PhysicalAddress().address1(address1);
        return new Location().physicalAddress(physicalAddress1);
    }
}
