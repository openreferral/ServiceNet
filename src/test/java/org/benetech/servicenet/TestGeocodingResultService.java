package org.benetech.servicenet;

import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.service.impl.GeocodingResultServiceImpl;
import org.benetech.servicenet.service.mapper.GeocodingResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class used only for tests, with some methods mocked
 */
@Service
@Transactional
public class TestGeocodingResultService extends GeocodingResultServiceImpl {

    public TestGeocodingResultService(GeocodingResultRepository geocodingResultRepository,
                                      GeocodingResultMapper geocodingResultMapper) {
        super(geocodingResultRepository, geocodingResultMapper);
    }

    @Override
    public Optional<GeocodingResult> getGeocodeForLocationIfUnique(Location location, MatchingContext context) {
        return Optional.empty();
    }
}
