package org.benetech.servicenet.adapter.sheltertech;

import static org.benetech.servicenet.config.Constants.SHELTER_TECH_PROVIDER;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class ShelterTechDataRelationshipsTest {

    private static final String COMPLETE_JSON = "sheltertech/complete.json";

    @Autowired
    private ShelterTechDataAdapter adapter;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    private static SingleImportData importData;

    @BeforeClass
    public static void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(COMPLETE_JSON);
        importData = new SingleImportData(json, new DataImportReport(), SHELTER_TECH_PROVIDER, true);
    }

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Test
    public void testRelationsAfterGetServiceFromJson() {
        // Service has relation to the Organization
        adapter.importData(importData);
        List<ServiceDTO> results = serviceService.findAll();
        assertEquals(1, results.size());

        List<OrganizationDTO> organizations = organizationService.findAllDTOs();
        assertEquals(1, organizations.size());

        assertEquals(organizations.get(0).getId(), results.get(0).getOrganizationId());
    }

    @Test
    public void testRelationsAfterGetLocationFromJson() {
        // Location has relation to the Organization
        adapter.importData(importData);
        List<LocationDTO> results = locationService.findAll();
        assertEquals(1, results.size());

        List<OrganizationDTO> organizations = organizationService.findAllDTOs();
        assertEquals(1, organizations.size());

        assertEquals(organizations.get(0).getId(), results.get(0).getOrganizationId());
    }

    @Test
    public void testRelationsAfterGetRegularScheduleFromJson() {
        // RegularSchedule has relation to the Service or Location
        adapter.importData(importData);
        List<RegularScheduleDTO> results = regularScheduleService.findAll();
        assertEquals(1, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        assertEquals(1, services.size());

        assertEquals(services.get(0).getId(), results.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetOpeningHoursFromJson() {
        // ScheduleDays (OpeningHours) has relation to the RegularSchedule
        adapter.importData(importData);
        List<OpeningHoursDTO> results = openingHoursService.findAll();
        assertEquals(2, results.size());

        List<RegularScheduleDTO> schedules = regularScheduleService.findAll();
        assertEquals(1, schedules.size());

        for (OpeningHoursDTO result : results) {
            assertEquals(schedules.get(0).getId(), result.getRegularScheduleId());
        }
    }

    @Test
    public void testRelationsAfterGetPhoneFromJson() {
        // Phones has relation to the Location
        adapter.importData(importData);
        List<PhoneDTO> results = phoneService.findAll();
        assertEquals(1, results.size());

        List<LocationDTO> locations = locationService.findAll();
        assertEquals(1, locations.size());

        assertEquals(locations.get(0).getId(), results.get(0).getLocationId());
    }

    @Test
    public void testRelationsAfterGetEligibilityFromJson() {
        // Eligibility has relation to the Service
        adapter.importData(importData);
        List<EligibilityDTO> results = eligibilityService.findAll();
        assertEquals(1, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        assertEquals(1, services.size());

        assertEquals(services.get(0).getId(), results.get(0).getSrvcId());
    }
}
