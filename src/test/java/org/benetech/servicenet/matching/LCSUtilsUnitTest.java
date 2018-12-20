package org.benetech.servicenet.matching;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LCSUtilsUnitTest {

    @Test
    public void shouldReturnLengthOfTheShorterIncludedString() {
        String string1 = "Long text with secret string in it";
        String string2 = "secret string";
        int result = LCSUtils.getLongestCommonSubsequence(string1, string2);
        assertEquals(13, result);
    }

    @Test
    public void shouldReturnLengthOfCommonSubsequence() {
        String string1 = "String Subsequence Example";
        String string2 = "123SubsequenceString";
        int result = LCSUtils.getLongestCommonSubsequence(string1, string2);
        assertEquals(11, result);
    }

    @Test
    public void shouldReturnLengthOfSubsequenceSeparatedWithOtherStrings() {
        String string1 = "xxxOxOxxOxOxxOOxxx";
        String string2 = "yOyOyOyyyyyyyOyyOyyyyyO";
        int result = LCSUtils.getLongestCommonSubsequence(string1, string2);
        assertEquals(6, result);
    }
}
