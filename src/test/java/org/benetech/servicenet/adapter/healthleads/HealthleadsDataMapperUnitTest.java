package org.benetech.servicenet.adapter.healthleads;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.benetech.servicenet.adapter.healthleads.model.HealthleadsEligibility;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLanguage;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsOrganization;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhone;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhysicalAddress;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsRequiredDocument;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsService;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceAtLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceTaxonomy;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsTaxonomy;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HealthleadsDataMapperUnitTest {

    private static final int YEAR = 2018;
    private static final Double LAT = 128.0;
    private static final Double LNG = -3.5;
    private static final int THREE = 3;
    private static final Integer EXTENSION_1 = 123;
    private static final Integer EXTENSION_2 = 321;

    @Test
    public void testExtractEligibility() {
        HealthleadsEligibility eligibility = new HealthleadsEligibility();
        eligibility.setEligibility("Eligibility  string");

        org.benetech.servicenet.domain.Eligibility extracted
            = HealthLeadsDataMapper.INSTANCE.extractEligibility(eligibility).get();
        
        assertEquals("Eligibility  string", extracted.getEligibility());
    }

    @Test
    public void testExtractLocation() {
        HealthleadsLocation location = new HealthleadsLocation();
        location.setAlternateName("alternateName");
        location.setDescription("description");
        location.setLatitude("128.0");
        location.setLongitude("-3.5");
        location.setName("Name");
        location.setTransportation("Transportation");

        org.benetech.servicenet.domain.Location extracted
            = HealthLeadsDataMapper.INSTANCE.extractLocation(location);
        
        assertEquals("alternateName", extracted.getAlternateName());
        assertEquals("description", extracted.getDescription());
        assertEquals(LAT, extracted.getLatitude());
        assertEquals(LNG, extracted.getLongitude());
        assertEquals("Name", extracted.getName());
        assertEquals("Transportation", extracted.getTransportation());
    }
    
    @Test
    public void testExtractOrganization() {
        HealthleadsOrganization organization = new HealthleadsOrganization();
        organization.setAlternateName("alternateName");
        organization.setDescription("description");
        organization.setName("Name");
        organization.setEmail("email@email.com");
        organization.setLegalStatus("legalStatus");
        organization.setTaxId("taxId");
        organization.setTaxStatus("taxStatus");
        organization.setUrl("www.organization.com");
        organization.setYearIncorporated("2018-01-02");

        org.benetech.servicenet.domain.Organization extracted
            = HealthLeadsDataMapper.INSTANCE.extractOrganization(organization);

        assertEquals("alternateName", extracted.getAlternateName());
        assertEquals("description", extracted.getDescription());
        assertEquals("Name", extracted.getName());
        assertEquals("email@email.com", extracted.getEmail());
        assertEquals("legalStatus", extracted.getLegalStatus());
        assertEquals("taxId", extracted.getTaxId());
        assertEquals("taxStatus", extracted.getTaxStatus());
        assertEquals("www.organization.com", extracted.getUrl());
        assertEquals(LocalDate.of(YEAR, 1, 2), extracted.getYearIncorporated());
    }

    @Test
    public void testExtractPhysicalAddress() {
        HealthleadsPhysicalAddress physicalAddress = new HealthleadsPhysicalAddress();
        physicalAddress.setAddress("address");
        physicalAddress.setAttention("attention");
        physicalAddress.setCity("city");
        physicalAddress.setCountry("Country");
        physicalAddress.setPostalCode("postalCode");
        physicalAddress.setRegion("region");
        physicalAddress.setStateProvince("state");

        org.benetech.servicenet.domain.PhysicalAddress extracted
            = HealthLeadsDataMapper.INSTANCE.extractPhysicalAddress(physicalAddress).get();

        assertEquals("address", extracted.getAddress1());
        assertEquals("attention", extracted.getAttention());
        assertEquals("city", extracted.getCity());
        assertEquals("Country", extracted.getCountry());
        assertEquals("postalCode", extracted.getPostalCode());
        assertEquals("region", extracted.getRegion());
        assertEquals("state", extracted.getStateProvince());
    }

    @Test
    public void testExtractRequiredDocument() {
        HealthleadsRequiredDocument requiredDocument = new HealthleadsRequiredDocument();
        requiredDocument.setDocument("Document");

        org.benetech.servicenet.domain.RequiredDocument extracted
            = HealthLeadsDataMapper.INSTANCE.extractRequiredDocument(requiredDocument).get();
        
        assertEquals("Document", extracted.getDocument());
    }
    
    @Test
    public void testExtractService() {
        HealthleadsService service = new HealthleadsService();
        service.setAlternateName("alternateName");
        service.setDescription("description");
        service.setName("Name");
        service.setEmail("email@email.com");
        service.setUrl("www.service.com");
        service.setAccreditations("accreditations");
        service.setApplicationProcess("applicationProcess");
        service.setFees("fees");
        service.setLicenses("licenses");
        service.setStatus("status");
        service.setInterpretationServices("interpretationServices");
        service.setWaitTime("waitTime");

        org.benetech.servicenet.domain.Service extracted
            = HealthLeadsDataMapper.INSTANCE.extractService(service);

        assertEquals("alternateName", extracted.getAlternateName());
        assertEquals("description", extracted.getDescription());
        assertEquals("Name", extracted.getName());
        assertEquals("email@email.com", extracted.getEmail());
        assertEquals("www.service.com", extracted.getUrl());
        assertEquals("accreditations", extracted.getAccreditations());
        assertEquals("applicationProcess", extracted.getApplicationProcess());
        assertEquals("fees", extracted.getFees());
        assertEquals("licenses", extracted.getLicenses());
        assertEquals("status", extracted.getStatus());
        assertEquals("interpretationServices", extracted.getInterpretationServices());
        assertEquals("waitTime", extracted.getWaitTime());
    }

    @Test
    public void testExtractServiceAtLocation() {
        HealthleadsServiceAtLocation serviceAtLocation = new HealthleadsServiceAtLocation();
        serviceAtLocation.setDescription("description");

        org.benetech.servicenet.domain.ServiceAtLocation extracted
            = HealthLeadsDataMapper.INSTANCE.extractServiceAtLocation(serviceAtLocation);

        assertEquals("description", extracted.getDescription());
    }

    @Test
    public void testExtractServiceTaxonomy() {
        HealthleadsServiceTaxonomy serviceTaxonomy = new HealthleadsServiceTaxonomy();
        serviceTaxonomy.setTaxonomyDetail("taxDetail");

        org.benetech.servicenet.domain.ServiceTaxonomy extracted
            = HealthLeadsDataMapper.INSTANCE.extractServiceTaxonomy(serviceTaxonomy);

        assertEquals("taxDetail", extracted.getTaxonomyDetails());
    }

    @Test
    public void testExtractTaxonomy() {
        HealthleadsTaxonomy taxonomy = new HealthleadsTaxonomy();
        taxonomy.setName("Name");
        taxonomy.setVocabulary("Vocabulary");

        org.benetech.servicenet.domain.Taxonomy extracted
            = HealthLeadsDataMapper.INSTANCE.extractTaxonomy(taxonomy).get();

        assertEquals("Name", extracted.getName());
        assertEquals("Vocabulary", extracted.getVocabulary());
    }

    @Test
    public void testExtractLanguages() {
        HealthleadsLanguage language = new HealthleadsLanguage();
        language.setLanguage("English ; German ;Polish");

        Set<org.benetech.servicenet.domain.Language> extracted
            = HealthLeadsDataMapper.INSTANCE.extractLanguages(Set.of(language));

        assertEquals(THREE, extracted.size());
        for (org.benetech.servicenet.domain.Language extractedLanguage : extracted) {
            assertTrue(extractedLanguage.getLanguage().equals("English")
                || extractedLanguage.getLanguage().equals("German")
                || extractedLanguage.getLanguage().equals("Polish"));
        }
    }

    @Test
    public void testExtractPhones() {
        Set<HealthleadsPhone> phones = new HashSet<>();
        HealthleadsPhone phone1 = new HealthleadsPhone();
        phone1.setDescription("description1");
        phone1.setExtension("123");
        phone1.setLanguage("English");
        phone1.setNumber("123456789");
        phone1.setType("type1");
        phones.add(phone1);
        HealthleadsPhone phone2 = new HealthleadsPhone();
        phone2.setDescription("description2");
        phone2.setExtension("321");
        phone2.setLanguage("German");
        phone2.setNumber("987654321");
        phone2.setType("type2");
        phones.add(phone2);

        Set<org.benetech.servicenet.domain.Phone> extractedPhones
            = HealthLeadsDataMapper.INSTANCE.extractPhones(phones);

        assertEquals(2, extractedPhones.size());
        for (org.benetech.servicenet.domain.Phone extractedPhone : extractedPhones) {
            if ("type1".equals(extractedPhone.getType())) {
                assertEquals("description1", extractedPhone.getDescription());
                assertEquals(EXTENSION_1, extractedPhone.getExtension());
                assertEquals("English", extractedPhone.getLanguage());
                assertEquals("123456789", extractedPhone.getNumber());
            } else if ("type2".equals(extractedPhone.getType())) {
                assertEquals("description2", extractedPhone.getDescription());
                assertEquals(EXTENSION_2, extractedPhone.getExtension());
                assertEquals("German", extractedPhone.getLanguage());
                assertEquals("987654321", extractedPhone.getNumber());
            } else {
                fail();
            }
        }
    }
}
