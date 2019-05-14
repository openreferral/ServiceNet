package org.benetech.servicenet.adapter.shared.model;

import java.util.Arrays;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

public enum Weekday {
    MONDAY("MONDAY", "MON", "MO"),
    TUESDAY("TUESDAY", "TUE", "TU"),
    WEDNESDAY("WEDNESDAY", "WED", "WE"),
    THURSDAY("THURSDAY", "THU", "TH"),
    FRIDAY("FRIDAY", "FRI", "FR"),
    SATURDAY("SATURDAY", "SAT", "SA"),
    SUNDAY("SUNDAY", "SUN", "SU");

    private String[] weekdays;

    Weekday(String... weekdays) {
        this.weekdays = weekdays;
    }

    public String[] getWeekdays() {
        return weekdays;
    }

    public static Weekday getWeekday(String weekday) {
        if (StringUtils.isBlank(weekday)) {
            return null;
        }
        String cleaned = cleanedDay(weekday);

        for (Weekday day : Weekday.values()) {
            if (Arrays.asList(day.weekdays).contains(cleaned)) {
                return day;
            }
        }
        return null;
    }

    private static String cleanedDay(String day) {
        return day.trim().replaceAll("[^a-zA-Z]", "").toUpperCase(Locale.ROOT);
    }
}
