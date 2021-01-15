package org.benetech.servicenet.manager;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.TestPersistanceHelper;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.benetech.servicenet.TestConstants.PROVIDER;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ImportManagerTest {

    private static final String STRING_1 = "STRING_1";
    private static final String STRING_2 = "STRING_2";
    private static final String EXTERNAL_ID_1 = "ext1";
    private static final String EXTERNAL_ID_2 = "ext2";
    private static final String EXTERNAL_ID_3 = "ext3";
    private static final int THREE = 3;

    @Autowired
    private ImportManager importManager;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TestPersistanceHelper helper;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void setUp() {
        testDatabaseManagement.clearDb();
        helper.generateSystemAccount(PROVIDER);
    }

    @Test
    @Transactional
    public void testRollingBackOrganization() {
        Organization org1 = generateOrganization(STRING_1, EXTERNAL_ID_1);
        Organization org2 = generateInvalidOrganization(EXTERNAL_ID_2);
        Organization org3 = generateOrganization(STRING_2, EXTERNAL_ID_3);

        assertEquals(0, organizationService.findAll().size());

        importManager.createOrUpdateOrganization(org1, EXTERNAL_ID_1, new ImportData(
            new DataImportReport(), PROVIDER, true), true);
        importManager.createOrUpdateOrganization(org2, EXTERNAL_ID_2, new ImportData(
            new DataImportReport(), PROVIDER, true), true);
        importManager.createOrUpdateOrganization(org3, EXTERNAL_ID_3, new ImportData(
            new DataImportReport(), PROVIDER, true), true);

        assertEquals(THREE, organizationService.findAll().size());
    }

    private Organization generateOrganization(String string, String id) {
        return new Organization().name(string).externalDbId(id).active(true);
    }

    private Organization generateInvalidOrganization(String id) {
        return generateOrganization(null, id);
    }
}
