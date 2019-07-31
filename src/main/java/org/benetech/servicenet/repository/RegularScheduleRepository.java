package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.RegularSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the RegularSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegularScheduleRepository extends JpaRepository<RegularSchedule, UUID> {

    @Query("SELECT schedule FROM RegularSchedule schedule WHERE schedule.srvc.id = :serviceId")
    Optional<RegularSchedule> findOneByServiceId(@Param("serviceId") UUID serviceId);

    @Query("SELECT schedule FROM RegularSchedule schedule WHERE schedule.location.id = :locationId")
    Optional<RegularSchedule> findOneByLocationId(@Param("locationId") UUID locationId);

    Page<RegularSchedule> findAll(Pageable pageable);
}
