package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    @Query("SELECT loc FROM Location loc " +
        "LEFT JOIN FETCH loc.accessibilities " +
        "LEFT JOIN FETCH loc.langs " +
        "LEFT JOIN FETCH loc.phones " +
        "LEFT JOIN FETCH loc.regularSchedule schedule " +
        "LEFT JOIN FETCH schedule.openingHours " +
        "WHERE loc.externalDbId = :extId AND loc.providerName = :providerName")
    Optional<Location> findOneWithEagerAssociationsByExternalDbIdAndProviderName(@Param("extId") String externalDbId,
                                                                                 @Param("providerName") String providerName);

    Page<Location> findAll(Pageable pageable);
}
