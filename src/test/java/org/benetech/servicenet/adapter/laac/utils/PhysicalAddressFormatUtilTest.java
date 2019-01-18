package org.benetech.servicenet.adapter.laac.utils;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

    @Test
    public void testAddressFormats() {
        PhysicalAddress result = PhysicalAddressFormatUtil.resolveAddress(FORMAT_1);

        assertNotNull(result);

        assertEquals(CITY, result.getCity());
        assertEquals(STATE, result.getStateProvince());
        assertEquals(POSTAL_CODE, result.getPostalCode());
        assertEquals(COUNTRY, result.getCountry());
        assertNull(result.getAddress1());
        assertNull(result.getAttention());
        assertNull(result.getRegion());

        result = PhysicalAddressFormatUtil.resolveAddress(FORMAT_2);

        assertNotNull(result);

        assertEquals(CITY, result.getCity());
        assertEquals(STATE, result.getStateProvince());
        assertEquals(POSTAL_CODE, result.getPostalCode());
        assertEquals(COUNTRY, result.getCountry());
        assertEquals(ADDRESS, result.getAddress1());
        assertEquals(ATTENTION, result.getAttention());
        assertNull(result.getRegion());

        result = PhysicalAddressFormatUtil.resolveAddress(FORMAT_3);

        assertNotNull(result);

        assertEquals(CITY, result.getCity());
        assertEquals(STATE, result.getStateProvince());
        assertEquals(POSTAL_CODE, result.getPostalCode());
        assertEquals(COUNTRY, result.getCountry());
        assertEquals(ADDRESS, result.getAddress1());
        assertNull(result.getAttention());
        assertNull(result.getRegion());
    }

    @Test
    public void testInvalidFormats() {
        assertNull(PhysicalAddressFormatUtil.resolveAddress(INVALID_FORMAT_1));
        assertNull(PhysicalAddressFormatUtil.resolveAddress(INVALID_FORMAT_2));
        assertNull(PhysicalAddressFormatUtil.resolveAddress(INVALID_FORMAT_3));
        assertNull(PhysicalAddressFormatUtil.resolveAddress(null));
        assertNull(PhysicalAddressFormatUtil.resolveAddress(SPACE));
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
