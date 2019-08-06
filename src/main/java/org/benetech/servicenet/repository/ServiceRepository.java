package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the Service entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {

    @Query("SELECT srvc FROM Service srvc " +
        "LEFT JOIN FETCH srvc.contacts " +
        "LEFT JOIN FETCH srvc.phones " +
        "LEFT JOIN FETCH srvc.regularSchedule schedule " +
        "LEFT JOIN FETCH schedule.openingHours " +
        "WHERE srvc.externalDbId = :extId AND srvc.providerName = :providerName")
    Optional<Service> findOneWithEagerAssociationsByExternalDbIdAndProviderName(@Param("extId") String externalDbId,
                                                                                @Param("providerName") String providerName);

    Page<Service> findAll(Pageable pageable);
}
