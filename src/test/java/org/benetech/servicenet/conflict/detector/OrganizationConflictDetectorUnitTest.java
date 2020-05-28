package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.junit.Test;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrganizationConflictDetectorUnitTest {

    private static final String DEFAULT_NAME = "AAAAAA";
    private static final String DEFAULT_ALTERNATE_NAME = "BBBBBB";
    private static final String DEFAULT_DESCRIPTION = "CCCCCC";
    private static final String DEFAULT_EMAIL = "DDDDDD";
    private static final String DEFAULT_URL = "EEEEEE";
    private static final String DEFAULT_TAX_STATUS = "FFFFFF";
    private static final String DEFAULT_TAX_ID = "GGGGGG";
    private static final String DEFAULT_LEGAL_STATUS = "IIIIII";
    private static final int THREE = 3;
    private static final int FIVE = 5;
    private static final int EIGHT = 8;
    private static final int TEN = 10;
    private static final int FOURTEEN = 14;
    private static final int FIFTHEEN = 15;
    private static final int TWENTY = 20;
    private static final int FIFTY = 50;

    private OrganizationConflictDetector conflictDetector = new OrganizationConflictDetector();

    @Test
    public void shouldNotFindAnyConflicts() {
        Organization organization = createDefaultOgranization();
        Organization mirrorOrganization = createDefaultOgranization();

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldNotFindAnyConflictsWhenDifferentLetterCase() {
        Organization organization = createDefaultOgranization();
        Organization mirrorOrganization = new Organization()
            .name(DEFAULT_NAME.toLowerCase())
            .alternateName(DEFAULT_ALTERNATE_NAME.toLowerCase())
            .description(DEFAULT_DESCRIPTION.toLowerCase())
            .email(DEFAULT_EMAIL.toLowerCase())
            .url(DEFAULT_URL.toLowerCase())
            .taxStatus(DEFAULT_TAX_STATUS.toLowerCase())
            .taxId(DEFAULT_TAX_ID.toLowerCase())
            .yearIncorporated(null)
            .legalStatus(DEFAULT_LEGAL_STATUS.toLowerCase());

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldFindAConflict() {
        Organization organization = createDefaultOgranization();
        Organization mirrorOrganization = createDefaultOgranization();
        mirrorOrganization.setName(StringUtils.randomAlphanumeric(7));

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(conflicts.size(), 1);
        assertEquals(organization.getName(), conflicts.get(0).getCurrentValue());
        assertEquals(mirrorOrganization.getName(), conflicts.get(0).getOfferedValue());
    }

    @Test
    public void shouldFindConflicts() {
        Organization organization = createDefaultOgranization();
        Organization mirrorOrganization = new Organization()
            .name(StringUtils.randomAlphanumeric(7))
            .alternateName(StringUtils.randomAlphanumeric(TEN))
            .description(StringUtils.randomAlphanumeric(FIFTY))
            .email(StringUtils.randomAlphanumeric(FIVE))
            .url(StringUtils.randomAlphanumeric(FOURTEEN))
            .taxStatus(StringUtils.randomAlphanumeric(THREE))
            .taxId(StringUtils.randomAlphanumeric(TWENTY))
            .yearIncorporated(null)
            .legalStatus(StringUtils.randomAlphanumeric(FIFTHEEN));

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(EIGHT, conflicts.size());
    }

    @Test
    public void shouldFindConflictsWhenTheOtherValueIsNull() {
        Organization organization = createDefaultOgranization();
        Organization mirrorOrganization = new Organization();

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(EIGHT, conflicts.size());
    }

    @Test
    public void shouldNotFindConflictWhenBothValuesAreNull() {
        Organization organization = createDefaultOgranization();
        organization.setUrl(null);
        Organization mirrorOrganization = createDefaultOgranization();
        mirrorOrganization.setUrl(null);

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldNotFindAConflictWhenCBothValuesAreBlank() {
        Organization organization = createDefaultOgranization();
        organization.setLegalStatus("       ");
        Organization mirrorOrganization = createDefaultOgranization();
        mirrorOrganization.setLegalStatus(null);

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldNotFindConflictWhenBothValuesAreEmptyStrings() {
        Organization organization = createDefaultOgranization();
        organization.setEmail("");
        Organization mirrorOrganization = createDefaultOgranization();
        mirrorOrganization.setEmail("");

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldFindConflictWhenCurrentIsNull() {
        Organization organization = createDefaultOgranization();
        organization.setTaxId(null);
        Organization mirrorOrganization = createDefaultOgranization();

        List<Conflict> conflicts = conflictDetector.detectConflicts(organization, mirrorOrganization);

        assertEquals(1, conflicts.size());
        assertEquals(org.apache.commons.lang3.StringUtils.EMPTY, conflicts.get(0).getCurrentValue());
    }

    private Organization createDefaultOgranization() {
        return new Organization()
            .name(DEFAULT_NAME)
            .alternateName(DEFAULT_ALTERNATE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .email(DEFAULT_EMAIL)
            .url(DEFAULT_URL)
            .taxStatus(DEFAULT_TAX_STATUS)
            .taxId(DEFAULT_TAX_ID)
            .yearIncorporated(null)
            .legalStatus(DEFAULT_LEGAL_STATUS);
    }
}
