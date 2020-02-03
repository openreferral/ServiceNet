package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.ServiceFieldsValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceFieldsValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceFieldsValueRepository extends JpaRepository<ServiceFieldsValue, UUID> {

}
