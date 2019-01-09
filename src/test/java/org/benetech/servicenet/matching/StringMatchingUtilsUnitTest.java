package org.benetech.servicenet.matching;

import org.benetech.servicenet.matching.counter.StringMatchingUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringMatchingUtilsUnitTest {

    @Test
    public void shouldExtractInitials() {
        String result = StringMatchingUtils.extractInitials("The Initials extraction");
        assertEquals("TIe", result);
    }

    @Test
    public void shouldSortWords() {
        String result = StringMatchingUtils.sort("Aaa C Bbb");
        assertEquals("Aaa Bbb C", result);
    }

    @Test
    public void shouldNormalizeTheString() {
        String result = StringMatchingUtils.normalize("N!orma&lized String 123");
        assertEquals("NORMALIZED STRING 123", result);
    }

    @Test
    public void shouldNormalizeMultipleSpaces() {
        String result = StringMatchingUtils.normalize(" Double  spaces & triple   spaces ");
        assertEquals("DOUBLE SPACES TRIPLE SPACES", result);
    }
}
