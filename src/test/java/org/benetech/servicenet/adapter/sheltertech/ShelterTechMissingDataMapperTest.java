package org.benetech.servicenet.adapter.sheltertech;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechLocationMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechOrganizationMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechPhoneMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechPhysicalAddressMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechPostalAddressMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechRegularScheduleMapper;
import org.benetech.servicenet.adapter.sheltertech.mapper.ShelterTechServiceMapper;
import org.benetech.servicenet.adapter.sheltertech.model.AddressRaw;
import org.benetech.servicenet.adapter.sheltertech.model.OrganizationRaw;
import org.benetech.servicenet.adapter.sheltertech.model.PhoneRaw;
import org.benetech.servicenet.adapter.sheltertech.model.ScheduleRaw;
import org.benetech.servicenet.adapter.sheltertech.model.ServiceRaw;
import org.benetech.servicenet.adapter.sheltertech.model.ShelterTechRawData;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ShelterTechMissingDataMapperTest {

    private static final String MINIMAL_JSON = "sheltertech/minimal.json";

    private OrganizationRaw data;

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL_JSON);
        ShelterTechRawData entry = new Gson().fromJson(json, ShelterTechRawData.class);
        data = entry.getOrganizations().get(0);
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForOrganization() {
        Organization result = ShelterTechOrganizationMapper.INSTANCE
            .mapToOrganization(data, null);

        assertEquals("The Organization", result.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullOrganizationName() {
        data.setName(null);
        ShelterTechOrganizationMapper.INSTANCE
            .mapToOrganization(data, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForEmptyOrganizationName() {
        data.setName("");
        ShelterTechOrganizationMapper.INSTANCE
            .mapToOrganization(data, null);
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForService() {
        Service result = ShelterTechServiceMapper.INSTANCE
            .mapToService(data.getServices().get(0)).get();

        assertEquals("Service Name", result.getName());
    }

    @Test
    public void shouldReturnNullForNullForService() {
        Optional<Service> result = ShelterTechServiceMapper.INSTANCE
            .mapToService(null);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnNullForEmptyService() {
        Optional<Service> result = ShelterTechServiceMapper.INSTANCE
            .mapToService(new ServiceRaw());

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForLocation() {
        Location result = ShelterTechLocationMapper.INSTANCE.mapToLocation(data.getAddress()).get();

        assertEquals("Line 1 - City (STATE)", result.getName());
    }

    @Test
    public void shouldReturnNullForNullAddress() {
        Optional<Location> result = ShelterTechLocationMapper.INSTANCE.mapToLocation(null);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnNullForEmptyLocation() {
        Optional<Location> result = ShelterTechLocationMapper.INSTANCE.mapToLocation(new AddressRaw());

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForEligibility() {
        Eligibility result = ShelterTechServiceMapper.INSTANCE
            .eligibilityFromString(data.getServices().get(0).getEligibility());

        assertEquals("Service Eligibility", result.getEligibility());
    }

    @Test
    public void shouldReturnNullForNullEligibilityString() {
        Eligibility result = ShelterTechServiceMapper.INSTANCE
            .eligibilityFromString(null);

        assertNull(result);
    }

    @Test
    public void shouldReturnNullForEmptyEligibilityString() {
        Eligibility result = ShelterTechServiceMapper.INSTANCE
            .eligibilityFromString("");

        assertNull(result);
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhone() {
        Phone result = ShelterTechPhoneMapper.INSTANCE.mapToPhone(data.getPhones().get(0));

        assertEquals("(111) 222-3333", result.getNumber());
    }

    @Test
    public void shouldReturnNullIfPhoneIsNull() {
        Phone result = ShelterTechPhoneMapper.INSTANCE.mapToPhone(null);

        assertNull(result);
    }

    @Test
    public void shouldReturnNullIfPhoneNumberIsEmpty() {
        Phone result = ShelterTechPhoneMapper.INSTANCE.mapToPhone(new PhoneRaw());

        assertNull(result);
    }

    @Test
    public void shouldReturnNullIfPhoneNumberIsEmptyString() {
        PhoneRaw emptyNumberPhone = new PhoneRaw();
        emptyNumberPhone.setNumber("");
        Phone result = ShelterTechPhoneMapper.INSTANCE.mapToPhone(emptyNumberPhone);

        assertNull(result);
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhysicalAddress() {
        PhysicalAddress result = ShelterTechPhysicalAddressMapper.INSTANCE
            .mapAddressRawToPhysicalAddress(data.getAddress());

        assertEquals("Line 1", result.getAddress1());
        assertEquals("City", result.getCity());
        assertEquals("STATE", result.getStateProvince());
    }

    @Test
    public void shouldReturnNullForNullPhysicalAddress() {
        PhysicalAddress result = ShelterTechPhysicalAddressMapper.INSTANCE
            .mapAddressRawToPhysicalAddress(null);

        assertNull(result);
    }

    @Test
    public void shouldReturnNullForEmptyPhysicalAddress() {
        PhysicalAddress result = ShelterTechPhysicalAddressMapper.INSTANCE
            .mapAddressRawToPhysicalAddress(new AddressRaw());

        assertNull(result);
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPostalAddress() {
        PostalAddress result = ShelterTechPostalAddressMapper.INSTANCE
            .mapAddressRawToPostalAddress(data.getAddress());

        assertEquals("Line 1", result.getAddress1());
        assertEquals("City", result.getCity());
        assertEquals("STATE", result.getStateProvince());
    }

    @Test
    public void shouldReturnNullForNullPostalAddress() {
        PostalAddress result = ShelterTechPostalAddressMapper.INSTANCE
            .mapAddressRawToPostalAddress(null);

        assertNull(result);
    }

    @Test
    public void shouldReturnNullForEmptyPostalAddress() {
        PostalAddress result = ShelterTechPostalAddressMapper.INSTANCE
            .mapAddressRawToPostalAddress(new AddressRaw());

        assertNull(result);
    }

    @Test
    public void shouldReturnNullForNullRegularSchedule() {
        RegularSchedule result = ShelterTechRegularScheduleMapper.INSTANCE
            .mapToRegularSchedule(null);

        assertNull(result);
    }

    @Test
    public void shouldReturnNullForEmptySchedule() {
        RegularSchedule result = ShelterTechRegularScheduleMapper.INSTANCE
            .mapToRegularSchedule(new ScheduleRaw());

        assertNull(result);
    }

    @Test
    public void shouldReturnNullForNullRequiredDocumentsString() {
        Set<RequiredDocument> result = ShelterTechServiceMapper.INSTANCE
            .docsFromString(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptySetForEmptyRequiredDocumentsString() {
        Set<RequiredDocument> result = ShelterTechServiceMapper.INSTANCE
            .docsFromString("");

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptySetForNullOpeningHours() {
        Set<OpeningHours> result = ShelterTechRegularScheduleMapper.INSTANCE
            .openingHoursFromScheduleDaysRaw(null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptySetForEmptyOpeningHours() {
        Set<OpeningHours> result = ShelterTechRegularScheduleMapper.INSTANCE
            .openingHoursFromScheduleDaysRaw(new ArrayList<>());

        assertTrue(result.isEmpty());
    }
}
