package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.HolidaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HolidaySchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayScheduleRepository extends JpaRepository<HolidaySchedule, Long> {

}
