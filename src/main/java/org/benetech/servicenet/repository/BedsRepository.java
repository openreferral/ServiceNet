package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.Beds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Beds entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BedsRepository extends JpaRepository<Beds, UUID> {

}
