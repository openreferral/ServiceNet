package org.benetech.servicenet.matching;

import org.benetech.servicenet.ServiceNetApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class UrlSimilarityCounterUnitTest {

    private static final float PRECISION = 0.001f;

    @Autowired
    private UrlSimilarityCounter urlSimilarityCounter;

    @Test
    public void shouldReturnMinRatioForDifferentNormalizedAndUpperCased() {
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("one.com", "two.com"), PRECISION);
    }

    @Test
    public void shouldReturnProperRatioForSameNormalizedAndUpperCased() {
        assertEquals(0.095, urlSimilarityCounter.countSimilarityRatio("one.com", "http://ONE.com"), PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForSameNormalized() {
        assertEquals(0.1, urlSimilarityCounter.countSimilarityRatio("www.one.com", "https://one.com"), PRECISION);
    }
}
