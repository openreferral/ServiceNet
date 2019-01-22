package org.benetech.servicenet.adapter.sheltertech;

import org.benetech.servicenet.adapter.eden.model.Weekday;

import java.util.Map;
import java.util.TreeMap;

public final class ShelterTechConstants {

    public static final String PROVIDER_NAME = "ShelterTech";

    public static final Map<String, Weekday> WEEKDAYS;

    static {
        WEEKDAYS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        WEEKDAYS.put("Monday", Weekday.MON);
        WEEKDAYS.put("Tuesday", Weekday.TUE);
        WEEKDAYS.put("Wednesday", Weekday.WED);
        WEEKDAYS.put("Thursday", Weekday.THU);
        WEEKDAYS.put("Friday", Weekday.FRI);
        WEEKDAYS.put("Saturday", Weekday.SAT);
        WEEKDAYS.put("Sunday", Weekday.SUN);
    }

    private ShelterTechConstants() {
    }
}
