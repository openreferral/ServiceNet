package org.benetech.servicenet.matching;

import java.math.BigDecimal;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.matching.counter.YearIncorporatedSimilarityCounter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class YearIncorporatedSimilarityCounterIntTest {
    
    private static final int YEAR_2011 = 2011;
    private static final int YEAR_2012 = 2012;
    private static final int NOVEMBER = 11;
    private static final int DECEMBER = 12;
    private static final int DAY_21 = 21;
    private static final int DAY_23 = 23;
    private static final BigDecimal SIMILARITY_02 = BigDecimal.valueOf(0.2);
    private static final BigDecimal SIMILARITY_08 = BigDecimal.valueOf(0.8);

    @Autowired
    private YearIncorporatedSimilarityCounter yearIncorporatedSimilarityCounter;

    @Test
    public void shouldReturnMinRatioForDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(YEAR_2012, NOVEMBER, DAY_21),
            LocalDate.of(YEAR_2011, DECEMBER, 1)).compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnProperRatioForSameYear() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(YEAR_2012, DECEMBER, DAY_21),
            LocalDate.of(YEAR_2012, 1, DAY_21)).compareTo(SIMILARITY_02));
    }

    @Test
    public void shouldReturnMinRatioForSameMonthButDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(YEAR_2012, DECEMBER, DAY_23),
            LocalDate.of(YEAR_2011, DECEMBER, 1)).compareTo(BigDecimal.valueOf(0)));
    }

    @Test
    public void shouldReturnProperRatioForSameMonth() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(YEAR_2011, DECEMBER, 1),
            LocalDate.of(YEAR_2011, DECEMBER, DAY_21)).compareTo(SIMILARITY_08));
    }

    @Test
    public void shouldReturnMaxRatioForSameDay() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(YEAR_2011, DECEMBER, DAY_21),
            LocalDate.of(YEAR_2011, DECEMBER, DAY_21)).compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldReturnProperRatioForSameDayButDifferentMonths() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(YEAR_2012, NOVEMBER, 1),
            LocalDate.of(YEAR_2012, DECEMBER, 1)).compareTo(SIMILARITY_02));
    }

    @Test
    public void shouldReturnMinRatioForSameDayButDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(YEAR_2012, 1, 1),
            LocalDate.of(YEAR_2011, 1, 1)).compareTo(BigDecimal.ZERO));
    }
}
