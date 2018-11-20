package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceTaxonomy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceTaxonomyRepository extends JpaRepository<ServiceTaxonomy, Long> {

}
