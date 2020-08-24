package org.benetech.servicenet.repository;

import java.util.List;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data  repository for the ExclusionsConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExclusionsConfigRepository extends JpaRepository<ExclusionsConfig, UUID> {

    List<ExclusionsConfig> findAllByAccountName(String accountName);
}
