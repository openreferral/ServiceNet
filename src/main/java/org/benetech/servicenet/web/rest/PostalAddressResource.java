package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
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
 * REST controller for managing PostalAddress.
 */
@RestController
@RequestMapping("/api")
public class PostalAddressResource {

    private static final String ENTITY_NAME = "postalAddress";
    private final Logger log = LoggerFactory.getLogger(PostalAddressResource.class);
    private final PostalAddressService postalAddressService;

    public PostalAddressResource(PostalAddressService postalAddressService) {
        this.postalAddressService = postalAddressService;
    }

    /**
     * POST  /postal-addresses : Create a new postalAddress.
     *
     * @param postalAddressDTO the postalAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postalAddressDTO,
     * or with status 400 (Bad Request) if the postalAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/postal-addresses")
    @Timed
    public ResponseEntity<PostalAddressDTO> createPostalAddress(
        @Valid @RequestBody PostalAddressDTO postalAddressDTO) throws URISyntaxException {
        log.debug("REST request to save PostalAddress : {}", postalAddressDTO);
        if (postalAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new postalAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostalAddressDTO result = postalAddressService.save(postalAddressDTO);
        return ResponseEntity.created(new URI("/api/postal-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /postal-addresses : Updates an existing postalAddress.
     *
     * @param postalAddressDTO the postalAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postalAddressDTO,
     * or with status 400 (Bad Request) if the postalAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the postalAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/postal-addresses")
    @Timed
    public ResponseEntity<PostalAddressDTO> updatePostalAddress(
        @Valid @RequestBody PostalAddressDTO postalAddressDTO) throws URISyntaxException {
        log.debug("REST request to update PostalAddress : {}", postalAddressDTO);
        if (postalAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PostalAddressDTO result = postalAddressService.save(postalAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postalAddressDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /postal-addresses : get all the postalAddresses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of postalAddresses in body
     */
    @GetMapping("/postal-addresses")
    @Timed
    public List<PostalAddressDTO> getAllPostalAddresses() {
        log.debug("REST request to get all PostalAddresses");
        return postalAddressService.findAll();
    }

    /**
     * GET  /postal-addresses/:id : get the "id" postalAddress.
     *
     * @param id the id of the postalAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postalAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/postal-addresses/{id}")
    @Timed
    public ResponseEntity<PostalAddressDTO> getPostalAddress(@PathVariable UUID id) {
        log.debug("REST request to get PostalAddress : {}", id);
        Optional<PostalAddressDTO> postalAddressDTO = postalAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postalAddressDTO);
    }

    /**
     * DELETE  /postal-addresses/:id : delete the "id" postalAddress.
     *
     * @param id the id of the postalAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/postal-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deletePostalAddress(@PathVariable UUID id) {
        log.debug("REST request to delete PostalAddress : {}", id);
        postalAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
