package org.benetech.servicenet.adapter.sheltertech;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.config.Constants.SHELTER_TECH_PROVIDER;

import java.io.IOException;
import java.util.List;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.AddressDTO;
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
public class ShelterTechInvalidFieldsDataAdapterTest {

    private static final String INVALID_FIELDS_JSON = "sheltertech/invalid_fields.json";

    @Autowired
    private ShelterTechDataAdapter adapter;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    private static SingleImportData importData;

    @SuppressWarnings("PMD.JUnit4TestShouldUseBeforeAnnotation")
    @BeforeClass
    public static void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(INVALID_FIELDS_JSON);
        importData = new SingleImportData(json, new DataImportReport(), SHELTER_TECH_PROVIDER, true);
    }

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Test
    public void shouldImportOrganizationWithInvalidFields() {
        adapter.importData(importData);
        Organization result = organizationService.findAll().get(0);
        assertEquals("The Organization", result.getName());

        // Fields with validation errors
        assertEquals("", result.getEmail());
        assertEquals("", result.getUrl());
    }

    @Test
    public void shouldImportServiceWithInvalidFields() {
        adapter.importData(importData);
        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("Service Name", result.getName());

        // Fields with validation errors;
        assertEquals("", result.getEmail());
        assertEquals("", result.getUrl());
    }

    @Test
    public void shouldImportPhysicalAddressWithInvalidFields() {
        adapter.importData(importData);
        assertEquals(1, physicalAddressService.findAll().size());
        AddressDTO result = physicalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAttention());
        assertEquals("", result.getAddress1());
        assertEquals("", result.getCity());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getCountry());
    }

    @Test
    public void shouldImportPostalAddressWithInvalidFields() {
        adapter.importData(importData);
        assertEquals(1, postalAddressService.findAll().size());
        AddressDTO result = postalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAttention());
        assertEquals("", result.getAddress1());
        assertEquals("", result.getCity());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getCountry());
    }

    @Test
    public void shouldImportPhoneWithInvalidFields() {
        adapter.importData(importData);
        List<PhoneDTO> result = phoneService.findAll();
        assertEquals((Integer) 32, result.get(0).getExtension());

        // Fields with validation errors
        assertEquals("", result.get(0).getNumber());
        assertEquals("", result.get(0).getType());
    }
}
