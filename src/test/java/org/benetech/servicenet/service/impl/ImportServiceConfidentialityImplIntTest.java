package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.shared.model.ImportData;
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
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.SharedImportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Set;

import static org.benetech.servicenet.TestConstants.PROVIDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class, MockedGeocodingConfiguration.class})
public class ImportServiceConfidentialityImplIntTest {

    private static final String NEW_EXTERNAL_ID = "1000";
    private static final String EXISTING_EXTERNAL_ID = "2000";
    private static final String NEW_STRING = "new string";
    private static final String EXISTING_STRING = "existing string";
    private static final String OTHER_STRING = "other string";
    private static final boolean NEW_BOOLEAN = true;
    private static final boolean EXISTING_BOOLEAN = false;
    private static final int NEW_INT = 1;
    private static final int OTHER_INT = 2;
    private static final ImportData IMPORT_DATA = new ImportData(new DataImportReport(), PROVIDER, true);

    @Autowired
    private EntityManager em;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ImportManager importManager;

    @Autowired
    private LocationImportService locationImportService;

    @Autowired
    private ServiceImportService serviceImportService;

    @Autowired
    private SharedImportService sharedImportService;

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

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Test
    @Transactional
    public void shouldNotCreateLocationIfConfidential() {
        Location location = generateNewLocation();
        location.setIsConfidential(true);

        importManager.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(0, locationService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdateLocationIfConfidential() {
        Location location = generateExistingLocation();
        importManager.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);
        assertEquals(1, locationService.findAll().size());

        location.setIsConfidential(true);
        location.setName(NEW_STRING);
        var updated = importManager.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(1, locationService.findAll().size());
        assertEquals(EXISTING_STRING, locationService.findAll().get(0).getName());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreatePhysicalAddressIfConfidential() {
        PhysicalAddress address = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING);
        address.setIsConfidential(true);
        Location location = generateExistingLocation().physicalAddress(address);
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(0, physicalAddressService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdatePhysicalAddressIfConfidential() {
        PhysicalAddress existingAddress = new PhysicalAddress().address1(EXISTING_STRING).city(EXISTING_STRING).stateProvince(EXISTING_STRING);
        Location location = generateExistingLocation().physicalAddress(existingAddress);
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);
        assertEquals(1, physicalAddressService.findAll().size());

        PhysicalAddress newAddress = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING);
        newAddress.setIsConfidential(true);
        location.setPhysicalAddress(newAddress);
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(1, physicalAddressService.findAll().size());
        assertEquals(EXISTING_STRING, physicalAddressService.findAll().get(0).getAddress1());
    }

    @Test
    @Transactional
    public void shouldNotCreatePostalAddressIfConfidential() {
        PostalAddress address = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING);
        address.setIsConfidential(true);
        Location location = generateExistingLocation().postalAddress(address);
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(0, postalAddressService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdatePostalAddressIfConfidential() {
        PostalAddress existingAddress = new PostalAddress().address1(EXISTING_STRING).city(EXISTING_STRING).stateProvince(EXISTING_STRING);
        Location location = generateExistingLocation().postalAddress(existingAddress);
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);
        assertEquals(1, postalAddressService.findAll().size());

        PostalAddress newAddress = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING);
        newAddress.setIsConfidential(true);
        location.setPostalAddress(newAddress);
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(1, postalAddressService.findAll().size());
        assertEquals(EXISTING_STRING, postalAddressService.findAll().get(0).getAddress1());
    }

    @Test
    @Transactional
    public void shouldNotCreateAccessibilityIfConfidentialEvenIfLocationHasFewOfThemButNotThisOne() {
        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities().accessibility(OTHER_STRING);
        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities().accessibility(NEW_STRING).details(NEW_STRING);
        newAccessibility.setIsConfidential(true);
        Location location = generateExistingLocation();
        location.setAccessibilities(Set.of(newAccessibility, otherAccessibility));
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(1, accessibilityService.findAll().size());
        assertEquals(OTHER_STRING, accessibilityService.findAll().get(0).getAccessibility());
    }

