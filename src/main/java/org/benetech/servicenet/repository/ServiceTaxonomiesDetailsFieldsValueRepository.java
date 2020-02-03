package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.ServiceTaxonomiesDetailsFieldsValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceTaxonomiesDetailsFieldsValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceTaxonomiesDetailsFieldsValueRepository extends
    JpaRepository<ServiceTaxonomiesDetailsFieldsValue, UUID> {
}
