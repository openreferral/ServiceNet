package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.dto.DataImportReportDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing DataImportReport.
 */
@RestController
@RequestMapping("/api")
public class DataImportReportResource {

    private final Logger log = LoggerFactory.getLogger(DataImportReportResource.class);

    private static final String ENTITY_NAME = "dataImportReport";

    private final DataImportReportService dataImportReportService;

    public DataImportReportResource(DataImportReportService dataImportReportService) {
        this.dataImportReportService = dataImportReportService;
    }

    /**
     * POST  /data-import-reports : Create a new dataImportReport.
     *
     * @param dataImportReportDTO the dataImportReportDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataImportReportDTO,
     * or with status 400 (Bad Request) if the dataImportReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/data-import-reports")
    @Timed
    public ResponseEntity<DataImportReportDTO> createDataImportReport(
        @Valid @RequestBody DataImportReportDTO dataImportReportDTO) throws URISyntaxException {
        log.debug("REST request to save DataImportReport : {}", dataImportReportDTO);
        if (dataImportReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new dataImportReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataImportReportDTO result = dataImportReportService.save(dataImportReportDTO);
        return ResponseEntity.created(new URI("/api/data-import-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /data-import-reports : Updates an existing dataImportReport.
     *
     * @param dataImportReportDTO the dataImportReportDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dataImportReportDTO,
     * or with status 400 (Bad Request) if the dataImportReportDTO is not valid,
     * or with status 500 (Internal Server Error) if the dataImportReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/data-import-reports")
    @Timed
    public ResponseEntity<DataImportReportDTO> updateDataImportReport(
        @Valid @RequestBody DataImportReportDTO dataImportReportDTO) throws URISyntaxException {
        log.debug("REST request to update DataImportReport : {}", dataImportReportDTO);
        if (dataImportReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataImportReportDTO result = dataImportReportService.save(dataImportReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dataImportReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /data-import-reports : get all the dataImportReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dataImportReports in body
     */
    @GetMapping("/data-import-reports")
    @Timed
    public List<DataImportReportDTO> getAllDataImportReports() {
        log.debug("REST request to get all DataImportReports");
        return dataImportReportService.findAll();
    }

    /**
     * GET  /data-import-reports/:id : get the "id" dataImportReport.
     *
     * @param id the id of the dataImportReportDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dataImportReportDTO, or with status 404 (Not Found)
     */
    @GetMapping("/data-import-reports/{id}")
    @Timed
    public ResponseEntity<DataImportReportDTO> getDataImportReport(@PathVariable UUID id) {
        log.debug("REST request to get DataImportReport : {}", id);
        Optional<DataImportReportDTO> dataImportReportDTO = dataImportReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dataImportReportDTO);
    }

    /**
     * DELETE  /data-import-reports/:id : delete the "id" dataImportReport.
     *
     * @param id the id of the dataImportReportDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/data-import-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteDataImportReport(@PathVariable UUID id) {
        log.debug("REST request to delete DataImportReport : {}", id);
        dataImportReportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
