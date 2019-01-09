package org.benetech.servicenet.matching;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.matching.counter.EmailSimilarityCounter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class EmailSimilarityCounterIntTest {

    private static final float PRECISION = 0.001f;

    @Autowired
    private EmailSimilarityCounter emailSimilarityCounter;

    @Test
    public void shouldReturnMinRatioForDifferentDomains() {
        assertEquals(0, emailSimilarityCounter.countSimilarityRatio("one@email.com", "one@email.org"), PRECISION);
    }

    @Test
    public void shouldReturnProperRatioForSameDomain() {
        assertEquals(0.01, emailSimilarityCounter.countSimilarityRatio("one@email.com", "two@email.com"), PRECISION);
    }

    @Test
    public void shouldReturnProperRatioForSameDomainButUpperCase() {
        assertEquals(0.01, emailSimilarityCounter.countSimilarityRatio("one@email.com", "two@EMAIL.COM"), PRECISION);
    }

    @Test
    public void shouldReturnProperRatioForSameNormalizedLocalPart() {
        assertEquals(0.9, emailSimilarityCounter.countSimilarityRatio("One@email.com", "onE@email.com"), PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForSameLocalPart() {
        assertEquals(1.0, emailSimilarityCounter.countSimilarityRatio("one@email.com", "one@email.com"), PRECISION);
    }
}
