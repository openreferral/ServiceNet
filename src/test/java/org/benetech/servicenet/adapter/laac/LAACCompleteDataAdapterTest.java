package org.benetech.servicenet.adapter.laac;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.benetech.servicenet.config.Constants.LAAC_PROVIDER;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.AddressDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class LAACCompleteDataAdapterTest {

    private static final String COMPLETE_JSON = "laac/complete.json";
    private static final int YEAR = 2019;
    private static final int DAY = 21;

    @Autowired
    private LAACDataAdapter adapter;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void setUp() throws IOException {
        testDatabaseManagement.clearDb();
        String json = AdapterTestsUtils.readResourceAsString(COMPLETE_JSON);
        SingleImportData importData = new SingleImportData(json, new DataImportReport(), LAAC_PROVIDER, true);
        adapter.importData(importData);
    }

    @Test
    public void shouldImportCompleteOrganization() {
        Organization result = organizationService.findAll().get(0);
        assertEquals("Patients Health - health.com", result.getName());
        assertEquals(LocalDate.of(YEAR, 1, DAY), result.getYearIncorporated());
        assertEquals("Who we are, What are we doing?\n The description of the organization", result.getDescription());
        assertEquals("http:www.org.com/health", result.getUrl());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
        assertEquals(Boolean.TRUE, result.getActive());
    }

    @Test
    public void shouldImportCompleteService() {
        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("Patients Health - health.com - Service", result.getName());
        assertEquals("The description of the Service Types", result.getDescription());
        assertEquals("Health-Clinic, Housing Service", result.getType());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
        assertEquals("LAAC", result.getProviderName());
        assertEquals("Patients Health - health.com", result.getOrganizationName());
    }

    @Test
    public void shouldImportCompleteLocation() {
        LocationDTO result = locationService.findAll().get(0);

        assertEquals("Patients Health - health.com - Location", result.getName());
        assertEquals("Area 1, Area 2, Area 3, Area 4", result.getDescription());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
    }

    @Test
    public void shouldImportCompleteContact() {
        List<ContactDTO> result = contactService.findAll();

        assertEquals("Ben Smith", result.get(0).getName());
        assertEquals("Patients Health - health.com", result.get(0).getOrganizationName());
        assertEquals("LAAC", result.get(0).getProviderName());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.get(0).getExternalDbId());
    }

    @Test
    public void shouldImportCompleteEligibility() {
        EligibilityDTO result = eligibilityService.findAll().get(0);

        assertEquals("Eligibility Type, Other", result.getEligibility());
    }

    @Test
    public void shouldImportCompletePhone() {
        List<PhoneDTO> result = phoneService.findAll();

        assertEquals("(123) 123-4561 ext. 200", result.get(0).getNumber());
    }

    @Test
    public void shouldImportCompleteLangs() {
        Set<String> langs = languageService.findAll().stream().map(LanguageDTO::getLanguage).collect(Collectors.toSet());

        assertEquals(2, langs.size());
        assertTrue(langs.contains("Russian / Росси́я"));
        assertTrue(langs.contains("ARABIC / لعَرَبِيَّة"));
    }

    @Test
    public void shouldImportCompletePhysicalAddress() {
        AddressDTO result = physicalAddressService.findAll().get(0);

        assertEquals("123 Street", result.getAddress1());
        assertEquals("Suite 500", result.getAttention());
        assertEquals("The City", result.getCity());
        assertEquals("STATE", result.getStateProvince());
        assertEquals("12341", result.getPostalCode());
        assertEquals("Country Name", result.getCountry());
        assertEquals("Patients Health - health.com - Location", result.getLocationName());
    }
}