    @Test
    @Transactional
    public void shouldNotUpdateAccessibilityIfConfidential() {
        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities().accessibility(OTHER_STRING);
        Location location = generateExistingLocation();
        location.setAccessibilities(Set.of(otherAccessibility));
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);
        assertEquals(1, accessibilityService.findAll().size());

        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities().accessibility(NEW_STRING).details(NEW_STRING);
        newAccessibility.setIsConfidential(true);
        location.setAccessibilities(Set.of(otherAccessibility, newAccessibility));
        locationImportService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, IMPORT_DATA);

        assertEquals(1, accessibilityService.findAll().size());
        assertEquals(OTHER_STRING, accessibilityService.findAll().get(0).getAccessibility());
    }

    @Test
    @Transactional
    public void shouldNotCreateOrganizationIfConfidential() {
        Organization organization = generateNewOrganization(generateExistingAccount());
        organization.setIsConfidential(true);

        var created = importManager.createOrUpdateOrganization(organization, EXISTING_EXTERNAL_ID, new ImportData(new DataImportReport(), PROVIDER, true));

        assertEquals(0, organizationService.findAllDTOs().size());
        assertNull(created);
    }

    @Test
    @Transactional
    public void shouldNotUpdateOrganizationIfConfidential() {
        SystemAccount account = generateExistingAccount();
        Organization newOrganization = generateExistingOrganization(account);
        importManager.createOrUpdateOrganization(newOrganization, EXISTING_EXTERNAL_ID, new ImportData(new DataImportReport(), PROVIDER, true));
        assertEquals(1, organizationService.findAllDTOs().size());

        newOrganization.setIsConfidential(true);
        newOrganization.setName(NEW_STRING);
        var updated = importManager.createOrUpdateOrganization(newOrganization, EXISTING_EXTERNAL_ID, new ImportData(new DataImportReport(), PROVIDER, true));

        assertEquals(1, organizationService.findAllDTOs().size());
        assertEquals(EXISTING_STRING, organizationService.findAllDTOs().get(0).getName());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreateServiceIfConfidential() {
        Service service = generateNewService();
        service.setIsConfidential(true);

        var created = importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(0, serviceService.findAll().size());
        assertNull(created);
    }

    @Test
    @Transactional
    public void shouldNotUpdateServiceIfConfidential() {
        Service newService = generateNewService();
        newService.setIsConfidential(true);
        generateExistingService();

        int dbSize = serviceService.findAll().size();
        Service updated = importManager.createOrUpdateService(newService, EXISTING_EXTERNAL_ID, PROVIDER,
            new DataImportReport());

        assertEquals(dbSize, serviceService.findAll().size());
        assertNull(updated);
    }

    @Test
    @Transactional
    public void shouldNotCreatePhonesIfConfidentialEvenIfServiceHasNoneOfThem() {
        Phone phone = new Phone().number(NEW_STRING);
        phone.setIsConfidential(true);
        Service service = generateExistingService().phones(Set.of(phone));

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER,
            new DataImportReport());

        assertEquals(0, phoneService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplacePhonesIfConfidentialEvenIfServiceHasFewOfThemButNotThisOne() {
        Phone otherPhone = new Phone().number(OTHER_STRING);
        Service service = generateExistingService().phones(Set.of(otherPhone));
        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        Phone newPhone = new Phone().number(NEW_STRING);
        newPhone.setIsConfidential(true);
        service.setPhones(Set.of(newPhone));

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(0, phoneService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplacePhonesIfConfidential() {
        Phone phoneToBeUpdated = new Phone().number(EXISTING_STRING);
        Phone otherPhone = new Phone().number(OTHER_STRING);
        otherPhone.setIsConfidential(true);
        Service service = generateExistingService().phones(Set.of(phoneToBeUpdated, otherPhone));

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(1, phoneService.findAll().size());
        assertEquals(EXISTING_STRING, phoneService.findAll().get(0).getNumber());
    }

    @Test
    @Transactional
    public void shouldNotCreateEligibilityIfConfidential() {
        Eligibility eligibility = new Eligibility().eligibility(NEW_STRING);
        eligibility.setIsConfidential(true);
        Service service = generateExistingService().eligibility(eligibility);

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(0, eligibilityService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotUpdateEligibilityIfConfidential() {
        Eligibility existingEligibility = new Eligibility().eligibility(EXISTING_STRING);
        Service service = generateExistingService().eligibility(existingEligibility);
        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());
        assertEquals(1, eligibilityService.findAll().size());

        Eligibility newEligibility = new Eligibility().eligibility(NEW_STRING);
        newEligibility.setIsConfidential(true);
        service.setEligibility(newEligibility);

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(1, eligibilityService.findAll().size());
        assertEquals(EXISTING_STRING, eligibilityService.findAll().get(0).getEligibility());
    }

    @Test
    @Transactional
    public void shouldNotCreateLangsIfConfidentialEvenIfServiceHasNoneOfThem() {
        Language language = new Language().language(NEW_STRING);
        language.setIsConfidential(true);
        Service service = generateExistingService().langs(Set.of(language));

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(0, languageService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplaceLangsIfConfidentialEvenIfServiceHasFewOfThemButNotThisOne() {
        Language otherLanguage = new Language().language(OTHER_STRING);
        Language newLanguage = new Language().language(NEW_STRING);
        newLanguage.setIsConfidential(true);
        Service service = generateExistingService().langs(Set.of(otherLanguage, newLanguage));

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(1, languageService.findAll().size());
        assertEquals(OTHER_STRING, languageService.findAll().get(0).getLanguage());
    }

    @Test
    @Transactional
    public void shouldNotReplaceLangsIfConfidential() {
        Language languageToBeUpdated = new Language().language(EXISTING_STRING);
        Service service = generateExistingService().langs(Set.of(languageToBeUpdated));
        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        Language otherLanguage = new Language().language(OTHER_STRING);
        otherLanguage.setIsConfidential(true);
        service.setLangs(Set.of(languageToBeUpdated, otherLanguage));
        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(1, languageService.findAll().size());
        assertEquals(EXISTING_STRING, languageService.findAll().get(0).getLanguage());
    }

    @Test
    @Transactional
    public void shouldNotCreateOpeningHoursIfConfidentialEvenIfServiceHasNoneOfThem() {
        OpeningHours openingHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        openingHours.setIsConfidential(true);
        Service service = generateExistingService();

        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(0, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotReplaceOpeningHoursIfConfidentialEvenIfServiceHasFewOfThemButNotThisOne() {
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        RegularSchedule schedule = new RegularSchedule();
        schedule.setOpeningHours(Set.of(otherOpeningHours));
        Service service = generateExistingService();
        service.setRegularSchedule(schedule);
        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());
        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(1, openingHoursService.findAll().size());

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        newOpeningHours.setIsConfidential(true);
        schedule.setOpeningHours(Set.of(newOpeningHours));
        importManager.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
    }

    private Location generateNewLocation() {
        Location result = new Location().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
        result.setIsConfidential(true);
        return result;
    }

    private Location generateExistingLocation() {
        return new Location().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
    }

    private Service generateNewService() {
        Service result = new Service().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
        result.setIsConfidential(true);
        return result;
    }

    private Service generateExistingService() {
        return new Service().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
    }

    private SystemAccount generateExistingAccount() {
        return new SystemAccount().name(PROVIDER);
    }

    private Organization generateNewOrganization(SystemAccount account) {
        Organization result = new Organization().externalDbId(NEW_EXTERNAL_ID).account(account)
            .name(NEW_STRING).active(NEW_BOOLEAN);
        result.setIsConfidential(true);
        return result;
    }

    private Organization generateExistingOrganization(SystemAccount account) {
        return new Organization().externalDbId(EXISTING_EXTERNAL_ID).account(account)
            .name(EXISTING_STRING).active(EXISTING_BOOLEAN);
    }
}
