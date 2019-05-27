package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.FiltersActivityDTO;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Activity.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    private final ActivityService activityService;

    private final UserService userService;

    public ActivityResource(ActivityService activityService, UserService userService) {
        this.activityService = activityService;
        this.userService = userService;
    }

    /**
     * GET  /activities : get all the activities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of activities in body
     */
    @PostMapping("/activities")
    @Timed
    public ResponseEntity<List<ActivityDTO>> getAllActivities(@Valid @RequestBody FiltersActivityDTO filtersForActivity,
    @PathParam("search") String search, Pageable pageable) {
        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
        UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);

        Page<ActivityDTO> page = activityService.getAllOrganizationActivities(pageable, systemAccountId, search,
        filtersForActivity);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activities");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/activities/{orgId}")
    @Timed
    public ResponseEntity<ActivityDTO> getActivityDetails(@PathVariable UUID orgId) {
        return activityService.getOneByOrganizationId(orgId)
            .map(r -> ResponseEntity.ok().body(r))
            .orElse(ResponseEntity.badRequest()
                .build());
    }
}
