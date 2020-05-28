package org.benetech.servicenet.matching;

import java.math.BigDecimal;
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

    private static final BigDecimal POINT_NINE = BigDecimal.valueOf(0.9);

    private static final BigDecimal POINT_ZERO_ONE = BigDecimal.valueOf(0.01);

    @Autowired
    private EmailSimilarityCounter emailSimilarityCounter;

    @Test
    public void shouldReturnMinRatioForDifferentDomains() {
        assertEquals(0, emailSimilarityCounter.countSimilarityRatio("one@email.com", "one@email.org")
            .compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnProperRatioForSameDomain() {
        assertEquals(0, emailSimilarityCounter.countSimilarityRatio("one@email.com", "two@email.com")
            .compareTo(POINT_ZERO_ONE));
    }

    @Test
    public void shouldReturnProperRatioForSameDomainButUpperCase() {
        assertEquals(0, emailSimilarityCounter.countSimilarityRatio("one@email.com", "two@EMAIL.COM")
            .compareTo(POINT_ZERO_ONE));
    }

    @Test
    public void shouldReturnProperRatioForSameNormalizedLocalPart() {
        assertEquals(0, emailSimilarityCounter.countSimilarityRatio("One@email.com", "onE@email.com")
            .compareTo(POINT_NINE));
    }

    @Test
    public void shouldReturnMaxRatioForSameLocalPart() {
        assertEquals(0, emailSimilarityCounter.countSimilarityRatio("one@email.com", "one@email.com")
            .compareTo(BigDecimal.valueOf(1)));
    }

    @Test
    public void shouldReturnMaxRatioForSameLocalPartWithoutDomain() {
        assertEquals(0, emailSimilarityCounter.countSimilarityRatio("one", "one").compareTo(BigDecimal.valueOf(1)));
    }
}
