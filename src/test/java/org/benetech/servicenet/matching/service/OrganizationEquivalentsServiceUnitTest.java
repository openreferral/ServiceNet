package org.benetech.servicenet.matching.service;

import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.model.OrganizationEquivalent;
import org.benetech.servicenet.matching.service.impl.OrganizationEquivalentsService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static org.benetech.servicenet.matching.service.EntityEquivalentTestUtils.isMatchBetweenEntities;
import static org.benetech.servicenet.matching.service.EntityEquivalentTestUtils.verifyIfEmptyResult;
import static org.junit.Assert.assertTrue;

@Ignore("Not implemented yet")
public class OrganizationEquivalentsServiceUnitTest {

    private OrganizationEquivalentsService equivalentsService;

    @Before
    public void setUp() {
        equivalentsService = new OrganizationEquivalentsService();
    }

    @Test
    public void shouldGenerateProperSimpleEntityEquivalentsForOrganization() {
        UUID location1 = UUID.randomUUID(), location2 = UUID.randomUUID();
        UUID funding1 = UUID.randomUUID(), funding2 = UUID.randomUUID();

        Organization org1 = generateOrganization(location1, funding1);
        Organization org2 = generateOrganization(location2, funding2);

        OrganizationEquivalent result = equivalentsService.generateEquivalent(org1, org2);

        assertTrue(isMatchBetweenEntities(result, Location.class, location1, location2));
        assertTrue(isMatchBetweenEntities(result, Funding.class, location1, location2));
    }

    @Test
    public void shouldSetLocationEquivalentToNullForOrganizationIfAtLeastEntityFromPairIsNull() {
        Organization objectWithNullValue = generateOrganization(null, UUID.randomUUID());
        Organization objectWithRandomValues = generateOrganization(UUID.randomUUID(), UUID.randomUUID());
        verifyIfEmptyResult(equivalentsService, objectWithNullValue, objectWithRandomValues, Location.class);
    }

    @Test
    public void shouldSetFundingEquivalentToNullForOrganizationIfAtLeastEntityFromPairIsNull() {
        Organization objectWithNullValue = generateOrganization(UUID.randomUUID(), null);
        Organization objectWithRandomValues = generateOrganization(UUID.randomUUID(), UUID.randomUUID());
        verifyIfEmptyResult(equivalentsService, objectWithNullValue, objectWithRandomValues, Funding.class);
    }

    private Organization generateOrganization(UUID locationId, UUID fundingId) {
        Location location = null;
        Funding funding = null;

        if (locationId != null) {
            location = new Location();
            location.setId(locationId);
        }
        
        if (fundingId != null) {
            funding = new Funding();
            funding.setId(fundingId);
        }

        return new Organization().location(location).funding(funding);
    }
}
