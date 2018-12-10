package org.benetech.servicenet.matching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Value("${similarity-ratio.weight.url}")
    private float ratio;

    @Override
    public float countSimilarityRatio(String url1, String ulr2) {
        float result = 1;
        //TODO: implement logic
        return result * ratio;
    }
}
