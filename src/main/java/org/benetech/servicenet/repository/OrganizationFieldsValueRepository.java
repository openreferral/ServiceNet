package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.OrganizationFieldsValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrganizationFieldsValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationFieldsValueRepository extends
    JpaRepository<OrganizationFieldsValue, UUID> {

}
