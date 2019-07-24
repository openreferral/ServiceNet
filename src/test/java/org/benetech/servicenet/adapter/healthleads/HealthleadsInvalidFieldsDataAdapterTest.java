package org.benetech.servicenet.adapter.healthleads;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.adapter.AdapterTestsUtils.readResourceAsString;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.ELIGIBILITY;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.JSON;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.LANGUAGES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.LOCATIONS;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.ORGANIZATIONS;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.PHONES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.PHYSICAL_ADDRESSES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.REQUIRED_DOCUMENTS;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.SERVICES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.SERVICES_AT_LOCATION;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.SERVICES_TAXONOMY;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.TAXONOMY;
import static org.benetech.servicenet.config.Constants.HEALTHLEADS_PROVIDER;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class HealthleadsInvalidFieldsDataAdapterTest {

    private static final String INCOMPLETE = "healthleads/invalid_fields/";

    @Autowired
    private HealthleadsDataAdapter adapter;

    @Autowired
    private ImportManager importManager;


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
    private TaxonomyService taxonomyService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setUp() throws IOException {
        testDatabaseManagement.clearDb();
        List<String> fileNames = Arrays.asList(
            ELIGIBILITY, LANGUAGES, LOCATIONS,
            ORGANIZATIONS, PHONES, PHYSICAL_ADDRESSES,
            SERVICES_TAXONOMY, TAXONOMY, REQUIRED_DOCUMENTS,
            SERVICES, SERVICES_AT_LOCATION
        );

        List<DocumentUpload> uploads = new ArrayList<>();
        List<String> data = new ArrayList<>();
        for (String fileName : fileNames) {
            DocumentUpload upload = new DocumentUpload();
            upload.setFilename(fileName);
            uploads.add(upload);
            data.add(readResourceAsString(INCOMPLETE + fileName + JSON));
        }

        MultipleImportData importData = new MultipleImportData(data, uploads, new DataImportReport(), HEALTHLEADS_PROVIDER, true, null);
        adapter.importData(importData);
    }

    @Test
    public void shouldImportOrganizationWithInvalidFields() {
        assertEquals(1, organizationService.findAll().size());
        Organization result = organizationService.findAll().get(0);
        assertEquals("Organization Name", result.getName());

        // Fields with validation errors
        assertEquals("", result.getUrl());
        assertEquals("", result.getEmail());
        assertEquals("", result.getTaxStatus());
    }
    @Test
    public void shouldImportLocationWithInvalidFields() {
        assertEquals(1, locationService.findAll().size());
        LocationDTO result = locationService.findAll().get(0);
        assertEquals("Location Name", result.getName());

        // Fields with validation errors
        assertEquals("", result.getAlternateName());
        assertEquals("", result.getTransportation());
    }

    @Test
    public void shouldImportServiceWithInvalidFields() {
        assertEquals(1, serviceService.findAll().size());
        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("Service Name", result.getName());

        // Fields with validation errors
        assertEquals("", result.getEmail());
        assertEquals("", result.getUrl());
        assertEquals("", result.getStatus());
    }

    @Test
    public void shouldImportLocationBasedPhoneWithInvalidFields() {
        List<PhoneDTO> result = phoneService.findAll();

        assertTrue(result.stream().anyMatch(x ->
                    x.getExtension().equals(200)
                    && x.getDescription().equals("Phone Description For Location")
                    // Fields with validation errors
                    && x.getNumber().equals("")
                    && x.getType().equals("")
                    && x.getLanguage().equals("")
            )
        );
    }

    @Test
    public void shouldImportServiceBasedPhoneWithInvalidFields() {
        List<PhoneDTO> result = phoneService.findAll();

        assertTrue(result.stream().anyMatch(x ->
                    x.getExtension().equals(500)
                    && x.getDescription().equals("Phone Description")
                    // Fields with validation errors
                    && x.getNumber().equals("")
                    && x.getType().equals("")
                    && x.getLanguage().equals("")
            )
        );
    }

    @Test
    public void shouldImportPhysicalAddressWithInvalidFields() {
        assertEquals(1, physicalAddressService.findAll().size());
        PhysicalAddressDTO result = physicalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAddress1());
        assertEquals("", result.getAttention());
        assertEquals("", result.getCity());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getRegion());
        assertEquals("", result.getCountry());
    }

    @Test
    public void shouldImportTaxonomyWithInvalidFields() {
        assertEquals(1, taxonomyService.findAll().size());
        TaxonomyDTO result = taxonomyService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getName());
        assertEquals("", result.getVocabulary());
    }
}
