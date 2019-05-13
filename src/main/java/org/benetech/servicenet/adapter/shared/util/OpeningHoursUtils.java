package org.benetech.servicenet.adapter.shared.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.shared.model.Hours;

import java.util.Optional;
import org.benetech.servicenet.adapter.shared.model.Weekday;
import org.benetech.servicenet.adapter.shared.model.WeekdayMid;
import org.benetech.servicenet.adapter.shared.model.WeekdayShort;

public final class OpeningHoursUtils {

    private static final String TIME_SEPARATOR = ":";
    private static final String DEFAULT_MINUTES = "00";

    private static final int START = 0;
    private static final int MID = 1;
    private static final int END = 3;

    private static final int SHORT_LENGTH = 2;
    private static final int MID_LENGTH = 3;

    private static final int HOURS_ADDITION = 12;

    public static int getWeekday(String weekday) {
        if (StringUtils.isNotBlank(weekday)) {
            String cleaned = cleanedDay(weekday);
            switch (cleaned.length()) {
                case MID_LENGTH: return WeekdayMid.valueOf(cleaned).ordinal();
                case SHORT_LENGTH: return WeekdayShort.valueOf(cleaned).ordinal();
                default: return Weekday.valueOf(cleaned).ordinal();
            }
        }

        return 0;
    }

    public static String normalizeTime(String untrimmedTime) {
        if (StringUtils.isBlank(untrimmedTime)) {
            return null;
        }

        String time = untrimmedTime.trim();

        // Check if matches known string pattern (eg. 8am, 6:30pm, 8, 6:30, 700, 1200)
        Pattern knownPattern = Pattern.compile("[0-9:]{1,5}(am|pm)?$|\\d{1,4}");
        Matcher match = knownPattern.matcher(time);

        if (!match.matches()) {
            // Return the input, whenever we don't have match with known pattern
            return time;
        }

        // Check if there are 3 or 4 digits and handle as Int
        Pattern onlyDigits = Pattern.compile("\\d{3,4}");
        Matcher digitsMatch = onlyDigits.matcher(time);

        if (digitsMatch.matches()) {
            return getHoursFromInteger(Integer.valueOf(time));
        }

        try {
            String cleanedTime = "";

            if (time.contains("am")) {
                cleanedTime = time.replace("am", "");
            }

            String[] timeParts = !cleanedTime.isEmpty() ? cleanedTime.split(TIME_SEPARATOR) : time.split(TIME_SEPARATOR);

            if (time.contains("pm")) {
                int lastIdx = timeParts.length - 1;
                timeParts[lastIdx] = timeParts[lastIdx].replace("pm", "");
                timeParts[0] = String.valueOf(Integer.valueOf(timeParts[0]) + HOURS_ADDITION);
            }

            if (timeParts.length == 1) {
                return formatMinutesOrHours(timeParts[0]) + TIME_SEPARATOR + DEFAULT_MINUTES;
            }

            return formatMinutesOrHours(timeParts[0]) + TIME_SEPARATOR + formatMinutesOrHours(timeParts[1]);
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

    public static String getHoursFromInteger(Integer time) {
        String result = null;
        if (time != null) {
            String nonFormattedTime = time.toString();
            if (StringUtils.isNotBlank(nonFormattedTime)) {
                result = getFormattedTime(nonFormattedTime);
            }
        }

        return result;
    }

    private static String getFormattedTime(String nonFormatted) {
        String result;
        Pattern fullHourSchema = Pattern.compile("[0-9]{4}");
        Matcher fullHourMatcher = fullHourSchema.matcher(nonFormatted);
        Pattern hourSchema = Pattern.compile("[0-9]{3}");
        Matcher hourMatcher = hourSchema.matcher(nonFormatted);

        if (fullHourMatcher.find()) {
            result = formatFromTwoDigitHour(fullHourMatcher.group());
        } else if (hourMatcher.find()) {
            result = formatFromOneDigitHour(hourMatcher.group());
        } else {
            result = nonFormatted;
        }

        return result;
    }

    private static String formatFromOneDigitHour(String hourAndMin) {
        return "0" + StringUtils.mid(hourAndMin, START, MID) + ":" + StringUtils.mid(hourAndMin, MID, END);
    }

    private static String formatFromTwoDigitHour(String hourAndMin) {
        return StringUtils.mid(hourAndMin, START, MID + 1) + ":" + StringUtils.mid(hourAndMin, MID + 1, END + 1);
    }

    private static String formatMinutesOrHours(String time) {
        try {
            return String.format("%02d", Integer.valueOf(time));
        } catch (NumberFormatException e) {
            return time;
        }

    }

    private static String cleanedDay(String day) {
        return day.trim().replaceAll("[^a-zA-Z]", "").toUpperCase(Locale.ROOT);
    }

    private OpeningHoursUtils() {
    }
}
