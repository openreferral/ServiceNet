package org.benetech.servicenet;

import org.benetech.servicenet.domain.Address;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.service.impl.GeocodingResultServiceImpl;
import org.benetech.servicenet.service.mapper.GeocodingResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    public List<GeocodingResult> createOrUpdateGeocodingResult(Address address, MatchingContext context) {
        return new ArrayList<>();
    }
}
