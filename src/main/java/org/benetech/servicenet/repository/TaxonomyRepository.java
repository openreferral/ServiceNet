package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.Taxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Taxonomy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxonomyRepository extends JpaRepository<Taxonomy, Long> {

}
