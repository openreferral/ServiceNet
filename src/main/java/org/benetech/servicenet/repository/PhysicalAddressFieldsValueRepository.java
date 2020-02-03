package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.PhysicalAddressFieldsValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PhysicalAddressFieldsValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhysicalAddressFieldsValueRepository extends
    JpaRepository<PhysicalAddressFieldsValue, UUID> {

}
