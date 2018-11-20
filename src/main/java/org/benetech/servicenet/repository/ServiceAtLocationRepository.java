package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.ServiceAtLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceAtLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceAtLocationRepository extends JpaRepository<ServiceAtLocation, Long> {

}
