package org.benetech.servicenet.adapter.icarol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.HttpClient;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.icarol.model.ICarolComplexResponseElement;
import org.benetech.servicenet.adapter.icarol.model.ICarolSimpleResponseElement;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.MongoDbService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.benetech.servicenet.service.impl.DocumentUploadServiceImpl;
import org.benetech.servicenet.service.mapper.DocumentUploadMapper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.benetech.servicenet.adapter.AdapterTestsUtils.mockEndpointWithBatch;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AbstractDataAdapterDocumentUploadTest {

    private static final String EDEN_PROVIDER = "Eden";
    private static final String ICAROL_CATALOG = "icarol/";
    private static final String IDS_FILE = "ids.json";
    private static final String PROGRAMS_FILE = "programs.json";
    private static final String SITES_FILE = "sites.json";
    private static final String SERVICE_SITES_FILE = "serviceSites.json";
    private static final String AGENCIES_FILE = "agencies.json";
    private static final String IDS_MISSING_FILE = "ids-missing.json";
    private static final String PROGRAMS_MISSING_FILE = "programs-missing.json";
    private static final String SITES_MISSING_FILE = "sites-missing.json";
    private static final String SERVICE_SITES_MISSING_FILE = "serviceSites-missing.json";
    private static final String AGENCIES_MISSING_FILE = "agencies-missing.json";

    @Mock
    private HttpClient client;

    @Mock
    private DocumentUploadRepository documentUploadRepository;

    @Mock
    private DocumentUploadMapper documentUploadMapper;

    @Mock
    private DataImportReportService dataImportReportService;

    @Mock
    private UserService userService;

    @Mock
    private MongoDbService mongoDbService;

    @Mock
    private ApplicationContext applicationContext;

    @Autowired
    @InjectMocks
    private DocumentUploadServiceImpl documentUploadService;

    private static MockServerClient mockServer = startClientAndServer(1080);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void shouldSaveOriginalDocTest() throws IOException {
        String allIds = AdapterTestsUtils.readResourceAsString(ICAROL_CATALOG + IDS_FILE);
        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now());

        ICarolComplexResponseElement data = getAllIdsInBatches(allIds);
        mockEndpointWithBatch(data.getProgramBatches().get(0), ICAROL_CATALOG + PROGRAMS_FILE, mockServer);
        mockEndpointWithBatch(data.getSiteBatches().get(0), ICAROL_CATALOG + SITES_FILE, mockServer);
        mockEndpointWithBatch(
            data.getServiceSiteBatches().get(0), ICAROL_CATALOG + SERVICE_SITES_FILE, mockServer);
        mockEndpointWithBatch(data.getAgencyBatches().get(0), ICAROL_CATALOG + AGENCIES_FILE, mockServer);

        DocumentUploadDTO document = documentUploadService.uploadApiData(allIds, EDEN_PROVIDER, report);

        assertNotNull(document.getOriginalDocumentId());
    }

    @Test
    public void shouldCreateOrganizationsTest() throws IOException {
        String allIds = AdapterTestsUtils.readResourceAsString(ICAROL_CATALOG + IDS_FILE);
        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now());

        ICarolComplexResponseElement data = getAllIdsInBatches(allIds);
        mockEndpointWithBatch(data.getProgramBatches().get(0), ICAROL_CATALOG + PROGRAMS_FILE, mockServer);
        mockEndpointWithBatch(data.getSiteBatches().get(0), ICAROL_CATALOG + SITES_FILE, mockServer);
        mockEndpointWithBatch(
            data.getServiceSiteBatches().get(0), ICAROL_CATALOG + SERVICE_SITES_FILE, mockServer);
        mockEndpointWithBatch(data.getAgencyBatches().get(0), ICAROL_CATALOG + AGENCIES_FILE, mockServer);

        documentUploadService.uploadApiData(allIds, EDEN_PROVIDER, report);

        assertEquals(Integer.valueOf(2), report.getNumberOfCreatedOrgs());
        assertEquals(Integer.valueOf(0), report.getNumberOfUpdatedOrgs());
    }

    @Test
    public void shouldCreateServicesTest() throws IOException {
        String allIds = AdapterTestsUtils.readResourceAsString(ICAROL_CATALOG + IDS_FILE);
        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now());

        ICarolComplexResponseElement data = getAllIdsInBatches(allIds);
        mockEndpointWithBatch(data.getProgramBatches().get(0), ICAROL_CATALOG + PROGRAMS_FILE, mockServer);
        mockEndpointWithBatch(data.getSiteBatches().get(0), ICAROL_CATALOG + SITES_FILE, mockServer);
        mockEndpointWithBatch(
            data.getServiceSiteBatches().get(0), ICAROL_CATALOG + SERVICE_SITES_FILE, mockServer);
        mockEndpointWithBatch(data.getAgencyBatches().get(0), ICAROL_CATALOG + AGENCIES_FILE, mockServer);

        documentUploadService.uploadApiData(allIds, EDEN_PROVIDER, report);

        assertEquals(Integer.valueOf(4), report.getNumberOfCreatedServices());
        assertEquals(Integer.valueOf(0), report.getNumberOfUpdatedServices());
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailDueToMissingValuesTest() throws IOException {
        String allIds = AdapterTestsUtils.readResourceAsString(ICAROL_CATALOG + IDS_MISSING_FILE);
        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now());

        ICarolComplexResponseElement data = getAllIdsInBatches(allIds);
        mockEndpointWithBatch(
            data.getProgramBatches().get(0), ICAROL_CATALOG + PROGRAMS_MISSING_FILE, mockServer);
        mockEndpointWithBatch(
            data.getSiteBatches().get(0), ICAROL_CATALOG + SITES_MISSING_FILE, mockServer);
        mockEndpointWithBatch(
            data.getServiceSiteBatches().get(0), ICAROL_CATALOG + SERVICE_SITES_MISSING_FILE, mockServer);
        mockEndpointWithBatch(
            data.getAgencyBatches().get(0), ICAROL_CATALOG + AGENCIES_MISSING_FILE, mockServer);

        documentUploadService.uploadApiData(allIds, EDEN_PROVIDER, report);
    }

    private static ICarolComplexResponseElement getAllIdsInBatches(String allIds) {
        Type collectionType = new TypeToken<Collection<ICarolSimpleResponseElement>>() {
        }.getType();
        Collection<ICarolSimpleResponseElement> responseElements = new Gson().fromJson(allIds, collectionType);
        return new ICarolComplexResponseElement(responseElements);
    }

}
