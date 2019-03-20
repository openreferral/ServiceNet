package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ServiceNetApp.class, MockedUserTestConfiguration.class })
public class PhysicalAddressConflictDetectorTest {

    private static final String DEFAULT_ATTENTION = "AAAAAA";
    private static final String DEFAULT_ADDRESS1 = "BBBBBB";
    private static final String DEFAULT_CITY = "CCCCCC";
    private static final String DEFAULT_REGION = "DDDDDD";
    private static final String DEFAULT_STATE_PROVINCE = "EEEEEE";
    private static final String DEFAULT_POSTAL_CODE = "FFFFFF";
    private static final String DEFAULT_COUNTRY = "GGGGGG";

    @Autowired
    private PhysicalAddressConflictDetector conflictDetector;

    @Test
    public void shouldNotFindAnyConflicts() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        PhysicalAddress mirrorAddress = createDefaultPhysicalAddress();

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldNotFindAnyConflictsWhenDifferentLetterCase() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        PhysicalAddress mirrorAddress = new PhysicalAddress()
            .attention(DEFAULT_ATTENTION.toLowerCase())
            .address1(DEFAULT_ADDRESS1.toLowerCase())
            .city(DEFAULT_CITY.toLowerCase())
            .region(DEFAULT_REGION.toLowerCase())
            .stateProvince(DEFAULT_STATE_PROVINCE.toLowerCase())
            .postalCode(DEFAULT_POSTAL_CODE.toLowerCase())
            .country(DEFAULT_COUNTRY.toLowerCase());

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldFindAConflict() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        PhysicalAddress mirrorAddress = createDefaultPhysicalAddress();
        mirrorAddress.setAddress1(StringUtils.randomAlphanumeric(7));

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(conflicts.size(), 1);
        assertEquals(address.getAddress1(), conflicts.get(0).getCurrentValue());
        assertEquals(mirrorAddress.getAddress1(), conflicts.get(0).getOfferedValue());
    }

    @Test
    public void shouldFindConflicts() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        PhysicalAddress mirrorAddress = new PhysicalAddress()
            .attention(StringUtils.randomAlphanumeric(7))
            .address1(StringUtils.randomAlphanumeric(5))
            .city(StringUtils.randomAlphanumeric(3))
            .region(StringUtils.randomAlphanumeric(1))
            .stateProvince(StringUtils.randomAlphanumeric(12))
            .postalCode(StringUtils.randomAlphanumeric(50))
            .country(StringUtils.randomAlphanumeric(60));

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(7, conflicts.size());
    }


    @Test
    public void shouldFindConflictsWhenTheOtherValueIsNull() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        PhysicalAddress mirrorAddress = new PhysicalAddress();

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(7, conflicts.size());
    }

    @Test
    public void shouldNotFindConflictWhenBothValuesAreNull() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        address.setCity(null);

        PhysicalAddress mirrorAddress = createDefaultPhysicalAddress();
        mirrorAddress.setCity(null);

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldNotFindAConflictWhenCBothValuesAreBlank() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        address.setAttention("     ");

        PhysicalAddress mirrorAddress = createDefaultPhysicalAddress();
        mirrorAddress.setAttention(null);

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldNotFindConflictWhenBothValuesAreEmptyStrings() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        address.setCountry("");

        PhysicalAddress mirrorAddress = createDefaultPhysicalAddress();
        mirrorAddress.setCountry("");

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(Collections.emptyList(), conflicts);
    }

    @Test
    public void shouldFindConflictWhenCurrentIsNull() {
        PhysicalAddress address = createDefaultPhysicalAddress();
        address.setCountry(null);

        PhysicalAddress mirrorAddress = createDefaultPhysicalAddress();

        List<Conflict> conflicts = conflictDetector.detectConflicts(address, mirrorAddress);

        assertEquals(1, conflicts.size());
        assertEquals(org.apache.commons.lang3.StringUtils.EMPTY, conflicts.get(0).getCurrentValue());
    }

    private PhysicalAddress createDefaultPhysicalAddress() {
        return new PhysicalAddress()
            .attention(DEFAULT_ATTENTION)
            .address1(DEFAULT_ADDRESS1)
            .city(DEFAULT_CITY)
            .region(DEFAULT_REGION)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .postalCode(DEFAULT_POSTAL_CODE)
            .country(DEFAULT_COUNTRY);
    }
}
