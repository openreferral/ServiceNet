package org.benetech.servicenet.web.rest;

import java.util.Map;
import java.util.UUID;
import javax.websocket.server.PathParam;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Beneficiary;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.BeneficiaryService;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.ReferralService;
import org.benetech.servicenet.service.dto.BeneficiaryDTO;

import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.dto.CheckInDTO;
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
 * REST controller for managing {@link org.benetech.servicenet.domain.Beneficiary}.
 */
@RestController
@RequestMapping("/api")
@SuppressWarnings("PMD.PreserveStackTrace")
public class BeneficiaryResource {

    private final Logger log = LoggerFactory.getLogger(BeneficiaryResource.class);

    private static final String ENTITY_NAME = "beneficiary";
    private static final String ORG_ENTITY_NAME = "organization";

    private final BeneficiaryService beneficiaryService;
    private final ReferralService referralService;
    private final OrganizationService organizationService;

    public BeneficiaryResource(BeneficiaryService beneficiaryService, ReferralService referralService, OrganizationService organizationService) {
        this.beneficiaryService = beneficiaryService;
        this.referralService = referralService;
        this.organizationService = organizationService;
    }

    /**
     * {@code POST  /beneficiaries} : Create a new beneficiary.
     *
     * @param beneficiaryDTO the beneficiaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beneficiaryDTO, or with status {@code 400 (Bad Request)} if the beneficiary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/beneficiaries")
    public ResponseEntity<BeneficiaryDTO> createBeneficiary(@RequestBody BeneficiaryDTO beneficiaryDTO) throws URISyntaxException {
        log.debug("REST request to save Beneficiary : {}", beneficiaryDTO);
        if (beneficiaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new beneficiary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BeneficiaryDTO result = beneficiaryService.save(beneficiaryDTO);
        return ResponseEntity.created(new URI("/api/beneficiaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beneficiaries} : Updates an existing beneficiary.
     *
     * @param beneficiaryDTO the beneficiaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiaryDTO,
     * or with status {@code 400 (Bad Request)} if the beneficiaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beneficiaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/beneficiaries")
    public ResponseEntity<BeneficiaryDTO> updateBeneficiary(@RequestBody BeneficiaryDTO beneficiaryDTO) throws URISyntaxException {
        log.debug("REST request to update Beneficiary : {}", beneficiaryDTO);
        if (beneficiaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BeneficiaryDTO result = beneficiaryService.save(beneficiaryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, beneficiaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /beneficiaries} : get all the beneficiaries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beneficiaries in body.
     */
    @GetMapping("/beneficiaries")
    public ResponseEntity<List<BeneficiaryDTO>> getAllBeneficiaries(Pageable pageable) {
        log.debug("REST request to get a page of Beneficiaries");
        Page<BeneficiaryDTO> page = beneficiaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /beneficiaries/:id} : get the "id" beneficiary.
     *
     * @param id the id of the beneficiaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beneficiaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beneficiaries/{id}")
    public ResponseEntity<BeneficiaryDTO> getBeneficiary(@PathVariable UUID id) {
        log.debug("REST request to get Beneficiary : {}", id);
        Optional<BeneficiaryDTO> beneficiaryDTO = beneficiaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(beneficiaryDTO);
    }

