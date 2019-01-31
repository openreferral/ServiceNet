package org.benetech.servicenet.adapter.laac;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.type.ListType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class LAACCompleteDataMapperTest {

    private static final String COMPLETE_JSON = "laac/complete.json";

    private LAACDataMapper mapper = LAACDataMapper.INSTANCE;

    private LAACData data;

    @Before
    public void setUp() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(COMPLETE_JSON);
        List<LAACData> entities = new Gson().fromJson(json, new ListType<>(LAACData.class));
        data = entities.get(0);
    }

    @Test
    public void shouldMapCompleteOrganization() {
        Organization result = mapper.extractOrganization(data);

        assertEquals("Patients Health - health.com", result.getName());
        assertEquals(LocalDate.of(2019, 1, 21), result.getYearIncorporated());
        assertEquals("Who we are, What are we doing?\n The description of the organization", result.getDescription());
        assertEquals("http:www.org.com/health", result.getUrl());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
    }

    @Test
    @Ignore("ID is not mapped to externalDbId") //TODO: Remove
    public void shouldMapCompleteService() {
        Service result = mapper.extractService(data);

        assertEquals("Patients Health - health.com - Service", result.getName());
        assertEquals("The description of the Service Types", result.getDescription());
        assertEquals("Health-Clinic, Housing Service", result.getType());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
    }

    @Test
    @Ignore("ID is not mapped to externalDbId") //TODO: Remove
    public void shouldMapCompleteLocation() {
        Location result = mapper.extractLocation(data);

        assertEquals("Patients Health - health.com - Location", result.getName());
        assertEquals("Area 1, Area 2, Area 3, Area 4", result.getDescription());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
    }

    @Test
    @Ignore("ID is not mapped to externalDbId") //TODO: Remove
    public void shouldMapCompleteContact() {
        Optional<Contact> result = mapper.extractContact(data);

        assertTrue(result.isPresent());
        assertEquals("Ben Smith", result.get().getName());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.get().getExternalDbId());
    }

    @Test
    public void shouldMapCompleteEligibility() {
        Optional<Eligibility> result = mapper.extractEligibility(data);

        assertTrue(result.isPresent());
        assertEquals("Eligibility Type, Other", result.get().getEligibility());
    }

    @Test
    public void shouldMapCompletePhone() {
        Phone result = mapper.extractPhone(data);

        assertEquals("(123) 123-4561 ext. 200", result.getNumber());
    }

    @Test
    public void shouldMapCompleteLangs() {
        Set<Language> result = mapper.extractLanguages(data);
        Set<String> langs = result.stream().map(Language::getLanguage).collect(Collectors.toSet());

        assertEquals(2, result.size());
        assertTrue(langs.contains("Russian / Росси́я"));
        assertTrue(langs.contains("ARABIC / لعَرَبِيَّة"));
    }
}
