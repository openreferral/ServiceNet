package org.benetech.servicenet.adapter.linkforcare;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.config.Constants.LINK_FOR_CARE_PROVIDER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.converter.XlsxFileConverter;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

// TODO: add more tests
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class LinkForCareDataAdapterTest {

    @Autowired
    private LinkForCareDataAdapter adapter;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Test
    public void testSavingLAACData() throws IOException, OpenXML4JException, SAXException {
        XlsxFileConverter xlsxFileConverter = new XlsxFileConverter();
        InputStream xlsxIs = AdapterTestsUtils.readResourceAsSteam("linkforcare/complete.xlsx");
        File json = xlsxFileConverter.convertToFile(xlsxIs);
        SingleImportData importData = new SingleImportData(json, new DataImportReport(), LINK_FOR_CARE_PROVIDER, true);

        adapter.importData(importData);
        assertExtractedOrganization();
    }
    private void assertExtractedOrganization() {
        assertEquals(1, organizationService.findAll().size());
        // TODO: check all the fields
    }
}
