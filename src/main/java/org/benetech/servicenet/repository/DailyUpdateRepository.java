package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.DailyUpdate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DailyUpdate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyUpdateRepository extends JpaRepository<DailyUpdate, UUID> {
}
