package org.benetech.servicenet.repository;

import org.benetech.servicenet.domain.DataImportReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


/**
 * Spring Data  repository for the DataImportReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataImportReportRepository extends JpaRepository<DataImportReport, UUID> {

    DataImportReport findFirstByJobNameOrderByEndDateDesc(String jobName);
}
