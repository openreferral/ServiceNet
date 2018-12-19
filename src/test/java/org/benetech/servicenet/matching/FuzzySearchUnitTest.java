package org.benetech.servicenet.matching;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FuzzySearchUnitTest {

    private static final int HIGH_RATIO = 80;
    private static final int HIGH_PARTIAL_RATIO = 95;

    @Test
    public void shouldReturnHighRatioForSimilarStrings() {
        assertTrue(FuzzySearch.ratio("First", "Firts") >= HIGH_RATIO);
        assertTrue(FuzzySearch.ratio("McDonalds", "McDonald's") >= HIGH_RATIO);
        //TODO: Extend this tests with more real life examples when available
    }

    @Test
    public void shouldReturnLowRatioForDifferentStrings() {
        assertTrue(FuzzySearch.ratio("First", "Second") < HIGH_RATIO);
        //TODO: Extend this tests with more real life examples when available
    }

    @Test
    public void shouldReturnHighPartialRatioForSimilarStrings() {
        assertTrue(FuzzySearch.partialRatio("First Second", "First") >= HIGH_PARTIAL_RATIO);
        assertTrue(FuzzySearch.partialRatio("AnotherExample", "Example") >= HIGH_PARTIAL_RATIO);
        //TODO: Extend this tests with more real life examples when available
    }

    @Test
    public void shouldReturnLowPartialRatioForDifferentStrings() {
        assertTrue(FuzzySearch.partialRatio("First", "Second") < HIGH_PARTIAL_RATIO);
        //TODO: Extend this tests with more real life examples when available
    }
}
