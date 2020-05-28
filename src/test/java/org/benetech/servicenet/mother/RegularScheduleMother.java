package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.RegularSchedule;

import java.util.Set;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class RegularScheduleMother {

    public static final int WEEKDAY_1 = 0;
    public static final int WEEKDAY_2 = 1;
    public static final String OPENS_AT_1 = "00:00";
    public static final String OPENS_AT_2 = "01:33";
    public static final String CLOSES_AT_1 = "07:40";
    public static final String CLOSES_AT_2 = "12:32";

    public static RegularSchedule createWithTwoOpeningHours() {
        return new RegularSchedule()
            .openingHours(getTwoOpeningHours());
    }

    private static Set<OpeningHours> getTwoOpeningHours() {
        return Set.of(
            new OpeningHours().weekday(WEEKDAY_1).opensAt(OPENS_AT_1).closesAt(CLOSES_AT_1),
            new OpeningHours().weekday(WEEKDAY_2).opensAt(OPENS_AT_2).closesAt(CLOSES_AT_2));
    }
}
