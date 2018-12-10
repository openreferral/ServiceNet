package org.benetech.servicenet.matching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DescriptionSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Value("${similarity-ratio.weight.description}")
    private float ratio;

    @Override
    public float countSimilarityRatio(String description1, String description2) {
        float result = 1;
        //TODO: implement logic
        return result * ratio;
    }
}
