package org.benetech.servicenet.adapter.laac;

import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.Service;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LAACCompleteDataAdapterTest {

    private static final String COMPLETE_JSON = "laac/complete.json";
    private static final String PROVIDER_NAME = "LAAC";

    @InjectMocks
    private LAACDataAdapter adapter;

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

        String json = AdapterTestsUtils.readResourceAsString(COMPLETE_JSON);
        SingleImportData importData = new SingleImportData(json, new DataImportReport(), PROVIDER_NAME, true);
        adapter.importData(importData);
    }

    @Test
    public void shouldImportCompleteOrganization() {
        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
        verify(importService, times(1))
            .createOrUpdateOrganization(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        Organization result = captor.getValue();
        assertEquals("Patients Health - health.com", result.getName());
        assertEquals(LocalDate.of(2019, 1, 21), result.getYearIncorporated());
        assertEquals("Who we are, What are we doing?\n The description of the organization", result.getDescription());
        assertEquals("http:www.org.com/health", result.getUrl());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
    }

    @Test
    @Ignore("ID is not mapped to externalDbId") //TODO: Remove
    public void shouldImportCompleteService() {
        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(importService, times(1))
            .createOrUpdateService(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        Service result = captor.getValue();
        assertEquals("Patients Health - health.com - Service", result.getName());
        assertEquals("The description of the Service Types", result.getDescription());
        assertEquals("Health-Clinic, Housing Service", result.getType());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
    }

    @Test
    @Ignore("ID is not mapped to externalDbId") //TODO: Remove
    public void shouldImportCompleteLocation() {
        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(importService, times(1))
            .createOrUpdateLocation(captor.capture(), anyString(), anyString());
        Location result = captor.getValue();

        assertEquals("Patients Health - health.com - Location", result.getName());
        assertEquals("Area 1, Area 2, Area 3, Area 4", result.getDescription());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.getExternalDbId());
    }

    @Test
    @Ignore("ID is not mapped to externalDbId") //TODO: Remove
    public void shouldImportCompleteContact() {
        ArgumentCaptor<Set<Contact>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdateContactsForOrganization(captor.capture(), any(Organization.class));

        List<Contact> result = new ArrayList<>(captor.getValue());

        assertEquals("Ben Smith", result.get(0).getName());
        assertEquals("626f8818-9d53-4b8b-8d72-d5b6980cacb4", result.get(0).getExternalDbId());
    }

    @Test
    public void shouldImportCompleteEligibility() {
        ArgumentCaptor<Eligibility> captor = ArgumentCaptor.forClass(Eligibility.class);
        verify(importService, times(1))
            .createOrUpdateEligibility(captor.capture(), any(Service.class));

        Eligibility result = captor.getValue();

        assertEquals("Eligibility Type, Other", result.getEligibility());
    }

    @Test
    public void shouldImportCompletePhone() {
        ArgumentCaptor<Set<Phone>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdatePhonesForService(captor.capture(), any(Service.class), any(Location.class));

        List<Phone> result = new ArrayList<>(captor.getValue());

        assertEquals("(123) 123-4561 ext. 200", result.get(0).getNumber());
    }

    @Test
    public void shouldImportCompleteLangs() {
        ArgumentCaptor<Set<Language>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdateLangsForService(captor.capture(), any(Service.class), any(Location.class));

        Set<String> langs = captor.getValue().stream().map(Language::getLanguage).collect(Collectors.toSet());

        assertEquals(2, captor.getValue().size());
        assertTrue(langs.contains("Russian / Росси́я"));
        assertTrue(langs.contains("ARABIC / لعَرَبِيَّة"));
    }
}
