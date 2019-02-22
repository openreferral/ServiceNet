package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.GeocodingResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the GeocodingResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeocodingResultRepository extends JpaRepository<GeocodingResult, UUID> {

}
