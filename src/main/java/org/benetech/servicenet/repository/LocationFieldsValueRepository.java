package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.LocationFieldsValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LocationFieldsValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationFieldsValueRepository extends JpaRepository<LocationFieldsValue, UUID> {

}
