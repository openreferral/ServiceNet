package org.benetech.servicenet.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActivityFilterServiceImpl implements ActivityFilterService {

    private final GeocodingResultRepository geocodingResultRepository;

    private final UserService userService;

    private final TaxonomyRepository taxonomyRepository;

    public ActivityFilterServiceImpl(GeocodingResultRepository geocodingResultRepository,
        UserService userService,
        TaxonomyRepository taxonomyRepository) {
        this.geocodingResultRepository = geocodingResultRepository;
        this.userService = userService;
        this.taxonomyRepository = taxonomyRepository;
    }

    @Override
    public Set<String> getPostalCodes() {
        return geocodingResultRepository.getDistinctPostalCodesFromGeoResults();
    }

    @Override
    public Set<String> getRegions() {
        return geocodingResultRepository.getDistinctRegionsFromGeoResults();
    }

    @Override
    public Set<String> getCities() {
        return geocodingResultRepository.getDistinctCityFromGeoResults();
    }

    @Override
    public Set<String> getTaxonomies() {
        return taxonomyRepository.findAll().stream().map(Taxonomy::getName).collect(Collectors.toSet());
    }
}
