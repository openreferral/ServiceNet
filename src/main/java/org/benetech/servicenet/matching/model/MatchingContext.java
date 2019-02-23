package org.benetech.servicenet.matching.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.benetech.servicenet.matching.counter.GeoApi;

@Data
@AllArgsConstructor
public class MatchingContext {

    private GeoApi geoApi;

    public MatchingContext(String googleApiKey) {
        this.geoApi = new GeoApi(googleApiKey);
    }
}
