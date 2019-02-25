package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.dto.GeocodingResultDTO;
import org.benetech.servicenet.service.mapper.GeocodingResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class GeocodingResultServiceImpl implements GeocodingResultService {

    private final GeocodingResultRepository geocodingResultRepository;

    private final GeocodingResultMapper geocodingResultMapper;

    public GeocodingResultServiceImpl(GeocodingResultRepository geocodingResultRepository,
                                      GeocodingResultMapper geocodingResultMapper) {
        this.geocodingResultRepository = geocodingResultRepository;
        this.geocodingResultMapper = geocodingResultMapper;
    }

    @Override
    public GeocodingResultDTO save(GeocodingResultDTO geocodingResultDTO) {
        log.debug("Request to save GeocodingResult : {}", geocodingResultDTO);
        GeocodingResult geocodingResult = geocodingResultMapper.toEntity(geocodingResultDTO);
        geocodingResult = save(geocodingResult);
        return geocodingResultMapper.toDto(geocodingResult);
    }

    @Override
    public GeocodingResult save(GeocodingResult geocodingResult) {
        return geocodingResultRepository.save(geocodingResult);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GeocodingResultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GeocodingResults");
        return geocodingResultRepository.findAll(pageable)
            .map(geocodingResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GeocodingResultDTO> findOne(UUID id) {
        log.debug("Request to get GeocodingResult : {}", id);
        return geocodingResultRepository.findById(id)
            .map(geocodingResultMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete GeocodingResult : {}", id);
        geocodingResultRepository.deleteById(id);
    }

    @Override
    public Optional<GeocodingResult> getGeocodeForLocationIfUnique(Location location, MatchingContext context) {
        List<GeocodingResult> result = createOrUpdateGeocodingResult(location, context);
        if (result.size() == 1) {
            return Optional.of(result.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<GeocodingResult> findAllForLocationOrFetchIfEmpty(Location location, MatchingContext context) {
        String addressString = context.getGeoApi().extract255AddressChars(location.getPhysicalAddress());
        List<GeocodingResult> result = geocodingResultRepository.findAllByAddress(addressString);
        if (result.isEmpty()) {
            return createOrUpdateGeocodingResult(location, context);
        }
        return result;
    }

    private List<GeocodingResult> createOrUpdateGeocodingResult(Location location, MatchingContext context) {
        if (location.getPhysicalAddress() == null) {
            return new ArrayList<>();
        }

        String addressString = context.getGeoApi().extract255AddressChars(location.getPhysicalAddress());
        List<GeocodingResult> currentResults = geocodingResultRepository.findAllByAddress(addressString);
        if (!currentResults.isEmpty()) {
            return currentResults;
        }
        return Arrays.stream(context.getGeoApi().geocode(location))
            .map(x -> save(new GeocodingResult(addressString, x)))
            .collect(Collectors.toList());
    }
}
