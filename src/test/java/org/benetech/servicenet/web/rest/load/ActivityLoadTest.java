package org.benetech.servicenet.web.rest.load;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ParallelLoadExtension;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.repository.SiloRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.scheduler.ReferenceDataGenerator;
import org.benetech.servicenet.web.rest.unauthorized.PublicRecordResource;
import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.domain.TestMappings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith({ParallelLoadExtension.class})
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ActivityLoadTest {
    final static int ORG_COUNT = 2000;

    @BeforeAll
    public static void loadData(@Autowired ReferenceDataGenerator referenceDataGenerator,
        @Autowired UserProfileRepository userProfileRepository, @Autowired SiloRepository siloRepository) {
        new LoadTestUtils(userProfileRepository, siloRepository).setTestSilo("admin");
        referenceDataGenerator.createReferenceData("admin", ORG_COUNT);
    }

    @Test
    @DisplayName("Test parallel load for ActivityResource.getProviderRecords")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityResourcePerformanceTest.class, testMethod = "getAPageOfUsersRecords"),
    })
    public void testLoadGetAPageOfUsersActivityRecords() {
    }

    @Test
    @DisplayName("Test parallel load for ActivityResource.getAllRecords")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityResourcePerformanceTest.class, testMethod = "getAPageOfAllRecords")
    })
    public void testLoadGetAPageOfAllActivityRecords() {
    }

    @Test
    @DisplayName("Test parallel load for ActivityResource.getRecordsForMap")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityResourcePerformanceTest.class, testMethod = "getRecordsForMap")
    })
    public void testLoadGetActivityRecordsForMap() {
    }

    @Test
    @DisplayName("Test parallel load for ActivityResource.getASpecificRecord")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityResourcePerformanceTest.class, testMethod = "getASpecificRecord"),
    })
    public void testLoadGetASpecificRecord() {
    }

    @Test
    @DisplayName("Test parallel load for ActivityFilterResource.getPostalCodes")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityFilterResourcePerformanceTest.class, testMethod = "getPostalCodes")
    })
    public void testLoadGetPostalCodes() {
    }

    @Test
    @DisplayName("Test parallel load for ActivityFilterResource.getCities")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityFilterResourcePerformanceTest.class, testMethod = "getCities")
    })
    public void testLoadGetCities() {
    }

    @Test
    @DisplayName("Test parallel load for ActivityFilterResource.getRegions")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityFilterResourcePerformanceTest.class, testMethod = "getRegions")
    })
    public void testLoadGetRegions() {
    }

    @Test
    @DisplayName("Test parallel load for ActivityFilterResource.getTaxonomies")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityFilterResourcePerformanceTest.class, testMethod = "getTaxonomies")
    })
    public void testLoadGetTaxonomies() {
    }

    @Test
    @DisplayName("Test parallel load for PublicRecordResource.getAllRecords")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = PublicRecordResource.class, testMethod = "getAPageOfAllPublicRecords")
    })
    public void testLoadPublicGetAPageOfAllActivityRecords() {
    }

    @Test
    @DisplayName("Test parallel load for PublicRecordResource.getRecordsForMap")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = PublicRecordResource.class, testMethod = "getPublicRecordsForMap")
    })
    public void testLoadPublicGetActivityRecordsForMap() {
    }

    @Test
    @DisplayName("Test parallel load for PublicRecordResource.getPostalCodes")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = PublicRecordResourcePerformanceTest.class, testMethod = "getPostalCodes")
    })
    public void testLoadPublicGetPostalCodes() {
    }

    @Test
    @DisplayName("Test parallel load for PublicRecordResource.getCities")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = PublicRecordResourcePerformanceTest.class, testMethod = "getCities")
    })
    public void testLoadPublicGetCities() {
    }

    @Test
    @DisplayName("Test parallel load for PublicRecordResource.getRegions")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = PublicRecordResourcePerformanceTest.class, testMethod = "getRegions")
    })
    public void testLoadPublicGetRegions() {
    }

    @Test
    @DisplayName("Test parallel load for PublicRecordResource.getTaxonomies")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = PublicRecordResourcePerformanceTest.class, testMethod = "getTaxonomies")
    })
    public void testLoadPublicGetTaxonomies() {
    }

    @Test
    @DisplayName("Test parallel load for PublicRecordResource.getProviderTaxonomies")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = PublicRecordResourcePerformanceTest.class, testMethod = "getProviderTaxonomies")
    })
    public void testLoadPublicGetProviderTaxonomies() {
    }
}