    /**
     * {@code DELETE  /beneficiaries/:id} : delete the "id" beneficiary.
     *
     * @param id the id of the beneficiaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/beneficiaries/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable UUID id) {
        log.debug("REST request to delete Beneficiary : {}", id);
        beneficiaryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code POST  /beneficiaries/check-in} : Get or Create a beneficiary and then make a check in.
     *
     * @param checkInDTO the checkInDTO to find or create beneficiary and make check in.
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)}, or with status {@code 400 (Bad Request)} if the beneficiary Id or Organization Id not found
     */
    @PostMapping("/beneficiaries/check-in")
    public ResponseEntity<Void> checkIn(@RequestBody CheckInDTO checkInDTO) {
        log.debug("REST request to Check In Beneficiary : {}", checkInDTO);
        Beneficiary beneficiary = null;
        boolean isBeneficiaryNew = false;
        Optional<Organization> cbo = organizationService.findOne(checkInDTO.getCboId());
        if (!checkInDTO.getPhoneNumber().isBlank()) {
            Optional<Beneficiary> beneficiaryOptional = beneficiaryService.findByPhoneNumber(checkInDTO.getPhoneNumber());
            isBeneficiaryNew = beneficiaryOptional.isEmpty();
            beneficiary = beneficiaryOptional
                .orElseGet(() -> beneficiaryService.create(checkInDTO.getPhoneNumber()));
        } else if (checkInDTO.getBeneficiaryId() != null) {
            try {
                Optional<Beneficiary> beneficiaryOpt = beneficiaryService
                    .getOne(checkInDTO.getBeneficiaryId());
                if (beneficiaryOpt.isEmpty()) {
                    throw new BadRequestAlertException("Can not find beneficiary with provided ID",
                        ENTITY_NAME, "idnotfound");
                }
                beneficiary = beneficiaryOpt.get();
            } catch (IllegalArgumentException iae) {
                throw new BadRequestAlertException("The provided Unique Identifier is invalid", ENTITY_NAME, "idinvalid");
            }
        }
        if (cbo.isEmpty()) {
            throw new BadRequestAlertException("Can not find organization with provided ID", ORG_ENTITY_NAME, "idnotfound");
        }

        referralService.checkIn(beneficiary, isBeneficiaryNew, cbo.get().getId(), checkInDTO.getLocationId());
        return ResponseEntity.ok().build();
    }

    /**
     * {@code POST  /beneficiaries/refer} : Get or Create a beneficiary and refer him to provided organizations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (Ok)}, or with status {@code 400 (Bad Request)} if the beneficiary Id or Organization Id not found
     */
    @PostMapping("/beneficiaries/refer")
    public ResponseEntity<Void> refer(
        @RequestBody Map<UUID, UUID> organizationLocs,
        @PathParam("referringOrganizationId") String referringOrganizationId,
        @PathParam("referringLocationId") String referringLocationId,
        @PathParam("beneficiaryId") String beneficiaryId,
        @PathParam("phoneNumber") String phoneNumber)
    {
        log.debug("REST request to Refer Beneficiary {} from {} to: {}",
            beneficiaryId, referringOrganizationId, organizationLocs);
        Beneficiary beneficiary = null;
        Optional<Organization> cbo = organizationService.findOne(UUID.fromString(referringOrganizationId));
        if (StringUtils.isNotBlank(phoneNumber)) {
            Optional<Beneficiary> beneficiaryOptional = beneficiaryService.findByPhoneNumber(phoneNumber);
            beneficiary = beneficiaryOptional
                .orElseGet(() -> beneficiaryService.create(phoneNumber));
        } else if (StringUtils.isNotBlank(beneficiaryId)) {
            try {
                Optional<Beneficiary> beneficiaryOpt = beneficiaryService
                    .getOne(beneficiaryId);
                if (beneficiaryOpt.isEmpty()) {
                    throw new BadRequestAlertException("Can not find beneficiary with provided ID",
                        ENTITY_NAME, "idnotfound");
                }
                beneficiary = beneficiaryOpt.get();
            } catch (IllegalArgumentException iae) {
                throw new BadRequestAlertException("The provided Unique Identifier is invalid", ENTITY_NAME, "idinvalid");
            }
        }
        if (cbo.isEmpty()) {
            throw new BadRequestAlertException("Can not find organization with provided ID", ORG_ENTITY_NAME, "idnotfound");
        }

        referralService.refer(beneficiary, cbo.get(), UUID.fromString(referringLocationId), organizationLocs);
        return ResponseEntity.ok().build();
    }
}
