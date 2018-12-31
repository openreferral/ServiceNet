package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.DataImportReportDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing DataImportReport.
 */
public interface DataImportReportService {

    /**
     * Save a dataImportReport.
     *
     * @param dataImportReportDTO the entity to save
     * @return the persisted entity
     */
    DataImportReportDTO save(DataImportReportDTO dataImportReportDTO);

    /**
     * Get all the dataImportReports.
     *
     * @return the list of entities
     */
    List<DataImportReportDTO> findAll();


    /**
     * Get the "id" dataImportReport.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DataImportReportDTO> findOne(UUID id);

    /**
     * Delete the "id" dataImportReport.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
