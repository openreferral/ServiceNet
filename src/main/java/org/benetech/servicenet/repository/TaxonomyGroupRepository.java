package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.TaxonomyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the TaxonomyGroup entity.
 */
@Repository
public interface TaxonomyGroupRepository extends JpaRepository<TaxonomyGroup, UUID> {

    @Query(value = "select distinct taxonomyGroup from TaxonomyGroup taxonomyGroup left join fetch taxonomyGroup.taxonomies",
        countQuery = "select count(distinct taxonomyGroup) from TaxonomyGroup taxonomyGroup")
    Page<TaxonomyGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct taxonomyGroup from TaxonomyGroup taxonomyGroup left join fetch taxonomyGroup.taxonomies")
    List<TaxonomyGroup> findAllWithEagerRelationships();

    @Query("select taxonomyGroup from TaxonomyGroup taxonomyGroup "
        + "left join fetch taxonomyGroup.taxonomies where taxonomyGroup.id =:id")
    Optional<TaxonomyGroup> findOneWithEagerRelationships(@Param("id") UUID id);

}
