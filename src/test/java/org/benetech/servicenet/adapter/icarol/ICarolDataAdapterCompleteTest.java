package org.benetech.servicenet.adapter.icarol;

import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.icarol.eden.EdenDataAdapter;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ICarolDataAdapterCompleteTest {

    private static final String COMPLETE_JSON = "icarol/complete.json";
    private static final String PROVIDER_NAME = "Eden";
    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
        "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco " +
        "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit " +
        "esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
        "officia deserunt mollit anim id est laborum.";
    private static final String DIFF_LOREM_IPSUM = "different " + LOREM_IPSUM;

    @InjectMocks
    private EdenDataAdapter adapter;

    @Mock
    private ImportService importService;

    private SingleImportData importData;

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
    @Ignore("We should use descriptionText instead of description")
    public void shouldImportCompleteOrganizations() {
        adapter.importData(importData);
        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
        verify(importService, times(1))
            .createOrUpdateOrganization(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        Organization result = captor.getValue();
        assertEquals("HOUSING AUTHORITY OF THE COUNTY OF Commoncounty (ABCD)", result.getName());
        assertNull(result.getAlternateName());
        assertNull(result.getEmail());
        assertEquals("1", result.getExternalDbId());
        assertEquals("Special District; County", result.getLegalStatus());
        assertEquals(DIFF_LOREM_IPSUM, result.getDescription());
        assertTrue(result.getActive());
        assertEquals("www.example.org", result.getUrl());
        assertFalse(result.getIsConfidential());
    }

    @Test
    @Ignore("https://github.com/benetech/ServiceNet/wiki/iCarol-Data-Mapping-(Eden-and-UWBA) " +
            "Program.requiredDocumentation, column: 'Mapped to'")
    public void shouldImportCompleteService() {
        adapter.importData(importData);
        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(importService, times(1))
            .createOrUpdateService(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        Service result = captor.getValue();
        assertFalse(result.getIsConfidential());
        assertEquals("CarpetHanger Office", result.getName());
        assertNull(result.getAlternateName());
        assertEquals(DIFF_LOREM_IPSUM, result.getDescription());
        assertEquals("www.example.org", result.getUrl());
        assertNull(result.getEmail());
        assertEquals("Active", result.getStatus());
        assertNull(result.getInterpretationServices());
        assertEquals("Call or check website for notices on applications.", result.getApplicationProcess());
        assertNull(result.getWaitTime());
        assertEquals("Free", result.getFees());
        assertNull(result.getAccreditations());
        assertNull(result.getLicenses());
        assertEquals("Program", result.getType());
        assertEquals("3", result.getExternalDbId());
    }

    @Test
    public void shouldImportCompleteLocation() {
        adapter.importData(importData);
        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(importService, times(1))
            .createOrUpdateLocation(captor.capture(), anyString(), anyString());
        Location result = captor.getValue();

        assertEquals("12345 Cool Street - CarpetHanger (CA)", result.getName());
        assertEquals(40.123456, result.getLatitude());
        assertEquals(-120.123456, result.getLongitude());
        assertEquals("11", result.getExternalDbId());
    }

    @Test
    @Ignore("Map county to region")
    public void shouldImportCompletePhysicalAddress() {
        adapter.importData(importData);
        ArgumentCaptor<PhysicalAddress> captor = ArgumentCaptor.forClass(PhysicalAddress.class);
        verify(importService, times(1))
            .createOrUpdatePhysicalAddress(captor.capture(), any(Location.class));

        PhysicalAddress result = captor.getValue();

        assertNull(result.getAttention());
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("Commoncounty", result.getRegion());
        assertEquals("CA", result.getStateProvince());
        assertEquals("12345", result.getPostalCode());
        assertEquals("United States", result.getCountry());
    }

    @Test
    @Ignore("Map county to region")
    public void shouldImportCompletePostalAddress() {
        adapter.importData(importData);
        ArgumentCaptor<PostalAddress> captor = ArgumentCaptor.forClass(PostalAddress.class);
        verify(importService, times(1))
            .createOrUpdatePostalAddress(captor.capture(), any(Location.class));

        PostalAddress result = captor.getValue();

        assertNull(result.getAttention());
        assertEquals("12345 Cool Street", result.getAddress1());
        assertEquals("CarpetHanger", result.getCity());
        assertEquals("Commoncounty", result.getRegion());
        assertEquals("CA", result.getStateProvince());
        assertEquals("12345", result.getPostalCode());
        assertEquals("United States", result.getCountry());
    }

    @Test
    public void shouldImportCompleteEligibility() {
        adapter.importData(importData);
        ArgumentCaptor<Eligibility> captor = ArgumentCaptor.forClass(Eligibility.class);
        verify(importService, times(1))
            .createOrUpdateEligibility(captor.capture(), any(Service.class));

        Eligibility result = captor.getValue();

        assertEquals("Low-income family, elderly (age 62 or over), persons with disabilities, or other persons.",
            result.getEligibility());
    }

    @Test
    @Ignore("We should normalize the hours")
    public void shouldImportOpeningHours() {
        adapter.importData(importData);
        ArgumentCaptor<Set<OpeningHours>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdateOpeningHoursForService(captor.capture(), any(Service.class));

        List<OpeningHours> result = new ArrayList<>(captor.getValue());
        for (int i = 0; i < 5; i++) {
            assertEquals((Integer) i, result.get(i).getWeekday());
        }
        for (OpeningHours hours : result) {
            assertEquals("08:00", hours.getOpensAt());
            assertEquals("04:45", hours.getClosesAt());
        }
    }

    @Test
    @Ignore("Unhandled languages")
    public void shouldImportLanguages() {
        adapter.importData(importData);
        ArgumentCaptor<Set<Language>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdateLangsForService(captor.capture(), any(Service.class), any(Location.class));

        List<Language> result = new ArrayList<>(captor.getValue()).stream()
            .sorted().collect(Collectors.toList());

        assertEquals("Farsi", result.get(0).getLanguage());
        assertEquals("Spanish", result.get(1).getLanguage());
        assertEquals("Vietnamese", result.get(2).getLanguage());
        assertEquals("English", result.get(3).getLanguage());
    }

    @Test
    @Ignore("The type should be based on label or purpose.")
    public void shouldImportCompletePhones() {
        adapter.importData(importData);
        ArgumentCaptor<Set<Phone>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(1))
            .createOrUpdatePhonesForService(captor.capture(),  any(Service.class), any(Location.class));

        List<Phone> result = new ArrayList<>(captor.getValue());

        Phone first = result.get(0);
        assertEquals("123-465-7890", first.getNumber());
        assertNull(first.getExtension());
        assertEquals("Main number for program", first.getType());
        assertNull(first.getLanguage());
        assertEquals("CarpetHanger  Office", first.getDescription());
        Phone second = result.get(1);
        assertEquals("678-901-2345", second.getNumber());
        assertNull(second.getExtension());
        assertEquals("Fax", second.getType());
        assertNull(second.getLanguage());
        assertNull(second.getDescription());
        Phone third = result.get(2);
        assertEquals("789-012-3456", third.getNumber());
        assertNull(third.getExtension());
        assertEquals("MYY", third.getType());
        assertNull(third.getLanguage());
        assertNull(third.getDescription());
    }

    @Test
    @Ignore("We should add details based on site.accessibility.public.")
    public void shouldImportCompleteAccessibility() {
        adapter.importData(importData);
        ArgumentCaptor<AccessibilityForDisabilities> captor = ArgumentCaptor.forClass(AccessibilityForDisabilities.class);
        verify(importService, times(1))
            .createOrUpdateAccessibility(captor.capture(), any(Location.class));

        AccessibilityForDisabilities result = captor.getValue();

        assertEquals("Wheelchair accessible/Ramp/Special parking/Restroom", result.getAccessibility());
        assertEquals("AB RoadCompany lines 1, 2, 3 and 4 stop within 2 blocks. BART-CarpetHanger  " +
                     "Station connects with AB RoadCompany lines 1, 2, 3 and 4",
                     result.getDetails());
    }
}
