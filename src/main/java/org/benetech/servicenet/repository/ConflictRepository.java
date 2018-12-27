package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Conflict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data  repository for the Conflict entity.
 */
@Repository
public interface ConflictRepository extends JpaRepository<Conflict, UUID> {

    @Query(value = "select distinct conflict from Conflict conflict left join fetch conflict.acceptedThisChanges",
        countQuery = "select count(distinct conflict) from Conflict conflict")
    Page<Conflict> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct conflict from Conflict conflict left join fetch conflict.acceptedThisChanges")
    List<Conflict> findAllWithEagerRelationships();

    @Query("select conflict from Conflict conflict left join fetch conflict.acceptedThisChanges where conflict.id =:id")
    Optional<Conflict> findOneWithEagerRelationships(@Param("id") UUID id);

}
