package org.benetech.servicenet.matching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class YearIncorporatedSimilarityCounter extends AbstractSimilarityCounter<LocalDate> {

    @Value("${similarity-ratio.weight.year-incorporated.same-year}")
    private float sameYearWeight;

    @Value("${similarity-ratio.weight.year-incorporated.same-month}")
    private float sameMontWeight;

    @Override
    public float countSimilarityRatio(LocalDate date1, LocalDate date2) {
        if (areYearsDifferent(date1, date2)) {
            return NO_MATCH_RATIO;
        }

        if (areMonthsDifferent(date1, date2)) {
            return sameYearWeight;
        }

        if (areDaysDifferent(date1, date2)) {
            return sameMontWeight;
        }

        return COMPLETE_MATCH_RATIO;
    }

    private boolean areYearsDifferent(LocalDate date1, LocalDate date2) {
        return date1.getYear() != date2.getYear();
    }

    private boolean areMonthsDifferent(LocalDate date1, LocalDate date2) {
        return date1.getMonth() != date2.getMonth();
    }

    private boolean areDaysDifferent(LocalDate date1, LocalDate date2) {
        return date1.getDayOfYear() != date2.getDayOfYear();
    }
}
