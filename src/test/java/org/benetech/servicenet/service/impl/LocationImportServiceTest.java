package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestPersistanceHelper;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.HolidayScheduleService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.benetech.servicenet.TestConstants.EXISTING_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.EXISTING_INT;
import static org.benetech.servicenet.TestConstants.EXISTING_STRING;
import static org.benetech.servicenet.TestConstants.NEW_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.NEW_INT;
import static org.benetech.servicenet.TestConstants.NEW_STRING;
import static org.benetech.servicenet.TestConstants.OTHER_INT;
import static org.benetech.servicenet.TestConstants.OTHER_STRING;
import static org.benetech.servicenet.TestConstants.PROVIDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class LocationImportServiceTest {

    @Autowired
    private LocationImportService importService;

    @Autowired
    private TestPersistanceHelper helper;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private AccessibilityForDisabilitiesService accessibilityService;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private HolidayScheduleService holidayScheduleService;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private LanguageService languageService;
    
    @Autowired
    private PhoneService phoneService;

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateLocation() {
        Location location = helper.generateNewLocation();

        assertEquals(0, locationService.findAll().size());
        importService.createOrUpdateLocation(location, NEW_EXTERNAL_ID, PROVIDER);

        List<LocationDTO> all = locationService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateLocation() {
        Location newLocation = helper.generateNewLocation();
        helper.generateExistingLocation();

        assertEquals(1, locationService.findAll().size());
        importService.createOrUpdateLocation(newLocation, EXISTING_EXTERNAL_ID, PROVIDER);

        List<LocationDTO> all = locationService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreatePhysicalAddress() {
        Location location = helper.generateNewLocation();
        PhysicalAddress address = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .attention(NEW_STRING).country(NEW_STRING).postalCode(NEW_STRING).region(NEW_STRING);
        location.setPhysicalAddress(address);

        assertEquals(0, physicalAddressService.findAll().size());
        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhysicalAddressDTO> all = physicalAddressService.findAll();
        assertEquals(1, all.size());
        PhysicalAddressDTO physicalAddressDTO = all.get(0);
        assertNotNull(physicalAddressDTO.getLocationId());
        assertEquals(NEW_STRING, physicalAddressDTO.getAddress1());
        assertEquals(NEW_STRING, physicalAddressDTO.getCity());
        assertEquals(NEW_STRING, physicalAddressDTO.getStateProvince());
        assertEquals(NEW_STRING, physicalAddressDTO.getAttention());
        assertEquals(NEW_STRING, physicalAddressDTO.getCountry());
        assertEquals(NEW_STRING, physicalAddressDTO.getPostalCode());
        assertEquals(NEW_STRING, physicalAddressDTO.getRegion());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdatePhysicalAddress() {
        Location location = helper.generateExistingLocation();
        PhysicalAddress existingAddress = new PhysicalAddress().address1(EXISTING_STRING).city(EXISTING_STRING)
            .stateProvince(EXISTING_STRING).attention(EXISTING_STRING).country(EXISTING_STRING)
            .postalCode(EXISTING_STRING).region(EXISTING_STRING).location(location);
        helper.persist(existingAddress);
        helper.flushAndRefresh(location);

        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        PhysicalAddress newAddress = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .attention(NEW_STRING).country(NEW_STRING).postalCode(NEW_STRING).region(NEW_STRING);
        locationToUpdate.setPhysicalAddress(newAddress);

        assertEquals(1, locationService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhysicalAddressDTO> all = physicalAddressService.findAll();
        assertEquals(1, all.size());
        PhysicalAddressDTO physicalAddressDTO = all.get(0);
        assertEquals(location.getId(), physicalAddressDTO.getLocationId());
        assertEquals(NEW_STRING, physicalAddressDTO.getAddress1());
        assertEquals(NEW_STRING, physicalAddressDTO.getCity());
        assertEquals(NEW_STRING, physicalAddressDTO.getStateProvince());
        assertEquals(NEW_STRING, physicalAddressDTO.getAttention());
        assertEquals(NEW_STRING, physicalAddressDTO.getCountry());
        assertEquals(NEW_STRING, physicalAddressDTO.getPostalCode());
        assertEquals(NEW_STRING, physicalAddressDTO.getRegion());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreatePostalAddress() {
        Location location = helper.generateNewLocation();
        PostalAddress address = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .attention(NEW_STRING).country(NEW_STRING).postalCode(NEW_STRING).region(NEW_STRING);
        location.setPostalAddress(address);

        assertEquals(0, postalAddressService.findAll().size());

        importService.createOrUpdateLocation(location, NEW_EXTERNAL_ID, PROVIDER);

        assertEquals(1, locationService.findAll().size());
        List<PostalAddressDTO> all = postalAddressService.findAll();
        assertEquals(1, all.size());
        PostalAddressDTO postalAddressDTO = all.get(0);
        assertNotNull(postalAddressDTO.getLocationId());
        assertEquals(NEW_STRING, postalAddressDTO.getAddress1());
        assertEquals(NEW_STRING, postalAddressDTO.getCity());
        assertEquals(NEW_STRING, postalAddressDTO.getStateProvince());
        assertEquals(NEW_STRING, postalAddressDTO.getAttention());
        assertEquals(NEW_STRING, postalAddressDTO.getCountry());
        assertEquals(NEW_STRING, postalAddressDTO.getPostalCode());
        assertEquals(NEW_STRING, postalAddressDTO.getRegion());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdatePostalAddress() {
        Location location = helper.generateExistingLocation();
        PostalAddress existingAddress = new PostalAddress().address1(EXISTING_STRING).city(EXISTING_STRING)
            .stateProvince(EXISTING_STRING).attention(EXISTING_STRING).country(EXISTING_STRING)
            .attention(NEW_STRING).postalCode(EXISTING_STRING).region(EXISTING_STRING).location(location);
        helper.persist(existingAddress);
        helper.flushAndRefresh(location);

        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        PostalAddress newAddress = new PostalAddress().address1(NEW_STRING).city(NEW_STRING)
            .stateProvince(NEW_STRING).attention(NEW_STRING).country(NEW_STRING)
            .postalCode(NEW_STRING).region(NEW_STRING);
        locationToUpdate.setPostalAddress(newAddress);

        assertEquals(1, locationService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        assertEquals(1, locationService.findAll().size());
        List<PostalAddressDTO> all = postalAddressService.findAll();
        assertEquals(1, all.size());
        PostalAddressDTO postalAddressDTO = all.get(0);
        assertEquals(location.getId(), postalAddressDTO.getLocationId());
        assertEquals(NEW_STRING, postalAddressDTO.getAddress1());
        assertEquals(NEW_STRING, postalAddressDTO.getCity());
        assertEquals(NEW_STRING, postalAddressDTO.getStateProvince());
        assertEquals(NEW_STRING, postalAddressDTO.getAttention());
        assertEquals(NEW_STRING, postalAddressDTO.getCountry());
        assertEquals(NEW_STRING, postalAddressDTO.getPostalCode());
        assertEquals(NEW_STRING, postalAddressDTO.getRegion());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateAccessibilityIfLocationHasNoneOfThem() {
        Location location = helper.generateNewLocation();
        AccessibilityForDisabilities accessibility = new AccessibilityForDisabilities()
            .accessibility(NEW_STRING);
        location.setAccessibilities(helper.mutableSet(accessibility));

        assertEquals(0, accessibilityService.findAll().size());

        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<AccessibilityForDisabilitiesDTO> created = accessibilityService.findAll();
        assertEquals(1, created.size());
        AccessibilityForDisabilitiesDTO accessibilityDTO = created.get(0);
        assertNotNull(accessibilityDTO.getLocationId());
        assertEquals(NEW_STRING, accessibilityDTO.getAccessibility());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateAccessibilityIfLocationHasFewOfThemButNotThisOne() {
        Location location = helper.generateExistingLocation();
        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities()
            .accessibility(OTHER_STRING)
            .location(location);
        helper.persist(otherAccessibility);
        helper.flushAndRefresh(location);

        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities()
            .accessibility(NEW_STRING).details(NEW_STRING);
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setAccessibilities(helper.mutableSet(newAccessibility));

        assertEquals(1, accessibilityService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<AccessibilityForDisabilitiesDTO> all = accessibilityService.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(OTHER_STRING)));
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(NEW_STRING)));
        assertTrue(all.stream().allMatch(a -> a.getLocationId() != null));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateAccessibility() {
        Location location = helper.generateExistingLocation();
        AccessibilityForDisabilities accessibilityToBeUpdated = new AccessibilityForDisabilities()
            .accessibility(EXISTING_STRING).details(EXISTING_STRING)
            .location(location);
        helper.persist(accessibilityToBeUpdated);
        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities()
            .accessibility(OTHER_STRING)
            .location(location);
        helper.persist(otherAccessibility);
        helper.flushAndRefresh(location);

        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities()
            .accessibility(EXISTING_STRING).details(NEW_STRING);
        locationToUpdate.setAccessibilities(helper.mutableSet(newAccessibility));

        assertEquals(2, accessibilityService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<AccessibilityForDisabilitiesDTO> all = accessibilityService.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(EXISTING_STRING)));
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(OTHER_STRING)));
        assertTrue(all.stream().anyMatch(a -> NEW_STRING.equals(a.getDetails())));
        assertTrue(all.stream().anyMatch(a -> a.getDetails() == null));
        assertTrue(all.stream().allMatch(a -> a.getLocationId().equals(location.getId())));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateHolidayScheduleForLocation() {
        Location location = helper.generateNewLocation();
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.of(2019, 1, 3);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(start).endDate(end);
        location.setHolidaySchedule(schedule);

        assertEquals(0, holidayScheduleService.findAll().size());
        importService.createOrUpdateLocation(location, NEW_EXTERNAL_ID, PROVIDER);

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        HolidayScheduleDTO scheduleDTO = all.get(0);
        assertEquals(NEW_STRING, scheduleDTO.getClosesAt());
        assertFalse(scheduleDTO.isClosed());
        assertNotNull(scheduleDTO.getLocationId());
        assertEquals(start, scheduleDTO.getStartDate());
        assertEquals(end, scheduleDTO.getEndDate());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateHolidayScheduleForLocation() {
        Location location = helper.generateExistingLocation();
        LocalDate start = LocalDate.of(2018, 1, 1);
        LocalDate end = LocalDate.of(2018, 1, 3);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(EXISTING_STRING).closed(true).location(location)
            .startDate(start).endDate(end);
        helper.persist(schedule);
        helper.flushAndRefresh(location);

        LocalDate newStart = LocalDate.of(2019, 1, 1);
        LocalDate newEnd = LocalDate.of(2019, 1, 3);
        HolidaySchedule newSchedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(newStart).endDate(newEnd);
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setHolidaySchedule(newSchedule);

        assertEquals(1, holidayScheduleService.findAll().size());
        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        HolidayScheduleDTO scheduleDTO = all.get(0);
        assertEquals(NEW_STRING, scheduleDTO.getClosesAt());
        assertFalse(scheduleDTO.isClosed());
        assertNotNull(scheduleDTO.getLocationId());
        assertEquals(newStart, scheduleDTO.getStartDate());
        assertEquals(newEnd, scheduleDTO.getEndDate());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateOpeningHoursIfLocationHasNoneOfThem() {
        Location location = helper.generateNewLocation();
        OpeningHours openingHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        location.setRegularSchedule(new RegularSchedule().openingHours(helper.mutableSet(openingHours)));

        assertEquals(0, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertNotNull(allSchedules.get(0).getLocationId());
        assertEquals(1, allHours.size());
        OpeningHoursDTO hoursDTO = allHours.get(0);
        assertEquals(NEW_INT, hoursDTO.getWeekday());
        assertEquals(NEW_STRING, hoursDTO.getOpensAt());
        assertEquals(NEW_STRING, hoursDTO.getClosesAt());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceOpeningHoursIfLocationHasFewOfThemButNotThisOne() {
        Location location = helper.generateExistingLocation();
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        helper.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule().openingHours(helper.mutableSet(otherOpeningHours)).location(location);
        helper.persist(schedule);
        helper.flushAndRefresh(location);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT)
            .opensAt(NEW_STRING).closesAt(NEW_STRING);
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setRegularSchedule(new RegularSchedule().openingHours(helper.mutableSet(newOpeningHours)));

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(1, openingHoursService.findAll().size());
        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertEquals(location.getId(), allSchedules.get(0).getLocationId());
        assertEquals(1, allHours.size());
        OpeningHoursDTO hoursDTO = allHours.get(0);
        assertEquals(NEW_INT, hoursDTO.getWeekday());
        assertEquals(NEW_STRING, hoursDTO.getOpensAt());
        assertEquals(NEW_STRING, hoursDTO.getClosesAt());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceOpeningHoursForLocation() {
        Location location = helper.generateExistingLocation();
        OpeningHours openingHoursToBeUpdated = new OpeningHours().weekday(EXISTING_INT)
            .opensAt(EXISTING_STRING).closesAt(EXISTING_STRING);
        helper.persist(openingHoursToBeUpdated);
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT)
            .opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        helper.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule()
            .openingHours(helper.mutableSet(openingHoursToBeUpdated, otherOpeningHours))
            .location(location);
        helper.persist(schedule);
        helper.flushAndRefresh(location);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        RegularSchedule newSchedule = new RegularSchedule().openingHours(helper.mutableSet(newOpeningHours));
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setRegularSchedule(newSchedule);

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(2, openingHoursService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertEquals(location.getId(), allSchedules.get(0).getLocationId());
        assertEquals(1, allHours.size());
        OpeningHoursDTO hoursDTO = allHours.get(0);
        assertEquals(NEW_INT, hoursDTO.getWeekday());
        assertEquals(NEW_STRING, hoursDTO.getOpensAt());
        assertEquals(NEW_STRING, hoursDTO.getClosesAt());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateLangsIfLocationHasNoneOfThem() {
        Location location = helper.generateNewLocation();
        Language language = new Language().language(NEW_STRING);
        location.setLangs(helper.mutableSet(language));

        assertEquals(0, languageService.findAll().size());

        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertNotNull(all.get(0).getLocationId());
        assertEquals(NEW_STRING, all.get(0).getLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceLangsIfLocationHasFewOfThemButNotThisOne() {
        Location location = helper.generateExistingLocation();
        Language otherLanguage = new Language().language(OTHER_STRING).location(location);
        helper.persist(otherLanguage);
        helper.flushAndRefresh(location);

        Language newLanguage = new Language().language(NEW_STRING);
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setLangs(helper.mutableSet(newLanguage));

        assertEquals(1, languageService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertEquals(location.getId(), all.get(0).getLocationId());
        assertEquals(NEW_STRING, all.get(0).getLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceLangsForLocation() {
        Location location = helper.generateExistingLocation();

        Language languageToBeUpdated = new Language().language(EXISTING_STRING).location(location);
        helper.persist(languageToBeUpdated);

        Language otherLanguage = new Language().language(OTHER_STRING).location(location);
        helper.persist(otherLanguage);
        helper.flushAndRefresh(location);

        Language newLanguage = new Language().language(NEW_STRING);
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setLangs(helper.mutableSet(newLanguage));

        assertEquals(2, languageService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertEquals(location.getId(), all.get(0).getLocationId());
        assertEquals(NEW_STRING, all.get(0).getLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreatePhonesIfLocationHasNoneOfThem() {
        Location location = helper.generateNewLocation();
        Phone phone = new Phone().number(NEW_STRING);
        location.setPhones(helper.mutableSet(phone));

        assertEquals(0, phoneService.findAll().size());

        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertNotNull(all.get(0).getLocationId());
        assertEquals(NEW_STRING, all.get(0).getNumber());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplacePhonesIfLocationHasFewOfThemButNotThisOne() {
        Location location = helper.generateExistingLocation();
        Phone otherPhone = new Phone().number(OTHER_STRING).location(location);
        helper.persist(otherPhone);
        helper.flushAndRefresh(location);

        Phone newPhone = new Phone().number(NEW_STRING);
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setPhones(helper.mutableSet(newPhone));

        assertEquals(1, phoneService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertEquals(location.getId(), all.get(0).getLocationId());
        assertEquals(NEW_STRING, all.get(0).getNumber());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplacePhonesForLocation() {
        Location location = helper.generateExistingLocation();
        Phone phoneToBeUpdated = new Phone().number(EXISTING_STRING).location(location);
        helper.persist(phoneToBeUpdated);
        Phone otherPhone = new Phone().number(OTHER_STRING).location(location);
        helper.persist(otherPhone);
        helper.flushAndRefresh(location);

        Phone newPhone = new Phone().number(NEW_STRING);
        Location locationToUpdate = helper.generateExistingLocationDoNotPersist();
        locationToUpdate.setPhones(helper.mutableSet(newPhone));

        assertEquals(2, phoneService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertEquals(location.getId(), all.get(0).getLocationId());
        assertEquals(NEW_STRING, all.get(0).getNumber());
    }
}
