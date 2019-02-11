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
import org.benetech.servicenet.domain.SystemAccount;
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
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ImportServiceConfidentialityImplIntTest {

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
    public void shouldNotCreateLocationIfConfidential() {
        Location location = generateNewLocation();
        location.setIsConfidential(true);

        int dbSize = locationService.findAll().size();
        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        assertEquals(dbSize, locationService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdateLocationIfConfidential() {
        Location newLocation = generateNewLocation();
        newLocation.setIsConfidential(true);
        generateExistingLocation();

        int dbSize = locationService.findAll().size();
        Location updated = importService.createOrUpdateLocation(newLocation, EXISTING_EXTERNAL_ID, PROVIDER);

        assertEquals(dbSize, locationService.findAll().size());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreatePhysicalAddressIfConfidential() {
        Location location = generateExistingLocation();
        PhysicalAddress address = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);
        address.setIsConfidential(true);
        em.flush();
        em.refresh(location);

        int dbSize = physicalAddressService.findAll().size();
        importService.createOrUpdatePhysicalAddress(address, location);

        assertEquals(dbSize, physicalAddressService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdatePhysicalAddressIfConfidential() {
        Location location = generateExistingLocation();
        PhysicalAddress existingAddress = new PhysicalAddress().address1(EXISTING_STRING).city(EXISTING_STRING).stateProvince(EXISTING_STRING)
            .location(location);
        em.persist(existingAddress);
        em.flush();
        em.refresh(location);

        PhysicalAddress newAddress = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);
        newAddress.setIsConfidential(true);

        int dbSize = locationService.findAll().size();
        PhysicalAddress updated = importService.createOrUpdatePhysicalAddress(newAddress, location);

        assertEquals(dbSize, locationService.findAll().size());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreatePostalAddressIfConfidential() {
        Location location = generateExistingLocation();
        PostalAddress address = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);
        address.setIsConfidential(true);

        int dbSize = postalAddressService.findAll().size();
        importService.createOrUpdatePostalAddress(address, location);

        assertEquals(dbSize, postalAddressService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdatePostalAddressIfConfidential() {
        Location location = generateExistingLocation();
        PostalAddress existingAddress = new PostalAddress().address1(EXISTING_STRING).city(EXISTING_STRING).stateProvince(EXISTING_STRING)
            .location(location);
        em.persist(existingAddress);
        em.flush();
        em.refresh(location);

        PostalAddress newAddress = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .location(location);
        newAddress.setIsConfidential(true);

        int dbSize = locationService.findAll().size();
        PostalAddress updated = importService.createOrUpdatePostalAddress(newAddress, location);

        assertEquals(dbSize, locationService.findAll().size());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreateAccessibilityIfConfidentialEvenIfLocationHasFewOfThemButNotThisOne() {
        Location location = generateExistingLocation();

        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities().accessibility(OTHER_STRING)
            .location(location);
        em.persist(otherAccessibility);
        em.flush();
        em.refresh(location);

        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities().accessibility(NEW_STRING).details(NEW_STRING)
            .location(location);
        newAccessibility.setIsConfidential(true);

        int dbSize = accessibilityService.findAll().size();
        importService.createOrUpdateAccessibility(newAccessibility, location);

        assertEquals(dbSize, accessibilityService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdateAccessibilityIfConfidential() {
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
        newAccessibility.setIsConfidential(true);

        int dbSize = locationService.findAll().size();
        AccessibilityForDisabilities updated = importService.createOrUpdateAccessibility(newAccessibility, location);

        assertEquals(dbSize, locationService.findAll().size());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreateOrganizationIfConfidential() {
        Organization organization = generateNewOrganization(generateExistingAccount());
        organization.setIsConfidential(true);

        int dbSize = organizationService.findAllDTOs().size();
        importService.createOrUpdateOrganization(organization, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(dbSize, organizationService.findAllDTOs().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdateOrganizationIfConfidential() {
        SystemAccount account = generateExistingAccount();
        Organization newOrganization = generateNewOrganization(account);
        newOrganization.setIsConfidential(true);
        generateExistingOrganization(account);

        int dbSize = organizationService.findAllDTOs().size();
        Organization updated = importService.createOrUpdateOrganization(newOrganization, EXISTING_EXTERNAL_ID, PROVIDER,
            new DataImportReport());

        assertEquals(dbSize, organizationService.findAllDTOs().size());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreateServiceIfConfidential() {
        Service service = generateNewService();
        service.setIsConfidential(true);

        int dbSize = serviceService.findAll().size();
        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(dbSize, serviceService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdateServiceIfConfidential() {
        Service newService = generateNewService();
        newService.setIsConfidential(true);
        generateExistingService();

        int dbSize = serviceService.findAll().size();
        Service updated = importService.createOrUpdateService(newService, EXISTING_EXTERNAL_ID, PROVIDER,
            new DataImportReport());

        assertEquals(dbSize, serviceService.findAll().size());
        assertNull(updated);
    }
    
    @Test
    @Transactional
    public void shouldNotCreatePhonesIfConfidentialEvenIfServiceHasNoneOfThem() {
        Service service = generateExistingService();
        Phone phone = new Phone().number(NEW_STRING).srvc(service);
        phone.setIsConfidential(true);

        int dbSize = phoneService.findAll().size();
        importService.createOrUpdatePhonesForService(Set.of(phone), service, null);

        assertEquals(dbSize, phoneService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplacePhonesIfConfidentialEvenIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        em.persist(otherPhone);
        em.flush();
        em.refresh(service);

        Phone newPhone = new Phone().number(NEW_STRING).srvc(service);
        newPhone.setIsConfidential(true);

        importService.createOrUpdatePhonesForService(Set.of(newPhone), service, null);

        assertEquals(0, phoneService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplacePhonesIfConfidential() {
        Service service = generateExistingService();

        Phone phoneToBeUpdated = new Phone().number(EXISTING_STRING).srvc(service);
        em.persist(phoneToBeUpdated);

        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        em.persist(otherPhone);
        em.flush();
        em.refresh(service);

        Phone newPhone = new Phone().number(NEW_STRING).srvc(service);
        newPhone.setIsConfidential(true);

        importService.createOrUpdatePhonesForService(Set.of(newPhone), service, null);

        assertEquals(0, phoneService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotCreateEligibilityIfConfidential() {
        Service service = generateExistingService();
        Eligibility eligibility = new Eligibility().eligibility(NEW_STRING).srvc(service);
        eligibility.setIsConfidential(true);

        int dbSize = eligibilityService.findAll().size();
        importService.createOrUpdateEligibility(eligibility, service);

        assertEquals(dbSize, eligibilityService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdateEligibilityIfConfidential() {
        Service service = generateExistingService();
        Eligibility existingEligibility = new Eligibility().eligibility(EXISTING_STRING).srvc(service);
        em.persist(existingEligibility);
        em.flush();
        em.refresh(service);

        Eligibility newEligibility = new Eligibility().eligibility(NEW_STRING).srvc(service);
        newEligibility.setIsConfidential(true);

        int dbSize = eligibilityService.findAll().size();
        Service serviceFromDb = serviceService.findWithEagerAssociations(EXISTING_EXTERNAL_ID, PROVIDER).get();
        Eligibility updated = importService.createOrUpdateEligibility(newEligibility, serviceFromDb);

        assertEquals(dbSize, eligibilityService.findAll().size());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreateLangsIfConfidentialEvenIfServiceHasNoneOfThem() {
        Service service = generateExistingService();
        Language language = new Language().language(NEW_STRING).srvc(service);
        language.setIsConfidential(true);

        languageService.findAll().size();
        importService.createOrUpdateLangsForService(Set.of(language), service, null);

        assertEquals(0, languageService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplaceLangsIfConfidentialEvenIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(service);

        Language newLanguage = new Language().language(NEW_STRING).srvc(service);
        newLanguage.setIsConfidential(true);

        languageService.findAll().size();
        importService.createOrUpdateLangsForService(Set.of(newLanguage), service, null);

        assertEquals(0, languageService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplaceLangsIfConfidential() {
        Service service = generateExistingService();

        Language languageToBeUpdated = new Language().language(EXISTING_STRING).srvc(service);
        em.persist(languageToBeUpdated);

        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(service);

        Language newLanguage = new Language().language(NEW_STRING).srvc(service);
        newLanguage.setIsConfidential(true);

        languageService.findAll().size();
        importService.createOrUpdateLangsForService(Set.of(newLanguage), service, null);

        assertEquals(0, languageService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotCreateOpeningHoursIfConfidentialEvenIfServiceHasNoneOfThem() {
        Service service = generateExistingService();
        OpeningHours openingHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        openingHours.setIsConfidential(true);

        int scheduleDbSize = regularScheduleService.findAll().size();
        openingHoursService.findAll().size();
        importService.createOrUpdateOpeningHoursForService(Set.of(openingHours), service);

        assertEquals(scheduleDbSize + 1, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplaceOpeningHoursIfConfidentialEvenIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        RegularSchedule schedule = new RegularSchedule();
        em.persist(otherOpeningHours);
        schedule.setOpeningHours(Set.of(otherOpeningHours));
        em.persist(schedule);
        service.setRegularSchedule(schedule);
        em.flush();

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        newOpeningHours.setIsConfidential(true);

        int scheduleDbSize = regularScheduleService.findAll().size();
        openingHoursService.findAll().size();
        importService.createOrUpdateOpeningHoursForService(Set.of(newOpeningHours), service);

        assertEquals(scheduleDbSize, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplaceOpeningHoursIfConfidential() {
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
        newOpeningHours.setIsConfidential(true);

        int scheduleDbSize = regularScheduleService.findAll().size();
        openingHoursService.findAll().size();
        importService.createOrUpdateOpeningHoursForService(Set.of(newOpeningHours), service);

        assertEquals(scheduleDbSize, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
    }

    private Location generateNewLocation() {
        Location result = new Location().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
        result.setIsConfidential(true);
        return result;
    }

    private Location generateExistingLocation() {
        Location result = new Location().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }
    
    private Service generateNewService() {
        Service result = new Service().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
        result.setIsConfidential(true);
        return result;
    }

    private Service generateExistingService() {
        Service result = new Service().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }

    private SystemAccount generateExistingAccount() {
        SystemAccount account = new SystemAccount().name(PROVIDER);
        em.persist(account);
        return account;
    }

    private Organization generateNewOrganization(SystemAccount account) {
        Organization result = new Organization().externalDbId(NEW_EXTERNAL_ID).account(account)
            .name(NEW_STRING).active(NEW_BOOLEAN);
        result.setIsConfidential(true);
        return result;
    }

    private Organization generateExistingOrganization(SystemAccount account) {
        Organization result = new Organization().externalDbId(EXISTING_EXTERNAL_ID).account(account)
            .name(EXISTING_STRING).active(EXISTING_BOOLEAN);
        em.persist(result);
        return result;
    }
}
