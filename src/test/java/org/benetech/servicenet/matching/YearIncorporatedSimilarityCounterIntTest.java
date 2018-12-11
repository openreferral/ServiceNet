package org.benetech.servicenet.matching;

import org.benetech.servicenet.ServiceNetApp;
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

    private static final float PRECISION = 0.001f;

    @Autowired
    private YearIncorporatedSimilarityCounter yearIncorporatedSimilarityCounter;

    @Test
    public void shouldReturnMinRatioForDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 11, 21),
            LocalDate.of(2011, 12, 1)), PRECISION);
    }

    @Test
    public void shouldReturnProperRatioForSameYear() {
        assertEquals(0.08, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 12, 21),
            LocalDate.of(2012, 1, 21)), PRECISION);
    }

    @Test
    public void shouldReturnMinRatioForSameMonthButDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 12, 23),
            LocalDate.of(2011, 12, 1)), PRECISION);
    }

    @Test
    public void shouldReturnProperRatioForSameMonth() {
        assertEquals(0.32, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2011, 12, 1),
            LocalDate.of(2011, 12, 21)), PRECISION);
    }

    @Test
    public void shouldReturnMaxRatioForSameDay() {
        assertEquals(0.4, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2011, 12, 21),
            LocalDate.of(2011, 12, 21)), PRECISION);
    }

    @Test
    public void shouldReturnProperRatioForSameDayButDifferentMonths() {
        assertEquals(0.08, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 11, 1),
            LocalDate.of(2012, 12, 1)), PRECISION);
    }

    @Test
    public void shouldReturnMinRatioForSameDayButDifferentYears() {
        assertEquals(0, yearIncorporatedSimilarityCounter.countSimilarityRatio(
            LocalDate.of(2012, 1, 1),
            LocalDate.of(2011, 1, 1)), PRECISION);
    }
}
