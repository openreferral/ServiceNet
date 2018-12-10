package org.benetech.servicenet.matching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Value("${similarity-ratio.weight.email}")
    private float ratio;

    @Override
    public float countSimilarityRatio(String email1, String email2) {
        float result = 1;
        //TODO: implement logic
        return result * ratio;
    }
}
