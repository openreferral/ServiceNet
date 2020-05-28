package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.HolidaySchedule;

import java.time.LocalDate;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public final class HolidayScheduleMother {

    public static final LocalDate END_DATE = LocalDate.of(2013, 1, 10);
    public static final LocalDate START_DATE = LocalDate.of(2012, 12, 20);
    public static final String CLOSES_AT = "13:32";
    public static final String OPENS_AT = "08:00";
    public static final boolean CLOSED = true;

    public static HolidaySchedule createDefault() {
        return new HolidaySchedule()
            .closed(CLOSED)
            .opensAt(OPENS_AT)
            .closesAt(CLOSES_AT)
            .startDate(START_DATE)
            .endDate(END_DATE);
    }
}
