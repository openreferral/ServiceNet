package org.benetech.servicenet.adapter.smcconnect;

import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
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
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.benetech.servicenet.adapter.AdapterTestsUtils.readResourceAsString;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.SMCCONNECT;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.JSON;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.PROVIDER;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.ADDRESSES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.CONTACTS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.HOLIDAY_SCHEDULE;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.LOCATIONS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.MAIL_ADDRESSES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.ORGANIZATIONS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.PHONES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.PROGRAMS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.REGULAR_SCHEDULES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.SERVICES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class SMCConnectDataRelationshipsTest {

    @Autowired
    private SMCConnectDataAdapter adapter;

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
    private RegularScheduleService regularScheduleService;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private HolidayScheduleService holidayScheduleService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setUp() throws IOException {
        testDatabaseManagement.clearDb();
        List<String> fileNames = Arrays.asList(ADDRESSES, CONTACTS, HOLIDAY_SCHEDULE, LOCATIONS,
            MAIL_ADDRESSES, ORGANIZATIONS, PHONES, PROGRAMS, REGULAR_SCHEDULES, SERVICES);

        List<DocumentUpload> uploads = new ArrayList<>();
        List<String> data = new ArrayList<>();
        for (String fileName : fileNames) {
            DocumentUpload upload = new DocumentUpload();
            upload.setFilename(fileName);
            uploads.add(upload);
            data.add(readResourceAsString(SMCCONNECT + fileName + JSON));
        }

        MultipleImportData importData = new MultipleImportData(data, uploads,
            new DataImportReport(), PROVIDER, true, null);
        adapter.importData(importData);
    }

    @Test
    public void testRelationsAfterGetLanguageFromJsonServiceBased() {
        // Language has a reference to the Service and Location
        List<LanguageDTO> results = languageService.findAll();
        assertEquals(2, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        List<LocationDTO> locations = locationService.findAll();

        results.forEach(r -> {
            if (r.getSrvcId() != null) {
                assertEquals(services.get(0).getId(), r.getSrvcId());
            } else {
                assertEquals(locations.get(0).getId(), r.getLocationId());
            }
        });
    }

    @Test
    public void testRelationsAfterGetLanguageFromJsonLocationBased() {
        // Language has a reference to the Service and Location
        List<LanguageDTO> results = languageService.findAll();
         assertEquals(2, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        List<LocationDTO> locations = locationService.findAll();

        results.forEach(r -> {
            if (r.getSrvcId() != null) {
                assertEquals(services.get(0).getId(), r.getSrvcId());
            } else {
                assertEquals(locations.get(0).getId(), r.getLocationId());
            }
        });
    }

    @Test
    public void testRelationsAfterGetEligibilityFromJson() {
        // Eligibility has a reference to the Service
        List<EligibilityDTO> results = eligibilityService.findAll();

        List<ServiceDTO> services = serviceService.findAll();
        
        assertEquals(services.get(0).getId(), results.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetPhoneFromJson() {
        // Phone has a reference to the Service
        List<PhoneDTO> results = phoneService.findAll();

        List<ServiceDTO> services = serviceService.findAll();
        
        assertEquals(services.get(0).getId(), results.get(0).getSrvcId());
    }

    @Test
    public void testRelationsAfterGetPhysicalAddressFromJson() {
        // PhysicalAddress has a reference to the Location
        List<PhysicalAddressDTO> results = physicalAddressService.findAll();

        List<LocationDTO> locations = locationService.findAll();
        
        assertEquals(locations.get(0).getId(), results.get(0).getLocationId());
    }

    @Test
    public void testRelationsAfterGetPostalAddressFromJson() {
        // PostalAddress has a reference to the Location
        List<PostalAddressDTO> results = postalAddressService.findAll();

        List<LocationDTO> locations = locationService.findAll();
        
        assertEquals(locations.get(0).getId(), results.get(0).getLocationId());
    }

    @Test
    public void testRelationsAfterGetServiceFromJson() {
        // Service has a reference to the Organization
        List<ServiceDTO> results = serviceService.findAll();

        List<OrganizationDTO> organizations = organizationService.findAllDTOs();
        
        assertEquals(organizations.get(0).getId(), results.get(0).getOrganizationId());
    }

    @Test
    public void testRelationsAfterGetOpeningHoursFromJson() {
        // OpeningHours has a reference to the RegularSchedule
        List<OpeningHoursDTO> results = openingHoursService.findAll();
         assertEquals(5, results.size());

        List<RegularScheduleDTO> schedules = regularScheduleService.findAll();
        assertEquals(2, schedules.size());

        results.forEach(r ->
            assertThat(
                r.getRegularScheduleId(),
                Matchers
                    .either(Matchers.is(schedules.get(0).getId()))
                    .or(Matchers.is(schedules.get(1).getId())))
        );
    }

    @Test
    public void testRelationsAfterGetRegularScheduleFromJson() {
        // RegularSchedule has a reference to the Service and Location
        List<RegularScheduleDTO> results = regularScheduleService.findAll();
         assertEquals(2, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        List<LocationDTO> locations = locationService.findAll();

        results.forEach(r -> {
            if (r.getSrvcId() != null) {
                assertEquals(services.get(0).getId(), r.getSrvcId());
            } else {
                assertEquals(locations.get(0).getId(), r.getLocationId());
            }
        });

    }

    @Test
    public void testRelationsAfterGetFundingFromJson() {
        // Funding has a reference to the Service or Organization
        List<FundingDTO> results = fundingService.findAll();
         assertEquals(2, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        List<OrganizationDTO> organizations = organizationService.findAllDTOs();

        results.forEach(r -> {
            if (r.getSrvcId() != null) {
                assertEquals(services.get(0).getId(), r.getSrvcId());
            } else {
                assertEquals(organizations.get(0).getId(), r.getOrganizationId());
            }
        });
    }

    @Test
    public void testRelationsAfterGetContactFromJson() {
        // Contact has a reference to the Service and Organization
        List<ContactDTO> results = contactService.findAll();
         assertEquals(2, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        List<OrganizationDTO> organizations = organizationService.findAllDTOs();

        results.forEach(r -> {
            if (r.getSrvcId() != null) {
                assertEquals(services.get(0).getId(), r.getSrvcId());
            } else {
                assertEquals(organizations.get(0).getId(), r.getOrganizationId());
            }
        });
    }

    @Test
    public void testRelationsAfterGetProgramFromJson() {
        // Program has a reference to the Organization
        List<ProgramDTO> results = programService.findAll();

        List<OrganizationDTO> organizations = organizationService.findAllDTOs();

        assertEquals(organizations.get(0).getId(), results.get(0).getOrganizationId());
    }

    @Test
    public void testRelationsAfterGetHolidayScheduleFromJson() {
        // HolidaySchedule has a reference to the Service and Location
        List<HolidayScheduleDTO> results = holidayScheduleService.findAll();
         assertEquals(2, results.size());

        List<ServiceDTO> services = serviceService.findAll();
        List<LocationDTO> locations = locationService.findAll();

        results.forEach(r -> {
            if (r.getSrvcId() != null) {
                assertEquals(services.get(0).getId(), r.getSrvcId());
            } else {
                assertEquals(locations.get(0).getId(), r.getLocationId());
            }
        });
    }
}
