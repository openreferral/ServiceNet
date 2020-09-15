package org.benetech.servicenet.web.rest.load;

import org.benetech.servicenet.web.rest.ActivityResourceIntTest;
import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.jsmart.zerocode.core.runner.parallel.ZeroCodeLoadRunner;
import org.junit.runner.RunWith;

@LoadWith("config/load_config_5_users.properties")
@TestMapping(testClass = ActivityResourceIntTest.class, testMethod = "getAPageOfUsersActivityRecords")
@RunWith(ZeroCodeLoadRunner.class)
public class ActivityResourceLoadTest {

}
