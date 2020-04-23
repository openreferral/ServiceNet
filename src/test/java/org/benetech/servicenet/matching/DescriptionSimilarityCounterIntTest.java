package org.benetech.servicenet.matching;

import java.math.BigDecimal;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.matching.counter.DescriptionSimilarityCounter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class DescriptionSimilarityCounterIntTest {

    @Autowired
    private DescriptionSimilarityCounter descriptionSimilarityCounter;

    @Test
    public void shouldReturnMaxRatioForTheSameStringAfterNormalization() {
        String string1 = "   %@$#Aaa! Aa";
        String string2 = "%@$#a.aA aa  ";

        BigDecimal result = descriptionSimilarityCounter.countSimilarityRatio(string1, string2);
        assertEquals(0, result.compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldReturnMinRatioForStringWithNoCommonSubsequences() {
        String string1 = "AAAA";
        String string2 = "BBBB";

        BigDecimal result = descriptionSimilarityCounter.countSimilarityRatio(string1, string2);
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnLongestCommonSubsequenceLengthDividedByLengthOfLongerString() {
        String string1 = "AAAA";
        String string2 = "AA";

        BigDecimal result = descriptionSimilarityCounter.countSimilarityRatio(string1, string2);
        assertEquals(0, result.compareTo(BigDecimal.valueOf(0.5)));
    }
}
