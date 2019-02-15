package org.benetech.servicenet.adapter.icarol;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.icarol.eden.EdenDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ICarolDataAdapterCompleteTest {

    private static final String COMPLETE_JSON = "icarol/complete.json";
    private static final String PROVIDER_NAME = "Eden";
    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
        "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit " +
        "esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
        "officia deserunt mollit anim id est laborum.";
    private static final String DIFF_LOREM_IPSUM = "different " + LOREM_IPSUM;

    @Autowired
    private EdenDataAdapter adapter;

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
    private PostalAddressService postalAddressService;

    @Autowired
    private LocationService locationService;

    private SingleImportData importData;

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(COMPLETE_JSON);
        importData = new SingleImportData(json, new DataImportReport(), PROVIDER_NAME, true);
    }

    @Test
    @Ignore("We should use descriptionText instead of description")
    public void shouldImportCompleteOrganizations() {
        adapter.importData(importData);

        Organization result = organizationService.findAll().get(0);
        assertEquals("HOUSING AUTHORITY OF THE COUNTY OF Commoncounty (ABCD)", result.getName());
        assertNull(result.getAlternateName());
        assertNull(result.getEmail());
        assertEquals("1", result.getExternalDbId());
        assertEquals("Special District; County", result.getLegalStatus());
        assertEquals(DIFF_LOREM_IPSUM, result.getDescription());
        assertTrue(result.getActive());
        assertEquals("www.example.org", result.getUrl());
        assertFalse(result.getIsConfidential());
    }

    @Test
    @Ignore("https://github.com/benetech/ServiceNet/wiki/iCarol-Data-Mapping-(Eden-and-UWBA) " +
            "Program.requiredDocumentation, column: 'Mapped to'")
    public void shouldImportCompleteService() {
        adapter.importData(importData);

        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("CarpetHanger Office", result.getName());
        assertNull(result.getAlternateName());
        assertEquals(DIFF_LOREM_IPSUM, result.getDescription());
        assertEquals("www.example.org", result.getUrl());
        assertNull(result.getEmail());
        assertEquals("Active", result.getStatus());
        assertNull(result.getInterpretationServices());
        assertEquals("Call or check website for notices on applications.", result.getApplicationProcess());
        assertNull(result.getWaitTime());
        assertEquals("Free", result.getFees());
        assertNull(result.getAccreditations());
        assertNull(result.getLicenses());
        assertEquals("Program", result.getType());
        assertEquals("3", result.getExternalDbId());
    }

    @Test
    public void shouldImportCompleteLocation() {
        adapter.importData(importData);

        LocationDTO result = locationService.findAll().get(0);

        assertEquals("12345 Cool Street - CarpetHanger (CA)", result.getName());
        assertEquals(40.123456, result.getLatitude());
        assertEquals(-120.123456, result.getLongitude());
        assertEquals("11", result.getExternalDbId());
    }

    @Test
    @Ignore("Map county to region")
    public void shouldImportCompletePhysicalAddress() {
        adapter.importData(importData);

        PhysicalAddressDTO result = physicalAddressService.findAll().get(0);

        assertNull(result.getAttention());
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("Commoncounty", result.getRegion());
        assertEquals("CA", result.getStateProvince());
        assertEquals("12345", result.getPostalCode());
        assertEquals("United States", result.getCountry());
    }

    @Test
    @Ignore("Map county to region")
    public void shouldImportCompletePostalAddress() {
        adapter.importData(importData);

        PostalAddressDTO result = postalAddressService.findAll().get(0);

        assertNull(result.getAttention());
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("Commoncounty", result.getRegion());
        assertEquals("CA", result.getStateProvince());
        assertEquals("12345", result.getPostalCode());
        assertEquals("United States", result.getCountry());
    }

    @Test
    public void shouldImportCompleteEligibility() {
        adapter.importData(importData);

        EligibilityDTO result = eligibilityService.findAll().get(0);

        assertEquals("Low-income family, elderly (age 62 or over), persons with disabilities, or other persons.",
            result.getEligibility());
    }

    @Test
    @Ignore("We should normalize the hours")
    public void shouldImportOpeningHours() {
        adapter.importData(importData);

        List<OpeningHoursDTO> result = openingHoursService.findAll();

        for (int i = 0; i < 5; i++) {
            assertEquals((Integer) i, result.get(i).getWeekday());
        }
        for (OpeningHoursDTO hours : result) {
            assertEquals("08:00", hours.getOpensAt());
            assertEquals("04:45", hours.getClosesAt());
        }
    }

    @Test
    @Ignore("Unhandled languages")
    public void shouldImportLanguages() {
        adapter.importData(importData);
        List<LanguageDTO> result = languageService.findAll();

        assertEquals("Farsi", result.get(0).getLanguage());
        assertEquals("Spanish", result.get(1).getLanguage());
        assertEquals("Vietnamese", result.get(2).getLanguage());
        assertEquals("English", result.get(3).getLanguage());
    }

    @Test
    @Ignore("The type should be based on label or purpose.")
    public void shouldImportCompletePhones() {
        adapter.importData(importData);

        List<PhoneDTO> result = phoneService.findAll();

        PhoneDTO first = result.get(0);
        assertEquals("123-465-7890", first.getNumber());
        assertNull(first.getExtension());
        assertEquals("Main number for program", first.getType());
        assertNull(first.getLanguage());
        assertEquals("CarpetHanger  Office", first.getDescription());
        PhoneDTO second = result.get(1);
        assertEquals("678-901-2345", second.getNumber());
        assertNull(second.getExtension());
        assertEquals("Fax", second.getType());
        assertNull(second.getLanguage());
        assertNull(second.getDescription());
        PhoneDTO third = result.get(2);
        assertEquals("789-012-3456", third.getNumber());
        assertNull(third.getExtension());
        assertEquals("MYY", third.getType());
        assertNull(third.getLanguage());
        assertNull(third.getDescription());
    }

    @Test
    @Ignore("We should add details based on site.accessibility.public.")
    public void shouldImportCompleteAccessibility() {
        adapter.importData(importData);

        AccessibilityForDisabilitiesDTO result = accessibilityForDisabilitiesService.findAll().get(0);

        assertEquals("Wheelchair accessible/Ramp/Special parking/Restroom", result.getAccessibility());
        assertEquals("AB RoadCompany lines 1, 2, 3 and 4 stop within 2 blocks. BART-CarpetHanger  " +
                     "Station connects with AB RoadCompany lines 1, 2, 3 and 4",
                     result.getDetails());
    }
}
