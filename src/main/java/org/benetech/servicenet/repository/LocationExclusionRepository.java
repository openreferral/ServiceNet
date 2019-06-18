package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.LocationExclusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the LocationExclusion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationExclusionRepository extends JpaRepository<LocationExclusion, UUID> {

}
