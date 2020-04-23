package org.benetech.servicenet.adapter.laac;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.config.Constants.LAAC_PROVIDER;

import java.io.IOException;
import java.util.List;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class LAACDataRelationshipsTest {

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
    private LocationService locationService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    private SingleImportData importData;

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString("laac/complete.json");
        importData = new SingleImportData(json, new DataImportReport(), LAAC_PROVIDER, true);
        adapter.importData(importData);
    }

    @Test
    public void testRelationsAfterGetOrganizationFromJson() {
        // Organization is related to Location and Service
        List<OrganizationDTO> results = organizationService.findAllDTOs();
        assertEquals(1, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        List<LocationDTO> locations = locationService.findAll();

        assertEquals(services.get(0).getOrganizationId(), results.get(0).getId());
        assertEquals(locations.get(0).getOrganizationId(), results.get(0).getId());
    }

    @Test
    public void testRelationsAfterGetLanguageFromJson() {
        // Language is related to Service or Location
        List<LanguageDTO> results = languageService.findAll();
        assertEquals(2, results.size());

        List<ServiceDTO> services = serviceService.findAll();

        assertEquals(services.get(0).getId(), results.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetPhysicalAddressFromJson() {
        // Physical Address is related to Location
        List<PhysicalAddressDTO> results = physicalAddressService.findAll();
        assertEquals(1, results.size());

        List<LocationDTO> locations = locationService.findAll();

        assertEquals(locations.get(0).getId(), results.get(0).getLocationId());
    }

    @Test
    public void testRelationsAfterGetPhoneFromJson() {
        // Phone is related to Service or Location
        List<PhoneDTO> results = phoneService.findAll();
        assertEquals(1, results.size());

        List<ServiceDTO> services = serviceService.findAll();

        assertEquals(services.get(0).getId(), results.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetEligibilityFromJson() {
        // Eligibility is related to Service
        List<EligibilityDTO> results = eligibilityService.findAll();
        assertEquals(1, results.size());

        List<ServiceDTO> services = serviceService.findAll();

        assertEquals(services.get(0).getId(), results.get(0).getSrvcId());
    }
}
