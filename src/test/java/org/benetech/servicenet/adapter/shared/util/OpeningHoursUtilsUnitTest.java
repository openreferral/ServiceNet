package org.benetech.servicenet.adapter.shared.util;

import org.benetech.servicenet.adapter.shared.model.Hours;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

public class OpeningHoursUtilsUnitTest {

    @Test
    public void shouldExtractHoursProperly() {
        Optional<Hours> result = OpeningHoursUtils.getHoursFromString("09:00AM-11:00AM", "-");
        assertTrue(result.isPresent());
        assertEquals("09:00AM", result.get().getOpen());
        assertEquals("11:00AM", result.get().getClose());
    }

    @Test
    public void shouldSetOpenToCustomStringIfOccurs() {
        Optional<Hours> result = OpeningHoursUtils.getHoursFromString("CLOSED", "-");
        assertTrue(result.isPresent());
        assertEquals("CLOSED", result.get().getOpen());
        assertNull(result.get().getClose());
    }
}
