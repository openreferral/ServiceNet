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
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.service.ImportService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LAACDataAdapterTest {

    private static final String PROVIDER_NAME = "LAAC";
    private static final int THREE = 3;
    private static final int TWO = 2;
    private static final String ID_FORMAT = "extId%d";
    private static final String SERVICE_NAME_FORMAT = "Organization Name %d - Service";
    private static final String SERVICE_TYPE_FORMAT = "ServiceType0%d, ServiceType1%d";
    private static final String SERVICE_DESCRIPTION_FORMAT = "ServiceType Desc %d";
    private static final String LOCATION_NAME_FORMAT = "Organization Name %d - Location";
    private static final String LOCATION_DESCRIPTION_FORMAT = "Area%d0, Area%d1, Area%d2, Area%d3";
    private static final String ORG_NAME_FORMAT = "Organization Name %d";
    private static final String ORG_DESCRIPTION_FORMAT = "Who we are description%d";
    private static final String ORG_URL_FORMAT = "http://www.org.com%d";
    private static final LocalDate ORG_BASE_YEAR_INCORPORATED = LocalDate.of(2019, 1, 20);
    private static final String ELIGIBILITY_FORMAT = "This is eligibility!%d";
    private static final String RUSSIAN = "Russian / Росси́я";
    private static final String SPANISH = "Spanish / Español";
    private static final String NOT_AVAILABLE = "N/A";
    private static final String CITY_FORMAT = "City%d";
    private static final String COUNTRY_FORMAT = "Country%d";
    private static final String POSTAL_CODE_FORMAT = "1234%d";
    private static final String STATE_FORMAT = "State%d";
    private static final String ATTENTION_FORMAT = "Attention%d";
    private static final String ADDRESS_FORMAT = "Address%d";
    private static final String PHONE_FORMAT = "(123) 123-456%d";

    @InjectMocks
    private LAACDataAdapter adapter;

    @Mock
    private ImportService importService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSavingLAACData() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString("laac/orgs.json");
        SingleImportData importData = new SingleImportData(json, new DataImportReport(), PROVIDER_NAME, true);

        when(importService.createOrUpdateService(any(Service.class),
            anyString(), anyString(), any(DataImportReport.class)))
            .thenReturn(new Service());

        when(importService.createOrUpdateLocation(any(Location.class), anyString(), anyString()))
            .thenReturn(new Location());

        when(importService.createOrUpdateOrganization(any(Organization.class), anyString(), anyString(),
            any(Service.class), any(Set.class), any(DataImportReport.class)))
            .thenReturn(new Organization());

        adapter.importData(importData);

        assertExtractedService();
        assertExtractedLocation();
        assertExtractedOrganization();
        assertExtractedEligibility();
        assertExtractedContact();
        assertExtractedLanguages();
        assertExtractedPhysicalAddress();
        assertExtractedPhones();

        verify(importService, times(THREE))
            .createOrUpdateServiceAtLocation(any(ServiceAtLocation.class), anyString(), anyString(),
                any(Service.class), any(Location.class));
    }

    private void assertExtractedPhones() {
        ArgumentCaptor<Set<Phone>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(THREE))
            .createOrUpdatePhonesForService(captor.capture(), any(Service.class), any(Location.class));

        Set<Phone> set1 = captor.getAllValues().get(0);
        assertEquals(1, set1.size());
        assertEquals(String.format(PHONE_FORMAT, 1), set1.iterator().next().getNumber());

        Set<Phone> set2 = captor.getAllValues().get(1);
        assertEquals(1, set2.size());
        assertEquals(String.format(PHONE_FORMAT, 2), set2.iterator().next().getNumber());

        Set<Phone> set3 = captor.getAllValues().get(2);
        assertEquals(1, set3.size());
        assertEquals(String.format(PHONE_FORMAT, 3), set3.iterator().next().getNumber());
    }

    private void assertExtractedPhysicalAddress() {
        ArgumentCaptor<PhysicalAddress> captor = ArgumentCaptor.forClass(PhysicalAddress.class);
        verify(importService, times(THREE))
            .createOrUpdatePhysicalAddress(captor.capture(), any(Location.class));

        PhysicalAddress physicalAddress1 = captor.getAllValues().get(0);
        assertEquals(String.format(CITY_FORMAT, 1), physicalAddress1.getCity());
        assertEquals(String.format(COUNTRY_FORMAT, 1), physicalAddress1.getCountry());
        assertEquals(String.format(POSTAL_CODE_FORMAT, 1), physicalAddress1.getPostalCode());
        assertEquals(String.format(STATE_FORMAT, 1), physicalAddress1.getStateProvince());
        assertEquals(NOT_AVAILABLE, physicalAddress1.getAddress1());
        assertNull(physicalAddress1.getAttention());
        
        PhysicalAddress physicalAddress2 = captor.getAllValues().get(1);
        assertEquals(String.format(CITY_FORMAT, 2), physicalAddress2.getCity());
        assertEquals(String.format(COUNTRY_FORMAT, 2), physicalAddress2.getCountry());
        assertEquals(String.format(POSTAL_CODE_FORMAT, 2), physicalAddress2.getPostalCode());
        assertEquals(String.format(STATE_FORMAT, 2), physicalAddress2.getStateProvince());
        assertEquals(String.format(ADDRESS_FORMAT, 2), physicalAddress2.getAddress1());
        assertNull(physicalAddress2.getAttention());

        PhysicalAddress physicalAddress3 = captor.getAllValues().get(2);
        assertEquals(String.format(CITY_FORMAT, 3), physicalAddress3.getCity());
        assertEquals(String.format(COUNTRY_FORMAT, 3), physicalAddress3.getCountry());
        assertEquals(String.format(POSTAL_CODE_FORMAT, 3), physicalAddress3.getPostalCode());
        assertEquals(String.format(STATE_FORMAT, 3), physicalAddress3.getStateProvince());
        assertEquals(String.format(ADDRESS_FORMAT, 3), physicalAddress3.getAddress1());
        assertEquals(String.format(ATTENTION_FORMAT, 3), physicalAddress3.getAttention());
    }

    private void assertExtractedLanguages() {
        ArgumentCaptor<Set<Language>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(THREE))
            .createOrUpdateLangsForService(captor.capture(), any(Service.class), any(Location.class));

        Set<Language> set1 = captor.getAllValues().get(0);
        for (Language language : set1) {
            assertTrue(RUSSIAN.equals(language.getLanguage())
                || SPANISH.equals(language.getLanguage()));
        }
    }

    private void assertExtractedContact() {
        ArgumentCaptor<Set<Contact>> captor = ArgumentCaptor.forClass(Set.class);
        verify(importService, times(TWO))
            .createOrUpdateContactsForOrganization(captor.capture(), any(Organization.class));


        Set<Contact> set = captor.getAllValues().get(0);
        assertEquals(1, set.size());
        for (Contact contact : set) {
            assertEquals("John Smith", contact.getName());
        }
        set = captor.getAllValues().get(1);
        assertEquals(1, set.size());
        for (Contact contact : set) {
            assertEquals("Jane Black", contact.getName());
        }
    }

    private void assertExtractedEligibility() {
        ArgumentCaptor<Eligibility> captor = ArgumentCaptor.forClass(Eligibility.class);
        verify(importService, times(TWO))
            .createOrUpdateEligibility(captor.capture(), any(Service.class));

        assertEquals(String.format(ELIGIBILITY_FORMAT, 1),
            captor.getAllValues().get(0).getEligibility());
        assertEquals(String.format(ELIGIBILITY_FORMAT, 2),
            captor.getAllValues().get(1).getEligibility());
    }

    private void assertExtractedOrganization() {
        ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
        verify(importService, times(THREE))
            .createOrUpdateOrganization(captor.capture(), anyString(), anyString(),
                any(Service.class), any(Set.class), any(DataImportReport.class));

        int i = 1;
        for (Organization organization : captor.getAllValues()) {
            assertEquals(String.format(ID_FORMAT, i), organization.getExternalDbId());
            assertEquals(String.format(ORG_NAME_FORMAT, i), organization.getName());
            assertEquals(String.format(ORG_DESCRIPTION_FORMAT, i), organization.getDescription());
            assertEquals(String.format(ORG_URL_FORMAT, i), organization.getUrl());
            assertEquals(ORG_BASE_YEAR_INCORPORATED.plusDays(i), organization.getYearIncorporated());
            i++;
        }
    }

    private void assertExtractedService() {
        ArgumentCaptor<Service> captor = ArgumentCaptor.forClass(Service.class);
        verify(importService, times(THREE))
            .createOrUpdateService(captor.capture(), anyString(), anyString(), any(DataImportReport.class));

        int i = 1;
        for (Service service : captor.getAllValues()) {
            assertEquals(String.format(SERVICE_NAME_FORMAT, i), service.getName());
            assertEquals(String.format(SERVICE_TYPE_FORMAT, i, i), service.getType());
            assertEquals(String.format(SERVICE_DESCRIPTION_FORMAT, i), service.getDescription());
            i++;
        }
    }

    private void assertExtractedLocation() {
        ArgumentCaptor<Location> captor = ArgumentCaptor.forClass(Location.class);
        verify(importService, times(THREE))
            .createOrUpdateLocation(captor.capture(), anyString(), anyString());

        int i = 1;
        for (Location location : captor.getAllValues()) {
            assertEquals(String.format(LOCATION_NAME_FORMAT, i), location.getName());
            assertEquals(String.format(LOCATION_DESCRIPTION_FORMAT, i, i ,i ,i), location.getDescription());
            i++;
        }
    }
}
