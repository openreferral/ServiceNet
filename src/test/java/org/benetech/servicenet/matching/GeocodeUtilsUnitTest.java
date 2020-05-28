package org.benetech.servicenet.matching;

import com.google.maps.model.LatLng;
import org.benetech.servicenet.matching.counter.GeocodeUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeocodeUtilsUnitTest {

    private static final float PRECISION = 0.1f;

    private static final float LAT1_1 = 20;
    private static final float LNG1_1 = 160;
    private static final float LAT2_1 = -20;
    private static final float LNG2_1 = -160;
    private static final double EXPECTED_1 = 6224890.0;

    private static final double LAT1_2 = 54.878;
    private static final double LNG1_2 = 34.434;
    private static final double LAT2_2 = -32.382;
    private static final double LNG2_2 = 33.2233;
    private static final double EXPECTED_2 = 9703561.1;

    private static final double LAT1_3 = 54.787951;
    private static final double LNG1_3 = -117.456054;
    private static final double LAT2_3 = 51.343022;
    private static final double LNG2_3 = -106.642270;
    private static final double EXPECTED_3 = 816594.2;

    private static final float LNG1_4 = 180;
    private static final float LNG2_4 = -180;

    private static final float LAT1_5 = 10;

    @Test
    public void shouldReturnProperDistanceBetweenToPhysicalPoints() {
        LatLng coordinates1 = new LatLng(LAT1_1, LNG1_1);
        LatLng coordinates2 = new LatLng(LAT2_1, LNG2_1);

        assertEquals(EXPECTED_1, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenToPhysicalPoints2() {
        LatLng coordinates1 = new LatLng(LAT1_2, LNG1_2);
        LatLng coordinates2 = new LatLng(LAT2_2, LNG2_2);

        assertEquals(EXPECTED_2, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenToPhysicalPointsWithPreciseFloatingPoint() {
        LatLng coordinates1 = new LatLng(LAT1_3, LNG1_3);
        LatLng coordinates2 = new LatLng(LAT2_3, LNG2_3);

        assertEquals(EXPECTED_3, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenOppositeMapPoints() {
        LatLng coordinates1 = new LatLng(0, LNG1_4);
        LatLng coordinates2 = new LatLng(0, LNG2_4);

        assertEquals(0, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }

    @Test
    public void shouldReturnProperDistanceBetweenToSamePoints() {
        LatLng coordinates1 = new LatLng(LAT1_5, 60);
        LatLng coordinates2 = new LatLng(LAT1_5, 60);

        assertEquals(0, GeocodeUtils.getStraightLineDistanceInMeters(coordinates1, coordinates2), PRECISION);
    }
}
