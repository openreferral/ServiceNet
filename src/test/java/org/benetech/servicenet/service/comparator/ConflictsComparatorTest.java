package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.SystemAccountDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConflictsComparatorTest {

    private static final String PATH = "org.benetech.servicenet.domain.";
    private static final String LOCATION = PATH + "Location";
    private static final String PHYSICAL_ADDRESS = PATH + "PhysicalAddress";
    private static final String POSTAL_ADDRESS = PATH + "PostalAddress";
    private static final String ORGANIZATION = PATH + "Organization";

    private static final String PHONE = "phone";
    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String EMAIL = "email";

    private ConflictsComparator comparator;

    @Before
    public void setUp() {
        comparator = new ConflictsComparator();
    }

    @Test
    public void shouldCompareByNumberOfSystemAccounts() {
        ConflictDTO conflictWithTwoAccounts = getValidConflict().acceptedThisChange(
            Set.of(new SystemAccountDTO(), new SystemAccountDTO()))
            .build();
        ConflictDTO conflictWithOneAccount = getValidConflict().acceptedThisChange(
            Set.of(new SystemAccountDTO()))
            .build();

        assertTrue(comparator.compare(conflictWithTwoAccounts, conflictWithOneAccount) < 0);
        assertTrue(comparator.compare(conflictWithOneAccount, conflictWithTwoAccounts) > 0);
    }

    @Test
    public void shouldReturn0WhenNumbersOfSystemAccountsAreTheSame() {
        ConflictDTO conflictWithTwoAccounts = getValidConflict().acceptedThisChange(
            Set.of(new SystemAccountDTO()))
            .build();
        ConflictDTO conflictWithOneAccount = getValidConflict().acceptedThisChange(
            Set.of(new SystemAccountDTO()))
            .build();

        assertEquals(0, comparator.compare(conflictWithTwoAccounts, conflictWithOneAccount));
    }

    @Test
    public void shouldPrioritizeAddressesOverOrganization() {
        ConflictDTO locationConflict = getValidConflict().entityPath(LOCATION).build();
        ConflictDTO physicalAddressConflict = getValidConflict().entityPath(PHYSICAL_ADDRESS).build();
        ConflictDTO postalAddressConflict = getValidConflict().entityPath(POSTAL_ADDRESS).build();
        ConflictDTO organizationConflict = getValidConflict().entityPath(ORGANIZATION).build();

        assertEquals(0, comparator.compare(locationConflict, postalAddressConflict));
        assertEquals(0, comparator.compare(locationConflict, physicalAddressConflict));
        assertTrue(comparator.compare(locationConflict, organizationConflict) < 0);
    }

    @Test
    public void shouldPrioritizeOrganizationPhoneOverOtherName() {
        ConflictDTO orgNameConflict = getValidConflict().entityPath(ORGANIZATION).fieldName(NAME).build();
        ConflictDTO orgPhoneConflict = getValidConflict().entityPath(ORGANIZATION).fieldName(PHONE).build();

        assertTrue(comparator.compare(orgPhoneConflict, orgNameConflict) < 0);
    }

    @Test
    public void shouldPrioritizeOrganizationNameOverOtherFields() {
        ConflictDTO orgNameConflict = getValidConflict().entityPath(ORGANIZATION).fieldName(NAME).build();
        ConflictDTO orgUrlConflict = getValidConflict().entityPath(ORGANIZATION).fieldName(URL).build();
        ConflictDTO orgEmailConflict = getValidConflict().entityPath(ORGANIZATION).fieldName(EMAIL).build();

        assertTrue(comparator.compare(orgNameConflict, orgUrlConflict) < 0);
        assertTrue(comparator.compare(orgNameConflict, orgEmailConflict) < 0);
    }

    private ConflictDTO.ConflictDTOBuilder getValidConflict() {
        return ConflictDTO.builder().entityPath("").fieldName("");
    }
}
