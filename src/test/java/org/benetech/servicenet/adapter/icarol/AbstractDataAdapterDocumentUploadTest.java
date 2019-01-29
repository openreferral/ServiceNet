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
import org.benetech.servicenet.service.impl.DocumentUploadServiceImpl;
import org.benetech.servicenet.service.mapper.DocumentUploadMapper;
import org.benetech.servicenet.web.rest.errors.ExceptionTranslator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;

import static org.benetech.servicenet.adapter.AdapterTestsUtils.mockEndpointWithBatch;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class AbstractDataAdapterDocumentUploadTest {

    private static final String EDEN_PROVIDER = "Eden";
    private static final String ICAROL_CATALOG = "icarol/";

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

    @Autowired
    private HttpMessageConverter<?>[] httpMessageConverters;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

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
    @Ignore
    public void shouldSaveAllToDbTest() throws IOException {
        String allIds = AdapterTestsUtils.readResourceAsString(ICAROL_CATALOG + "ids.json");
        DataImportReport report = new DataImportReport().startDate(ZonedDateTime.now());
        ICarolComplexResponseElement data = getAllIdsInBatches(allIds);

        mockEndpointWithBatch(data.getProgramBatches().get(0), ICAROL_CATALOG + "programs.json", mockServer);
        mockEndpointWithBatch(data.getSiteBatches().get(0), ICAROL_CATALOG + "sites.json", mockServer);
        mockEndpointWithBatch(
            data.getServiceSiteBatches().get(0), ICAROL_CATALOG + "serviceSites.json", mockServer);
        mockEndpointWithBatch(data.getAgencyBatches().get(0), ICAROL_CATALOG + "agencies.json", mockServer);

        documentUploadService.uploadApiData(allIds, EDEN_PROVIDER, report);
    }

    private static ICarolComplexResponseElement getAllIdsInBatches(String allIds) {
        Type collectionType = new TypeToken<Collection<ICarolSimpleResponseElement>>() {
        }.getType();
        Collection<ICarolSimpleResponseElement> responseElements = new Gson().fromJson(allIds, collectionType);
        return new ICarolComplexResponseElement(responseElements);
    }

}
