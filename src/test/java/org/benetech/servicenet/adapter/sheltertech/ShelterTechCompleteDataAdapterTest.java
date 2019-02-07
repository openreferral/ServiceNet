package org.benetech.servicenet.adapter.sheltertech;

import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShelterTechCompleteDataAdapterTest {

    private static final String COMPLETE_JSON = "sheltertech/complete.json";
    private static final String PROVIDER_NAME = "ShelterTech";

    @InjectMocks
    private ShelterTechDataAdapter adapter;

    @Mock
    private ImportService importService;

    SingleImportData importData;

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
        importData = new SingleImportData(json, new DataImportReport(), PROVIDER_NAME, true);
    }

    @Test
    public void shouldImportCompleteOrganization() {
        adapter.importData(importData);
        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
        verify(importService, times(1))
            .createOrUpdateOrganization(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        Organization result = captor.getValue();
        assertEquals("The Organization", result.getName());
        assertEquals("Alternate Organization", result.getAlternateName());
        assertEquals("org@email.com", result.getEmail());
        assertEquals("111", result.getExternalDbId());
        assertEquals("Non-profit", result.getLegalStatus());
        assertEquals("Health center offering Legal Help as well", result.getDescription());
        assertTrue(result.getActive());
        assertEquals("http://www.organization.org/xyz/", result.getUrl());
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
        assertEquals("Certified", result.getStatus());
        assertEquals("service@email.com", result.getEmail());
        assertEquals("Free", result.getFees());
        assertEquals("222", result.getExternalDbId());
        assertEquals("The Service Description", result.getDescription());
        assertEquals("http://www.service.org/", result.getUrl());
        assertEquals("15 minutes", result.getWaitTime());
    }

    @Test
    @Ignore("address_1 is used as name, instead of using LocationUtils") //TODO Remove
    public void shouldImportCompleteLocation() {
        adapter.importData(importData);
        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(importService, times(1))
            .createOrUpdateLocation(captor.capture(), anyString(), anyString());
        Location result = captor.getValue();

        assertEquals("1233 90th St. - San Francisco (CA)", result.getName());
        assertEquals(11.1222222, result.getLatitude());
        assertEquals(-111.11111, result.getLongitude());
        assertEquals("888", result.getExternalDbId());
    }

    @Test
    public void shouldImportCompletePhysicalAddress() {
        adapter.importData(importData);
        ArgumentCaptor<PhysicalAddress> captor = ArgumentCaptor.forClass(PhysicalAddress.class);
        verify(importService, times(1))
            .createOrUpdatePhysicalAddress(captor.capture(), any(Location.class));

        PhysicalAddress result = captor.getValue();

        assertEquals("Room 540", result.getAttention());
        assertEquals("1233 90th St.", result.getAddress1());
        assertEquals("San Francisco", result.getCity());
        assertEquals("CA", result.getStateProvince());
        assertEquals("65454", result.getPostalCode());
        assertEquals("USA", result.getCountry());
    }

    @Test
    public void shouldImportCompletePostalAddress() {
        adapter.importData(importData);
        ArgumentCaptor<PostalAddress> captor = ArgumentCaptor.forClass(PostalAddress.class);
        verify(importService, times(1))
            .createOrUpdatePostalAddress(captor.capture(), any(Location.class));

        PostalAddress result = captor.getValue();

        assertEquals("Room 540", result.getAttention());
        assertEquals("1233 90th St.", result.getAddress1());
        assertEquals("San Francisco", result.getCity());
        assertEquals("CA", result.getStateProvince());
        assertEquals("65454", result.getPostalCode());
        assertEquals("USA", result.getCountry());
    }

    @Test
    public void shouldImportCompleteEligibility() {
        adapter.importData(importData);
        ArgumentCaptor<Eligibility> captor = ArgumentCaptor.forClass(Eligibility.class);
        verify(importService, times(1))
            .createOrUpdateEligibility(captor.capture(), any(Service.class));

        Eligibility result = captor.getValue();

        assertEquals("Adults", result.getEligibility());
    }

    @Test
    public void shouldImportCompletePhone() {
        adapter.importData(importData);
        ArgumentCaptor<Set<Phone>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdatePhonesForOrganization(captor.capture(), any());

        List<Phone> result = new ArrayList<>(captor.getValue());

        assertEquals("(111) 222-3333", result.get(0).getNumber());
        assertEquals((Integer) 32, result.get(0).getExtension());
        assertEquals("fax: (663) 433-4324", result.get(0).getType());
        assertEquals("en", result.get(0).getLanguage());
    }

    @Test
    public void shouldImportCompleteRegularSchedule() {
        adapter.importData(importData);
        ArgumentCaptor<RegularSchedule> captor = ArgumentCaptor.forClass(RegularSchedule.class);
        verify(importService, times(1))
            .createOrUpdateRegularSchedule(captor.capture(), any(Service.class));
    }

    @Test
    public void shouldImportCompleteRequiredDocument() {
        adapter.importData(importData);
        ArgumentCaptor<RequiredDocument> captor = ArgumentCaptor.forClass(RequiredDocument.class);
        verify(importService, times(1))
            .createOrUpdateRequiredDocument(captor.capture(), any(), any(), any());

        RequiredDocument result = captor.getValue();

        assertEquals("ID, or any other document with photo", result.getDocument());
    }

    @Test
    @Ignore("Opening Hours are not persisted with ImportService") //TODO: Remove
    public void shouldImportOpeningHours() {
        adapter.importData(importData);
        ArgumentCaptor<Set<OpeningHours>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdateOpeningHoursForService(captor.capture(), any(Service.class));

        List<OpeningHours> result = new ArrayList<>(captor.getValue()).stream()
            .sorted().collect(Collectors.toList());
        assertEquals((Integer) 4, result.get(0).getWeekday());
        assertEquals("22:00", result.get(0).getOpensAt());
        assertEquals("17:00", result.get(0).getClosesAt());
        assertEquals((Integer) 2, result.get(1).getWeekday());
        assertEquals("22:00", result.get(1).getOpensAt());
        assertEquals("19:00", result.get(1).getClosesAt());
    }
}
