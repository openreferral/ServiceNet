package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.sheltertech.model.ScheduleDayRaw;
import org.benetech.servicenet.adapter.sheltertech.model.ScheduleRaw;
import org.benetech.servicenet.adapter.sheltertech.model.ShelterTechWeekday;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.RegularSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mapper
public interface ShelterTechRegularScheduleMapper {

    ShelterTechRegularScheduleMapper INSTANCE = Mappers.getMapper(ShelterTechRegularScheduleMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "srvc")
    @Mapping(ignore = true, target = "location")
    @Mapping(ignore = true, target = "serviceAtlocation")
    @Mapping(source = "scheduleDays", target = "openingHours", qualifiedByName = "openingHoursFromScheduleDaysRaw")
    RegularSchedule mapToRegularSchedule(ScheduleRaw scheduleRaw);

    @Named("openingHoursFromScheduleDaysRaw")
    default Set<OpeningHours> openingHoursFromScheduleDaysRaw(List<ScheduleDayRaw> days) {
        Set<OpeningHours> hours =  new HashSet<>();
        for (ScheduleDayRaw raw : days) {
            hours.add(INSTANCE.mapToOpeningHours(raw));
        }

        return hours;
    }

    default OpeningHours mapToOpeningHours(ScheduleDayRaw scheduleRaw) {
        return new OpeningHours()
            .opensAt(hoursFromInteger(scheduleRaw.getOpensAt()))
            .closesAt(hoursFromInteger(scheduleRaw.getClosesAt()))
            .weekday(weekdaysFromScheduleDays(scheduleRaw.getScheduleDays()));
    }

    default Integer weekdaysFromScheduleDays(String day) {
        if (StringUtils.isNotBlank(day)) {
            return ShelterTechWeekday.valueOf(day.toUpperCase(Locale.ROOT)).ordinal();
        }

        return null;
    }

    default String hoursFromInteger(Integer time) {
        String result = null;
        if (time != null) {
            String nonFormattedTime = time.toString();
            if (StringUtils.isNotBlank(nonFormattedTime)) {
                result = getFormattedTime(nonFormattedTime);
            }
        }

        return result;
    }

    default String getFormattedTime(String nonFormatted) {
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

    default String formatFromOneDigitHour(String hourAndMin) {
        int start = 0;
        int mid = 1;
        int end = 3;
        return "0" + StringUtils.mid(hourAndMin, start, mid) + ":" + StringUtils.mid(hourAndMin, mid, end);
    }

    default String formatFromTwoDigitHour(String hourAndMin) {
        int start = 0;
        int mid = 2;
        int end = 4;
        return StringUtils.mid(hourAndMin, start, mid) + ":" + StringUtils.mid(hourAndMin, mid, end);
    }

}
