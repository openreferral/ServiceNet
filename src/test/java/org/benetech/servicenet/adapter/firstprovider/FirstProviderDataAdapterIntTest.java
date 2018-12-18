package org.benetech.servicenet.adapter.firstprovider;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.EligibilityService;
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
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.benetech.servicenet.service.mapper.LocationMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@Transactional
public class FirstProviderDataAdapterIntTest {

    @Autowired
    private FirstProviderDataAdapter adapter;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private AccessibilityForDisabilitiesService accessibilityForDisabilitiesService;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private LocationMapper locationMapper;

    @Before
    public void persistData() throws IOException {
        String data = AdapterTestsUtils.readResourceAsString("FirstProviderData.json");
        adapter.importData(new SingleImportData(data, null));
    }

    @Test
    public void shouldPersistEntitiesFromJsonData() {
        int entriesNumber = 2;

        assertEquals(entriesNumber, locationService.findAll().size());
        assertEquals(entriesNumber, phoneService.findAll().size());
        assertEquals(entriesNumber, physicalAddressService.findAll().size());
        assertEquals(entriesNumber, postalAddressService.findAll().size());

        assertEquals(entriesNumber, organizationService.findAll().size());
        assertEquals(entriesNumber, eligibilityService.findAll().size());
        assertEquals(entriesNumber, serviceService.findAll().size());
        assertEquals(6, programService.findAll().size());
        assertEquals(4, languageService.findAll().size());
        assertEquals(14, openingHoursService.findAll().size());
        assertEquals(entriesNumber, accessibilityForDisabilitiesService.findAll().size());
        assertEquals(2, regularScheduleService.findAll().size());
    }

    @Test
    public void shouldPersistEntitiesWithReferenceToTheLocation() {
        LocationDTO firstLocation = locationService.findAll().get(0);
        PostalAddressDTO firstPostalAddress = postalAddressService.findAll().get(0);
        PhysicalAddressDTO firstPhysicalAddress = physicalAddressService.findAll().get(0);
        PhoneDTO firstPhone = phoneService.findAll().get(0);
        OrganizationDTO firstOrganization = organizationService.findAll().get(0);
        AccessibilityForDisabilitiesDTO firstAccessibility = accessibilityForDisabilitiesService.findAll().get(0);
        RegularScheduleDTO firstRegularSchedule = regularScheduleService.findAll().get(0);

        assertEquals(firstPostalAddress.getLocationId(), firstLocation.getId());
        assertEquals(firstPhysicalAddress.getLocationId(), firstLocation.getId());
        assertEquals(firstPhone.getLocationId(), firstLocation.getId());
        assertEquals(firstOrganization.getLocationId(), firstLocation.getId());
        assertEquals(firstAccessibility.getLocationId(), firstLocation.getId());
        assertEquals(firstRegularSchedule.getLocationId(), firstLocation.getId());
    }

    @Test
    public void shouldPersistEntitiesWithReferenceToTheService() {
        ServiceDTO firstService = serviceService.findAll().get(0);
        PhoneDTO firstPhone = phoneService.findAll().get(0);
        RegularScheduleDTO firstRegularSchedule = regularScheduleService.findAll().get(0);
        List<LanguageDTO> langs = languageService.findAll().subList(0, 1);

        assertEquals(firstPhone.getSrvcId(), firstService.getId());
        assertEquals(firstPhone.getSrvcId(), firstService.getId());
        assertEquals(firstRegularSchedule.getSrvcId(), firstService.getId());
        for (LanguageDTO language : langs) {
            assertEquals(language.getSrvcId(), firstService.getId());
        }
    }

    @Test
    public void shouldPersistEntitiesWithReferenceToTheOrganization() {
        OrganizationDTO firstOrganization = organizationService.findAll().get(0);
        ServiceDTO firstService = serviceService.findAll().get(0);
        List<ProgramDTO> programs = programService.findAll().subList(0, 1);

        assertEquals(firstService.getOrganizationId(), firstOrganization.getId());
        for (ProgramDTO program : programs) {
            assertEquals(program.getOrganizationId(), firstOrganization.getId());
        }
    }
}
