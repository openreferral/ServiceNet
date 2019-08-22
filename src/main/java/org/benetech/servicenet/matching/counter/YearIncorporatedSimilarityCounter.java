package org.benetech.servicenet.matching.counter;

import java.math.BigDecimal;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class YearIncorporatedSimilarityCounter extends AbstractSimilarityCounter<LocalDate> {

    @Value("${similarity-ratio.weight.year-incorporated.same-year}")
    private BigDecimal sameYearWeight;

    @Value("${similarity-ratio.weight.year-incorporated.same-month}")
    private BigDecimal sameMontWeight;

    @Override
    public BigDecimal countSimilarityRatio(LocalDate date1, LocalDate date2, MatchingContext context) {
        if (date1 == null || date2 == null) {
            return NO_MATCH_RATIO;
        }
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
