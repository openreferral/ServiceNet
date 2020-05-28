package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsBaseData;
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

import java.io.IOException;
import
    java.util.Arrays;
import java.util.List;

import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.ELIGIBILITY;
import static org.benetech.servicenet.adapter.healthleads.HealthleadsTestResources.HEALTHLEADS;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HealthleadsDataResolverUnitTest {

    private static final int THREE_ELEMENTS = 3;

    private static final String SERV = "serv";
    private static final String ELIG = "elig";
    private static final String LANG = "lang";
    private static final String LOC = "loc";
    private static final String ORG = "org";
    private static final String TAX = "tax";
    private static final String TAXOOMY_SERVIE = "servtax";
    private static final String PHONE = "phone";
    private static final String PROGRAM = "program";
    private static final String LICENSES = "lic";
    private static final String REQUIRED_DOC = "reqdoc";
    private static final String ADDRESS_ID = "addressId";
    private static final String CONTACT = "contact";
    private static final String SERVICE_AT_LOCATION = "serviceAtLocation";
    private static final String TYPE = "type";
    private static final String ALTERNATE_NAME = "AlterName";
    private static final String SCHEDULE = "schedule";
    private static final String NAME = "Name";
    private static final String DESCRIPTION = "description";
    private static final String TRANSPORTATION = "transportation";
    private static final String ELIGIBILITY_DESCRIPTION = "eligibility desc ";
    private static final String ENGLISH = "English";
    private static final String ENGLISH_SPANISH = "English;Spanish";
    private static final String ENGLISH_GERMAN = "English;German";
    private static final Double LATITUDE = 20.0;
    private static final Double LONGITUDE = -10.5;
    private static final String TAX_STATUS = "taxStatus";
    private static final List<String> YEARS_INCORPORATED = Arrays.asList("2019-01-02", "2019-02-02", "2019-03-15");
    private static final String LEGAL_STATUS = "LegalStatus";
    private static final String EMAIL = "email@email.com";
    private static final String URL = "www.url.com";
    private static final String PHONE_NUMBER = "(123) 123-4567-";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String ADDRESS = "address ";
    private static final String ATTENTION = "attention";
    private static final String STATE = "state";
    private static final String REGION = "region";
    private static final String POSTAL_CODE = "postalCode";
    private static final String DOCUMENT = "document";
    private static final String FEES = "fees";
    private static final String ACCREDITATIONS = "accreditatons";
    private static final String INTERPOLATION_SERVICES = "interServices";
    private static final String APPLICATION_PROCESS = "applicationProcess";
    private static final String TARGET_POPULATION = "targetPopulation";
    private static final String NOTES = "notes";
    private static final String STATUS = "Status";
    private static final String WAIT_TIME = "waittime";
    private static final String TAXONOMY_DETAIL = "taxonomyDetail";
    private static final String PARENT_NAME = "parentName";
    private static final String PARENT_ID = "parentId";
    private static final String VOCABULARY = "vocabulary";

    private DataResolver dataResolver = new DataResolver();

    @Test
    public void testGetEligibilityFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + ELIGIBILITY + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, ELIGIBILITY);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsEligibility);
            HealthleadsEligibility eligibility = (HealthleadsEligibility) baseData;
            assertEquals(ELIGIBILITY_DESCRIPTION + i, eligibility.getEligibility());
            assertEquals(SERV + i, eligibility.getServiceId());
            assertEquals(ELIG + i, eligibility.getId());
            i++;
        }
    }

    @Test
    public void testGetLanguageFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + LANGUAGES + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, LANGUAGES);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsLanguage);
            HealthleadsLanguage language = (HealthleadsLanguage) baseData;
            assertEquals(LANG + i, language.getId());
            assertEquals(LOC + i, language.getLocationId());
            assertEquals(SERV + i, language.getServiceId());
            i++;
        }
        assertEquals(ENGLISH_SPANISH, ((HealthleadsLanguage) data.get(0)).getLanguage());
        assertEquals(ENGLISH_GERMAN, ((HealthleadsLanguage) data.get(1)).getLanguage());
        assertEquals(ENGLISH, ((HealthleadsLanguage) data.get(2)).getLanguage());
    }

    @Test
    public void testGetLocationFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + LOCATIONS + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, LOCATIONS);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsLocation);
            HealthleadsLocation location = (HealthleadsLocation) baseData;
            assertEquals(ALTERNATE_NAME + i, location.getAlternateName());
            assertEquals(SCHEDULE + i, location.getSchedule());
            assertEquals(ORG + i, location.getOrganizationId());
            assertEquals(i * LATITUDE, location.getLatitude(), 0);
            assertEquals(i * LONGITUDE, location.getLongitude(), 0);
            assertEquals(NAME + i, location.getName());
            assertEquals(DESCRIPTION + i, location.getDescription());
            assertEquals(LOC + i, location.getId());
            assertEquals(TRANSPORTATION + i, location.getTransportation());
            i++;
        }
    }

    @Test
    public void testGetOrganizationFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + ORGANIZATIONS + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, ORGANIZATIONS);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsOrganization);
            HealthleadsOrganization org = (HealthleadsOrganization) baseData;
            assertEquals(ALTERNATE_NAME + i, org.getAlternateName());
            assertEquals(TAX_STATUS + i, org.getTaxStatus());
            assertEquals(NAME + i, org.getName());
            assertEquals(DESCRIPTION + i, org.getDescription());
            assertEquals(YEARS_INCORPORATED.get(i - 1), org.getYearIncorporated().toString());
            assertEquals(ORG + i, org.getId());
            assertEquals(LEGAL_STATUS + i, org.getLegalStatus());
            assertEquals(EMAIL + i, org.getEmail());
            assertEquals(URL + i, org.getUrl());
            assertEquals(TAX + i, org.getTaxId());
            i++;
        }
    }

    @Test
    public void testGetPhonesFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + PHONES + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, PHONES);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsPhone);
            HealthleadsPhone phone = (HealthleadsPhone) baseData;
            assertEquals(PHONE_NUMBER + i, phone.getNumber());
            assertEquals(String.valueOf(i), phone.getExtension());
            assertEquals(SERV + i, phone.getServiceId());
            assertEquals(ORG + i, phone.getOrganizationId());
            assertEquals(DESCRIPTION + i, phone.getDescription());
            assertEquals(ENGLISH, phone.getLanguage());
            assertEquals(PHONE + i, phone.getId());
            assertEquals(CONTACT + i, phone.getContactId());
            assertEquals(TYPE + i, phone.getType());
            assertEquals(LOC + i, phone.getLocationId());
            i++;
        }
    }

    @Test
    public void testGetPhysicalAddressesFormJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + PHYSICAL_ADDRESSES + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, PHYSICAL_ADDRESSES);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsPhysicalAddress);
            HealthleadsPhysicalAddress physicalAddress = (HealthleadsPhysicalAddress) baseData;
            assertEquals(COUNTRY + i, physicalAddress.getCountry());
            assertEquals(CITY + i, physicalAddress.getCity());
            assertEquals(ADDRESS + i, physicalAddress.getAddress());
            assertEquals(ATTENTION + i, physicalAddress.getAttention());
            assertEquals(STATE + i, physicalAddress.getStateProvince());
            assertEquals(ADDRESS_ID + i, physicalAddress.getId());
            assertEquals(REGION + i, physicalAddress.getRegion());
            assertEquals(POSTAL_CODE + i, physicalAddress.getPostalCode());
            assertEquals(LOC + i, physicalAddress.getLocationId());
            i++;
        }
    }

    @Test
    public void testGetRequiredDocumentsFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + REQUIRED_DOCUMENTS + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, REQUIRED_DOCUMENTS);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsRequiredDocument);
            HealthleadsRequiredDocument doc = (HealthleadsRequiredDocument) baseData;
            assertEquals(REQUIRED_DOC + i, doc.getId());
            assertEquals(SERV + i, doc.getServiceId());
            assertEquals(DOCUMENT + i, doc.getDocument());
            i++;
        }
    }

    @Test
    public void testGetServicesFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + SERVICES + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, SERVICES);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsService);
            HealthleadsService service = (HealthleadsService) baseData;
            assertEquals(ALTERNATE_NAME + i, service.getAlternateName());
            assertEquals(FEES + i, service.getFees());
            assertEquals(WAIT_TIME + i, service.getWaitTime());
            assertEquals(ACCREDITATIONS + i, service.getAccreditations());
            assertEquals(PROGRAM + i, service.getProgramId());
            assertEquals(DESCRIPTION + i, service.getDescription());
            assertEquals(INTERPOLATION_SERVICES + i, service.getInterpretationServices());
            assertEquals(URL + i, service.getUrl());
            assertEquals(APPLICATION_PROCESS + i, service.getApplicationProcess());
            assertEquals(LICENSES + i, service.getLicenses());
            assertEquals(TARGET_POPULATION + i, service.getTargetPopulation());
            assertEquals(ORG + i, service.getOrganizationId());
            assertEquals(NAME + i, service.getName());
            assertEquals(NOTES + i, service.getOtherNotes());
            assertEquals(SERV + i, service.getId());
            assertEquals(EMAIL + i, service.getEmail());
            assertEquals(STATUS + i, service.getStatus());
            i++;
        }
    }

    @Test
    public void testGetServicesAtLocationFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + SERVICES_AT_LOCATION + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, SERVICES_AT_LOCATION);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsServiceAtLocation);
            HealthleadsServiceAtLocation serviceAtLocation = (HealthleadsServiceAtLocation) baseData;
            assertEquals(DESCRIPTION + i, serviceAtLocation.getDescription());
            assertEquals(SERVICE_AT_LOCATION + i, serviceAtLocation.getId());
            assertEquals(LOC + i, serviceAtLocation.getLocationId());
            assertEquals(SERV + i, serviceAtLocation.getServiceId());
            i++;
        }
    }

    @Test
    public void testGetServiceTaxonomyFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + SERVICES_TAXONOMY + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, SERVICES_TAXONOMY);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsServiceTaxonomy);
            HealthleadsServiceTaxonomy serviceTaxonomy = (HealthleadsServiceTaxonomy) baseData;
            assertEquals(TAXONOMY_DETAIL + i, serviceTaxonomy.getTaxonomyDetail());
            assertEquals(TAX + i, serviceTaxonomy.getTaxonomyId());
            assertEquals(TAXOOMY_SERVIE + i, serviceTaxonomy.getId());
            assertEquals(SERV + i, serviceTaxonomy.getServiceId());
            i++;
        }
    }

    @Test
    public void testGetTaxonomyFromJson() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(HEALTHLEADS + TAXONOMY + JSON);

        List<HealthleadsBaseData> data = dataResolver.getDataFromJson(json, TAXONOMY);
        assertEquals(THREE_ELEMENTS, data.size());
        int i = 1;
        for (HealthleadsBaseData baseData : data) {
            assertTrue(baseData instanceof HealthleadsTaxonomy);
            HealthleadsTaxonomy taxonomy = (HealthleadsTaxonomy) baseData;
            assertEquals(NAME + i, taxonomy.getName());
            assertEquals(PARENT_NAME + i, taxonomy.getParentName());
            assertEquals(TAX + i, taxonomy.getId());
            assertEquals(VOCABULARY + i, taxonomy.getVocabulary());
            assertEquals(PARENT_ID + i, taxonomy.getParentId());
            i++;
        }
    }
}
