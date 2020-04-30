package org.benetech.servicenet.web.rest.external;

import com.codahale.metrics.annotation.Timed;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.benetech.servicenet.domain.ClientProfile;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.enumeration.RecordType;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.security.SecurityUtils;
import org.benetech.servicenet.service.ClientProfileService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.external.RecordRequest;
import org.benetech.servicenet.service.dto.external.RecordDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing external records request.
 */
@RestController
@RequestMapping("/partner-api")
public class RecordsResource {

    private final Logger log = LoggerFactory.getLogger(RecordsResource.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Autowired
    private ClientProfileService clientProfileService;

    @PreAuthorize("hasRole('" + AuthoritiesConstants.EXTERNAL + "')")
    @PostMapping("/records")
    @Timed
    public ResponseEntity<List<RecordDto>> getOrganizationMatchInfos(
        @Valid @RequestBody RecordRequest recordRequest) throws URISyntaxException, BadRequestAlertException {
        log.debug("REST request to get Record : {}", recordRequest);

        String clientId = SecurityUtils.getCurrentClientId();
        Optional<ClientProfile> optionalClientProfile = clientProfileService.findById(clientId);
        if (optionalClientProfile.isEmpty()) {
            throw new BadRequestAlertException("There is no provider associated with this account.",
                RecordType.ORGANIZATION.toString(), "id");
        }
        ClientProfile clientProfile = optionalClientProfile.get();
        UUID providerId = clientProfile.getSystemAccount().getId();
        Optional<Organization> optionalOrganization = organizationService.findByIdOrExternalDbId(
            recordRequest.getId(), providerId);
        if (optionalOrganization.isEmpty()) {
            throw new BadRequestAlertException("There is no organization with such id.",
                RecordType.ORGANIZATION.toString(), "id");
        }
        Organization organization = optionalOrganization.get();
        List<OrganizationMatch> matches = organizationMatchService.findAllMatchesForOrganization(organization.getId());

        List<RecordDto> results = this.mapMatchesToExternalRecords(matches, recordRequest.getSimilarity());
        return ResponseEntity.ok()
            .body(results);
    }

    private List<RecordDto> mapMatchesToExternalRecords(List<OrganizationMatch> matches, double similarity) {
        List<RecordDto> results = new ArrayList<>();
        for (OrganizationMatch match : matches) {
            RecordDto response = new RecordDto();
            response.setServiceNetId(match.getPartnerVersion().getId());
            response.setOrganizationName(match.getPartnerVersion().getName());
            response.setExternalDbId(match.getPartnerVersion().getExternalDbId());
            response.setSimilarity(match.getSimilarity().doubleValue());
            if (match.getSimilarity().doubleValue() >= similarity) {
                results.add(response);
            }
        }
        return results;
    }
}
