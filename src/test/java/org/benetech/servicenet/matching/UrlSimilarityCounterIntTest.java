package org.benetech.servicenet.matching;

import java.math.BigDecimal;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.matching.counter.UrlSimilarityCounter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class UrlSimilarityCounterIntTest {

    @Autowired
    private UrlSimilarityCounter urlSimilarityCounter;

    @Test
    public void shouldReturnMinRatioForDifferentNormalizedAndUpperCased() {
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("one.com", "two.com", null).compareTo(BigDecimal.valueOf(0)));
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio(null, "two.com", null).compareTo(BigDecimal.valueOf(0)));
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("http://google.com/rotacarebayarea.org/index.html ", "http://google.com/zuckerbergsanfranciscogeneral.org", null).compareTo(BigDecimal.valueOf(0)));
    }

    @Test
    public void shouldReturnMinRatioForDifferentNormalized() {
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("one.com", "http://ONE.com", null).compareTo(BigDecimal.valueOf(0.95)));
    }

    @Test
    public void shouldReturnMaxRatioForSameNormalized() {
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("www.one.com", "https://one.com", null).compareTo(BigDecimal.valueOf(1)));
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("http://www.one.com/ ", "https://one.com", null).compareTo(BigDecimal.valueOf(1)));
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("https:// zuckerbergsanfranciscogeneral.org/", "http://zuckerbergsanfranciscogeneral. org/", null).compareTo(BigDecimal.valueOf(1)));
        assertEquals(0, urlSimilarityCounter.countSimilarityRatio("http://rotacarebayarea.org/index.html ", "http://www.rotacarebayarea.org", null).compareTo(BigDecimal.valueOf(1)));
    }
}
