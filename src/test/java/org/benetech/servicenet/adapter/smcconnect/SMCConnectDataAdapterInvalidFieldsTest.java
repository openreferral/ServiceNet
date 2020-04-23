package org.benetech.servicenet.adapter.smcconnect;

import static org.benetech.servicenet.adapter.AdapterTestsUtils.readResourceAsString;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.ADDRESSES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.CONTACTS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.HOLIDAY_SCHEDULE;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.INVALID_FIELDS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.JSON;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.LOCATIONS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.MAIL_ADDRESSES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.ORGANIZATIONS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.PHONES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.PROGRAMS;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.REGULAR_SCHEDULES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.SERVICES;
import static org.benetech.servicenet.adapter.smcconnect.SMCConnectTestResources.SMCCONNECT;
import static org.benetech.servicenet.config.Constants.SMC_CONNECT_PROVIDER;
import static org.junit.Assert.assertEquals;

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
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.ProgramService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
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
public class SMCConnectDataAdapterInvalidFieldsTest {

    @Autowired
    private SMCConnectDataAdapter adapter;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private ProgramService programService;

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
            data.add(readResourceAsString(SMCCONNECT + INVALID_FIELDS + fileName + JSON));
        }

        MultipleImportData importData = new MultipleImportData(data, uploads,
            new DataImportReport(), SMC_CONNECT_PROVIDER, true);
        adapter.importData(importData);
    }

    @Test
    public void testAfterGetPhoneFromJsonWithInvalidFields() {
        assertEquals(1, phoneService.findAll().size());
        PhoneDTO result = phoneService.findAll().get(0);
        assertEquals("500", result.getExtension().toString());

        // Fields with validation errors
        assertEquals("", result.getNumber());
        assertEquals("", result.getType());
    }

    @Test
    public void testAfterGetPhysicalAddressFromJsonWithInvalidFields() {
        assertEquals(1, physicalAddressService.findAll().size());
        PhysicalAddressDTO result = physicalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAddress1());
        assertEquals("", result.getCity());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testAfterGetPostalAddressFromJsonWithInvalidFields() {
        assertEquals(1, postalAddressService.findAll().size());
        PostalAddressDTO result = postalAddressService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getAddress1());
        assertEquals("", result.getCity());
        assertEquals("", result.getStateProvince());
        assertEquals("", result.getPostalCode());
        assertEquals("", result.getCountry());
        assertEquals("", result.getAttention());
    }

    @Test
    public void testAfterGetServiceFromJsonWithInvalidFields() {
        assertEquals(1, serviceService.findAll().size());
        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("Service Name", result.getName());

        // Fields with validation errors
        assertEquals("", result.getUrl());
        assertEquals("", result.getEmail());
        assertEquals("", result.getStatus());
    }

    @Test
    public void testAfterGetFundingFromJsonWithInvalidFields() {
        List<FundingDTO> results = fundingService.findAll();
        assertEquals(2, results.size());

        // Fields with validation errors
        results.forEach(r -> {
            if (r.getSrvcId() != null) {
                assertEquals("", r.getSource());
            } else {
                assertEquals("", r.getSource());
            }
        });
    }

    @Test
    public void testAfterGetContactFromJsonWithInvalidFields() {
        List<ContactDTO> results = contactService.findAll();
        assertEquals(1, results.size());
        ContactDTO result = results.get(0);

        // Fields with validation errors
        assertEquals("", result.getName());
        assertEquals("", result.getTitle());
        assertEquals("", result.getEmail());
        assertEquals("", result.getDepartment());
    }

    @Test
    public void testAfterGetProgramFromJson() {
        assertEquals(1, programService.findAll().size());
        ProgramDTO result = programService.findAll().get(0);

        // Fields with validation errors
        assertEquals("", result.getName());
        assertEquals("", result.getAlternateName());
    }
}
