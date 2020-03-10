package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.RequestLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RequestLogger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestLoggerRepository extends JpaRepository<RequestLogger, UUID> {

}
