package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ImportService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HealthleadsCompleteDataAdapterTest {

    private static final String COMPLETE = "healthleads/complete/";
    private static final String PROVIDER_NAME = "healthleads";

    private MultipleImportData importData;

    @InjectMocks
    private HealthleadsDataAdapter adapter;

    @Mock
    private ImportService importService;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        when(importService.createOrUpdateService(any(Service.class),
            anyString(), anyString(), any(DataImportReport.class)))
            .thenReturn(new Service());

        when(importService.createOrUpdateLocation(any(Location.class), anyString(), anyString()))
            .thenReturn(new Location());

        when(importService.createOrUpdateOrganization(any(Organization.class), anyString(), anyString(),
            any(DataImportReport.class)))
            .thenReturn(new Organization());

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

        importData = new MultipleImportData(data, uploads, new DataImportReport(), PROVIDER_NAME, true);
    }

    @Test
    public void shouldImportCompleteOrganization() {
        adapter.importData(importData);
        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
        verify(importService, times(1))
            .createOrUpdateOrganization(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        Organization result = captor.getValue();
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
        adapter.importData(importData);
        ArgumentCaptor<Eligibility> captor = ArgumentCaptor.forClass(Eligibility.class);
        verify(importService, times(1))
            .createOrUpdateEligibility(captor.capture(), any(Service.class));

        Eligibility result = captor.getValue();

        assertEquals("Anyone", result.getEligibility());
    }

    @Test
    public void shouldImportCompleteLocationBasedLangs() {
        adapter.importData(importData);
        ArgumentCaptor<Set<Language>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdateLangsForService(captor.capture(), any(), any(Location.class));

        Set<String> langs = captor.getValue().stream().map(Language::getLanguage).collect(Collectors.toSet());

        assertEquals(2, captor.getValue().size());
        assertTrue(langs.contains("Polish"));
        assertTrue(langs.contains("German"));
    }

    @Test
    public void shouldImportCompleteServiceBasedLangs() {
        adapter.importData(importData);
        ArgumentCaptor<Set<Language>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdateLangsForService(captor.capture(), any(Service.class), any());

        Set<String> langs = captor.getValue().stream().map(Language::getLanguage).collect(Collectors.toSet());

        assertEquals(2, captor.getValue().size());
        assertTrue(langs.contains("English"));
        assertTrue(langs.contains("Spanish"));
    }

    @Test
    public void shouldImportCompleteLocation() {
        adapter.importData(importData);
        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(importService, times(1))
            .createOrUpdateLocation(captor.capture(), anyString(), anyString());
        Location result = captor.getValue();

        assertEquals("Location Name", result.getName());
        assertEquals("Alternate Location", result.getAlternateName());
        assertEquals("The Location Description", result.getDescription());
        assertEquals("Location Transportation", result.getTransportation());
        assertEquals(-10.321321, result.getLongitude());
        assertEquals(20.456654, result.getLatitude());
    }

    @Test
    public void shouldImportCompleteLocationBasedPhone() {
        adapter.importData(importData);
        ArgumentCaptor<Set<Phone>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdatePhonesForService(captor.capture(), any(), any(Location.class));

        List<Phone> result = new ArrayList<>(captor.getValue());

        assertEquals("(200) 200-2000", result.get(0).getNumber());
        assertEquals((Integer) 200, result.get(0).getExtension());
        assertEquals("fax", result.get(0).getType());
        assertEquals("Spanish", result.get(0).getLanguage());
        assertEquals("Phone Description For Location", result.get(0).getDescription());
    }

    @Test
    public void shouldImportCompleteServiceBasedPhone() {
        adapter.importData(importData);
        ArgumentCaptor<Set<Phone>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdatePhonesForService(captor.capture(), any(Service.class), any());

        List<Phone> result = new ArrayList<>(captor.getValue());

        assertEquals("(900) 500-9000", result.get(0).getNumber());
        assertEquals((Integer) 500, result.get(0).getExtension());
        assertEquals("voice", result.get(0).getType());
        assertEquals("English", result.get(0).getLanguage());
        assertEquals("Phone Description", result.get(0).getDescription());
    }

    @Test
    public void shouldImportCompletePhysicalAddress() {
        adapter.importData(importData);
        ArgumentCaptor<PhysicalAddress> captor = ArgumentCaptor.forClass(PhysicalAddress.class);
        verify(importService, times(1))
            .createOrUpdatePhysicalAddress(captor.capture(), any(Location.class));

        PhysicalAddress result = captor.getValue();

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
        adapter.importData(importData);
        ArgumentCaptor<RequiredDocument> captor = ArgumentCaptor.forClass(RequiredDocument.class);
        verify(importService, times(1))
            .createOrUpdateRequiredDocument(captor.capture(), any(), any(), any(Service.class));

        RequiredDocument result = captor.getValue();

        assertEquals("Required Document", result.getDocument());
    }

    @Test
    public void shouldImportCompleteService() {
        adapter.importData(importData);
        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(importService, times(1))
            .createOrUpdateService(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        Service result = captor.getValue();
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
        adapter.importData(importData);
        ArgumentCaptor<ServiceAtLocation> captor = ArgumentCaptor.forClass(ServiceAtLocation.class);
        verify(importService, times(1))
            .createOrUpdateServiceAtLocation(captor.capture(), anyString(), anyString(),
                any(Service.class), any(Location.class));
        ServiceAtLocation result = captor.getValue();

        assertEquals("Service At Location Description", result.getDescription());
    }

    @Test
    public void shouldImportCompleteServiceTaxonomy() {
        adapter.importData(importData);
        ArgumentCaptor<ServiceTaxonomy> captor = ArgumentCaptor.forClass(ServiceTaxonomy.class);
        verify(importService, times(1))
            .createOrUpdateServiceTaxonomy(captor.capture(), anyString(), anyString(),
                any(Service.class), any());
        ServiceTaxonomy result = captor.getValue();

        assertEquals("Taxonomy Detail", result.getTaxonomyDetails());
    }

    @Test
    public void shouldImportCompleteTaxonomy() {
        adapter.importData(importData);
        ArgumentCaptor<Taxonomy> captor = ArgumentCaptor.forClass(Taxonomy.class);
        verify(importService, times(1))
            .createOrUpdateTaxonomy(captor.capture(), anyString(), anyString());
        Taxonomy result = captor.getValue();

        assertEquals("Taxonomy Name", result.getName());
        assertEquals("The Taxonomy Vocabulary", result.getVocabulary());
    }
}
