package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.manager.ImportManager;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.PhysicalAddressDTO;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.benetech.servicenet.service.dto.ServiceAtLocationDTO;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class HealthleadsCompleteDataAdapterTest {

    private static final String COMPLETE = "healthleads/complete/";
    private static final String PROVIDER_NAME = "healthleads";

    @Autowired
    private HealthleadsDataAdapter adapter;

    @Autowired
    private ImportManager importManager;

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
    private ContactService contactService;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private ServiceAtLocationService serviceAtLocationService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

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
            data.add(readResourceAsString(COMPLETE + fileName + JSON));
        }

        MultipleImportData importData = new MultipleImportData(data, uploads, new DataImportReport(), PROVIDER_NAME, true, null);
        adapter.importData(importData);
    }

    @Test
    public void shouldImportCompleteOrganization() {
        assertEquals(1, organizationService.findAll().size());

        Organization result = organizationService.findAll().get(0);
        assertEquals("Organization Name", result.getName());
        assertEquals("Alternate Organization", result.getAlternateName());
        assertEquals("organization@org.com", result.getEmail());
        assertEquals("Organization Tax Status", result.getTaxStatus());
        assertEquals("taxId", result.getTaxId());
        assertEquals(LocalDate.of(2019, 1, 2), result.getYearIncorporated());
        assertEquals("The main purpose of the organization solutions to patients problems.", result.getDescription());
        assertEquals("www.organization.org", result.getUrl());
        assertEquals("organizationId", result.getExternalDbId());
    }

    @Test
    public void shouldImportCompleteEligibility() {
        assertEquals(1, eligibilityService.findAll().size());

        EligibilityDTO result = eligibilityService.findAll().get(0);

        assertEquals("Anyone", result.getEligibility());
    }

    @Test
    public void shouldImportCompleteLocationBasedLangs() {
        Set<String> langs = languageService.findAll().stream().map(LanguageDTO::getLanguage).collect(Collectors.toSet());

        assertTrue(langs.contains("Polish"));
        assertTrue(langs.contains("German"));
    }

    @Test
    public void shouldImportCompleteServiceBasedLangs() {
        Set<String> langs = languageService.findAll().stream().map(LanguageDTO::getLanguage).collect(Collectors.toSet());

        assertTrue(langs.contains("English"));
        assertTrue(langs.contains("Spanish"));
    }

    @Test
    public void shouldImportCompleteLocation() {
        assertEquals(1, locationService.findAll().size());

        LocationDTO result = locationService.findAll().get(0);

        assertEquals("Location Name", result.getName());
        assertEquals("Alternate Location", result.getAlternateName());
        assertEquals("The Location Description", result.getDescription());
        assertEquals("Location Transportation", result.getTransportation());
        assertEquals(-10.321321, result.getLongitude());
        assertEquals(20.456654, result.getLatitude());
    }

    @Test
    public void shouldImportCompleteLocationBasedPhone() {
        List<PhoneDTO> result = phoneService.findAll();

        assertTrue(result.stream().anyMatch(x ->
                x.getNumber().equals("(200) 200-2000")
                    && x.getExtension().equals(200)
                    && x.getType().equals("fax")
                    && x.getLanguage().equals("Spanish")
                    && x.getDescription().equals("Phone Description For Location")
            )
        );
    }

    @Test
    public void shouldImportCompleteServiceBasedPhone() {
        List<PhoneDTO> result = phoneService.findAll();

        assertTrue(result.stream().anyMatch(x ->
                x.getNumber().equals("(900) 500-9000")
                    && x.getExtension().equals(500)
                    && x.getType().equals("voice")
                    && x.getLanguage().equals("English")
                    && x.getDescription().equals("Phone Description")
            )
        );
    }

    @Test
    public void shouldImportCompletePhysicalAddress() {
        assertEquals(1, physicalAddressService.findAll().size());

        PhysicalAddressDTO result = physicalAddressService.findAll().get(0);

        assertEquals("2332 Secret Street", result.getAddress1());
        assertEquals("Room 500", result.getAttention());
        assertEquals("City", result.getCity());
        assertEquals("CA", result.getStateProvince());
        assertEquals("8490", result.getPostalCode());
        assertEquals("Region", result.getRegion());
        assertEquals("Country", result.getCountry());
    }

    @Test
    public void shouldImportCompleteRequiredDocument() {
        assertEquals(1, requiredDocumentService.findAll().size());

        RequiredDocumentDTO result = requiredDocumentService.findAll().get(0);

        assertEquals("Required Document", result.getDocument());
    }

    @Test
    public void shouldImportCompleteService() {
        assertEquals(1, serviceService.findAll().size());

        ServiceDTO result = serviceService.findAll().get(0);
        assertEquals("Service Name", result.getName());
        assertEquals("Alternate Service", result.getAlternateName());
        assertEquals("service@email.com", result.getEmail());
        assertEquals("www.service.com", result.getUrl());
        assertEquals("Service Description", result.getDescription());
        assertEquals("Service Status", result.getStatus());
        assertEquals("Interpretation Services", result.getInterpretationServices());
        assertEquals("1. Call", result.getApplicationProcess());
        assertEquals("1 hour", result.getWaitTime());
        assertEquals("Service Fee", result.getFees());
        assertEquals("Service Accreditations", result.getAccreditations());
        assertEquals("Service License", result.getLicenses());
        assertEquals("serviceId", result.getExternalDbId());
    }

    @Test
    @Ignore("ServiceAtLocation is ignored") //TODO: Remove
    public void shouldImportCompleteServiceAtLocation() {
        assertEquals(1, serviceAtLocationService.findAll().size());

        ServiceAtLocationDTO result = serviceAtLocationService.findAll().get(0);

        assertEquals("Service At Location Description", result.getDescription());
    }

    @Test
    public void shouldImportCompleteServiceTaxonomy() {
        assertEquals(1, serviceTaxonomyService.findAll().size());

        ServiceTaxonomyDTO result = serviceTaxonomyService.findAll().get(0);

        assertEquals("Taxonomy Detail", result.getTaxonomyDetails());
    }

    @Test
    public void shouldImportCompleteTaxonomy() {
        assertEquals(1, taxonomyService.findAll().size());

        TaxonomyDTO result = taxonomyService.findAll().get(0);

        assertEquals("Taxonomy Name", result.getName());
        assertEquals("The Taxonomy Vocabulary", result.getVocabulary());
    }
}
