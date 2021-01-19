package org.benetech.servicenet.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.benetech.servicenet.domain.LocationMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the LocationMatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationMatchRepository extends JpaRepository<LocationMatch, UUID>, LocationMatchRepositoryCustom {
    Set<LocationMatch> findByLocationId(UUID locationId);

    @Query("SELECT locationMatch FROM LocationMatch locationMatch "
        + "WHERE locationMatch.location.id = :locationId AND locationMatch.matchingLocation.id = :matchingLocationId")
    Optional<LocationMatch> findByLocationIdAndMatchingLocationId(@Param("locationId") UUID locationId,
        @Param("matchingLocationId") UUID matchingLocationId);

    boolean existsByLocationIdAndMatchingLocationId(UUID locationId, UUID matchingLocationId);

    void deleteByLocationIdAndMatchingLocationId(UUID locationId, UUID matchingLocationId);

    List<LocationMatch> findAllByLocationIdAndMatchingLocationId(UUID locationId, UUID matchingLocationId);
}
