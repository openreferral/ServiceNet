package org.benetech.servicenet.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.repository.LocationMatchRepository;
import org.benetech.servicenet.service.LocationMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocationMatchServiceImpl implements LocationMatchService {

    @Autowired
    private LocationMatchRepository locationMatchRepository;

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
        locationMatchRepository.deleteByLocationIdAndMatchingLocationId(locationId, matchingLocationId);
    }
}
