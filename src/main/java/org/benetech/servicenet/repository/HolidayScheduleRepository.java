package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.HolidaySchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data  repository for the HolidaySchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayScheduleRepository extends JpaRepository<HolidaySchedule, UUID> {

    Optional<HolidaySchedule> findOneByExternalDbIdAndProviderName(String externalDbId, String providerName);

    Page<HolidaySchedule> findAll(Pageable pageable);
}
