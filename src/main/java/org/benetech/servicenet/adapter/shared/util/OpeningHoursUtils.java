package org.benetech.servicenet.adapter.shared.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.shared.model.Hours;

import java.util.Optional;
import org.benetech.servicenet.adapter.shared.model.Weekday;

public final class OpeningHoursUtils {

    private static final String TIME_SEPARATOR = ":";
    private static final String DEFAULT_MINUTES = "00";

    private static final int HOURS_GROUP = 1;
    private static final int MINUTES_GROUP = 2;
    private static final int ABBREV_GROUP = 3;

    private static final int HOURS_ADDITION = 12;

    public static int getWeekday(String day) {
        Weekday weekday = Weekday.getWeekday(day);
        return weekday != null ? weekday.ordinal() : 0;
    }

    public static String normalizeTime(String untrimmedTime) {
        if (StringUtils.isBlank(untrimmedTime)) {
            return null;
        }

        String time = untrimmedTime.trim();

        // Check if matches known string pattern (eg. 8am, 6:30pm, 8, 6:30, 700, 1200)
        Pattern knownPattern = Pattern.compile("^[^0-9]*([0-9]{1,2}):?([0-9]{2})?[ ]*([Aa][Mm]|[Pp][Mm])?[^0-9]*$");
        Matcher match = knownPattern.matcher(time);

        if (!match.matches()) {
            // Return the input, whenever we don't have match with known pattern
            return time;
        }

        try {
            String hours = formatMinutesOrHours(match.group(HOURS_GROUP));
            String minutes = match.group(MINUTES_GROUP) != null ? match.group(MINUTES_GROUP) : DEFAULT_MINUTES;
            String abbrev = match.group(ABBREV_GROUP);

            if ("pm".equalsIgnoreCase(abbrev)) {
                return (Integer.valueOf(hours) + HOURS_ADDITION) + TIME_SEPARATOR + minutes;
            }
            return hours + TIME_SEPARATOR + minutes;
        } catch (Exception e) {
            // Return the input, whenever we get any type of exception
            return time;
        }
    }

    public static Optional<Hours> getHoursFromStringWithDelimiter(String data, String delimiter) {
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

    private static String formatMinutesOrHours(String time) {
        try {
            return String.format("%02d", Integer.valueOf(time));
        } catch (NumberFormatException e) {
            return time;
        }

    }

    private OpeningHoursUtils() {
    }
}
