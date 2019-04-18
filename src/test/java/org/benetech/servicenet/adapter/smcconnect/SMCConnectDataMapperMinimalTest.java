package org.benetech.servicenet.adapter.smcconnect;

import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.ADDRESSES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.CONTACTS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.DAYS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.HOLIDAY_SCHEDULE;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.JSON;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.LOCATIONS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.MAIL_ADDRESSES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.MINIMAL;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.ORGANIZATIONS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.PHONES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.PROGRAMS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.REGULAR_SCHEDULES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.SERVICES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.SMCCONNECT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.smcconnect.model.SmcAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcContact;
import org.benetech.servicenet.adapter.smcconnect.model.SmcHolidaySchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcLocation;
import org.benetech.servicenet.adapter.smcconnect.model.SmcMailAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcOrganization;
import org.benetech.servicenet.adapter.smcconnect.model.SmcPhone;
import org.benetech.servicenet.adapter.smcconnect.model.SmcProgram;
import org.benetech.servicenet.adapter.smcconnect.model.SmcRegularSchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcService;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.type.ListType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class SMCConnectDataMapperMinimalTest {

    private static final String DIR = SMCCONNECT + MINIMAL;

    private SmcConnectDataMapper mapper = SmcConnectDataMapper.INSTANCE;

    @Test
    public void shouldNotThrowExceptionForMinimalDataForOrganization() throws IOException {
        String json = AdapterTestsUtils
            .readResourceAsString(DIR + ORGANIZATIONS + JSON);
        List<SmcOrganization> entities = new Gson().fromJson(json, new ListType<>(SmcOrganization.class));

        Organization result = mapper.extractOrganization(entities.get(0));

        assertEquals("Organization Name", result.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForLackOfDataForOrganization() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + ORGANIZATIONS + JSON);
        List<SmcOrganization> entities = new Gson().fromJson(json, new ListType<>(SmcOrganization.class));
        entities.get(0).setName(null);

        mapper.extractOrganization(entities.get(0));
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForService() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + SERVICES + JSON);
        List<SmcService> entities = new Gson().fromJson(json, new ListType<>(SmcService.class));

        Service result = mapper.extractService(entities.get(0));

        assertEquals("Service Name", result.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForLackOfDataForService() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + SERVICES + JSON);
        List<SmcService> entities = new Gson().fromJson(json, new ListType<>(SmcService.class));
        entities.get(0).setName(null);

        mapper.extractService(entities.get(0));
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForLocation() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + LOCATIONS + JSON);
        List<SmcLocation> entities = new Gson().fromJson(json, new ListType<>(SmcLocation.class));

        Location result = mapper.extractLocation(entities.get(0));

        assertEquals("Name", result.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForLackOfDataForLocation() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + LOCATIONS + JSON);
        List<SmcLocation> entities = new Gson().fromJson(json, new ListType<>(SmcLocation.class));
        entities.get(0).setName(null);

        mapper.extractLocation(entities.get(0));
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForHolidaySchedule() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + HOLIDAY_SCHEDULE + JSON);
        List<SmcHolidaySchedule> entities = new Gson().fromJson(json, new ListType<>(SmcHolidaySchedule.class));

        HolidaySchedule result = mapper.extractHolidaySchedule(entities.get(0)).get();

        assertEquals(LocalDate.of(2001, 12, 24), result.getStartDate());
        assertEquals(LocalDate.of(2002, 1, 1), result.getEndDate());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForHolidaySchedule() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + HOLIDAY_SCHEDULE + JSON);
        List<SmcHolidaySchedule> entities = new Gson().fromJson(json, new ListType<>(SmcHolidaySchedule.class));
        entities.get(0).setStartDate(null);
        entities.get(0).setEndDate(null);

        assertFalse(mapper.extractHolidaySchedule(entities.get(0)).isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForRegularSchedule() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + REGULAR_SCHEDULES + JSON);
        List<SmcRegularSchedule> entities = new Gson().fromJson(json, new ListType<>(SmcRegularSchedule.class));

        OpeningHours result = mapper.extractOpeningHours(entities.get(0));

        assertEquals(DAYS.get(0), result.getWeekday());
    }

    @Test
    public void shouldReturnNullForLackOfDataForRegularSchedule() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + REGULAR_SCHEDULES + JSON);
        List<SmcRegularSchedule> entities = new Gson().fromJson(json, new ListType<>(SmcRegularSchedule.class));
        entities.forEach(e -> e.setWeekday(null));

        OpeningHours result = mapper.extractOpeningHours(entities.get(0));
        assertNull(result);
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPrograms() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + PROGRAMS + JSON);
        List<SmcProgram> entities = new Gson().fromJson(json, new ListType<>(SmcProgram.class));

        Program result = mapper.extractProgram(entities.get(0));

        assertEquals("Name", result.getName());
    }

    @Test
    public void shouldReturnNullForLackOfDataForPrograms() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + PROGRAMS + JSON);
        List<SmcProgram> entities = new Gson().fromJson(json, new ListType<>(SmcProgram.class));
        entities.get(0).setName(null);

        assertNull(mapper.extractProgram(entities.get(0)));
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForContacts() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + CONTACTS + JSON);
        List<SmcContact> entities = new Gson().fromJson(json, new ListType<>(SmcContact.class));

        Contact result = mapper.extractContact(entities.get(0));

        assertEquals("Contact Name", result.getName());
    }

    @Test
    public void shouldReturnNullForLackOfDataForContacts() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + CONTACTS + JSON);
        List<SmcContact> entities = new Gson().fromJson(json, new ListType<>(SmcContact.class));
        entities.get(0).setName(null);

        assertNull(mapper.extractContact(entities.get(0)));
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhone() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + PHONES + JSON);
        List<SmcPhone> entities = new Gson().fromJson(json, new ListType<>(SmcPhone.class));

        List<String> resultNumbers = mapper.extractPhones(new HashSet<>(entities)).stream()
            .map(Phone::getNumber).collect(Collectors.toList());

        assertEquals("(900) 500-9000", resultNumbers.get(0));
    }

    @Test
    public void shouldReturnEmptyCollectionForLackOfDataForPhone() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + PHONES + JSON);
        List<SmcPhone> entities = new Gson().fromJson(json, new ListType<>(SmcPhone.class));
        entities.forEach(e -> e.setNumber(null));

        Set<Phone> result = mapper.extractPhones(new HashSet<>(entities));

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyCollectionForLackOfDataForLanguagesServiceBased() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + SERVICES + JSON);
        List<SmcService> entities = new Gson().fromJson(json, new ListType<>(SmcService.class));
        entities.forEach(e -> e.setLanguages(null));

        Set<Language> result = mapper.extractLangs(entities.get(0));

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyCollectionForLackOfDataForLanguagesLocationBased() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + LOCATIONS + JSON);
        List<SmcLocation> entities = new Gson().fromJson(json, new ListType<>(SmcLocation.class));
        entities.forEach(e -> e.setLanguages(null));

        Set<Language> result = mapper.extractLangs(entities.get(0));

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhysicalAddress() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + ADDRESSES + JSON);
        List<SmcAddress> entities = new Gson().fromJson(json, new ListType<>(SmcAddress.class));
        PhysicalAddress result = mapper.extractPhysicalAddress(entities.get(0)).get();

        assertEquals("1111 Secret Street", result.getAddress1());
        assertEquals("City", result.getCity());
        assertEquals("SP", result.getStateProvince());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForPhysicalAddress() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + ADDRESSES + JSON);
        List<SmcAddress> entities = new Gson().fromJson(json, new ListType<>(SmcAddress.class));
        entities.get(0).setAddress1(null);
        entities.get(0).setCity(null);
        entities.get(0).setStateProvince(null);

        assertFalse(mapper.extractPhysicalAddress(entities.get(0)).isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPostalAddress() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + MAIL_ADDRESSES + JSON);
        List<SmcMailAddress> entities = new Gson().fromJson(json, new ListType<>(SmcMailAddress.class));
        PostalAddress result = mapper.extractPostalAddress(entities.get(0)).get();

        assertEquals("1st Address", result.getAddress1());
        assertEquals("MailCity", result.getCity());
        assertEquals("MailSP", result.getStateProvince());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForPostalAddress() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(DIR + MAIL_ADDRESSES + JSON);
        List<SmcMailAddress> entities = new Gson().fromJson(json, new ListType<>(SmcMailAddress.class));
        entities.get(0).setAddress1(null);
        entities.get(0).setCity(null);
        entities.get(0).setStateProvince(null);

        assertFalse(mapper.extractPostalAddress(entities.get(0)).isPresent());
    }
}
