package org.benetech.servicenet.adapter.sheltertech;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.repository.ContactRepository;
import org.benetech.servicenet.repository.EligibilityRepository;
import org.benetech.servicenet.repository.HolidayScheduleRepository;
import org.benetech.servicenet.repository.LanguageRepository;
import org.benetech.servicenet.repository.LocationRepository;
import org.benetech.servicenet.repository.OpeningHoursRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.repository.PhoneRepository;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.repository.PostalAddressRepository;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.repository.RequiredDocumentRepository;
import org.benetech.servicenet.repository.ServiceRepository;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ShelterTechCompleteDataAdapterTest {

    private static final String COMPLETE_JSON = "sheltertech/complete.json";
    private static final String PROVIDER_NAME = "ShelterTech";

    @Autowired
    private ShelterTechDataAdapter adapter;

    @Autowired
    private ImportManager importManager;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private AccessibilityForDisabilitiesService accessibilityForDisabilitiesService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private EligibilityRepository eligibilityRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhysicalAddressRepository physicalAddressRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Autowired
    private OpeningHoursRepository openingHoursRepository;

    @Autowired
    private HolidayScheduleRepository holidayScheduleRepository;

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private EntityManager em;

    private static SingleImportData importData;

    @BeforeClass
    public static void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(COMPLETE_JSON);
        importData = new SingleImportData(json, new DataImportReport(), PROVIDER_NAME, true);
    }

    @Before
    public void clearDb() {
        contactRepository.deleteAll();
        requiredDocumentRepository.deleteAll();
        openingHoursRepository.deleteAll();
        regularScheduleRepository.deleteAll();
        holidayScheduleRepository.deleteAll();
        languageRepository.deleteAll();
        eligibilityRepository.deleteAll();
        phoneRepository.deleteAll();
        postalAddressRepository.deleteAll();
        physicalAddressRepository.deleteAll();
        serviceRepository.deleteAll();
        locationRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void shouldImportCompleteOrganization() {
        adapter.importData(importData);
        assertEquals(1, organizationService.findAll().size());

        Organization result = organizationService.findAll().get(0);
        assertEquals("The Organization", result.getName());
        assertEquals("Alternate Organization", result.getAlternateName());
        assertEquals("org@email.com", result.getEmail());
        assertEquals("111", result.getExternalDbId());
        assertEquals("Non-profit", result.getLegalStatus());
        assertEquals("Health center offering Legal Help as well", result.getDescription());
        assertTrue(result.getActive());
        assertEquals("http://www.organization.org/xyz/", result.getUrl());
    }

    @Test
    public void shouldImportCompleteService() {
        adapter.importData(importData);
        assertEquals(1, serviceService.findAll().size());

        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("Service Name", result.getName());
        assertEquals("Alternate Service", result.getAlternateName());
        assertEquals("Certified", result.getStatus());
        assertEquals("service@email.com", result.getEmail());
        assertEquals("Free", result.getFees());
        assertEquals("222", result.getExternalDbId());
        assertEquals("The Service Description", result.getDescription());
        assertEquals("http://www.service.org/", result.getUrl());
        assertEquals("15 minutes", result.getWaitTime());
    }

    @Test
    @Ignore("address_1 is used as name, instead of using LocationUtils") //TODO Remove
    public void shouldImportCompleteLocation() {
        adapter.importData(importData);
        assertEquals(1, locationService.findAll().size());

        LocationDTO result = locationService.findAll().get(0);

        assertEquals("1233 90th St. - San Francisco (CA)", result.getName());
        assertEquals(11.1222222, result.getLatitude());
        assertEquals(-111.11111, result.getLongitude());
        assertEquals("888", result.getExternalDbId());
    }

    @Test
    public void shouldImportCompletePhysicalAddress() {
        adapter.importData(importData);
        assertEquals(1, physicalAddressService.findAll().size());

        PhysicalAddressDTO result = physicalAddressService.findAll().get(0);

        assertEquals("Room 540", result.getAttention());
        assertEquals("1233 90th St.", result.getAddress1());
        assertEquals("San Francisco", result.getCity());
        assertEquals("CA", result.getStateProvince());
        assertEquals("65454", result.getPostalCode());
        assertEquals("USA", result.getCountry());
    }

    @Test
    public void shouldImportCompletePostalAddress() {
        adapter.importData(importData);
        assertEquals(1, postalAddressService.findAll().size());

        PostalAddressDTO result = postalAddressService.findAll().get(0);

        assertEquals("Room 540", result.getAttention());
        assertEquals("1233 90th St.", result.getAddress1());
        assertEquals("San Francisco", result.getCity());
        assertEquals("CA", result.getStateProvince());
        assertEquals("65454", result.getPostalCode());
        assertEquals("USA", result.getCountry());
    }

    @Test
    public void shouldImportCompleteEligibility() {
        adapter.importData(importData);
        assertEquals(1, eligibilityService.findAll().size());

        EligibilityDTO result = eligibilityService.findAll().get(0);

        assertEquals("Adults", result.getEligibility());
    }

    @Test
    public void shouldImportCompletePhone() {
        adapter.importData(importData);
        assertEquals(1, phoneService.findAll().size());

        List<PhoneDTO> result = phoneService.findAll();

        assertEquals("(111) 222-3333", result.get(0).getNumber());
        assertEquals((Integer) 32, result.get(0).getExtension());
        assertEquals("fax: (663) 433-4324", result.get(0).getType());
        assertEquals("en", result.get(0).getLanguage());
    }

    @Test
    public void shouldImportCompleteRegularSchedule() {
        adapter.importData(importData);
        assertEquals(1, regularScheduleService.findAll().size());
    }

    @Test
    public void shouldImportCompleteRequiredDocument() {
        adapter.importData(importData);
        assertEquals(1, requiredDocumentService.findAll().size());

        RequiredDocumentDTO result = requiredDocumentService.findAll().get(0);

        assertEquals("ID, or any other document with photo", result.getDocument());
    }

    @Test
    @Ignore("Opening Hours are not persisted with ImportService") //TODO: Remove
    public void shouldImportOpeningHours() {
        assertEquals(2, openingHoursService.findAll().size());

        List<OpeningHoursDTO> result = openingHoursService.findAll();

        assertEquals((Integer) 4, result.get(0).getWeekday());
        assertEquals("22:00", result.get(0).getOpensAt());
        assertEquals("17:00", result.get(0).getClosesAt());
        assertEquals((Integer) 2, result.get(1).getWeekday());
        assertEquals("22:00", result.get(1).getOpensAt());
        assertEquals("19:00", result.get(1).getClosesAt());
    }
}
