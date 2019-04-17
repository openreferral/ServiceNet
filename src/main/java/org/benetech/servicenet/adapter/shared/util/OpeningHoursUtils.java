package org.benetech.servicenet.adapter.shared.util;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.shared.model.Hours;

import java.util.Optional;

public final class OpeningHoursUtils {

    private static final String TIME_SEPARATOR = ":";
    private static final String DEFAULT_MINUTES = "00";

    public static Optional<Hours> getHoursFromString(String data, String delimiter) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(delimiter)) {
            return Optional.empty();
        }
        Hours result = new Hours();
        String[] parts = data.split(delimiter);
        if (parts.length != 2) {
            result.setOpen(data);
        } else {
            result.setOpen(parts[0]);
            result.setClose(parts[1]);
        }
        return Optional.of(result);
    }

    public static String normalizeTime(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }

        String[] timeParts = time.split(TIME_SEPARATOR);

        if (timeParts.length == 1) {
            return formatMinutesOrHours(timeParts[0]) + TIME_SEPARATOR + DEFAULT_MINUTES;
        }

        return formatMinutesOrHours(timeParts[0]) + TIME_SEPARATOR + formatMinutesOrHours(timeParts[1]);
    }

    private static String formatMinutesOrHours(String time) {
        return String.format("%02d", Integer.valueOf(time));
    }

    private OpeningHoursUtils() {
    }
}
