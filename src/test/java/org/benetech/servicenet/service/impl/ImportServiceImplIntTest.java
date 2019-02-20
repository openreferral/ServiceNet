package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.TestPersistanceHelper;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.OrganizationImportService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImportServiceImplIntTest {

    private static final String ORG_ID = "orgId";
    private static final String LOC_1_ID = "loc1Id";
    private static final String LOC_2_ID = "loc2Id";
    private static final String SRVC_1_ID = "srvc1Id";
    private static final String SRVC_2_ID = "srvc2Id";
    private static final String TAX_ID = "taxId";
    private static final String PROVIDER = "provider";

    @InjectMocks
    private TmpImportServiceImpl importService;

    @Mock
    private OrganizationImportService organizationImportService;

    @Mock
    private LocationImportService locationImportService;

    @Mock
    private ServiceImportService serviceImportService;

    @Mock
    private TransactionSynchronizationService transactionSynchronizationService;

    @Mock
    private TaxonomyImportService taxonomyImportService;

    private TestPersistanceHelper helper;

    @Before
    public void setUp() {
        initMocks(this);
        helper = new TestPersistanceHelper();
    }

    @Test
    public void testCreatingOrganization() {
        Organization org = new Organization().externalDbId(ORG_ID);
        org.setId(UUID.randomUUID());

        Service service1 = new Service().externalDbId(SRVC_1_ID);
        Service service2 = new Service().externalDbId(SRVC_2_ID);

        Location location1 = new Location().externalDbId(LOC_1_ID);
        Location location2 = new Location().externalDbId(LOC_2_ID);

        org.setServices(helper.mutableSet(service1, service2));
        org.setLocations(helper.mutableSet(location1, location2));

        when(organizationImportService.createOrUpdateOrganization(any(Organization.class), anyString(),
            anyString(), any(DataImportReport.class))).thenReturn(org);
        when(locationImportService.createOrUpdateLocation(any(Location.class), anyString(), anyString(), any()))
            .thenReturn(location1);
        when(locationImportService.createOrUpdateLocation(any(Location.class), anyString(), anyString(), any()))
            .thenReturn(location2);
        when(serviceImportService.createOrUpdateService(any(Service.class), anyString(), anyString(),
            any(DataImportReport.class))).thenReturn(service1);
        when(serviceImportService.createOrUpdateService(any(Service.class), anyString(), anyString(),
            any(DataImportReport.class))).thenReturn(service2);

        importService.createOrUpdateOrganization(org, ORG_ID, PROVIDER, new DataImportReport());

        verify(organizationImportService)
            .createOrUpdateOrganization(any(Organization.class), anyString(), anyString(), any(DataImportReport.class));
        verify(locationImportService, times(2))
            .createOrUpdateLocation(any(Location.class), anyString(), anyString(), any());
        verify(serviceImportService, times(2))
            .createOrUpdateService(any(Service.class), anyString(), anyString(), any(DataImportReport.class));
        verify(transactionSynchronizationService)
            .registerSynchronizationOfMatchingOrganizations(any(Organization.class));
    }

    @Test
    public void testCreatingTaxonomy() {
        Taxonomy taxonomy = new Taxonomy();

        importService.createOrUpdateTaxonomy(taxonomy, TAX_ID, PROVIDER, new DataImportReport());

        verify(taxonomyImportService).createOrUpdateTaxonomy(taxonomy, TAX_ID, PROVIDER, new DataImportReport());
    }

    @Test
    public void testCreatingLocation() {
        Location location = new Location();

        importService.createOrUpdateLocation(location, LOC_1_ID, PROVIDER, new DataImportReport());

        verify(locationImportService).createOrUpdateLocation(location, LOC_1_ID, PROVIDER, new DataImportReport());
    }

    @Test
    public void testCreatingService() {
        Service service = new Service();
        DataImportReport report = new DataImportReport();

        importService.createOrUpdateService(service, SRVC_1_ID, PROVIDER, report);

        verify(serviceImportService).createOrUpdateService(service, SRVC_1_ID, PROVIDER, report);
    }
}
