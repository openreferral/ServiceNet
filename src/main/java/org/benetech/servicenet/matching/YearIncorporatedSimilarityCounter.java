package org.benetech.servicenet.matching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class YearIncorporatedSimilarityCounter extends AbstractSimilarityCounter<LocalDate> {

    @Value("${similarity-ratio.weight.year-incorporated}")
    private float ratio;

    @Override
    public float countSimilarityRatio(LocalDate date1, LocalDate date2) {
        float result = 1;
        //TODO: implement logic
        return result * ratio;
    }
}
