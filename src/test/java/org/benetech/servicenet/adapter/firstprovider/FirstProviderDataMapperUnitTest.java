package org.benetech.servicenet.adapter.firstprovider;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.firstprovider.model.RawData;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.Service;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FirstProviderDataMapperUnitTest {

    private RawData rawData;

    @Before
    public void init() throws IOException {
        String data = AdapterTestsUtils.readResourceAsString("FirstProviderDataSingleEntry.json");
        rawData = new Gson().fromJson(data, RawData.class);
    }

    @Test
    public void shouldExtractPhysicalAddressFromRawData() {
        PhysicalAddress extracted = FirstProviderDataMapper.INSTANCE.extractPhysicalAddress(rawData);
        assertEquals("1234 Address 2nd floor", extracted.getAddress1());
        assertEquals("Super City", extracted.getCity());
        assertEquals("CA", extracted.getStateProvince());
    }

    @Test
    public void shouldExtractPostalAddressFromRawData() {
        PostalAddress extracted = FirstProviderDataMapper.INSTANCE.extractPostalAddress(rawData);
        assertEquals("1234 Address 2nd floor", extracted.getAddress1());
        assertEquals("Super City", extracted.getCity());
        assertEquals("CA", extracted.getStateProvince());
    }

    @Test
    public void shouldExtractLocationFromRawData() {
        Location extracted = FirstProviderDataMapper.INSTANCE.extractLocation(rawData);
        String expectedName = LocationUtils.buildLocationName("Super City", "CA", "1234 Address 2nd floor");
        assertEquals(expectedName, extracted.getName());
        assertEquals(Double.valueOf("30.7149303"), extracted.getLatitude());
        assertEquals(Double.valueOf("-180.0893568"), extracted.getLongitude());
    }

    @Test
    public void shouldExtractPhoneFromRawData() {
        Phone extracted = FirstProviderDataMapper.INSTANCE.extractPhone(rawData);
        assertEquals("12345671234", extracted.getNumber());
        assertEquals(48, (int) extracted.getExtension());
    }

    @Test
    public void shouldExtractOrganizationFromRawData() {
        Organization extracted = FirstProviderDataMapper.INSTANCE.extractOrganization(rawData);
        assertEquals("e@mail.com", extracted.getEmail());
        assertEquals("Org name", extracted.getName());
    }

    @Test
    public void shouldExtractEligibilityFromRawData() {
        Eligibility extracted = FirstProviderDataMapper.INSTANCE.extractEligibility(rawData);
        assertEquals("Alzheimer's disease", extracted.getEligibility());
    }

    @Test
    public void shouldExtractServiceFromRawData() {
        Service extracted = FirstProviderDataMapper.INSTANCE.extractService(rawData);
        assertEquals("One + First", extracted.getName());
        assertEquals("https://www.example.com", extracted.getUrl());
        assertEquals("Required items: Id", extracted.getApplicationProcess());
        assertEquals("from 4$ to 50$", extracted.getFees());
        assertEquals("description info", extracted.getDescription());
    }

    @Test
    public void shouldExtractProgramsFromRawData() {
        Set<Program> extracted = new LinkedHashSet<>(FirstProviderDataMapper.INSTANCE.extractPrograms(rawData));
        assertEquals(2, extracted.size());

        Iterator<Program> iterator = extracted.iterator();
        assertEquals("Medical care", iterator.next().getName());
        assertEquals("Memory care", iterator.next().getName());
    }

    @Test
    public void shouldExtractLangsFromRawData() {
        Set<Language> extracted = new LinkedHashSet<>(FirstProviderDataMapper.INSTANCE.extractLangs(rawData));
        assertEquals(2, extracted.size());

        Iterator<Language> iterator = extracted.iterator();
        assertEquals("English", iterator.next().getLanguage());
        assertEquals("Spanish", iterator.next().getLanguage());
    }

    @Test
    public void shouldExtractOpeningHoursFromRawData() {
        Set<OpeningHours> extracted = new LinkedHashSet<>(FirstProviderDataMapper.INSTANCE.extractOpeningHours(rawData));

        assertEquals(7, extracted.size());
        Iterator<OpeningHours> iterator = extracted.iterator();
        OpeningHours day = iterator.next();
        assertEquals(0, (int) day.getWeekday());
        assertEquals("09:00AM", day.getOpensAt());
        assertEquals("08:00PM", day.getClosesAt());

        while (iterator.hasNext()) {
            day = iterator.next();
        }

        assertEquals(6, (int) day.getWeekday());
        assertEquals("CLOSED", day.getOpensAt());
        assertNull(day.getClosesAt());
    }
}
