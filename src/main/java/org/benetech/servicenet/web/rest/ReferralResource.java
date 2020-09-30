package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ReferralService;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.dto.ReferralDTO;

import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.Referral}.
 */
@RestController
@RequestMapping("/api")
public class ReferralResource {

    private final Logger log = LoggerFactory.getLogger(ReferralResource.class);

    private static final String ENTITY_NAME = "serviceNetReferral";

    private final ReferralService referralService;

    public ReferralResource(ReferralService referralService) {
        this.referralService = referralService;
    }

    /**
     * {@code POST  /referrals} : Create a new referral.
     *
     * @param referralDTO the referralDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referralDTO, or with status {@code 400 (Bad Request)} if the referral has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/referrals")
    public ResponseEntity<ReferralDTO> createReferral(@RequestBody ReferralDTO referralDTO) throws URISyntaxException {
        log.debug("REST request to save Referral : {}", referralDTO);
        if (referralDTO.getId() != null) {
            throw new BadRequestAlertException("A new referral cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReferralDTO result = referralService.save(referralDTO);
        return ResponseEntity.created(new URI("/api/referrals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /referrals} : Updates an existing referral.
     *
     * @param referralDTO the referralDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referralDTO,
     * or with status {@code 400 (Bad Request)} if the referralDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referralDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/referrals")
    public ResponseEntity<ReferralDTO> updateReferral(@RequestBody ReferralDTO referralDTO) throws URISyntaxException {
        log.debug("REST request to update Referral : {}", referralDTO);
        if (referralDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReferralDTO result = referralService.save(referralDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, referralDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /referrals} : get all the referrals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referrals in body.
     */
    @GetMapping("/referrals")
    public ResponseEntity<List<ReferralDTO>> getAllReferrals(Pageable pageable) {
        log.debug("REST request to get a page of Referrals");
        Page<ReferralDTO> page = referralService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referrals/:id} : get the "id" referral.
     *
     * @param id the id of the referralDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referralDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referrals/{id}")
    public ResponseEntity<ReferralDTO> getReferral(@PathVariable UUID id) {
        log.debug("REST request to get Referral : {}", id);
        Optional<ReferralDTO> referralDTO = referralService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referralDTO);
    }

    /**
     * {@code DELETE  /referrals/:id} : delete the "id" referral.
     *
     * @param id the id of the referralDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/referrals/{id}")
    public ResponseEntity<Void> deleteReferral(@PathVariable UUID id) {
        log.debug("REST request to delete Referral : {}", id);
        referralService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
