package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.LocationMatchService;
import org.benetech.servicenet.service.dto.LocationMatchDto;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Location Matches.
 */
@RestController
@RequestMapping("/api")
public class LocationMatchResource {

    private static final String ENTITY_NAME = "location_match";
    private final LocationMatchService locationMatchService;

    public LocationMatchResource(LocationMatchService locationMatchService) {
        this.locationMatchService = locationMatchService;
    }

    @PreAuthorize("hasRole('" + AuthoritiesConstants.USER + "')")
    @PostMapping("/location-matches")
    @Timed
    public ResponseEntity<LocationMatchDto> createLocationMatch(
        @RequestBody LocationMatchDto locationMatchDto) throws URISyntaxException {
        if (locationMatchDto.getId() != null) {
            throw new BadRequestAlertException("A new locationMatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationMatchDto result = locationMatchService.save(locationMatchDto);
        return ResponseEntity.created(new URI("/api/location-matches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PreAuthorize("hasRole('" + AuthoritiesConstants.USER + "')")
    @DeleteMapping("/location-matches")
    @Timed
    public ResponseEntity<Void> deleteServiceMatch(
        @RequestBody LocationMatchDto locationMatchDto) throws BadRequestAlertException {
        locationMatchService.delete(locationMatchDto.getLocation(), locationMatchDto.getMatchingLocation());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, locationMatchDto.getMatchingLocation().toString()))
            .build();
    }
}
