package org.benetech.servicenet.repository;

import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.DataImportReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataImportReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataImportReportRepository extends JpaRepository<DataImportReport, UUID> {

    DataImportReport findFirstByJobNameOrderByEndDateDesc(String jobName);

    Page<DataImportReport> findAll(Pageable pageable);

    Optional<DataImportReport> findFirstBySystemAccountOrderByEndDateDesc(String systemAccountName);
}
