package org.benetech.servicenet.matching;

import org.benetech.servicenet.domain.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationSimilarityCounter extends AbstractSimilarityCounter<Location> {

    @Override
    public float countSimilarityRatio(Location location1, Location location2) {
        //TODO: implement logic
        return 1.0f;
    }
}
