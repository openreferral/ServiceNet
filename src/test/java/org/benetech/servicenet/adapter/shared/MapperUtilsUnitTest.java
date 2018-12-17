package org.benetech.servicenet.adapter.shared;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class MapperUtilsUnitTest {

    @Test
    public void shouldNotJoinNullValues() {
        String result1 = MapperUtils.joinNotBlank(", ", "value", null);
        String result2 = MapperUtils.joinNotBlank(", ", null, "value");

        assertEquals("value", result1);
        assertEquals("value", result2);
    }

    @Test
    public void shouldNotJoinBlankValues() {
        String result1 = MapperUtils.joinNotBlank(", ", " ", "    ");
        String result2 = MapperUtils.joinNotBlank(", ", null, "");

        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
    }

    @Test
    public void shouldJoinValues() {
        String result = MapperUtils.joinNotBlank(", ", "value", "value2");
        assertEquals("value, value2", result);
    }

}
