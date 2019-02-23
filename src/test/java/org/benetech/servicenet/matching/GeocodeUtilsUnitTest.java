package org.benetech.servicenet.matching;

import com.google.maps.model.LatLng;
import org.benetech.servicenet.matching.counter.GeocodeUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeocodeUtilsUnitTest {

    private static final float PRECISION = 0.1f;

    @Test
    public void shouldReturnProperDistanceBetweenToPhysicalPoints() {
        LatLng coordinates1 = new LatLng(20, 160);
        LatLng coordinates2 = new LatLng(-20, -160);

        assertEquals(6224890.0, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenToPhysicalPoints2() {
        LatLng coordinates1 = new LatLng(54.878, 34.434);
        LatLng coordinates2 = new LatLng(-32.382, 33.2233);

        assertEquals(9703561.1, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenToPhysicalPointsWithPreciseFloatingPoint() {
        LatLng coordinates1 = new LatLng(54.787951, -117.456054);
        LatLng coordinates2 = new LatLng(51.343022, -106.642270);

        assertEquals(816594.2, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenOppositeMapPoints() {
        LatLng coordinates1 = new LatLng(0, 180);
        LatLng coordinates2 = new LatLng(0, -180);

        assertEquals(0, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenToSamePoints() {
        LatLng coordinates1 = new LatLng(10, 60);
        LatLng coordinates2 = new LatLng(10, 60);

        assertEquals(0, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }
}
