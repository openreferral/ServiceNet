package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.mother.AccessibilityForDisabilitiesMother;
import org.benetech.servicenet.mother.HolidayScheduleMother;
import org.benetech.servicenet.mother.LocationMother;
import org.benetech.servicenet.mother.PhysicalAddressMother;
import org.benetech.servicenet.mother.PostalAddressMother;
import org.benetech.servicenet.mother.RegularScheduleMother;
import org.benetech.servicenet.service.dto.LocationRecordDTO;
import org.junit.Test;

import java.util.Set;

import static org.benetech.servicenet.mother.AccessibilityForDisabilitiesMother.DEFAULT_ACCESSIBILITY;
import static org.benetech.servicenet.mother.AccessibilityForDisabilitiesMother.DEFAULT_DETAILS;
import static org.benetech.servicenet.mother.AccessibilityForDisabilitiesMother.DIFFERENT_ACCESSIBILITY;
import static org.benetech.servicenet.mother.AccessibilityForDisabilitiesMother.DIFFERENT_DETAILS;
import static org.benetech.servicenet.mother.LocationMother.ALTERNATE_NAME;
import static org.benetech.servicenet.mother.LocationMother.DESCRIPTION;
import static org.benetech.servicenet.mother.LocationMother.LATITUDE;
import static org.benetech.servicenet.mother.LocationMother.LONGITUDE;
import static org.benetech.servicenet.mother.LocationMother.NAME;
import static org.benetech.servicenet.mother.LocationMother.TRANSPORTATION;
import static org.benetech.servicenet.mother.RegularScheduleMother.CLOSES_AT_1;
import static org.benetech.servicenet.mother.RegularScheduleMother.CLOSES_AT_2;
import static org.benetech.servicenet.mother.RegularScheduleMother.OPENS_AT_1;
import static org.benetech.servicenet.mother.RegularScheduleMother.OPENS_AT_2;
import static org.benetech.servicenet.mother.RegularScheduleMother.WEEKDAY_1;
import static org.benetech.servicenet.mother.RegularScheduleMother.WEEKDAY_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocationMapperTest {

    private LocationMapper locationMapper = new LocationMapperImpl();

    @Test
    public void shouldMapLocationToRecord() {
        Location entry = LocationMother.createDefault();

        LocationRecordDTO result = locationMapper.toRecord(entry);

        assertEquals(NAME, result.getLocation().getName());
        assertEquals(ALTERNATE_NAME, result.getLocation().getAlternateName());
        assertEquals(DESCRIPTION, result.getLocation().getDescription());
        assertEquals(TRANSPORTATION, result.getLocation().getTransportation());
        assertEquals((Double) LONGITUDE, result.getLocation().getLongitude());
        assertEquals((Double) LATITUDE, result.getLocation().getLatitude());
    }

    @Test
    public void shouldMapPhysicalAddressToRecord() {
        PhysicalAddress physicalAddress = PhysicalAddressMother.createDefault();
        Location entry = new Location().physicalAddress(physicalAddress);

        LocationRecordDTO result = locationMapper.toRecord(entry);

        assertEquals(PhysicalAddressMother.ADDRESS_1, result.getPhysicalAddress().getAddress1());
        assertEquals(PhysicalAddressMother.ATTENTION, result.getPhysicalAddress().getAttention());
        assertEquals(PhysicalAddressMother.CITY, result.getPhysicalAddress().getCity());
        assertEquals(PhysicalAddressMother.REGION, result.getPhysicalAddress().getRegion());
        assertEquals(PhysicalAddressMother.STATE_PROVINCE, result.getPhysicalAddress().getStateProvince());
        assertEquals(PhysicalAddressMother.POSTAL_CODE, result.getPhysicalAddress().getPostalCode());
        assertEquals(PhysicalAddressMother.COUNTRY, result.getPhysicalAddress().getCountry());
    }

    @Test
    public void shouldMapPostalAddressToRecord() {
        PostalAddress postalAddress = PostalAddressMother.createDefault();
        Location entry = new Location().postalAddress(postalAddress);

        LocationRecordDTO result = locationMapper.toRecord(entry);

        assertEquals(PostalAddressMother.ADDRESS_1, result.getPostalAddress().getAddress1());
        assertEquals(PostalAddressMother.ATTENTION, result.getPostalAddress().getAttention());
        assertEquals(PostalAddressMother.CITY, result.getPostalAddress().getCity());
        assertEquals(PostalAddressMother.REGION, result.getPostalAddress().getRegion());
        assertEquals(PostalAddressMother.STATE_PROVINCE, result.getPostalAddress().getStateProvince());
        assertEquals(PostalAddressMother.POSTAL_CODE, result.getPostalAddress().getPostalCode());
        assertEquals(PostalAddressMother.COUNTRY, result.getPostalAddress().getCountry());
    }

    @Test
    public void shouldMapRegularScheduleToRecord() {
        RegularSchedule schedule = RegularScheduleMother.createWithTwoOpeningHours();
        Location entry = new Location().regularSchedule(schedule);

        LocationRecordDTO result = locationMapper.toRecord(entry);

        assertTrue(result.getRegularScheduleOpeningHours().stream()
            .anyMatch(x ->
                x.getWeekday().equals(WEEKDAY_1)
                    && x.getOpensAt().equals(OPENS_AT_1)
                    && x.getClosesAt().equals(CLOSES_AT_1)));

        assertTrue(result.getRegularScheduleOpeningHours().stream()
            .anyMatch(x ->
                x.getWeekday().equals(WEEKDAY_2)
                    && x.getOpensAt().equals(OPENS_AT_2)
                    && x.getClosesAt().equals(CLOSES_AT_2)));

    }

    @Test
    public void shouldMapHolidayScheduleToRecord() {
        HolidaySchedule schedule = HolidayScheduleMother.createDefault();
        Location entry = new Location().holidaySchedule(schedule);

        LocationRecordDTO result = locationMapper.toRecord(entry);

        assertEquals(HolidayScheduleMother.START_DATE, result.getHolidaySchedule().getStartDate());
        assertEquals(HolidayScheduleMother.END_DATE, result.getHolidaySchedule().getEndDate());
        assertEquals(HolidayScheduleMother.OPENS_AT, result.getHolidaySchedule().getOpensAt());
        assertEquals(HolidayScheduleMother.CLOSES_AT, result.getHolidaySchedule().getClosesAt());
        assertEquals(HolidayScheduleMother.CLOSED, result.getHolidaySchedule().isClosed());
    }

    @Test
    public void shouldMapLanguagesToRecord() {
        Location entry = new Location().langs(Set.of(
            new Language().language("en"),
            new Language().language("de")));

        LocationRecordDTO result = locationMapper.toRecord(entry);

        assertTrue(result.getLangs().stream()
            .anyMatch(x ->
                x.getLanguage().equals("en")));

        assertTrue(result.getLangs().stream()
            .anyMatch(x ->
                x.getLanguage().equals("de")));

    }

    @Test
    public void shouldMapAccessibilityToRecord() {
        Location entry = new Location().accessibilities(Set.of(
            AccessibilityForDisabilitiesMother.createDefault(),
            AccessibilityForDisabilitiesMother.createDifferent()));

        LocationRecordDTO result = locationMapper.toRecord(entry);

        assertTrue(result.getAccessibilities().stream()
            .anyMatch(x ->
                x.getAccessibility().equals(DEFAULT_ACCESSIBILITY)
                    && x.getDetails().equals(DEFAULT_DETAILS)));

        assertTrue(result.getAccessibilities().stream()
            .anyMatch(x ->
                x.getAccessibility().equals(DIFFERENT_ACCESSIBILITY)
                    && x.getDetails().equals(DIFFERENT_DETAILS)));
    }
}
