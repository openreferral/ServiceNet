package org.benetech.servicenet.matching;

import org.springframework.stereotype.Component;

@Component
public class DescriptionSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Override
    public float countSimilarityRatio(String description1, String description2) {
        //TODO: implement logic
        return 1.0f;
    }
}
