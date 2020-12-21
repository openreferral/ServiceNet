package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.websocket.server.PathParam;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ReferralService;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.OrganizationOptionDTO;
import org.benetech.servicenet.service.dto.ReferralDTO;

import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.service.dto.ReferralMadeFromUserDTO;
import org.benetech.servicenet.service.dto.ReferralMadeToUserDTO;
import org.benetech.servicenet.util.ReportUtils;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private static final String ENTITY_NAME = "referral";

    private final ReferralService referralService;

    private final UserService userService;

    public ReferralResource(ReferralService referralService, UserService userService) {
        this.referralService = referralService;
        this.userService = userService;
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

    /**
     * {@code GET  /referrals/search} : search the referrals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referrals in body.
     */
    @GetMapping("/referrals/search")
    public ResponseEntity<List<ReferralDTO>> searchReferrals(
        @PageableDefault(sort = "sentAt", direction = Direction.ASC) Pageable pageable,
        @PathParam("since") ZonedDateTime since, @PathParam("status") String status) {
        log.debug("REST request to get a page of Referrals");
        Page<ReferralDTO> page = referralService.findCurrentUsersReferrals(since, status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referrals/csv} : Gets current user's Referrals as CSV"
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and a csv in the body
     */
    @GetMapping(value = "/referrals/csv", produces = "text/csv")
    public ResponseEntity<FileSystemResource> getCurrentUsersReferralsCsv(
        @PageableDefault(sort = "sentAt", direction = Direction.ASC, size = Integer.MAX_VALUE) Pageable pageable,
        @PathParam("since") ZonedDateTime since, @PathParam("status") String status) {
        log.debug("REST request to get current user's Referrals as CSV");
        String[] headers = {"Beneficiary Phone Number", "Service Net ID", "Date Stamp", "Referred From", "Referred To", "Status"};
        String[] valueMappings = {"beneficiaryPhoneNumber", "id", "sentAt", "fromName", "toName", "status"};
        List<ReferralDTO> referrals = referralService.findCurrentUsersReferrals(since, status, pageable).toList();
        File csvOutputFile = ReportUtils
            .createCsvReport("Beneficiary_Referral_History", referrals, headers, valueMappings);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + csvOutputFile.getName())
            .contentLength(csvOutputFile.length())
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(new FileSystemResource(csvOutputFile));
    }

    /**
     * {@code GET  /referrals/number-made-from-us?to=} : Gets number of made referrals from current user's organizations to other organizations
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of number of referrals made in body.
     */
    @GetMapping("/referrals/number-made-from-us")
    public ResponseEntity<List<ReferralMadeFromUserDTO>> numberOfReferralsMadeFromUser(
        Pageable pageable, @RequestParam(required = false) UUID to
    ) {
        log.debug("REST request to get a number of made Referrals from user");
        Page<ReferralMadeFromUserDTO> page = referralService.getNumberOfReferralsMadeFromUser(to, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referrals/number-made-from-us/csv} : Gets number of made referrals from current user's organizations to other organizations
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and a csv in the body
     */
    @GetMapping(value = "/referrals/number-made-from-us/csv", produces = "text/csv")
    public ResponseEntity<FileSystemResource> numberOfReferralsMadeFromUserCsv(
        @PageableDefault(sort = "toOrg.id", direction = Direction.ASC, size = Integer.MAX_VALUE) Pageable pageable
    ) {
        log.debug("REST request to get a number of made Referrals from user as CSV");
        String[] headers = {"Organization id", "Organization name", "Referral count"};
        String[] valueMappings = {"orgId", "orgName", "referralCount"};
        List<ReferralMadeFromUserDTO> referrals = referralService.getNumberOfReferralsMadeFromUser(null, pageable).toList();
        UserProfile currentUser = userService.getCurrentUserProfile();
        File csvOutputFile = ReportUtils
            .createCsvReport("#_Referrals_Made_From_" + currentUser.getLogin(),
                referrals, headers, valueMappings);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + csvOutputFile.getName())
            .contentLength(csvOutputFile.length())
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(new FileSystemResource(csvOutputFile));
    }

    /**
     * {@code GET  /referrals/made-to-us?status} : Gets status of made referrals to current user's organizations
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of status of referrals made in body.
     */
    @GetMapping("/referrals/made-to-us")
    public ResponseEntity<List<ReferralMadeToUserDTO>> numberOfReferralsMadeToUser(
        Pageable pageable, @RequestParam(required = false) String status
    ) {
        log.debug("REST request to get a status of made Referrals to user");
        Page<ReferralMadeToUserDTO> page = referralService.getReferralsMadeToUser(status, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referrals/made-to-us/csv} : Gets number of made referrals from current user's organizations to other organizations as CSV
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and a csv in the body
     */
    @GetMapping(value = "/referrals/made-to-us/csv", produces = "text/csv")
    public ResponseEntity<FileSystemResource> referralsMadeToUserCsv(
        @PageableDefault(sort = "fulfilledAt", direction = Direction.ASC, size = Integer.MAX_VALUE) Pageable pageable) {
        log.debug("REST request to get a status of made Referrals to user as CSV");
        String[] headers = {"Organization id", "Organization name", "Referral count"};
        String[] valueMappings = {"orgId", "orgName", "status"};
        List<ReferralMadeToUserDTO> referrals = referralService.getReferralsMadeToUser(null, pageable).toList();
        File csvOutputFile = ReportUtils
            .createCsvReport("Referrals_Made_To_Us", referrals, headers, valueMappings);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + csvOutputFile.getName())
            .contentLength(csvOutputFile.length())
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(new FileSystemResource(csvOutputFile));
    }

    @GetMapping("/referrals/made-to-options")
    @Timed
    public ResponseEntity<List<OrganizationOptionDTO>> getReferralMadeToOptions() {
        return ResponseEntity.ok().body(
            referralService.getOrganizationOptionsForCurrentUser()
        );
    }
}
