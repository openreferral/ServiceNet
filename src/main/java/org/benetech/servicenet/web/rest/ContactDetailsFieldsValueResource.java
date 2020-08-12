package org.benetech.servicenet.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ContactDetailsFieldsValueService;
import org.benetech.servicenet.service.dto.ContactDetailsFieldsValueDTO;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.ContactDetailsFieldsValue}.
 */
@RestController
@RequestMapping("/api")
public class ContactDetailsFieldsValueResource {

    private final Logger log = LoggerFactory.getLogger(ContactDetailsFieldsValueResource.class);

    private static final String ENTITY_NAME = "contactDetailsFieldsValue";

    private final ContactDetailsFieldsValueService contactDetailsFieldsValueService;

    public ContactDetailsFieldsValueResource(ContactDetailsFieldsValueService contactDetailsFieldsValueService) {
        this.contactDetailsFieldsValueService = contactDetailsFieldsValueService;
    }

    /**
     * {@code POST  /contact-details-fields-values} : Create a new contactDetailsFieldsValue.
     *
     * @param contactDetailsFieldsValueDTO the contactDetailsFieldsValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
     * contactDetailsFieldsValueDTO, or with status {@code 400 (Bad Request)} if the contactDetailsFieldsValue has
     * already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/contact-details-fields-values")
    public ResponseEntity<ContactDetailsFieldsValueDTO> createContactDetailsFieldsValue(
        @Valid @RequestBody ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ContactDetailsFieldsValue : {}", contactDetailsFieldsValueDTO);
        if (contactDetailsFieldsValueDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new contactDetailsFieldsValue cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        ContactDetailsFieldsValueDTO result = contactDetailsFieldsValueService.save(contactDetailsFieldsValueDTO);
        return ResponseEntity.created(new URI("/api/contact-details-fields-values/" + result.getId()))
            .headers(HeaderUtil
                .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-details-fields-values} : Updates an existing contactDetailsFieldsValue.
     *
     * @param contactDetailsFieldsValueDTO the contactDetailsFieldsValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
     * contactDetailsFieldsValueDTO,
     * or with status {@code 400 (Bad Request)} if the contactDetailsFieldsValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactDetailsFieldsValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/contact-details-fields-values")
    public ResponseEntity<ContactDetailsFieldsValueDTO> updateContactDetailsFieldsValue(
        @Valid @RequestBody ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactDetailsFieldsValue : {}", contactDetailsFieldsValueDTO);
        if (contactDetailsFieldsValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactDetailsFieldsValueDTO result = contactDetailsFieldsValueService.save(contactDetailsFieldsValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactDetailsFieldsValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contact-details-fields-values} : get all the contactDetailsFieldsValues.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactDetailsFieldsValues in body.
     */
    @GetMapping("/contact-details-fields-values")
    public ResponseEntity<List<ContactDetailsFieldsValueDTO>> getAllContactDetailsFieldsValues(Pageable pageable) {
        log.debug("REST request to get all ContactDetailsFieldsValues");
        Page<ContactDetailsFieldsValueDTO> page = contactDetailsFieldsValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-details-fields-values");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /contact-details-fields-values/:id} : get the "id" contactDetailsFieldsValue.
     *
     * @param id the id of the contactDetailsFieldsValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactDetailsFieldsValueDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-details-fields-values/{id}")
    public ResponseEntity<ContactDetailsFieldsValueDTO> getContactDetailsFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to get ContactDetailsFieldsValue : {}", id);
        Optional<ContactDetailsFieldsValueDTO> contactDetailsFieldsValueDTO = contactDetailsFieldsValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactDetailsFieldsValueDTO);
    }

    /**
     * {@code DELETE  /contact-details-fields-values/:id} : delete the "id" contactDetailsFieldsValue.
     *
     * @param id the id of the contactDetailsFieldsValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/contact-details-fields-values/{id}")
    public ResponseEntity<Void> deleteContactDetailsFieldsValue(@PathVariable UUID id) {
        log.debug("REST request to delete ContactDetailsFieldsValue : {}", id);
        contactDetailsFieldsValueService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
