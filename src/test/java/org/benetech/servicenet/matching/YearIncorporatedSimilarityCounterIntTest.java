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

    @Autowired
    private YearIncorporatedSimilarityCounter yearIncorporatedSimilarityCounter;

    @Test
    public void shouldReturnMinRatioForDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 11, 21),
            LocalDate.of(2011, 12, 1)).compareTo(BigDecimal.ZERO));
    }

    @Test
    public void shouldReturnProperRatioForSameYear() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 12, 21),
            LocalDate.of(2012, 1, 21)).compareTo(BigDecimal.valueOf(0.2)));
    }

    @Test
    public void shouldReturnMinRatioForSameMonthButDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 12, 23),
            LocalDate.of(2011, 12, 1)).compareTo(BigDecimal.valueOf(0)));
    }

    @Test
    public void shouldReturnProperRatioForSameMonth() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2011, 12, 1),
            LocalDate.of(2011, 12, 21)).compareTo(BigDecimal.valueOf(0.8)));
    }

    @Test
    public void shouldReturnMaxRatioForSameDay() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2011, 12, 21),
            LocalDate.of(2011, 12, 21)).compareTo(BigDecimal.ONE));
    }

    @Test
    public void shouldReturnProperRatioForSameDayButDifferentMonths() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 11, 1),
            LocalDate.of(2012, 12, 1)).compareTo(BigDecimal.valueOf(0.2)));
    }

    @Test
    public void shouldReturnMinRatioForSameDayButDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 1, 1),
            LocalDate.of(2011, 1, 1)).compareTo(BigDecimal.ZERO));
    }
}
