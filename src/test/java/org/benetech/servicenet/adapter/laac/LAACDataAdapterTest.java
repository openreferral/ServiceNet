package org.benetech.servicenet.adapter.laac;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.benetech.servicenet.config.Constants.LAAC_PROVIDER;

import java.io.IOException;
import java.time.LocalDate;
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
public class LAACDataAdapterTest {

    private static final int THREE = 3;
    private static final int TWO = 2;
    private static final int FOUR = 4;
    private static final String ID_FORMAT = "extId%d";
    private static final String SERVICE_NAME_FORMAT = "Organization Name %d - Service";
    private static final String SERVICE_TYPE_FORMAT = "ServiceType0%d, ServiceType1%d";
    private static final String SERVICE_DESCRIPTION_FORMAT = "ServiceType Desc %d";
    private static final String LOCATION_NAME_FORMAT = "Organization Name %d - Location";
    private static final String LOCATION_DESCRIPTION_FORMAT = "Area%d0, Area%d1, Area%d2, Area%d3";
    private static final String ORG_NAME_FORMAT = "Organization Name %d";
    private static final String ORG_DESCRIPTION_FORMAT = "Who we are description%d";
    private static final String ORG_URL_FORMAT = "http://www.org.com%d";
    private static final LocalDate ORG_BASE_YEAR_INCORPORATED = LocalDate.of(2019, 1, 20);
    private static final String ELIGIBILITY_FORMAT = "This is eligibility!%d";
    private static final String RUSSIAN = "Russian / Росси́я";
    private static final String SPANISH = "Spanish / Español";
    private static final String NOT_AVAILABLE = "N/A";
    private static final String CITY_FORMAT = "City%d";
    private static final String COUNTRY_FORMAT = "Country%d";
    private static final String POSTAL_CODE_FORMAT = "1234%d";
    private static final String STATE_FORMAT = "State%d";
    private static final String ATTENTION_FORMAT = "Attention%d";
    private static final String ADDRESS_FORMAT = "Address%d";
    private static final String PHONE_FORMAT = "(123) 123-456%d";

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
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Test
    public void testSavingLAACData() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString("laac/orgs.json");
        SingleImportData importData = new SingleImportData(json, new DataImportReport(), LAAC_PROVIDER, true);

        adapter.importData(importData);

