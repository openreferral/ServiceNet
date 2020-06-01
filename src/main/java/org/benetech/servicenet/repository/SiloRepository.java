package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.Silo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Silo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiloRepository extends JpaRepository<Silo, UUID> {

}
