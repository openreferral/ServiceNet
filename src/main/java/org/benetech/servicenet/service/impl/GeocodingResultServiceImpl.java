package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.dto.GeocodingResultDTO;
import org.benetech.servicenet.service.mapper.GeocodingResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
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
        geocodingResult = geocodingResultRepository.save(geocodingResult);
        return geocodingResultMapper.toDto(geocodingResult);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GeocodingResultDTO> findAll() {
        log.debug("Request to get all GeocodingResults");
        return geocodingResultRepository.findAll().stream()
            .map(geocodingResultMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
}
