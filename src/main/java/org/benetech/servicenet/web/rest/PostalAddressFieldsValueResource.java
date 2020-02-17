package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.service.PostalAddressFieldsValueService;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.dto.PostalAddressFieldsValueDTO;

import org.benetech.servicenet.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
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

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.PostalAddressFieldsValue}.
 */
@RestController
@RequestMapping("/api")
public class PostalAddressFieldsValueResource {

    private final Logger log = LoggerFactory.getLogger(PostalAddressFieldsValueResource.class);

    private static final String ENTITY_NAME = "postalAddressFieldsValue";

    private final PostalAddressFieldsValueService postalAddressFieldsValueService;

    public PostalAddressFieldsValueResource(PostalAddressFieldsValueService postalAddressFieldsValueService) {
        this.postalAddressFieldsValueService = postalAddressFieldsValueService;
    }

    /**
     * {@code POST  /postal-address-fields-values} : Create a new postalAddressFieldsValue.
     *
     * @param postalAddressFieldsValueDTO the postalAddressFieldsValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * postalAddressFieldsValueDTO, or with status {@code 400 (Bad Request)} if the postalAddressFieldsValue has already
     * an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/postal-address-fields-values")
    public ResponseEntity<PostalAddressFieldsValueDTO> createPostalAddressFieldsValue(
        @Valid @RequestBody PostalAddressFieldsValueDTO postalAddressFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PostalAddressFieldsValue : {}", postalAddressFieldsValueDTO);
        if (postalAddressFieldsValueDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new postalAddressFieldsValue cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        PostalAddressFieldsValueDTO result = postalAddressFieldsValueService.save(postalAddressFieldsValueDTO);
        return ResponseEntity.created(new URI("/api/postal-address-fields-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /postal-address-fields-values} : Updates an existing postalAddressFieldsValue.
     *
     * @param postalAddressFieldsValueDTO the postalAddressFieldsValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
     * postalAddressFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the postalAddressFieldsValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postalAddressFieldsValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/postal-address-fields-values")
    public ResponseEntity<PostalAddressFieldsValueDTO> updatePostalAddressFieldsValue(
        @Valid @RequestBody PostalAddressFieldsValueDTO postalAddressFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostalAddressFieldsValue : {}", postalAddressFieldsValueDTO);
        if (postalAddressFieldsValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PostalAddressFieldsValueDTO result = postalAddressFieldsValueService.save(postalAddressFieldsValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postalAddressFieldsValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /postal-address-fields-values} : get all the postalAddressFieldsValues.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postalAddressFieldsValues in body.
     */
    @GetMapping("/postal-address-fields-values")
    public List<PostalAddressFieldsValueDTO> getAllPostalAddressFieldsValues() {
        log.debug("REST request to get all PostalAddressFieldsValues");
        return postalAddressFieldsValueService.findAll();
    }

    /**
     * {@code GET  /postal-address-fields-values/:id} : get the "id" postalAddressFieldsValue.
     *
     * @param id the id of the postalAddressFieldsValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postalAddressFieldsValueDTO, or
     * with status {@code 404 (Not Found)}.
     */
    @GetMapping("/postal-address-fields-values/{id}")
    public ResponseEntity<PostalAddressFieldsValueDTO> getPostalAddressFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to get PostalAddressFieldsValue : {}", id);
        Optional<PostalAddressFieldsValueDTO> postalAddressFieldsValueDTO = postalAddressFieldsValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postalAddressFieldsValueDTO);
    }

    /**
     * {@code DELETE  /postal-address-fields-values/:id} : delete the "id" postalAddressFieldsValue.
     *
     * @param id the id of the postalAddressFieldsValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/postal-address-fields-values/{id}")
    public ResponseEntity<Void> deletePostalAddressFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to delete PostalAddressFieldsValue : {}", id);
        postalAddressFieldsValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
