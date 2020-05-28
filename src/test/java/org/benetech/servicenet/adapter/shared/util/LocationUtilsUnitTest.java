package org.benetech.servicenet.adapter.shared.util;

import org.benetech.servicenet.adapter.shared.model.Coordinates;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocationUtilsUnitTest {

    private static final double LAT = 54.678983;
    private static final double LNG = 12.332445;
    private static final double PRECISION = 0.000001;

    @Test
    public void shouldReturnCoordinatesFromString() {
        Optional<Coordinates> result = LocationUtils.getCoordinatesFromString("54.678983,12.332445", ",");
        assertTrue(result.isPresent());
        assertEquals(LAT, result.get().getLatitude(), PRECISION);
        assertEquals(LNG, result.get().getLongitude(), PRECISION);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowAnErrorReturnIfUnableToParseCoordinates() {
        LocationUtils.getCoordinatesFromString("54.67.89.83,12.33.24.45", ",");
    }

    @Test
    public void shouldBuildProperLocationName() {
        String result = LocationUtils.buildLocationName("City", "State", "Address");
        assertEquals("Address - City (State)", result);
    }
}
