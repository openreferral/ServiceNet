package org.benetech.servicenet.web.rest.load;

import org.benetech.servicenet.ParallelLoadExtension;
import org.benetech.servicenet.web.rest.ActivityResourceIntTest;
import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.domain.TestMappings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ParallelLoadExtension.class})
public class ActivityResourceLoadTest {
    @Test
    @DisplayName("Test parallel load for ActivityResource")
    @LoadWith("config/activity_load_test_config.properties")
    @TestMappings({
        @TestMapping(testClass = ActivityResourceIntTest.class, testMethod = "getAPageOfUsersActivityRecords")
    })
    public void testLoad() {
    }
}
