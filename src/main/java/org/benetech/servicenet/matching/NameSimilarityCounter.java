package org.benetech.servicenet.matching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NameSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Value("${similarity-ratio.weight.name}")
    private float ratio;

    @Override
    public float countSimilarityRatio(String name1, String name2) {
        float result = 1;
        //TODO: implement logic
        return result * ratio;
    }
}
