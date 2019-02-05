package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
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
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.HolidayScheduleService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.ProgramService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.TmpImportService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    private TmpImportService importService;

    @Autowired
    private EntityManager em;

    // region services
    @Autowired
    private LocationService locationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ServiceService serviceService;

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
    private TaxonomyService taxonomyService;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private HolidayScheduleService holidayScheduleService;
    //endregion

    @Test
    @Transactional
    public void shouldCreateLocation() {
        Location location = generateNewLocation();

        assertEquals(0, locationService.findAll().size());
        importService.createOrUpdateLocation(location, NEW_EXTERNAL_ID, PROVIDER);

        List<LocationDTO> all = locationService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getName().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldUpdateLocation() {
        Location newLocation = generateNewLocation();
        generateExistingLocation();

        assertEquals(1, locationService.findAll().size());
        importService.createOrUpdateLocation(newLocation, EXISTING_EXTERNAL_ID, PROVIDER);

        List<LocationDTO> all = locationService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getName().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldCreatePhysicalAddress() {
        Location location = generateNewLocation();
        PhysicalAddress address = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .attention(NEW_STRING).country(NEW_STRING).postalCode(NEW_STRING).region(NEW_STRING);
        location.setPhysicalAddress(address);

        assertEquals(0, physicalAddressService.findAll().size());
        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhysicalAddressDTO> all = physicalAddressService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(pa -> pa.getLocationId() != null 
            && pa.getAddress1().equals(NEW_STRING)
            && pa.getCity().equals(NEW_STRING)
            && pa.getStateProvince().equals(NEW_STRING)
            && pa.getAttention().equals(NEW_STRING)
            && pa.getCountry().equals(NEW_STRING)
            && pa.getPostalCode().equals(NEW_STRING)
            && pa.getRegion().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldUpdatePhysicalAddress() {
        Location location = generateExistingLocation();
        PhysicalAddress existingAddress = new PhysicalAddress().address1(EXISTING_STRING).city(EXISTING_STRING)
            .stateProvince(EXISTING_STRING).attention(EXISTING_STRING).country(EXISTING_STRING)
            .postalCode(EXISTING_STRING).region(EXISTING_STRING).location(location);
        em.persist(existingAddress);
        em.flush();
        em.refresh(location);

        Location locationToUpdate = generateExistingLocationDoNotPersist();
        PhysicalAddress newAddress = new PhysicalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .attention(NEW_STRING).country(NEW_STRING).postalCode(NEW_STRING).region(NEW_STRING);
        locationToUpdate.setPhysicalAddress(newAddress);

        assertEquals(1, locationService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhysicalAddressDTO> all = physicalAddressService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(pa -> pa.getLocationId() != null
            && pa.getAddress1().equals(NEW_STRING)
            && pa.getCity().equals(NEW_STRING)
            && pa.getStateProvince().equals(NEW_STRING)
            && pa.getAttention().equals(NEW_STRING)
            && pa.getCountry().equals(NEW_STRING)
            && pa.getPostalCode().equals(NEW_STRING)
            && pa.getRegion().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldCreatePostalAddress() {
        Location location = generateNewLocation();
        PostalAddress address = new PostalAddress().address1(NEW_STRING).city(NEW_STRING).stateProvince(NEW_STRING)
            .attention(NEW_STRING).country(NEW_STRING).postalCode(NEW_STRING).region(NEW_STRING);
        location.setPostalAddress(address);

        assertEquals(0, postalAddressService.findAll().size());

        importService.createOrUpdateLocation(location, NEW_EXTERNAL_ID, PROVIDER);

        assertEquals(1, locationService.findAll().size());
        List<PostalAddressDTO> all = postalAddressService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(pa -> pa.getLocationId() != null
            && pa.getAddress1().equals(NEW_STRING)
            && pa.getCity().equals(NEW_STRING)
            && pa.getStateProvince().equals(NEW_STRING)
            && pa.getAttention().equals(NEW_STRING)
            && pa.getCountry().equals(NEW_STRING)
            && pa.getPostalCode().equals(NEW_STRING)
            && pa.getRegion().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldUpdatePostalAddress() {
        Location location = generateExistingLocation();
        PostalAddress existingAddress = new PostalAddress().address1(EXISTING_STRING).city(EXISTING_STRING)
            .stateProvince(EXISTING_STRING).attention(EXISTING_STRING).country(EXISTING_STRING)
            .attention(NEW_STRING).postalCode(EXISTING_STRING).region(EXISTING_STRING).location(location);
        em.persist(existingAddress);
        em.flush();
        em.refresh(location);

        Location locationToUpdate = generateExistingLocationDoNotPersist();
        PostalAddress newAddress = new PostalAddress().address1(NEW_STRING).city(NEW_STRING)
            .stateProvince(NEW_STRING).attention(NEW_STRING).country(NEW_STRING)
            .postalCode(NEW_STRING).region(NEW_STRING);
        locationToUpdate.setPostalAddress(newAddress);

        assertEquals(1, locationService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        assertEquals(1, locationService.findAll().size());
        List<PostalAddressDTO> all = postalAddressService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(pa -> pa.getLocationId() != null
            && pa.getAddress1().equals(NEW_STRING)
            && pa.getCity().equals(NEW_STRING)
            && pa.getStateProvince().equals(NEW_STRING)
            && pa.getAttention().equals(NEW_STRING)
            && pa.getCountry().equals(NEW_STRING)
            && pa.getPostalCode().equals(NEW_STRING)
            && pa.getRegion().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldCreateAccessibilityIfLocationHasNoneOfThem() {
        Location location = generateNewLocation();
        AccessibilityForDisabilities accessibility = new AccessibilityForDisabilities()
            .accessibility(NEW_STRING);
        location.setAccessibilities(mutableSet(accessibility));

        assertEquals(0, accessibilityService.findAll().size());

        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<AccessibilityForDisabilitiesDTO> created = accessibilityService.findAll();
        assertEquals(1, created.size());
        assertTrue(created.stream().allMatch(a -> a.getAccessibility().equals(NEW_STRING)
            && a.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateAccessibilityIfLocationHasFewOfThemButNotThisOne() {
        Location location = generateExistingLocation();
        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities()
            .accessibility(OTHER_STRING)
            .location(location);
        em.persist(otherAccessibility);
        em.flush();
        em.refresh(location);

        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities()
            .accessibility(NEW_STRING).details(NEW_STRING);
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setAccessibilities(mutableSet(newAccessibility));

        assertEquals(1, accessibilityService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<AccessibilityForDisabilitiesDTO> all = accessibilityService.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(OTHER_STRING)));
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(NEW_STRING)));
        assertTrue(all.stream().allMatch(a -> a.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldUpdateAccessibility() {
        Location location = generateExistingLocation();
        AccessibilityForDisabilities accessibilityToBeUpdated = new AccessibilityForDisabilities()
            .accessibility(EXISTING_STRING).details(EXISTING_STRING)
            .location(location);
        em.persist(accessibilityToBeUpdated);
        AccessibilityForDisabilities otherAccessibility = new AccessibilityForDisabilities()
            .accessibility(OTHER_STRING)
            .location(location);
        em.persist(otherAccessibility);
        em.flush();
        em.refresh(location);

        Location locationToUpdate = generateExistingLocationDoNotPersist();
        AccessibilityForDisabilities newAccessibility = new AccessibilityForDisabilities()
            .accessibility(EXISTING_STRING).details(NEW_STRING);
        locationToUpdate.setAccessibilities(mutableSet(newAccessibility));

        assertEquals(2, accessibilityService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<AccessibilityForDisabilitiesDTO> all = accessibilityService.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(EXISTING_STRING)));
        assertTrue(all.stream().anyMatch(a -> a.getAccessibility().equals(OTHER_STRING)));
        assertTrue(all.stream().anyMatch(a -> NEW_STRING.equals(a.getDetails())));
        assertTrue(all.stream().anyMatch(a -> a.getDetails() == null));
        assertTrue(all.stream().allMatch(a -> a.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateOrganization() {
        Organization organization = generateNewOrganization(generateExistingAccount());
        DataImportReport report = new DataImportReport();

        assertEquals(0, organizationService.findAllDTOs().size());

        Organization org = importService.createOrUpdateOrganization(organization,
            EXISTING_EXTERNAL_ID, PROVIDER, report);

        assertEquals(1, organizationService.findAllDTOs().size());
        assertEquals(NEW_STRING, org.getName());
        assertEquals(Integer.valueOf(1), report.getNumberOfCreatedOrgs());
    }

    @Test
    @Transactional
    public void shouldUpdateOrganization() {
        SystemAccount account = generateExistingAccount();
        Organization newOrganization = generateNewOrganization(account);
        generateExistingOrganization(account);
        DataImportReport report = new DataImportReport();

        assertEquals(1, organizationService.findAllDTOs().size());

        Organization updated = importService.createOrUpdateOrganization(newOrganization,
            EXISTING_EXTERNAL_ID, PROVIDER, report);

        assertEquals(1, organizationService.findAllDTOs().size());
        assertEquals(NEW_STRING, updated.getName());
        assertEquals(NEW_BOOLEAN, updated.getActive());
        assertEquals(Integer.valueOf(1), report.getNumberOfUpdatedOrgs());
    }

    @Test
    @Transactional
    public void shouldCreateService() {
        Service service = generateNewService();
        DataImportReport report = new DataImportReport();
        assertEquals(0, serviceService.findAll().size());

        Service srvc = importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, report);

        assertEquals(1, serviceService.findAll().size());
        assertEquals(NEW_STRING, srvc.getName());
        assertEquals(Integer.valueOf(1), report.getNumberOfCreatedServices());
    }

    @Test
    @Transactional
    public void shouldUpdateService() {
        generateExistingService();
        Service newService = generateNewService();
        DataImportReport report = new DataImportReport();

        assertEquals(1, serviceService.findAll().size());

        Service updated = importService.createOrUpdateService(newService,
            EXISTING_EXTERNAL_ID, PROVIDER, report);

        assertEquals(1, serviceService.findAll().size());
        assertEquals(NEW_STRING, updated.getName());
        assertEquals(Integer.valueOf(1), report.getNumberOfUpdatedServices());
    }
    
    @Test
    @Transactional
    public void shouldCreatePhonesIfServiceHasNoneOfThem() {
        Service service = generateNewService();
        Phone phone = new Phone().number(NEW_STRING).srvc(service);
        service.setPhones(mutableSet(phone));

        assertEquals(0, phoneService.findAll().size());

        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(p -> p.getNumber().equals(NEW_STRING)
            && p.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldReplacePhonesIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        em.persist(otherPhone);
        em.flush();
        em.refresh(service);

        Phone newPhone = new Phone().number(NEW_STRING);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setPhones(mutableSet(newPhone));

        assertEquals(1, phoneService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(p -> p.getNumber().equals(NEW_STRING)
            && p.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldReplacePhonesForService() {
        Service service = generateExistingService();
        Phone phoneToBeUpdated = new Phone().number(EXISTING_STRING).srvc(service);
        em.persist(phoneToBeUpdated);
        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        em.persist(otherPhone);
        em.flush();
        em.refresh(service);

        Phone newPhone = new Phone().number(NEW_STRING);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setPhones(mutableSet(newPhone));

        assertEquals(2, phoneService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(phone -> phone.getNumber().equals(NEW_STRING)
            && phone.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldCreatePhonesIfLocationHasNoneOfThem() {
        Location location = generateNewLocation();
        Phone phone = new Phone().number(NEW_STRING).location(location);
        location.setPhones(mutableSet(phone));

        assertEquals(0, phoneService.findAll().size());

        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(p -> p.getNumber().equals(NEW_STRING)
            && p.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldReplacePhonesIfLocationHasFewOfThemButNotThisOne() {
        Location location = generateExistingLocation();
        Phone otherPhone = new Phone().number(OTHER_STRING).location(location);
        em.persist(otherPhone);
        em.flush();
        em.refresh(location);

        Phone newPhone = new Phone().number(NEW_STRING);
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setPhones(mutableSet(newPhone));

        assertEquals(1, phoneService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(p -> p.getNumber().equals(NEW_STRING)
            && p.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldReplacePhonesForLocation() {
        Location location = generateExistingLocation();
        Phone phoneToBeUpdated = new Phone().number(EXISTING_STRING).location(location);
        em.persist(phoneToBeUpdated);
        Phone otherPhone = new Phone().number(OTHER_STRING).location(location);
        em.persist(otherPhone);
        em.flush();
        em.refresh(location);

        Phone newPhone = new Phone().number(NEW_STRING);
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setPhones(mutableSet(newPhone));

        assertEquals(2, phoneService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(phone -> phone.getNumber().equals(NEW_STRING)
            && phone.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateEligibility() {
        Service service = generateNewService();
        Eligibility eligibility = new Eligibility().eligibility(NEW_STRING);
        service.setEligibility(eligibility);

        assertEquals(0, eligibilityService.findAll().size());

        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<EligibilityDTO> all = eligibilityService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(e -> e.getEligibility().equals(NEW_STRING)
            && e.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldUpdateEligibility() {
        Service service = generateExistingService();
        Eligibility existingEligibility = new Eligibility().eligibility(EXISTING_STRING).srvc(service);
        em.persist(existingEligibility);
        em.flush();
        em.refresh(service);

        Eligibility newEligibility = new Eligibility().eligibility(NEW_STRING);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setEligibility(newEligibility);

        assertEquals(1, eligibilityService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<EligibilityDTO> all = eligibilityService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(e -> e.getEligibility().equals(NEW_STRING)
            && e.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateLangsIfServiceHasNoneOfThem() {
        Service service = generateNewService();
        Language language = new Language().language(NEW_STRING);
        service.setLangs(mutableSet(language));

        Organization org = generateExistingOrganizationDoNotPersist()
            .services(Collections.singleton(service));

        assertEquals(0, languageService.findAll().size());

        importService.createOrUpdateOrganization(org, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getLanguage().equals(NEW_STRING)
            && l.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldReplaceLangsIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(service);

        Language newLanguage = new Language().language(NEW_STRING);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setLangs(mutableSet(newLanguage));

        assertEquals(1, languageService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getLanguage().equals(NEW_STRING)
            && l.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldReplaceLangsForService() {
        Service service = generateExistingService();

        Language languageToBeUpdated = new Language().language(EXISTING_STRING).srvc(service);
        em.persist(languageToBeUpdated);

        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(service);

        Language newLanguage = new Language().language(NEW_STRING);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setLangs(mutableSet(newLanguage));

        assertEquals(2, languageService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getLanguage().equals(NEW_STRING)
            && l.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateLangsIfLocationHasNoneOfThem() {
        Location location = generateNewLocation();
        Language language = new Language().language(NEW_STRING);
        location.setLangs(mutableSet(language));

        Organization org = generateExistingOrganizationDoNotPersist()
            .locations(Collections.singleton(location));

        assertEquals(0, languageService.findAll().size());

        importService.createOrUpdateOrganization(org, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getLanguage().equals(NEW_STRING)
            && l.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldReplaceLangsIfLocationHasFewOfThemButNotThisOne() {
        Location location = generateExistingLocation();
        Language otherLanguage = new Language().language(OTHER_STRING).location(location);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(location);

        Language newLanguage = new Language().language(NEW_STRING);
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setLangs(mutableSet(newLanguage));

        assertEquals(1, languageService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getLanguage().equals(NEW_STRING)
            && l.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldReplaceLangsForLocation() {
        Location location = generateExistingLocation();

        Language languageToBeUpdated = new Language().language(EXISTING_STRING).location(location);
        em.persist(languageToBeUpdated);

        Language otherLanguage = new Language().language(OTHER_STRING).location(location);
        em.persist(otherLanguage);
        em.flush();
        em.refresh(location);

        Language newLanguage = new Language().language(NEW_STRING);
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setLangs(mutableSet(newLanguage));

        assertEquals(2, languageService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getLanguage().equals(NEW_STRING)
            && l.getLocationId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateOpeningHoursIfServiceHasNoneOfThem() {
        Service service = generateNewService();
        OpeningHours openingHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        service.setRegularSchedule(new RegularSchedule().openingHours(mutableSet(openingHours)));

        assertEquals(0, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertTrue(allSchedules.stream().allMatch(s -> s.getSrvcId() != null));
        assertEquals(1, allHours.size());
        assertTrue(allHours.stream().allMatch(o -> o.getWeekday().equals(NEW_INT)
                && o.getOpensAt().equals(NEW_STRING)
                && o.getClosesAt().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldReplaceOpeningHoursIfServiceHasFewOfThemButNotThisOne() {
        Service service = generateExistingService();
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        em.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule().openingHours(mutableSet(otherOpeningHours)).srvc(service);
        em.persist(schedule);
        em.flush();
        em.refresh(service);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setRegularSchedule(new RegularSchedule().openingHours(mutableSet(newOpeningHours)));

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(1, openingHoursService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertTrue(allSchedules.stream().allMatch(s -> s.getSrvcId() != null));
        assertEquals(1, allHours.size());
        assertTrue(allHours.stream().allMatch(o -> o.getWeekday().equals(NEW_INT)
            && o.getOpensAt().equals(NEW_STRING)
            && o.getClosesAt().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldReplaceOpeningHoursForService() {
        Service service = generateExistingService();
        OpeningHours openingHoursToBeUpdated = new OpeningHours().weekday(EXISTING_INT)
            .opensAt(EXISTING_STRING).closesAt(EXISTING_STRING);
        em.persist(openingHoursToBeUpdated);
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT)
            .opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        em.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule()
            .openingHours(mutableSet(openingHoursToBeUpdated, otherOpeningHours))
                .srvc(service);
        em.persist(schedule);
        em.flush();
        em.refresh(service);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        RegularSchedule newSchedule = new RegularSchedule().openingHours(mutableSet(newOpeningHours));
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setRegularSchedule(newSchedule);

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(2, openingHoursService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertTrue(allSchedules.stream().allMatch(s -> s.getSrvcId() != null));
        assertEquals(1, allHours.size());
        assertTrue(allHours.stream().allMatch(o -> o.getWeekday().equals(NEW_INT)
            && o.getOpensAt().equals(NEW_STRING)
            && o.getClosesAt().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldCreateOpeningHoursIfLocationHasNoneOfThem() {
        Location location = generateNewLocation();
        OpeningHours openingHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        location.setRegularSchedule(new RegularSchedule().openingHours(mutableSet(openingHours)));

        assertEquals(0, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
        importService.createOrUpdateLocation(location, EXISTING_EXTERNAL_ID, PROVIDER);

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertTrue(allSchedules.stream().allMatch(s -> s.getLocationId() != null));
        assertEquals(1, allHours.size());
        assertTrue(allHours.stream().allMatch(o -> o.getWeekday().equals(NEW_INT)
            && o.getOpensAt().equals(NEW_STRING)
            && o.getClosesAt().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldReplaceOpeningHoursIfLocationHasFewOfThemButNotThisOne() {
        Location location = generateExistingLocation();
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        em.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule().openingHours(mutableSet(otherOpeningHours)).location(location);
        em.persist(schedule);
        em.flush();
        em.refresh(location);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT)
            .opensAt(NEW_STRING).closesAt(NEW_STRING);
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setRegularSchedule(new RegularSchedule().openingHours(mutableSet(newOpeningHours)));

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(1, openingHoursService.findAll().size());
        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertTrue(allSchedules.stream().allMatch(s -> s.getLocationId() != null));
        assertEquals(1, allHours.size());
        assertTrue(allHours.stream().allMatch(o -> o.getWeekday().equals(NEW_INT)
            && o.getOpensAt().equals(NEW_STRING)
            && o.getClosesAt().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldReplaceOpeningHoursForLocation() {
        Location location = generateExistingLocation();
        OpeningHours openingHoursToBeUpdated = new OpeningHours().weekday(EXISTING_INT)
            .opensAt(EXISTING_STRING).closesAt(EXISTING_STRING);
        em.persist(openingHoursToBeUpdated);
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT)
            .opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        em.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule()
            .openingHours(mutableSet(openingHoursToBeUpdated, otherOpeningHours))
            .location(location);
        em.persist(schedule);
        em.flush();
        em.refresh(location);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        RegularSchedule newSchedule = new RegularSchedule().openingHours(mutableSet(newOpeningHours));
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setRegularSchedule(newSchedule);

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(2, openingHoursService.findAll().size());

        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertTrue(allSchedules.stream().allMatch(s -> s.getLocationId() != null));
        assertEquals(1, allHours.size());
        assertTrue(allHours.stream().allMatch(o -> o.getWeekday().equals(NEW_INT)
            && o.getOpensAt().equals(NEW_STRING)
            && o.getClosesAt().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldCreateTaxonomy() {
        Taxonomy taxonomy = generateNewTaxonomy();

        assertEquals(0, taxonomyService.findAll().size());
        importService.createOrUpdateTaxonomy(taxonomy, EXISTING_EXTERNAL_ID, PROVIDER);

        List<TaxonomyDTO> all = taxonomyService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(t -> t.getName().equals(NEW_STRING)));
    }
    
    @Test
    @Transactional
    public void shouldUpdateTaxonomy() {
        Taxonomy newTaxonomy = generateNewTaxonomy();
        generateExistingTaxonomy();

        assertEquals(1, taxonomyService.findAll().size());
        importService.createOrUpdateTaxonomy(newTaxonomy, EXISTING_EXTERNAL_ID, PROVIDER);

        List<TaxonomyDTO> all = taxonomyService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(t -> t.getName().equals(NEW_STRING)));
    }

    @Test
    @Transactional
    public void shouldCreateFundingForOrganization() {
        Organization organization = generateNewOrganization(generateExistingAccount());
        Funding funding = new Funding().source(NEW_STRING);
        organization.setFunding(funding);

        assertEquals(0, fundingService.findAll().size());
        importService.createOrUpdateOrganization(organization, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(f -> f.getSource().equals(NEW_STRING)
            && f.getOrganizationId() != null));
    }

    @Test
    @Transactional
    public void shouldUpdateFundingForOrganization() {
        Organization organization = generateExistingOrganization(generateExistingAccount());
        Funding funding = new Funding().source(EXISTING_STRING).organization(organization);
        em.persist(funding);
        em.flush();
        em.refresh(organization);

        Organization organizationToUpdate = generateExistingOrganizationDoNotPersist();
        Funding newFunding = new Funding().source(NEW_STRING);
        organizationToUpdate.setFunding(newFunding);

        assertEquals(1, fundingService.findAll().size());
        importService.createOrUpdateOrganization(organizationToUpdate,
            EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(f -> f.getSource().equals(NEW_STRING)
            && f.getOrganizationId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateFundingForService() {
        Service service = generateNewService();
        Funding funding = new Funding().source(NEW_STRING);
        service.setFunding(funding);

        assertEquals(0, fundingService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(f -> f.getSource().equals(NEW_STRING)
            && f.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldUpdateFundingForService() {
        Service service = generateExistingService();
        Funding funding = new Funding().source(EXISTING_STRING).srvc(service);
        em.persist(funding);
        em.flush();
        em.refresh(service);

        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        Funding newFunding = new Funding().source(NEW_STRING);
        serviceToUpdate.setFunding(newFunding);

        assertEquals(1, fundingService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(f -> f.getSource().equals(NEW_STRING)
            && f.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateProgramsIfOrganizationHasNoneOfThem() {
        Organization organization = generateNewOrganization(generateExistingAccount());
        Program program = new Program().name(NEW_STRING);
        organization.setPrograms(mutableSet(program));

        assertEquals(0, programService.findAll().size());
        importService.createOrUpdateOrganization(organization, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ProgramDTO> all = programService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(p -> p.getName().equals(NEW_STRING)
            && p.getOrganizationId() != null));
    }

    @Test
    @Transactional
    public void shouldReplaceProgramsIfOrganizationHasFewButNotThisOne() {
        Organization organization = generateExistingOrganization(generateExistingAccount());
        Program program = new Program().name(OTHER_STRING).organization(organization);
        em.persist(program);
        em.flush();
        em.refresh(organization);

        Program newProgram = new Program().name(NEW_STRING);
        Organization organizationToUpdate = generateExistingOrganizationDoNotPersist().programs(mutableSet(newProgram));

        assertEquals(1, programService.findAll().size());
        importService.createOrUpdateOrganization(organizationToUpdate,
            EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ProgramDTO> all = programService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(l -> l.getName().equals(NEW_STRING)
            && l.getOrganizationId() != null));
    }

    @Test
    @Transactional
    public void shouldCreateServiceTaxonomy() {
        Taxonomy taxonomy = generateExistingTaxonomy();
        Service service = generateNewService();
        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy().taxonomy(taxonomy).srvc(service);
        service.setTaxonomies(mutableSet(serviceTaxonomy));

        assertEquals(1, taxonomyService.findAll().size());
        assertEquals(0, serviceTaxonomyService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ServiceTaxonomyDTO> all = serviceTaxonomyService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(st -> st.getSrvcId() != null
            && st.getTaxonomyId().equals(taxonomy.getId())));
    }

    @Test
    @Transactional
    public void shouldUpdateServiceTaxonomy() {
        Taxonomy taxonomy = generateOtherTaxonomy();
        Service service = generateExistingService();
        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy().taxonomy(taxonomy).srvc(service)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER);
        em.persist(serviceTaxonomy);
        em.flush();
        em.refresh(service);

        Taxonomy newTaxonomy = generateNewTaxonomy();
        em.persist(newTaxonomy);
        em.flush();
        em.refresh(newTaxonomy);
        ServiceTaxonomy newServiceTaxonomy = new ServiceTaxonomy().taxonomy(newTaxonomy)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER);
        Service serviceToUpdate = generateExistingServiceDoNotPersist().taxonomies(
            mutableSet(newServiceTaxonomy));

        assertEquals(2, taxonomyService.findAll().size());
        assertEquals(1, serviceTaxonomyService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ServiceTaxonomyDTO> all = serviceTaxonomyService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(st -> st.getSrvcId() != null
            && st.getTaxonomyId().equals(newTaxonomy.getId())));
    }

    @Test
    @Transactional
    public void shouldCreateRequiredDocument() {
        Service service = generateNewService();
        RequiredDocument document = new RequiredDocument().document(NEW_STRING)
            .externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER);
        service.setDocs(mutableSet(document));

        assertEquals(0, requiredDocumentService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RequiredDocumentDTO> all = requiredDocumentService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(rd -> rd.getDocument().equals(NEW_STRING)
            && rd.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldUpdateRequiredDocument() {
        Service service = generateExistingService();
        RequiredDocument requiredDocument = new RequiredDocument().document(EXISTING_STRING)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER).srvc(service);
        em.persist(requiredDocument);
        em.flush();
        em.refresh(service);

        RequiredDocument newDocument = new RequiredDocument().document(NEW_STRING)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setDocs(mutableSet(newDocument));

        assertEquals(1, requiredDocumentService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RequiredDocumentDTO> all = requiredDocumentService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(rd -> rd.getDocument().equals(NEW_STRING)
            && rd.getSrvcId().equals(service.getId())));
    }

    @Test
    @Transactional
    public void shouldCreateContactsForService() {
        Service service = generateNewService();
        Contact contact = new Contact().name(NEW_STRING);
        service.setContacts(mutableSet(contact));

        assertEquals(0, contactService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ContactDTO> all = contactService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(c -> c.getName().equals(NEW_STRING)
            && c.getSrvcId() != null));
    }

    @Test
    @Transactional
    public void shouldUpdateContactsForService() {
        Service service = generateExistingService();
        Contact contact = new Contact().name(EXISTING_STRING).srvc(service);
        em.persist(contact);
        em.flush();
        em.refresh(service);

        Contact newContact = new Contact().name(NEW_STRING);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setContacts(mutableSet(newContact));

        assertEquals(1, contactService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ContactDTO> all = contactService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(c -> c.getName().equals(NEW_STRING)
            && c.getSrvcId().equals(service.getId())));
    }

    @Test
    @Transactional
    public void shouldCreateHolidayScheduleForService() {
        Service service = generateNewService();
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.of(2019, 1, 3);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(start).endDate(end);
        service.setHolidaySchedule(schedule);

        assertEquals(0, holidayScheduleService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(hs -> hs.getClosesAt().equals(NEW_STRING)
            && !hs.isClosed()
            && hs.getSrvcId() != null
            && hs.getStartDate().equals(start)
            && hs.getEndDate().equals(end)));
    }

    @Test
    @Transactional
    public void shouldUpdateHolidayScheduleForService() {
        Service service = generateExistingService();
        LocalDate start = LocalDate.of(2018, 1, 1);
        LocalDate end = LocalDate.of(2018, 1, 3);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(EXISTING_STRING).closed(true).srvc(service)
            .startDate(start).endDate(end);
        em.persist(schedule);
        em.flush();
        em.refresh(service);

        LocalDate newStart = LocalDate.of(2019, 1, 1);
        LocalDate newEnd = LocalDate.of(2019, 1, 3);
        HolidaySchedule newSchedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(newStart).endDate(newEnd);
        Service serviceToUpdate = generateExistingServiceDoNotPersist();
        serviceToUpdate.setHolidaySchedule(newSchedule);

        assertEquals(1, holidayScheduleService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(hs -> hs.getClosesAt().equals(NEW_STRING)
            && !hs.isClosed()
            && hs.getSrvcId() != null
            && hs.getStartDate().equals(newStart)
            && hs.getEndDate().equals(newEnd)));
    }

    @Test
    @Transactional
    public void shouldCreateHolidayScheduleForLocation() {
        Location location = generateNewLocation();
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.of(2019, 1, 3);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(start).endDate(end);
        location.setHolidaySchedule(schedule);

        assertEquals(0, holidayScheduleService.findAll().size());
        importService.createOrUpdateLocation(location, NEW_EXTERNAL_ID, PROVIDER);

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(hs -> hs.getClosesAt().equals(NEW_STRING)
            && !hs.isClosed()
            && hs.getLocationId() != null
            && hs.getStartDate().equals(start)
            && hs.getEndDate().equals(end)));
    }

    @Test
    @Transactional
    public void shouldUpdateHolidayScheduleForLocation() {
        Location location = generateExistingLocation();
        LocalDate start = LocalDate.of(2018, 1, 1);
        LocalDate end = LocalDate.of(2018, 1, 3);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(EXISTING_STRING).closed(true).location(location)
            .startDate(start).endDate(end);
        em.persist(schedule);
        em.flush();
        em.refresh(location);

        LocalDate newStart = LocalDate.of(2019, 1, 1);
        LocalDate newEnd = LocalDate.of(2019, 1, 3);
        HolidaySchedule newSchedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(newStart).endDate(newEnd);
        Location locationToUpdate = generateExistingLocationDoNotPersist();
        locationToUpdate.setHolidaySchedule(newSchedule);

        assertEquals(1, holidayScheduleService.findAll().size());
        importService.createOrUpdateLocation(locationToUpdate, EXISTING_EXTERNAL_ID, PROVIDER);

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        assertTrue(all.stream().allMatch(hs -> hs.getClosesAt().equals(NEW_STRING)
            && !hs.isClosed()
            && hs.getLocationId() != null
            && hs.getStartDate().equals(newStart)
            && hs.getEndDate().equals(newEnd)));
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

    private Location generateExistingLocationDoNotPersist() {
        return new Location().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
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

    private SystemAccount generateExistingAccount() {
        SystemAccount account = new SystemAccount().name(PROVIDER);
        em.persist(account);
        return account;
    }

    private Organization generateNewOrganization(SystemAccount account) {
        return new Organization().externalDbId(NEW_EXTERNAL_ID).account(account)
            .name(NEW_STRING).active(NEW_BOOLEAN);
    }

    private Service generateExistingServiceDoNotPersist() {
        return new Service().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
    }

    private Organization generateExistingOrganization(SystemAccount account) {
        Organization result = new Organization().externalDbId(EXISTING_EXTERNAL_ID).account(account)
            .name(EXISTING_STRING).active(EXISTING_BOOLEAN);
        em.persist(result);
        return result;
    }

    private Organization generateExistingOrganizationDoNotPersist() {
        return new Organization().externalDbId(EXISTING_EXTERNAL_ID).account(generateExistingAccount())
            .name(EXISTING_STRING).active(EXISTING_BOOLEAN);
    }

    private Taxonomy generateNewTaxonomy() {
        return new Taxonomy().externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER)
            .name(NEW_STRING);
    }

    private Taxonomy generateExistingTaxonomy() {
        Taxonomy result = new Taxonomy().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(EXISTING_STRING);
        em.persist(result);
        return result;
    }

    private Taxonomy generateOtherTaxonomy() {
        Taxonomy result = new Taxonomy().externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER)
            .name(OTHER_STRING);
        em.persist(result);
        return result;
    }

    @SafeVarargs
    private <T> Set<T> mutableSet(T ... objects) {
        return new HashSet<T>(Arrays.asList(objects));
    }
}
