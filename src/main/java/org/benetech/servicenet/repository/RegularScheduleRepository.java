package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.RegularSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the RegularSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegularScheduleRepository extends JpaRepository<RegularSchedule, UUID> {

}
