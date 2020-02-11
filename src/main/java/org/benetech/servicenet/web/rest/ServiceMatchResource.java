//CHECKSTYLE:OFF
package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;
import org.benetech.servicenet.service.ServiceMatchService;
import org.benetech.servicenet.service.dto.ServiceMatchDto;
import org.benetech.servicenet.web.rest.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Service Matches.
 */
@RestController
@RequestMapping("/api")
public class ServiceMatchResource {

    private static final String ENTITY_NAME = "service_match";
    private final Logger log = LoggerFactory.getLogger(ServiceMatchResource.class);
    private final ServiceMatchService serviceMatchService;

    public ServiceMatchResource(ServiceMatchService serviceMatchService) {
        this.serviceMatchService = serviceMatchService;
    }

    @PostMapping("/service-matches")
    @Timed
    public ResponseEntity<ServiceMatchDto> createOrganizationMatch(
        @RequestBody ServiceMatchDto serviceMatchDto) throws URISyntaxException {
        log.debug("REST request to save ServiceMatch : {}", serviceMatchDto);
        if (serviceMatchDto.getId() != null) {
            throw new BadRequestAlertException("A new serviceMatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceMatchDto result = serviceMatchService.save(serviceMatchDto);
        return ResponseEntity.created(new URI("/api/service-matches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
