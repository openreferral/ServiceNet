package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.dto.ExclusionsConfigDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * REST controller for managing {@link org.benetech.servicenet.domain.ExclusionsConfig}.
 */
@RestController
@RequestMapping("/api")
public class ExclusionsConfigResource {

    private final Logger log = LoggerFactory.getLogger(ExclusionsConfigResource.class);

    private static final String ENTITY_NAME = "exclusionsConfig";

    private final ExclusionsConfigService exclusionsConfigService;

    public ExclusionsConfigResource(ExclusionsConfigService exclusionsConfigService) {
        this.exclusionsConfigService = exclusionsConfigService;
    }

    /**
     * {@code POST  /exclusions-configs} : Create a new exclusionsConfig.
     *
     * @param exclusionsConfigDTO the exclusionsConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exclusionsConfigDTO,
     * or with status {@code 400 (Bad Request)} if the exclusionsConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/exclusions-configs")
    @Timed
    public ResponseEntity<ExclusionsConfigDTO> createExclusionsConfig(
        @Valid @RequestBody ExclusionsConfigDTO exclusionsConfigDTO) throws URISyntaxException {
        log.debug("REST request to save ExclusionsConfig : {}", exclusionsConfigDTO);
        if (exclusionsConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new exclusionsConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExclusionsConfigDTO result = exclusionsConfigService.save(exclusionsConfigDTO);
        return ResponseEntity.created(new URI("/api/exclusions-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exclusions-configs} : Updates an existing exclusionsConfig.
     *
     * @param exclusionsConfigDTO the exclusionsConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exclusionsConfigDTO,
     * or with status {@code 400 (Bad Request)} if the exclusionsConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exclusionsConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/exclusions-configs")
    @Timed
    public ResponseEntity<ExclusionsConfigDTO> updateExclusionsConfig(
        @Valid @RequestBody ExclusionsConfigDTO exclusionsConfigDTO) throws URISyntaxException {
        log.debug("REST request to update ExclusionsConfig : {}", exclusionsConfigDTO);
        if (exclusionsConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExclusionsConfigDTO result = exclusionsConfigService.save(exclusionsConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, exclusionsConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exclusions-configs} : get all the exclusionsConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exclusionsConfigs in body.
     */
    @GetMapping("/exclusions-configs")
    @Timed
    public List<ExclusionsConfigDTO> getAllExclusionsConfigs() {
        log.debug("REST request to get all ExclusionsConfigs");
        return exclusionsConfigService.findAll();
    }

    /**
     * {@code GET  /exclusions-configs/:id} : get the "id" exclusionsConfig.
     *
     * @param id the id of the exclusionsConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exclusionsConfigDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exclusions-configs/{id}")
    @Timed
    public ResponseEntity<ExclusionsConfigDTO> getExclusionsConfig(@PathVariable UUID id) {
        log.debug("REST request to get ExclusionsConfig : {}", id);
        Optional<ExclusionsConfigDTO> exclusionsConfigDTO = exclusionsConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exclusionsConfigDTO);
    }

    /**
     * {@code DELETE  /exclusions-configs/:id} : delete the "id" exclusionsConfig.
     *
     * @param id the id of the exclusionsConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/exclusions-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteExclusionsConfig(@PathVariable UUID id) {
        log.debug("REST request to delete ExclusionsConfig : {}", id);
        exclusionsConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
