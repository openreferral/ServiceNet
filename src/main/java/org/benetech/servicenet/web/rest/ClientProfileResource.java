package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.domain.ClientProfile;
import org.benetech.servicenet.repository.ClientProfileRepository;
import org.benetech.servicenet.errors.BadRequestAlertException;

import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ClientProfileService;
import org.benetech.servicenet.service.dto.ClientProfileDto;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.ClientProfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClientProfileResource {

    private final Logger log = LoggerFactory.getLogger(ClientProfileResource.class);

    private static final String ENTITY_NAME = "ClientProfile";

    @Autowired
    private ClientProfileService clientProfileService;

    /**
     * {@code POST  /client-profiles} : Create a new clientProfile.
     *
     * @param clientProfileDto the clientProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientProfile, or with status {@code 400 (Bad Request)} if the clientProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/client-profiles")
    public ResponseEntity<ClientProfileDto> createClientProfile(@RequestBody ClientProfileDto clientProfileDto) throws URISyntaxException {
        log.debug("REST request to save ClientProfile : {}", clientProfileDto);
        if (clientProfileDto.getId() != null) {
            throw new BadRequestAlertException("A new clientProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientProfileDto result = clientProfileService.save(clientProfileDto);
        return ResponseEntity.created(new URI("/api/client-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /client-profiles} : Updates an existing clientProfile.
     *
     * @param clientProfile the clientProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientProfile,
     * or with status {@code 400 (Bad Request)} if the clientProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/client-profiles")
    public ResponseEntity<ClientProfileDto> updateClientProfile(@RequestBody ClientProfileDto clientProfile) throws URISyntaxException {
        log.debug("REST request to update ClientProfile : {}", clientProfile);
        ClientProfileDto result = clientProfileService.update(clientProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /client-profiles} : get all the clientProfiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientProfiles in body.
     */
    @GetMapping("/client-profiles")
    public List<ClientProfileDto> getAllClientProfiles() {
        log.debug("REST request to get all ClientProfiles");
        return clientProfileService.findAll();
    }

    /**
     * {@code GET  /client-profiles/:id} : get the "id" clientProfile.
     *
     * @param clientId the id of the clientProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/client-profiles/{clientId}")
    public ResponseEntity<ClientProfileDto> getClientProfile(@PathVariable String clientId) {
        log.debug("REST request to get ClientProfile : {}", clientId);
        Optional<ClientProfileDto> clientProfile = clientProfileService.findOne(clientId);
        return ResponseUtil.wrapOrNotFound(clientProfile);
    }

    /**
     * {@code DELETE  /client-profiles/:id} : delete the "id" clientProfile.
     *
     * @param clientId the id of the clientProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/client-profiles/{clientId}")
    public ResponseEntity<Void> deleteClientProfile(@PathVariable String clientId) {
        log.debug("REST request to delete ClientProfile : {}", clientId);
        clientProfileService.delete(clientId);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, clientId)).build();
    }
}
