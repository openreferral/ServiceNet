package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.UserDTO;
import org.benetech.servicenet.web.rest.errors.InternalServerErrorException;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping("/activities")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(Pageable pageable) {
        log.debug("REST request to get a page of Activities");
        UserDTO currentUser = userService.getUserWithAuthorities()
            .map(UserDTO::new).orElseThrow(() -> new InternalServerErrorException("User could not be found"));

        Page<ActivityDTO> page = activityService.getAllOrganizationActivities(pageable, currentUser.getSystemAccountId());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activities");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
