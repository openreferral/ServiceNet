package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.OrganizationError;
import org.benetech.servicenet.repository.DataImportReportRepository;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.DataImportReportDTO;
import org.benetech.servicenet.service.mapper.DataImportReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing DataImportReport.
 */
@Service
@Transactional
public class DataImportReportServiceImpl implements DataImportReportService {

    private final Logger log = LoggerFactory.getLogger(DataImportReportServiceImpl.class);

    private final DataImportReportRepository dataImportReportRepository;

    private final DataImportReportMapper dataImportReportMapper;

    private final OrganizationService organizationService;

    public DataImportReportServiceImpl(DataImportReportRepository dataImportReportRepository,
        DataImportReportMapper dataImportReportMapper, OrganizationService organizationService) {
        this.dataImportReportRepository = dataImportReportRepository;
        this.dataImportReportMapper = dataImportReportMapper;
        this.organizationService = organizationService;
    }

    /**
     * Save a dataImportReport.
     *
     * @param dataImportReportDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DataImportReportDTO save(DataImportReportDTO dataImportReportDTO) {
        log.debug("Request to save DataImportReport : {}", dataImportReportDTO);

        DataImportReport dataImportReport = dataImportReportMapper.toEntity(dataImportReportDTO);
        dataImportReport = save(dataImportReport);
        return dataImportReportMapper.toDto(dataImportReport);
    }

    @Override
    public DataImportReport findLatestByJobName(String name) {
        return dataImportReportRepository.findFirstByJobNameOrderByEndDateDesc(name);
    }

    /**
     * Save a dataImportReport.
     *
     * @param dataImportReport the entity to save
     * @return the persisted entity
     */
    @Override
    public DataImportReport save(DataImportReport dataImportReport) {
        log.debug("Request to save DataImportReport : {}", dataImportReport);
        for (OrganizationError organizationError : dataImportReport.getOrganizationErrors()) {
            organizationService.save(organizationError.getOrganization());
        }

        return dataImportReportRepository.save(dataImportReport);
    }

    /**
     * Get all the dataImportReports.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataImportReportDTO> findAll() {
        log.debug("Request to get all DataImportReports");
        return dataImportReportRepository.findAll().stream()
            .map(dataImportReportMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one dataImportReport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DataImportReportDTO> findOne(UUID id) {
        log.debug("Request to get DataImportReport : {}", id);
        return dataImportReportRepository.findById(id)
            .map(dataImportReportMapper::toDto);
    }

    /**
     * Delete the dataImportReport by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete DataImportReport : {}", id);
        dataImportReportRepository.deleteById(id);
    }
}
