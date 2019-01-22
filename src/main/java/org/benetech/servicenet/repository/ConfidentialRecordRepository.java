package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.ConfidentialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the ConfidentialRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfidentialRecordRepository extends JpaRepository<ConfidentialRecord, UUID> {

}
