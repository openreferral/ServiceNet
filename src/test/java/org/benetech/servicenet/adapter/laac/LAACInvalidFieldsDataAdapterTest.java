package org.benetech.servicenet.adapter.laac;

import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
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
public class LAACInvalidFieldsDataAdapterTest {

    private static final String INVALID_FIELDS_JSON = "laac/invalid_fields.json";
    private static final String PROVIDER_NAME = "LAAC";

    @Autowired
    private LAACDataAdapter adapter;

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
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void setUp() throws IOException {
        testDatabaseManagement.clearDb();
        String json = AdapterTestsUtils.readResourceAsString(INVALID_FIELDS_JSON);
        SingleImportData importData = new SingleImportData(json, new DataImportReport(), PROVIDER_NAME, true, null);
        adapter.importData(importData);
    }

    @Test
    public void shouldImportOrganizationWithInvalidData() {
        Organization result = organizationService.findAll().get(0);
        assertEquals("Patients Health - health.com", result.getName());

        // Fields with validation errors
        assertEquals("", result.getUrl());
    }

    @Test
    public void shouldImportServiceWithInvalidData() {
        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("Patients Health - health.com - Service", result.getName());

        // Fields with validation errors
        assertEquals("", result.getType());
    }

    @Test
    public void shouldImportContactWithInvalidData() {
        List<ContactDTO> result = contactService.findAll();
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.get(0).getExternalDbId());

        // Fields with validation errors
        assertEquals("", result.get(0).getName());
    }

    @Test
    public void shouldImportPhoneWithInvalidData() {
        List<PhoneDTO> result = phoneService.findAll();

        // Fields with validation errors
        assertEquals("", result.get(0).getNumber());
    }

    @Test
    public void shouldImportPhysicalAddressWithInvalidData() {
        PhysicalAddressDTO result = physicalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAddress1());
        assertEquals("", result.getAttention());
        assertEquals("", result.getCity());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getCountry());
    }
}
