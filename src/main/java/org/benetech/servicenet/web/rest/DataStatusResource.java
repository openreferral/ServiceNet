package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.DataStatusService;
import org.benetech.servicenet.service.dto.DataStatusDto;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing SendGrid Mail.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class DataStatusResource {

    @Autowired
    private DataStatusService dataStatusService;

    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @GetMapping("/data-status")
    @Timed
    public ResponseEntity<List<DataStatusDto>> fetchDataStatus(Pageable pageable) {
        Page<DataStatusDto> page = dataStatusService.getDataStatuses(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-status");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
