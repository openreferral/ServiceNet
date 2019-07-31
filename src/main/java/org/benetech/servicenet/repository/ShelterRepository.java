package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Shelter entity.
 */
@SuppressWarnings("unused")
public interface ShelterRepository extends JpaRepository<Shelter, UUID>, ShelterRepositoryCustom {

    @Query(value = "select distinct shelter from Shelter shelter left join fetch shelter.tags",
        countQuery = "select count(distinct shelter) from Shelter shelter")
    Page<Shelter> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct shelter from Shelter shelter left join fetch shelter.tags")
    List<Shelter> findAllWithEagerRelationships();

    @Query("select shelter from Shelter shelter left join fetch shelter.tags where shelter.id =:id")
    Optional<Shelter> findOneWithEagerRelationships(@Param("id") Long id);

    Page<Shelter> findAll(Pageable pageable);
}
