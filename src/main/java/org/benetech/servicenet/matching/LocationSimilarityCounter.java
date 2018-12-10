package org.benetech.servicenet.matching;

import org.benetech.servicenet.domain.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LocationSimilarityCounter extends AbstractSimilarityCounter<Location> {

    @Value("${similarity-ratio.weight.location}")
    private float ratio;

    @Override
    public float countSimilarityRatio(Location location1, Location location2) {
        float result = 1;
        //TODO: implement logic
        return result * ratio;
    }
}
