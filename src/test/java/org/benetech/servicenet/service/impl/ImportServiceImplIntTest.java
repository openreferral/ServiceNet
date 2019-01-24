package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.ServiceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ImportServiceImplIntTest {

    private static final String PROVIDER = "provider";
    private static final String NEW_EXTERNAL_ID = "1000";
    private static final String EXISTING_EXTERNAL_ID = "2000";
    private static final String NEW_STRING = "new string";
    private static final String EXISTING_STRING = "existing string";
    private static final String OTHER_STRING = "other string";
    private static final boolean NEW_BOOLEAN = true;
    private static final boolean EXISTING_BOOLEAN = false;
    private static final int NEW_INT = 1;
    private static final int OTHER_INT = 2;
    private static final int EXISTING_INT = 3;

    @Autowired
    private EntityManager em;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ImportService importService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private AccessibilityForDisabilitiesService accessibilityService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Test
    @Transactional
    public void shouldCreateLocation() {
        Location location = generateNewLocation();

        int dbSize = locationService.findAll().size();
        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        assertEquals(dbSize + 1, locationService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldUpdateLocation() {
        Location newLocation = generateNewLocation();
        generateExistingLocation();

        int dbSize = locationService.findAll().size();
        Location updated = importService.createOrUpdateLocation(newLocation, EXISTING_EXTERNAL_ID, PROVIDER);

        assertEquals(dbSize, locationService.findAll().size());
        assertEquals(NEW_STRING, updated.getName());
    }

    @Test
    @Transactional
    public void shouldCreatePhysicalAddress() {
        Location location = generateExistingLocation();
        PhysicalAddress address = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);
        em.flush();
        em.refresh(location);

        int dbSize = physicalAddressService.findAll().size();
        importService.createOrUpdatePhysicalAddress(address, location);

        assertEquals(dbSize + 1, physicalAddressService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldUpdatePhysicalAddress() {
        Location location = generateExistingLocation();
        PhysicalAddress existingAddress = new PhysicalAddress().address1(EXISTING_STRING).city(EXISTING_STRING).stateProvince(EXISTING_STRING)
            .location(location);
        em.persist(existingAddress);
        em.flush();
        em.refresh(location);

        PhysicalAddress newAddress = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);

        int dbSize = locationService.findAll().size();
        PhysicalAddress updated = importService.createOrUpdatePhysicalAddress(newAddress, location);

        assertEquals(dbSize, locationService.findAll().size());
        assertEquals(NEW_STRING, updated.getAddress1());
        assertEquals(NEW_STRING, updated.getCity());
        assertEquals(NEW_STRING, updated.getStateProvince()
        );
    }

    @Test
    @Transactional
    public void shouldCreatePostalAddress() {
        Location location = generateExistingLocation();
        PostalAddress address = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);

        int dbSize = postalAddressService.findAll().size();
        importService.createOrUpdatePostalAddress(address, location);

        assertEquals(dbSize + 1, postalAddressService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldUpdatePostalAddress() {
        Location location = generateExistingLocation();
        PostalAddress existingAddress = new PostalAddress().address1(EXISTING_STRING).city(EXISTING_STRING).stateProvince(EXISTING_STRING)
            .location(location);
        em.persist(existingAddress);
        em.flush();
        em.refresh(location);

        PostalAddress newAddress = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);

        int dbSize = locationService.findAll().size();
        PostalAddress updated = importService.createOrUpdatePostalAddress(newAddress, location);

        assertEquals(dbSize, locationService.findAll().size());
        assertEquals(NEW_STRING, updated.getAddress1());
        assertEquals(NEW_STRING, updated.getCity());
        assertEquals(NEW_STRING, updated.getStateProvince());
    }

    @Test
    @Transactional
    public void shouldCreateAccessibilityIfLocationHasNoneOfThem() {
        Location location = generateExistingLocation();
        AccessibilityForDisabilities accessibility = new AccessibilityForDisabilities().accessibility(NEW_STRING)
            .location(location);

        int dbSize = accessibilityService.findAll().size();
        importService.createOrUpdateAccessibility(accessibility, location);

        assertEquals(dbSize + 1, accessibilityService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldCreateAccessibilityIfLocationHasFewOfThemButNotThisOne() {
        Location location = generateExistingLocation();

        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities().accessibility(OTHER_STRING)
            .location(location);
        em.persist(otherAccessibility);
        em.flush();
        em.refresh(location);

        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities().accessibility(NEW_STRING).details(NEW_STRING)
            .location(location);

        int dbSize = accessibilityService.findAll().size();
        importService.createOrUpdateAccessibility(newAccessibility, location);

        assertEquals(dbSize + 1, accessibilityService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldUpdateAccessibility() {
        Location location = generateExistingLocation();
        AccessibilityForDisabilities accessibilityToBeUpdated = new AccessibilityForDisabilities().accessibility(EXISTING_STRING).details(EXISTING_STRING)
            .location(location);
        em.persist(accessibilityToBeUpdated);

        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities().accessibility(OTHER_STRING)
            .location(location);
        em.persist(otherAccessibility);
        em.flush();
        em.refresh(location);

        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities().accessibility(EXISTING_STRING).details(NEW_STRING)
            .location(location);

        int dbSize = locationService.findAll().size();
        AccessibilityForDisabilities updated = importService.createOrUpdateAccessibility(newAccessibility, location);

        assertEquals(dbSize, locationService.findAll().size());
        assertEquals(EXISTING_STRING, updated.getAccessibility());
        assertEquals(NEW_STRING, updated.getDetails());
    }

    @Test
    @Transactional
    public void shouldCreateOrganization() {
        Organization organization = generateNewOrganization();

        int dbSize = organizationService.findAllDTOs().size();
        importService.createOrUpdateOrganization(organization, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(dbSize + 1, organizationService.findAllDTOs().size());
    }

    @Test
    @Transactional
    public void shouldUpdateOrganization() {
        Organization newOrganization = generateNewOrganization();
        generateExistingOrganization();

        int dbSize = organizationService.findAllDTOs().size();
        Organization updated = importService.createOrUpdateOrganization(newOrganization, EXISTING_EXTERNAL_ID, PROVIDER,
            new DataImportReport());

        assertEquals(dbSize, organizationService.findAllDTOs().size());
        assertEquals(NEW_STRING, updated.getName());
        assertEquals(NEW_BOOLEAN, updated.getActive());
    }

    @Test
    @Transactional
    public void shouldCreateService() {
        Service service = generateNewService();

        int dbSize = serviceService.findAll().size();
        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(dbSize + 1, serviceService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldUpdateService() {
        Service newService = generateNewService();
        generateExistingService();

        int dbSize = serviceService.findAll().size();
        Service updated = importService.createOrUpdateService(newService, EXISTING_EXTERNAL_ID, PROVIDER,
            new DataImportReport());

        assertEquals(dbSize, serviceService.findAll().size());
        assertEquals(NEW_STRING, updated.getName());
    }
    
    @Test
    @Transactional
    public void shouldCreatePhonesIfServiceHasNoneOfThem() {
        Service service = generateExistingService();
        Phone phone = new Phone().number(NEW_STRING).srvc(service);

        int dbSize = phoneService.findAll().size();
        importService.createOrUpdatePhonesForService(Set.of(phone), service, null);

        assertEquals(dbSize + 1, phoneService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldReplacePhonesIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        em.persist(otherPhone);
        em.flush();
        em.refresh(service);

        Phone newPhone = new Phone().number(NEW_STRING).srvc(service);

        int dbSize = phoneService.findAll().size();
        importService.createOrUpdatePhonesForService(Set.of(newPhone), service, null);

        assertEquals(dbSize, phoneService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldReplacePhones() {
        Service service = generateExistingService();

        Phone phoneToBeUpdated = new Phone().number(EXISTING_STRING).srvc(service);
        em.persist(phoneToBeUpdated);

        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        em.persist(otherPhone);
        em.flush();
        em.refresh(service);

        Phone newPhone = new Phone().number(NEW_STRING).srvc(service);

        int dbSize = phoneService.findAll().size();
        Set<Phone> updated = importService.createOrUpdatePhonesForService(Set.of(newPhone), service, null);

        assertEquals(dbSize - 1, phoneService.findAll().size());
        assertEquals(1, updated.size());
        assertTrue(updated.stream().anyMatch(phone -> phone.equals(newPhone)));
    }

    @Test
    @Transactional
    public void shouldCreateEligibility() {
        Service service = generateExistingService();
        Eligibility eligibility = new Eligibility().eligibility(NEW_STRING).srvc(service);

        int dbSize = eligibilityService.findAll().size();
        importService.createOrUpdateEligibility(eligibility, service);

        assertEquals(dbSize + 1, eligibilityService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldUpdateEligibility() {
        Service service = generateExistingService();
        Eligibility existingEligibility = new Eligibility().eligibility(EXISTING_STRING).srvc(service);
        em.persist(existingEligibility);
        em.flush();
        em.refresh(service);

        Eligibility newEligibility = new Eligibility().eligibility(NEW_STRING).srvc(service);

        int dbSize = eligibilityService.findAll().size();
        Service serviceFromDb = serviceService.findForExternalDb(EXISTING_EXTERNAL_ID, PROVIDER).get(); //TODO: anywhere else
        Eligibility updated = importService.createOrUpdateEligibility(newEligibility, serviceFromDb);

        assertEquals(dbSize, eligibilityService.findAll().size());
        assertEquals(NEW_STRING, updated.getEligibility());
    }

    @Test
    @Transactional
    public void shouldCreateLangsIfServiceHasNoneOfThem() {
        Service service = generateExistingService();
        Language language = new Language().language(NEW_STRING).srvc(service);

        int dbSize = languageService.findAll().size();
        importService.createOrUpdateLangsForService(Set.of(language), service, null);

        assertEquals(dbSize + 1, languageService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldReplaceLangsIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(service);

        Language newLanguage = new Language().language(NEW_STRING).srvc(service);

        int dbSize = languageService.findAll().size();
        importService.createOrUpdateLangsForService(Set.of(newLanguage), service, null);

        assertEquals(dbSize, languageService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldReplaceLangs() {
        Service service = generateExistingService();

        Language languageToBeUpdated = new Language().language(EXISTING_STRING).srvc(service);
        em.persist(languageToBeUpdated);

        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(service);

        Language newLanguage = new Language().language(NEW_STRING).srvc(service);

        int dbSize = languageService.findAll().size();
        Set<Language> updated = importService.createOrUpdateLangsForService(Set.of(newLanguage), service, null);

        assertEquals(dbSize - 1, languageService.findAll().size());
        assertEquals(1, updated.size());
        assertTrue(updated.stream().anyMatch(lang -> lang.equals(newLanguage)));
    }

    @Test
    @Transactional
    public void shouldCreateOpeningHoursIfServiceHasNoneOfThem() {
        Service service = generateExistingService();
        OpeningHours openingHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);

        int scheduleDbSize = regularScheduleService.findAll().size();
        int hoursDbSize = openingHoursService.findAll().size();
        importService.createOrUpdateOpeningHoursForService(Set.of(openingHours), service);

        assertEquals(scheduleDbSize + 1, regularScheduleService.findAll().size());
        assertEquals(hoursDbSize + 1, openingHoursService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldReplaceOpeningHoursIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        em.persist(otherOpeningHours);
        em.flush();
        em.refresh(service);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);

        int scheduleDbSize = regularScheduleService.findAll().size();
        int hoursDbSize = openingHoursService.findAll().size();
        importService.createOrUpdateOpeningHoursForService(Set.of(newOpeningHours), service);

        assertEquals(scheduleDbSize + 1, regularScheduleService.findAll().size());
        assertEquals(hoursDbSize + 1, openingHoursService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldReplaceOpeningHours() {
        Service service = generateExistingService();

        OpeningHours openingHoursToBeUpdated = new OpeningHours().weekday(EXISTING_INT).opensAt(EXISTING_STRING).closesAt(EXISTING_STRING);
        em.persist(openingHoursToBeUpdated);

        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        em.persist(otherOpeningHours);

        RegularSchedule scheduler = new RegularSchedule().openingHours(Set.of(openingHoursToBeUpdated, otherOpeningHours))
                .srvc(service);
        em.persist(scheduler);
        em.flush();
        em.refresh(service);

        OpeningHours newOpeningHours = new OpeningHours().weekday(EXISTING_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);

        int scheduleDbSize = regularScheduleService.findAll().size();
        int hoursDbSize = openingHoursService.findAll().size();
        Set<OpeningHours> updated = importService.createOrUpdateOpeningHoursForService(Set.of(newOpeningHours), service);

        assertEquals(scheduleDbSize, regularScheduleService.findAll().size());
        assertEquals(hoursDbSize - 1, openingHoursService.findAll().size());
        assertTrue(updated.stream().anyMatch(hours -> hours.equals(newOpeningHours)));
    }

    private Location generateNewLocation() {
        return new Location().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
    }

    private Location generateExistingLocation() {
        Location result = new Location().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }
    
    private Service generateNewService() {
        return new Service().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
    }

    private Service generateExistingService() {
        Service result = new Service().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }

    private Organization generateNewOrganization() {
        return new Organization().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING).active(NEW_BOOLEAN);
    }

    private Organization generateExistingOrganization() {
        Organization result = new Organization().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING).active(EXISTING_BOOLEAN);
        em.persist(result);
        return result;
    }
}
