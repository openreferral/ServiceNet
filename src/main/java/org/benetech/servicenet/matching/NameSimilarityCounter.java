package org.benetech.servicenet.matching;

import org.springframework.stereotype.Component;

@Component
public class NameSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Override
    public float countSimilarityRatio(String name1, String name2) {
        //TODO: implement logic
        return 1.0f;
    }
}
