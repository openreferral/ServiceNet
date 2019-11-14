package org.benetech.servicenet.matching;

import static junit.framework.TestCase.assertEquals;
import static org.benetech.servicenet.config.Constants.HEALTHLEADS_PROVIDER;
import static org.benetech.servicenet.config.Constants.LAAC_PROVIDER;
import static org.benetech.servicenet.config.Constants.SHELTER_TECH_PROVIDER;
import static org.benetech.servicenet.config.Constants.SMC_CONNECT_PROVIDER;
import static org.benetech.servicenet.matching.MatchingTestConstants.ADDRESSES;
import static org.benetech.servicenet.matching.MatchingTestConstants.BAD_MATCHES;
import static org.benetech.servicenet.matching.MatchingTestConstants.CONTACTS;
import static org.benetech.servicenet.matching.MatchingTestConstants.CSV;
import static org.benetech.servicenet.matching.MatchingTestConstants.ELIGIBILITY;
import static org.benetech.servicenet.matching.MatchingTestConstants.HEALTHLEADS_DIR;
import static org.benetech.servicenet.matching.MatchingTestConstants.HOLIDAY_SCHEDULES;
import static org.benetech.servicenet.matching.MatchingTestConstants.JSON;
import static org.benetech.servicenet.matching.MatchingTestConstants.LAAC_DIR;
import static org.benetech.servicenet.matching.MatchingTestConstants.LANGUAGES;
import static org.benetech.servicenet.matching.MatchingTestConstants.LOCATIONS;
import static org.benetech.servicenet.matching.MatchingTestConstants.MAIL_ADDRESSES;
import static org.benetech.servicenet.matching.MatchingTestConstants.ORGANIZATIONS;
import static org.benetech.servicenet.matching.MatchingTestConstants.PHONES;
import static org.benetech.servicenet.matching.MatchingTestConstants.PHYSICAL_ADDRESSES;
import static org.benetech.servicenet.matching.MatchingTestConstants.PROGRAMS;
import static org.benetech.servicenet.matching.MatchingTestConstants.REGULAR_SCHEDULES;
import static org.benetech.servicenet.matching.MatchingTestConstants.REQUIRED_DOCUMENTS;
import static org.benetech.servicenet.matching.MatchingTestConstants.SERVICES;
import static org.benetech.servicenet.matching.MatchingTestConstants.SERVICES_AT_LOCATION;
import static org.benetech.servicenet.matching.MatchingTestConstants.SERVICES_TAXONOMY;
import static org.benetech.servicenet.matching.MatchingTestConstants.SHELTER_TECH_DIR;
import static org.benetech.servicenet.matching.MatchingTestConstants.SMC_DIR;
import static org.benetech.servicenet.matching.MatchingTestConstants.TAXONOMY;
import static org.benetech.servicenet.matching.MatchingTestConstants.METADATA;
import static org.benetech.servicenet.matching.MatchingTestUtils.loadCsv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.benetech.servicenet.MockedGeocodingConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.adapter.AdapterTestsUtils;
import org.benetech.servicenet.adapter.healthleads.HealthleadsDataAdapter;
import org.benetech.servicenet.adapter.laac.LAACDataAdapter;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.shared.model.SingleImportData;
import org.benetech.servicenet.adapter.sheltertech.ShelterTechDataAdapter;
import org.benetech.servicenet.adapter.smcconnect.SMCConnectDataAdapter;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedGeocodingConfiguration.class})
public class MatchingAccuracyIntTest {

    @Autowired
    private HealthleadsDataAdapter healthleadsDataAdapter;

    @Autowired
    private LAACDataAdapter laacDataAdapter;

    @Autowired
    private ShelterTechDataAdapter shelterTechDataAdapter;

    @Autowired
    private SMCConnectDataAdapter smcConnectDataAdapter;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Configuration
    static class AsyncConfiguration {
        @Bean(name = "taskExecutor")
        @Primary
        public TaskExecutor taskExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @Before
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setUp() throws IOException {
        testDatabaseManagement.clearDb();
        importHealthLeadsData();
        importLaacData();
        importShelterTechData();
        importSmcConnectData();
        for (Organization org : organizationService.findAll()) {
            organizationMatchService.createOrUpdateOrganizationMatches(org, null);
        }
    }

    @Test
    public void shouldNotMatchAnyOrganization() {
        List<Organization> organizations = organizationService.findAll();
        assertEquals(8, organizations.size());
        List<OrganizationMatchDTO> matches = organizationMatchService.findAll();
        assertEquals(0, matches.size());
    }

    private void importHealthLeadsData() throws IOException {
        List<String> fileNames = Arrays.asList(
            ELIGIBILITY, LANGUAGES, LOCATIONS,
            ORGANIZATIONS, PHONES, PHYSICAL_ADDRESSES,
            SERVICES_TAXONOMY, TAXONOMY, REQUIRED_DOCUMENTS,
            SERVICES, SERVICES_AT_LOCATION, METADATA
        );

        List<DocumentUpload> uploads = new ArrayList<>();
        List<String> data = new ArrayList<>();
        for (String fileName : fileNames) {
            DocumentUpload upload = new DocumentUpload();
            upload.setFilename(fileName);
            uploads.add(upload);
            data.add(loadCsv(HEALTHLEADS_DIR + fileName + CSV));
        }
        MultipleImportData importData = new MultipleImportData(data, uploads, new DataImportReport(), HEALTHLEADS_PROVIDER, true, null);
        healthleadsDataAdapter.importData(importData);
    }

    private void importLaacData() throws IOException {
        String csv = loadCsv(LAAC_DIR + BAD_MATCHES + CSV);

        SingleImportData importData = new SingleImportData(csv, new DataImportReport(), LAAC_PROVIDER, true, null);
        laacDataAdapter.importData(importData);
    }

    private void importShelterTechData() throws IOException {
        String json = AdapterTestsUtils.readResourceAsString(SHELTER_TECH_DIR + BAD_MATCHES + JSON);

        SingleImportData importData = new SingleImportData(json, new DataImportReport(), SHELTER_TECH_PROVIDER, true, null);
        shelterTechDataAdapter.importData(importData);
    }

    private void importSmcConnectData() throws IOException {
        List<String> fileNames = Arrays.asList(
            ADDRESSES, CONTACTS, HOLIDAY_SCHEDULES,
            LOCATIONS, MAIL_ADDRESSES, ORGANIZATIONS,
            PHONES, PROGRAMS, REGULAR_SCHEDULES, SERVICES
        );

        List<DocumentUpload> uploads = new ArrayList<>();
        List<String> data = new ArrayList<>();
        for (String fileName : fileNames) {
            DocumentUpload upload = new DocumentUpload();
            upload.setFilename(fileName);
            uploads.add(upload);
            data.add(loadCsv(SMC_DIR + fileName + CSV));
        }
        MultipleImportData importData = new MultipleImportData(data, uploads, new DataImportReport(), SMC_CONNECT_PROVIDER, true, null);
        smcConnectDataAdapter.importData(importData);
    }
}
