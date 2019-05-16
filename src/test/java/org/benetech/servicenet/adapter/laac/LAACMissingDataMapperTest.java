package org.benetech.servicenet.adapter.laac;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.type.ListType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LAACMissingDataMapperTest {

    private static final String MINIMAL_JSON = "laac/minimal.json";

    private LAACDataMapper mapper = LAACDataMapper.INSTANCE;

    private LAACData data;

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(MINIMAL_JSON);
        List<LAACData> entities = new Gson().fromJson(json, new ListType<>(LAACData.class));
        data = entities.get(0);
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForOrganization() {
        Organization result = mapper.extractOrganization(data);

        assertEquals("Patients Health - health.com", result.getName());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForService() {
        Service result = mapper.extractService(data);

        assertEquals("Patients Health - health.com - Service", result.getName());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForLocation() {
        Location result = mapper.extractLocation(data);

        assertEquals("Patients Health - health.com - Location", result.getName());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForContact() {
        Optional<Contact> result = mapper.extractContact(data);

        assertTrue(result.isPresent());
        assertEquals("Ben Smith", result.get().getName());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForContact() {
        data.setContactName(null);
        Optional<Contact> result = mapper.extractContact(data);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForEligibility() {
        Optional<Eligibility> result = mapper.extractEligibility(data);

        assertTrue(result.isPresent());
        assertEquals("Eligibility Type, Other", result.get().getEligibility());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForEligibility() {
        data.setEligibility(null);
        Optional<Eligibility> result = mapper.extractEligibility(data);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhone() {
        Phone result = mapper.extractPhone(data).get();

        assertEquals("(123) 123-4561 ext. 200", result.getNumber());
    }

    @Test
    public void shouldReturnNullForLackOfDataForPhone() {
        data.setPhone(null);

        assertFalse(mapper.extractPhone(data).isPresent());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForLanguages() {
        Set<Language> result = mapper.extractLanguages(data);
        Set<String> langs = result.stream().map(Language::getLanguage).collect(Collectors.toSet());

        assertEquals(2, result.size());
        TestCase.assertTrue(langs.contains("Russian / Росси́я"));
        TestCase.assertTrue(langs.contains("ARABIC / لعَرَبِيَّة"));
    }

    @Test
    public void shouldReturnEmptyCollectionForLackOfDataForLanguages() {
        data.setSpokenLanguages(null);
        Set<Language> result = mapper.extractLanguages(data);

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldNotThrowExceptionForMinimalDataForPhysicalAddress() {
        Optional<PhysicalAddress> result = mapper.extractPhysicalAddress(data);

        assertTrue(result.isPresent());
        assertEquals("123 Street", result.get().getAddress1());
        assertEquals("Suite 500", result.get().getAttention());
        assertEquals("The City", result.get().getCity());
        assertEquals("STATE", result.get().getStateProvince());
        assertEquals("12341", result.get().getPostalCode());
        assertEquals("Country Name", result.get().getCountry());
    }

    @Test
    public void shouldReturnEmptyOptionalForLackOfDataForPhysicalAddress() {
        data.setAddress(null);
        Optional<PhysicalAddress> result = mapper.extractPhysicalAddress(data);

        assertFalse(result.isPresent());
    }
}
