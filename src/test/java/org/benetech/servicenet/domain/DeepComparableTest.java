package org.benetech.servicenet.domain;

import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.mapstruct.ap.internal.util.Collections;

public class DeepComparableTest {

    private static final String LOC_1_NAME = "Location 1";
    private static final String LOC_2_NAME = "Location 1";
    private static final String LOC_3_NAME = "Location 3";
    private static final String LOC_4_NAME = "Location 4";
    private static final String ORG_1_NAME = "Organization 1";
    private static final String ORG_2_NAME = "Organization 2";

    @Test
    public void organizationsWithDifferentNamesShouldNotBeDeepEqual() {
        Organization org1 = mockOrganization(ORG_1_NAME, null);
        Organization org2 = mockOrganization(ORG_2_NAME, null);

        Assert.assertFalse(org1.deepEquals(org2));
    }

    @Test
    public void organizationsWithTheSameNamesAndLocationsShouldBeDeepEqual() {
        Location loc1 = mockLocation(LOC_1_NAME);
        Location loc2 = mockLocation(LOC_2_NAME);
        Location loc1duplicate = mockLocation(LOC_1_NAME);

        Organization org1 = mockOrganization(ORG_1_NAME, new Location[]{loc1, loc2});
        Organization org1duplicate = mockOrganization(ORG_1_NAME, new Location[]{loc2, loc1duplicate});
        Assert.assertTrue(org1.deepEquals(org1duplicate));
    }

    @Test
    public void organizationsWithTheSameNamesAndDifferentLocationsShouldNotBeDeepEqual() {
        Location loc1 = mockLocation(LOC_1_NAME);
        Location loc2 = mockLocation(LOC_3_NAME);
        Location loc3 = mockLocation(LOC_4_NAME);

        Organization org1 = mockOrganization(ORG_1_NAME, new Location[]{loc1, loc2});
        Organization org2 = mockOrganization(ORG_1_NAME, new Location[]{loc1, loc3});
        Assert.assertFalse(org1.deepEquals(org2));
    }

    private Organization mockOrganization(String name, Location[] locations) {
        Organization org = new Organization();
        org.setId(UUID.randomUUID());
        org.setName(name);
        if (locations != null) {
            org.setLocations(Collections.asSet(locations));
        }
        return org;
    }

    private Location mockLocation(String name) {
        Location loc = new Location();
        loc.setId(UUID.randomUUID());
        loc.setName(name);
        return loc;
    }
}
