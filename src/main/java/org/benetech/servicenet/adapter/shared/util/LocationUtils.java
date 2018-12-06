package org.benetech.servicenet.adapter.shared.util;

import org.benetech.servicenet.adapter.shared.model.Coordinates;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public final class LocationUtils {

    public static Optional<Coordinates> getCoordinatesFromString(String data, String delimiter) {
        try {
            Coordinates result = new Coordinates();
            String[] parts = data.split(delimiter);
            result.setLatitude(Double.valueOf(parts[0]));
            result.setLongitude(Double.valueOf(parts[1]));
            return Optional.of(result);
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
            return Optional.empty();
        }
    }

    public static String buildLocationName(@NotNull String city, @NotNull String state, @NotNull String address1) {
        return address1 + " - " + city + " (" + state + ")";
    }

    private LocationUtils() {
    }
}
