package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.Silo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Spring Data  repository for the Silo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiloRepository extends JpaRepository<Silo, UUID> {

    Optional<Silo> getByName(String name);

    Optional<Silo> getByNameOrId(String name, UUID id);
}
