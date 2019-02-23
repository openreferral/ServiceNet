package org.benetech.servicenet.matching.counter;

import com.google.maps.model.LatLng;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

public final class GeocodeUtils {

    private static final int EARTH_DIAMETER = 12742000;

    public static double getStraightLineDistanceInMeters(LatLng coordinates1, LatLng coordinates2) {
        double latRadians = toRadians(coordinates2.lat - coordinates1.lat);
        double lngRadians = toRadians(coordinates2.lng - coordinates1.lng);
        double deg = pow(sin(latRadians / 2), 2) + cos(toRadians(coordinates1.lat))
            * cos(toRadians(coordinates2.lat)) * pow(sin(lngRadians / 2), 2);
        return EARTH_DIAMETER * Math.atan2(Math.sqrt(deg), Math.sqrt(1 - deg));
    }

    private GeocodeUtils() {
    }
}
