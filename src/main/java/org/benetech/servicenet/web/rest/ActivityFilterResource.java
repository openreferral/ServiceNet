package org.benetech.servicenet.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.ActivityFilter}.
 */
@RestController
@RequestMapping("/api")
public class ActivityFilterResource {

    private final Logger log = LoggerFactory.getLogger(ActivityFilterResource.class);

    private static final String ENTITY_NAME = "activityFilter";

    private final ActivityFilterService activityFilterService;

    private final UserService userService;

    public ActivityFilterResource(ActivityFilterService activityFilterService, UserService userService) {
        this.activityFilterService = activityFilterService;
        this.userService = userService;
    }

    /**
     * GET getPostalCodes
     */
    @GetMapping("/activity-filter/get-postal-codes")
    public Set<String> getPostalCodes() {
        return activityFilterService.getPostalCodes();
    }

    /**
     * GET getCounties
     */
    @GetMapping("/activity-filter/get-regions")
    public Set<String> getRegions() {
        return activityFilterService.getRegions();
    }

    /**
     * GET getCities
     */
    @GetMapping("/activity-filter/get-cities")
    public Set<String> getCities() {
        return activityFilterService.getCities();
    }

    /**
     * GET getTaxonomies
     */
    @GetMapping("/activity-filter/get-taxonomies")
    public TaxonomyFilterDTO getTaxonomies() {
        TaxonomyFilterDTO taxonomyFilterDTO = new TaxonomyFilterDTO();
        taxonomyFilterDTO.setTaxonomiesByProvider(
            activityFilterService.getTaxonomies()
        );
        taxonomyFilterDTO.setCurrentProvider(
            userService.getCurrentSystemAccountName()
        );
        return taxonomyFilterDTO;
    }

    /**
     * {@code POST  /activity-filters} : Create a new activityFilter.
     *
     * @param activityFilterDTO the activityFilterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityFilterDTO,
     * or with status {@code 400 (Bad Request)} if the activityFilter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/activity-filters")
    public ResponseEntity<ActivityFilterDTO> createActivityFilter(@RequestBody ActivityFilterDTO activityFilterDTO)
        throws URISyntaxException {
        log.debug("REST request to save ActivityFilter : {}", activityFilterDTO);
        if (activityFilterDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityFilter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityFilterDTO result = activityFilterService.save(activityFilterDTO);
        return ResponseEntity.created(new URI("/api/activity-filters/" + result.getId()))
            .headers(HeaderUtil
                .createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activity-filters} : Updates an existing activityFilter.
     *
     * @param activityFilterDTO the activityFilterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityFilterDTO,
     * or with status {@code 400 (Bad Request)} if the activityFilterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityFilterDTO couldn't be updated.
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/activity-filters")
    public ResponseEntity<ActivityFilterDTO> updateActivityFilter(@RequestBody ActivityFilterDTO activityFilterDTO) {
        log.debug("REST request to update ActivityFilter : {}", activityFilterDTO);
        if (activityFilterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityFilterDTO result = activityFilterService.save(activityFilterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, activityFilterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /activity-filters} : get all the activityFilters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityFilters in body.
     */
    @GetMapping("/activity-filters")
    public ResponseEntity<List<ActivityFilterDTO>> getAllActivityFilters(Pageable pageable) {
        log.debug("REST request to get a page of ActivityFilters");
        Page<ActivityFilterDTO> page = activityFilterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activity-filters");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /activity-filters/:id} : get the "id" activityFilter.
     *
     * @param id the id of the activityFilterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityFilterDTO,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-filters/{id}")
    public ResponseEntity<ActivityFilterDTO> getActivityFilter(@PathVariable UUID id) {
        log.debug("REST request to get ActivityFilter : {}", id);
        Optional<ActivityFilterDTO> activityFilterDTO = activityFilterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityFilterDTO);
    }

    /**
     * {@code DELETE  /activity-filters/:id} : delete the "id" activityFilter.
     *
     * @param id the id of the activityFilterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activity-filters/{id}")
    public ResponseEntity<Void> deleteActivityFilter(@PathVariable UUID id) {
        log.debug("REST request to delete ActivityFilter : {}", id);
        if (activityFilterService.getAllForCurrentUser().stream().anyMatch(filter -> filter.getId().equals(id))) {
            activityFilterService.delete(id);
            return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(
                ENTITY_NAME, id.toString())).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Get all the activityFilters for current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityFilters in body.
     */
    @GetMapping("/activity-filter/get-user-filters")
    public ResponseEntity<List<ActivityFilterDTO>> getAllActivityFilters() {
        List<ActivityFilterDTO> activityFilterDTOS = activityFilterService.getAllForCurrentUser();
        return ResponseEntity.ok().body(activityFilterDTOS);
    }

    /**
     * Create a new activityFilter.
     *
     * @param activityFilterDTO the activityFilterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new activityFilterDTO,
     * or with status {@code 400 (Bad Request)}.
     */
    @PostMapping("/activity-filter/save-user-filter")
    public ResponseEntity<ActivityFilterDTO> saveActivityFilter(@RequestBody ActivityFilterDTO activityFilterDTO) {
        if (activityFilterService.findByNameAndCurrentUser(activityFilterDTO.getName()).isPresent()) {
            throw new BadRequestAlertException("Name must be unique", ENTITY_NAME, "nameunique");
        }

        activityFilterDTO.setId(null);

        ActivityFilterDTO result = activityFilterService.saveForCurrentUser(activityFilterDTO);
        return ResponseEntity.ok().body(result);
    }

    /**
     * Get the current user activityFilter.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the current activityFilter in body.
     */
    @GetMapping("/activity-filter/current-user-filter")
    public ResponseEntity<ActivityFilterDTO> getCurrentUserActivityFilter() {
        return ResponseEntity.ok().body(activityFilterService.getCurrentUserActivityFilter());
    }

    /**
     * Save the current activityFilter for user.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/activity-filter/current-user-filter")
    public void saveCurrentUserActivityFilter(@RequestBody ActivityFilterDTO activityFilterDTO) {
        activityFilterService.saveCurrentUserActivityFilter(activityFilterDTO);
    }
}
