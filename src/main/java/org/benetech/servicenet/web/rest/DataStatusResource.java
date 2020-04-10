package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.dto.DataStatusDto;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing SendGrid Mail.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class DataStatusResource {

    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @GetMapping("/data-status")
    @Timed
    public ResponseEntity<List<DataStatusDto>> fetchDataStatus(Pageable pageable) {
        System.out.println("SSSSSSSSSSSSSSS113456");
        DataStatusDto dataStatusDto1 = new DataStatusDto();
        dataStatusDto1.setProviderName("ED");
        dataStatusDto1.setLastUpdateDateTime(DateTime.now().minus(100000L));

        DataStatusDto dataStatusDto2 = new DataStatusDto();
        dataStatusDto2.setProviderName("LAAAAAAAAA");
        dataStatusDto2.setLastUpdateDateTime(DateTime.now());

        DataStatusDto dataStatusDto3 = new DataStatusDto();
        dataStatusDto3.setProviderName("HEEEEEAAALLLLL");
        dataStatusDto3.setLastUpdateDateTime(DateTime.now().plus(10000L));
        List<DataStatusDto> results = Arrays.asList(
            dataStatusDto1, dataStatusDto2, dataStatusDto3
        );
        System.out.println(pageable.getPageNumber() + " " + pageable.getPageSize() + " " + pageable.getSort());
        Page<DataStatusDto> page = new PageImpl<>(
            results,
            PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()),
            results.size()
        );
        System.out.println("PPP " + page.getContent().size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/data-status");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
