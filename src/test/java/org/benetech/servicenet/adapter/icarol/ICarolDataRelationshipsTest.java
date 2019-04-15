package org.benetech.servicenet.adapter.icarol;

import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.icarol.eden.EdenDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class ICarolDataRelationshipsTest {

    private static final String COMPLETE_JSON = "icarol/complete.json";
    private static final String PROVIDER_NAME = "Eden";

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

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    private SingleImportData importData;

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(COMPLETE_JSON);
        importData = new SingleImportData(json, new DataImportReport(), PROVIDER_NAME, true, null);
        adapter.importData(importData);
    }

    @Test
    public void testRelationsAfterGetLanguageFromJson() {
        // Language has a reference to the Service or Location
        List<LanguageDTO> result = languageService.findAll();

        List<ServiceDTO> services = serviceService.findAll();

        assertEquals(services.get(0).getId(), result.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetAccessabilityForDisabilitiesFromJson() {
        // AccessibilityForDisabilities has a reference to the Location
        List<LocationDTO> locations = locationService.findAll();
        assertEquals(1, locations.size());
        List<AccessibilityForDisabilitiesDTO> result = accessibilityForDisabilitiesService.findAll();

        assertEquals(locations.get(0).getId(), result.get(0).getLocationId());
    }

    @Test
    public void testRelationsAfterGetEligibilityFromJson() {
        // Eligibility has a reference to the Service
        List<ServiceDTO> services = serviceService.findAll();
        assertEquals(1, services.size());
        List<EligibilityDTO> result = eligibilityService.findAll();

        assertEquals(services.get(0).getId(), result.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetPhoneFromJson() {
        // Phone has a reference to the Service or Location
        List<PhoneDTO> result = phoneService.findAll();

        List<ServiceDTO> services = serviceService.findAll();
        assertEquals(1, services.size());

        assertEquals(services.get(0).getId(), result.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetPhysicalAddressFromJson() {
        // PhysicalAddress has a reference to the Location
        List<LocationDTO> locations = locationService.findAll();
        assertEquals(1, locations.size());
        List<PhysicalAddressDTO> result = physicalAddressService.findAll();

        assertEquals(locations.get(0).getId(), result.get(0).getLocationId());
    }

    @Test
    public void testRelationsAfterGetPostalAddressFromJson() {
        // PostalAddress has a reference to the Location
        List<LocationDTO> locations = locationService.findAll();
        assertEquals(1, locations.size());
        List<PostalAddressDTO> result = postalAddressService.findAll();

        assertEquals(locations.get(0).getId(), result.get(0).getLocationId());
    }

    @Test
    public void testRelationsAfterGetServiceFromJson() {
        // Service has a reference to the Organization
        List<OrganizationDTO> organizations = organizationService.findAllDTOs();
        assertEquals(1, organizations.size());
        List<ServiceDTO> result = serviceService.findAll();

        assertEquals(organizations.get(0).getId(), result.get(0).getOrganizationId());
    }

    @Test
    public void testRelationsAfterGetOrganizationFromJson() {
        // Organization has a reference to the Location
        List<LocationDTO> locations = locationService.findAll();
        assertEquals(1, locations.size());
        List<OrganizationDTO> result = organizationService.findAllDTOs();

        assertEquals(locations.get(0).getOrganizationId(), result.get(0).getId());
    }

    @Test
    public void testRelationsAfterGetOpeningHoursFromJson() {
        // OpeningHours has a reference to the RegularSchedule
        List<RegularScheduleDTO> schedules = regularScheduleService.findAll();

        List<OpeningHoursDTO> hours = openingHoursService.findAll();

        assertEquals(schedules.get(0).getId(), hours.get(0).getRegularScheduleId());
    }

    @Test
    public void testRelationsAfterGetRegularScheduleFromJson() {
        // RegularSchedule has a reference to the Service
        List<ServiceDTO> services = serviceService.findAll();
        assertEquals(1, services.size());
        List<RegularScheduleDTO> result = regularScheduleService.findAll();

        assertEquals(services.get(0).getId(), result.get(0).getSrvcId());
    }
}