        assertExtractedService();
        assertExtractedLocation();
        assertExtractedOrganization();
        assertExtractedEligibility();
        assertExtractedContact();
        assertExtractedLanguages();
        assertExtractedPhysicalAddress();
        assertExtractedPhones();
    }

    private void assertExtractedPhones() {
        assertEquals(THREE, phoneService.findAll().size());

        PhoneDTO phone1 = phoneService.findAll().get(0);
        assertEquals(String.format(PHONE_FORMAT, 1), phone1.getNumber());

        PhoneDTO phone2 = phoneService.findAll().get(1);
        assertEquals(String.format(PHONE_FORMAT, TWO), phone2.getNumber());

        PhoneDTO phone3 = phoneService.findAll().get(2);
        assertEquals(String.format(PHONE_FORMAT, THREE), phone3.getNumber());
    }

    private void assertExtractedPhysicalAddress() {
        assertEquals(THREE, physicalAddressService.findAll().size());

        AddressDTO physicalAddress1 = physicalAddressService.findAll().get(0);
        assertEquals(String.format(CITY_FORMAT, 1), physicalAddress1.getCity());
        assertEquals(String.format(COUNTRY_FORMAT, 1), physicalAddress1.getCountry());
        assertEquals(String.format(POSTAL_CODE_FORMAT, 1), physicalAddress1.getPostalCode());
        assertEquals(String.format(STATE_FORMAT, 1), physicalAddress1.getStateProvince());
        assertEquals(NOT_AVAILABLE, physicalAddress1.getAddress1());
        assertNull(physicalAddress1.getAttention());

        AddressDTO physicalAddress2 = physicalAddressService.findAll().get(1);
        assertEquals(String.format(CITY_FORMAT, TWO), physicalAddress2.getCity());
        assertEquals(String.format(COUNTRY_FORMAT, TWO), physicalAddress2.getCountry());
        assertEquals(String.format(POSTAL_CODE_FORMAT, TWO), physicalAddress2.getPostalCode());
        assertEquals(String.format(STATE_FORMAT, TWO), physicalAddress2.getStateProvince());
        assertEquals(String.format(ADDRESS_FORMAT, TWO), physicalAddress2.getAddress1());
        assertNull(physicalAddress2.getAttention());

        AddressDTO physicalAddress3 = physicalAddressService.findAll().get(TWO);
        assertEquals(String.format(CITY_FORMAT, THREE), physicalAddress3.getCity());
        assertEquals(String.format(COUNTRY_FORMAT, THREE), physicalAddress3.getCountry());
        assertEquals(String.format(POSTAL_CODE_FORMAT, THREE), physicalAddress3.getPostalCode());
        assertEquals(String.format(STATE_FORMAT, THREE), physicalAddress3.getStateProvince());
        assertEquals(String.format(ADDRESS_FORMAT, THREE), physicalAddress3.getAddress1());
        assertEquals(String.format(ATTENTION_FORMAT, THREE), physicalAddress3.getAttention());
    }

    private void assertExtractedLanguages() {
        assertEquals(FOUR, languageService.findAll().size());

        for (LanguageDTO language : languageService.findAll()) {
            assertTrue(RUSSIAN.equals(language.getLanguage())
                || SPANISH.equals(language.getLanguage()));
        }
    }

    private void assertExtractedContact() {
        assertEquals(TWO, contactService.findAll().size());

        ContactDTO contact1 = contactService.findAll().get(0);
        assertEquals("John Smith", contact1.getName());

        ContactDTO contact2 = contactService.findAll().get(1);
        assertEquals("Jane Black", contact2.getName());
    }

    private void assertExtractedEligibility() {
        assertEquals(TWO, eligibilityService.findAll().size());
        assertEquals(String.format(ELIGIBILITY_FORMAT, 1),
            eligibilityService.findAll().get(0).getEligibility());
        assertEquals(String.format(ELIGIBILITY_FORMAT, TWO),
            eligibilityService.findAll().get(1).getEligibility());
    }

    private void assertExtractedOrganization() {
        assertEquals(THREE, organizationService.findAll().size());

        int i = 1;
        for (Organization organization : organizationService.findAll()) {
            assertEquals(String.format(ID_FORMAT, i), organization.getExternalDbId());
            assertEquals(String.format(ORG_NAME_FORMAT, i), organization.getName());
            assertEquals(String.format(ORG_DESCRIPTION_FORMAT, i), organization.getDescription());
            assertEquals(String.format(ORG_URL_FORMAT, i), organization.getUrl());
            assertEquals(ORG_BASE_YEAR_INCORPORATED.plusDays(i), organization.getYearIncorporated());
            i++;
        }
    }

    private void assertExtractedService() {
        assertEquals(THREE, serviceService.findAll().size());

        int i = 1;
        for (ServiceDTO service : serviceService.findAll()) {
            assertEquals(String.format(SERVICE_NAME_FORMAT, i), service.getName());
            assertEquals(String.format(SERVICE_TYPE_FORMAT, i, i), service.getType());
            assertEquals(String.format(SERVICE_DESCRIPTION_FORMAT, i), service.getDescription());
            i++;
        }
    }

    private void assertExtractedLocation() {
        assertEquals(THREE, locationService.findAll().size());

        int i = 1;
        for (LocationDTO location : locationService.findAll()) {
            assertEquals(String.format(LOCATION_NAME_FORMAT, i), location.getName());
            assertEquals(String.format(LOCATION_DESCRIPTION_FORMAT, i, i, i, i), location.getDescription());
            i++;
        }
    }
}
