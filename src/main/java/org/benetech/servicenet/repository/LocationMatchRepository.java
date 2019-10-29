package org.benetech.servicenet.repository;

import java.util.Optional;
import java.util.Set;
import org.benetech.servicenet.domain.LocationMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the LocationMatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationMatchRepository extends JpaRepository<LocationMatch, UUID> {
    Set<LocationMatch> findByLocationId(UUID locationId);

    Optional<LocationMatch> findByLocationIdAndMatchingLocationId(UUID locationId, UUID matchingLocationId);

    void deleteByLocationIdAndMatchingLocationId(UUID locationId, UUID matchingLocationId);
}
