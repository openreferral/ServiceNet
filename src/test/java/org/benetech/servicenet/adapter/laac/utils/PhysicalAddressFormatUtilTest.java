package org.benetech.servicenet.adapter.laac.utils;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PhysicalAddressFormatUtilTest {

    private static final String FORMAT_1;
    private static final String FORMAT_2;
    private static final String FORMAT_3;
    private static final String INVALID_FORMAT_1;
    private static final String INVALID_FORMAT_2;
    private static final String INVALID_FORMAT_3;
    private static final String CITY = "San Diego";
    private static final String STATE = "CA";
    private static final String POSTAL_CODE = "12345";
    private static final String COUNTRY = "United States of America";
    private static final String ADDRESS = "123 Broadway Street";
    private static final String ATTENTION = "Suite 123";
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";
    private static final String COMMA = ", ";
    private static final String NOT_AVAILABLE = "N/A";

    @Test
    public void testAddressFormats() {
        Optional<PhysicalAddress> result = PhysicalAddressFormatUtil.resolveAddress(FORMAT_1);

        assertTrue(result.isPresent());

        assertEquals(CITY, result.get().getCity());
        assertEquals(STATE, result.get().getStateProvince());
        assertEquals(POSTAL_CODE, result.get().getPostalCode());
        assertEquals(COUNTRY, result.get().getCountry());
        assertEquals(NOT_AVAILABLE, result.get().getAddress1());
        assertNull(result.get().getAttention());
        assertNull(result.get().getRegion());

        result = PhysicalAddressFormatUtil.resolveAddress(FORMAT_2);

        assertTrue(result.isPresent());

        assertEquals(CITY, result.get().getCity());
        assertEquals(STATE, result.get().getStateProvince());
        assertEquals(POSTAL_CODE, result.get().getPostalCode());
        assertEquals(COUNTRY, result.get().getCountry());
        assertEquals(ADDRESS, result.get().getAddress1());
        assertEquals(ATTENTION, result.get().getAttention());
        assertNull(result.get().getRegion());

        result = PhysicalAddressFormatUtil.resolveAddress(FORMAT_3);

        assertTrue(result.isPresent());

        assertEquals(CITY, result.get().getCity());
        assertEquals(STATE, result.get().getStateProvince());
        assertEquals(POSTAL_CODE, result.get().getPostalCode());
        assertEquals(COUNTRY, result.get().getCountry());
        assertEquals(ADDRESS, result.get().getAddress1());
        assertNull(result.get().getAttention());
        assertNull(result.get().getRegion());
    }

    @Test
    public void testInvalidFormats() {
        assertTrue(PhysicalAddressFormatUtil.resolveAddress(INVALID_FORMAT_1).isEmpty());
        assertTrue(PhysicalAddressFormatUtil.resolveAddress(INVALID_FORMAT_2).isEmpty());
        assertTrue(PhysicalAddressFormatUtil.resolveAddress(INVALID_FORMAT_3).isEmpty());
        assertTrue(PhysicalAddressFormatUtil.resolveAddress(null).isEmpty());
        assertTrue(PhysicalAddressFormatUtil.resolveAddress(SPACE).isEmpty());
    }

    static {
        FORMAT_1 = CITY + COMMA + STATE + SPACE + POSTAL_CODE + NEW_LINE +
            COUNTRY;
        FORMAT_2 = ADDRESS + NEW_LINE +
             ATTENTION + NEW_LINE +
            CITY + COMMA + STATE + SPACE + POSTAL_CODE + NEW_LINE +
            COUNTRY;
        FORMAT_3 = ADDRESS + NEW_LINE +
            CITY + COMMA + STATE + SPACE + POSTAL_CODE + NEW_LINE +
            COUNTRY;
        INVALID_FORMAT_1 = CITY + COMMA + STATE + SPACE + POSTAL_CODE + COUNTRY;
        INVALID_FORMAT_2 = ADDRESS + NEW_LINE +
            ATTENTION + NEW_LINE +
            CITY + COMMA + STATE + NEW_LINE +
            POSTAL_CODE + NEW_LINE +
            COUNTRY;
        INVALID_FORMAT_3 = ADDRESS + NEW_LINE +
            CITY + SPACE + STATE + SPACE + POSTAL_CODE + NEW_LINE +
            COUNTRY;
    }
}
