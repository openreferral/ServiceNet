package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.FieldExclusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;


/**
 * Spring Data  repository for the FieldExclusion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FieldExclusionRepository extends JpaRepository<FieldExclusion, UUID> {

    Set<FieldExclusion> findAllByConfigId(UUID configId);
}
