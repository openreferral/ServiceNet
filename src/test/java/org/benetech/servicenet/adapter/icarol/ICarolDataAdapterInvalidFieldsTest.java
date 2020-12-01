package org.benetech.servicenet.adapter.icarol;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.config.Constants.EDEN_PROVIDER;

import java.io.IOException;
import java.util.List;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.icarol.eden.EdenDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
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
public class ICarolDataAdapterInvalidFieldsTest {

    private static final String INVALID_FIELDS_JSON = "icarol/invalid_fields.json";

    @Autowired
    private EdenDataAdapter adapter;

    @Autowired
    private AccessibilityForDisabilitiesService accessibilityForDisabilitiesService;

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

    private SingleImportData importData;

    @Before
    public void setUp() throws IOException {
        testDatabaseManagement.clearDb();
        String json = AdapterTestsUtils.readResourceAsString(INVALID_FIELDS_JSON);
        importData = new SingleImportData(json, new DataImportReport(), EDEN_PROVIDER, true);
    }

    @Test
    public void shouldImportOrganizationsWithInvalidFields() {
        adapter.importData(importData);
        Organization result = organizationService.findAll().get(0);
        assertEquals("HOUSING AUTHORITY OF THE COUNTY OF Commoncounty (ABCD)", result.getName());

        // Field with validation errors
        assertEquals("", result.getUrl());
    }

    @Test
    public void shouldImportServiceWithInvalidFields() {
        adapter.importData(importData);
        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("CarpetHanger Office", result.getName());

        // Fields with validation errors
        assertEquals("", result.getUrl());
    }

    @Test
    public void shouldImportPhysicalAddressWithInvalidFields() {
        adapter.importData(importData);
        AddressDTO result = physicalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAddress1());
        assertEquals("", result.getCity());
        assertEquals("", result.getRegion());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getCountry());
    }

    @Test
    public void shouldImportPostalAddressWithInvalidFields() {
        adapter.importData(importData);
        AddressDTO result = postalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAddress1());
        assertEquals("", result.getCity());
        assertEquals("", result.getRegion());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getCountry());
    }

    @Test
    public void shouldImportPhonesWithInvalidFields() {
        adapter.importData(importData);
        List<PhoneDTO> result = phoneService.findAll();
        PhoneDTO first = result.get(0);
        PhoneDTO second = result.get(1);

        // Fields with validation errors
        assertEquals("", first.getNumber());
        assertEquals("", first.getType());
        assertEquals("", second.getNumber());
        assertEquals("", second.getType());
    }

    @Test
    public void shouldImportAccessibilityWithInvalidFields() {
        adapter.importData(importData);
        AccessibilityForDisabilitiesDTO result = accessibilityForDisabilitiesService.findAll().get(0);
        assertEquals("Wheelchair accessible/Ramp/Special parking/Restroom", result.getAccessibility());

        // Fields with validation errors
        assertEquals("", result.getDetails());
    }
}
