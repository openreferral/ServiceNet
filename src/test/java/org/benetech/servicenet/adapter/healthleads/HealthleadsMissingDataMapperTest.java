package org.benetech.servicenet.adapter.healthleads;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsEligibility;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLanguage;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsOrganization;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhone;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhysicalAddress;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsRequiredDocument;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsService;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceTaxonomy;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsTaxonomy;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.type.ListType;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.ELIGIBILITY;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.JSON;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.LANGUAGES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.LOCATIONS;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.ORGANIZATIONS;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.PHONES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.PHYSICAL_ADDRESSES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.REQUIRED_DOCUMENTS;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.SERVICES;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.SERVICES_TAXONOMY;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.TAXONOMY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//TODO: Add tests for ServiceAtLocation mapping, when the entity will be commonly used in the system
public class HealthleadsMissingDataMapperTest {

    private static final String MINIMAL = "healthleads/minimal/";

    private HealthLeadsDataMapper mapper = HealthLeadsDataMapper.INSTANCE;

    @Test
    public void shouldNotThrowExceptionForMinimalDataForOrganization() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + ORGANIZATIONS + JSON);
        List<HealthleadsOrganization> entities = new Gson().fromJson(json, new ListType<>(HealthleadsOrganization.class));

        Organization result = mapper.extractOrganization(entities.get(0));

        assertEquals("Organization Name", result.getName());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForService() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + SERVICES + JSON);
        List<HealthleadsService> entities = new Gson().fromJson(json, new ListType<>(HealthleadsService.class));

        Service result = mapper.extractService(entities.get(0));

        assertEquals("Service Name", result.getName());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForLocation() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + LOCATIONS + JSON);
        List<HealthleadsLocation> entities = new Gson().fromJson(json, new ListType<>(HealthleadsLocation.class));

        Location result = mapper.extractLocation(entities.get(0));

        assertEquals("Location Name", result.getName());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForEligibility() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + ELIGIBILITY + JSON);
        List<HealthleadsEligibility> entities = new Gson().fromJson(json, new ListType<>(HealthleadsEligibility.class));

        Eligibility result = mapper.extractEligibility(entities.get(0)).get();

        assertEquals("Anyone", result.getEligibility());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForEligibility() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + ELIGIBILITY + JSON);
        List<HealthleadsEligibility> entities = new Gson().fromJson(json, new ListType<>(HealthleadsEligibility.class));
        entities.get(0).setEligibility(null);

        assertFalse(mapper.extractEligibility(entities.get(0)).isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForRequiredDocument() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + REQUIRED_DOCUMENTS + JSON);
        List<HealthleadsRequiredDocument> entities = new Gson().fromJson(json, new ListType<>(HealthleadsRequiredDocument.class));

        RequiredDocument result = mapper.extractRequiredDocument(entities.get(0)).get();

        assertEquals("Required Document", result.getDocument());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForRequiredDocument() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + REQUIRED_DOCUMENTS + JSON);
        List<HealthleadsRequiredDocument> entities = new Gson().fromJson(json, new ListType<>(HealthleadsRequiredDocument.class));
        entities.get(0).setDocument(null);

        assertFalse(mapper.extractRequiredDocument(entities.get(0)).isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForTaxonomy() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + TAXONOMY + JSON);
        List<HealthleadsTaxonomy> entities = new Gson().fromJson(json, new ListType<>(HealthleadsTaxonomy.class));

        Taxonomy result = mapper.extractTaxonomy(entities.get(0)).get();

        assertEquals("Taxonomy Name", result.getName());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForTaxonomy() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + TAXONOMY + JSON);
        List<HealthleadsTaxonomy> entities = new Gson().fromJson(json, new ListType<>(HealthleadsTaxonomy.class));
        entities.get(0).setName(null);

        assertFalse(mapper.extractTaxonomy(entities.get(0)).isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForServiceTaxonomy() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + SERVICES_TAXONOMY + JSON);
        List<HealthleadsServiceTaxonomy> entities = new Gson().fromJson(json, new ListType<>(HealthleadsServiceTaxonomy.class));

        ServiceTaxonomy result = mapper.extractServiceTaxonomy(entities.get(0));

        assertEquals("Taxonomy Detail", result.getTaxonomyDetails());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhone() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + PHONES + JSON);
        List<HealthleadsPhone> entities = new Gson().fromJson(json, new ListType<>(HealthleadsPhone.class));

        List<String> resultNumbers = mapper.extractPhones(new HashSet<>(entities)).stream()
            .map(Phone::getNumber).collect(Collectors.toList());

        assertEquals("(900) 500-9000", resultNumbers.get(0));
    }

    @Test
    public void shouldReturnNullForLackOfDataForPhone() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + PHONES + JSON);
        List<HealthleadsPhone> entities = new Gson().fromJson(json, new ListType<>(HealthleadsPhone.class));
        entities.forEach(e -> e.setNumber(null));

        Set<Phone> result = mapper.extractPhones(new HashSet<>(entities));

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForLanguages() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + LANGUAGES + JSON);
        List<HealthleadsLanguage> entities = new Gson().fromJson(json, new ListType<>(HealthleadsLanguage.class));
        Set<String> langs = mapper.extractLanguages(Set.of(entities.get(0))).stream().map(Language::getLanguage).collect(Collectors.toSet());

        assertTrue(langs.contains("English"));
    }

    @Test
    public void shouldReturnEmptyCollectionForLackOfDataForLanguages() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + LANGUAGES + JSON);
        List<HealthleadsLanguage> entities = new Gson().fromJson(json, new ListType<>(HealthleadsLanguage.class));
        entities.forEach(e -> e.setLanguage(null));

        Set<Language> result = mapper.extractLanguages(Set.of(entities.get(0)));

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhysicalAddress() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + PHYSICAL_ADDRESSES + JSON);
        List<HealthleadsPhysicalAddress> entities = new Gson().fromJson(json, new ListType<>(HealthleadsPhysicalAddress.class));
        PhysicalAddress result = mapper.extractPhysicalAddress(entities.get(0)).get();

        assertEquals("2332 Secret Street", result.getAddress1());
        assertEquals("City", result.getCity());
        assertEquals("CA", result.getStateProvince());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForPhysicalAddress() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL + PHYSICAL_ADDRESSES + JSON);
        List<HealthleadsPhysicalAddress> entities = new Gson().fromJson(json, new ListType<>(HealthleadsPhysicalAddress.class));
        entities.get(0).setAddress(null);

        assertFalse(mapper.extractPhysicalAddress(entities.get(0)).isPresent());
    }
}
