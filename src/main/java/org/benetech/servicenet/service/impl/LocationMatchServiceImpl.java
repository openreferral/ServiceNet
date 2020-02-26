package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.repository.LocationMatchRepository;
import org.benetech.servicenet.service.LocationMatchService;
import org.benetech.servicenet.service.dto.LocationMatchDto;
import org.benetech.servicenet.service.mapper.LocationMatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocationMatchServiceImpl implements LocationMatchService {

    private final Logger log = LoggerFactory.getLogger(LocationMatchServiceImpl.class);

    @Autowired
    private LocationMatchRepository locationMatchRepository;

    @Autowired
    private LocationMatchMapper mapper;

    @Override
    public LocationMatchDto save(LocationMatchDto locationMatchDto) {
        Optional<LocationMatch> existingMatch = locationMatchRepository.findByLocationIdAndMatchingLocationId(
            locationMatchDto.getLocation(), locationMatchDto.getMatchingLocation());
        if (existingMatch.isEmpty()) {
            log.debug("Request to save LocationMatch for location id : {} and matching location id: {}",
                locationMatchDto.getLocation(), locationMatchDto.getMatchingLocation());
            return mapper.toDto(locationMatchRepository.save(mapper.toEntity(locationMatchDto)));
        } else {
            return mapper.toDto(existingMatch.get());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationMatchDto> findAllDtoForLocation(UUID locationId) {
        log.debug("Request to get all LocationMatches for location id : {}", locationId);
        return locationMatchRepository.findByLocationId(locationId).stream()
            .map(mapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public LocationMatch saveOrUpdate(LocationMatch locationMatch) {
        Optional<LocationMatch> existingMatch = locationMatchRepository.findByLocationIdAndMatchingLocationId(
            locationMatch.getLocation().getId(), locationMatch.getMatchingLocation().getId());
        if (existingMatch.isEmpty()) {
            return locationMatchRepository.save(locationMatch);
        } else {
            return existingMatch.get();
        }
    }

    @Override
    public Set<LocationMatch> findAllForLocation(UUID locationId) {
        return locationMatchRepository.findByLocationId(locationId);
    }

    @Override
    public void delete(UUID locationId, UUID matchingLocationId) {
        Optional<LocationMatch> existingMatch = locationMatchRepository.findByLocationIdAndMatchingLocationId(
            locationId, matchingLocationId);
        if (existingMatch.isPresent()) {
            log.debug("Request to delete LocationMatch : {}", existingMatch.get().getId());
            locationMatchRepository.delete(existingMatch.get());
        }
    }

    @Override
    public List<LocationMatch> findAll() {
        return locationMatchRepository.findAll();
    }
}
