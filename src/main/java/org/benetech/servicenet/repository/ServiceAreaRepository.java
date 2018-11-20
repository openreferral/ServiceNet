package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.ServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceArea, Long> {

}
