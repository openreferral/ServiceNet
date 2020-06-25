package org.benetech.servicenet.web.rest.external;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.dto.external.RecordDetailsDTO;
import org.benetech.servicenet.service.dto.external.RecordRequest;
import org.benetech.servicenet.service.dto.external.RecordDto;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing external records request.
 */
@RestController
@RequestMapping("/partner-api")
@Api(tags = "external-access", description = "Endpoints with external access")
public class RecordsResource {

    private final Logger log = LoggerFactory.getLogger(RecordsResource.class);

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RecordsService recordsService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Autowired
    private ClientProfileService clientProfileService;

    /**
     * POST  /records : Resource to get the list of similar organizations
     *
     * @param recordRequest object containing string of id of searched organization and number of similarity threshold
     * @return the ResponseEntity with status 200 (OK) and list of RecordDto
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.EXTERNAL + "')")
    @PostMapping("/records")
    @Timed
    @ApiOperation(value = "Resource to get the list of organizations within a similarity threshold to"
        + " organization with provided id. Given ID can be either the ID of the organization in ServiceNet database"
        + " or ID from the external database of that organization's provider.")
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

    /**
     * GET  /record-details : Resource to get all the record details for the specific organization
     *
     * @param elementId string
     * @return the ResponseEntity with status 200 (OK) and RecordDetailsDTO
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.EXTERNAL + "')")
    @GetMapping("/record-details/{elementId}")
    @Timed
    @ApiOperation(value = "Resource to get all the record details for the organization object found"
        + " by provided elementId. elementId can be either ServiceNet's database ID of organization, "
        + " service or location or external provider's database ID of organization, service or location."
        + " Returned are record details for organization related to the object with elementId.")
    public ResponseEntity<RecordDetailsDTO> getRecordDetails(@PathVariable String elementId) {
        Optional<Organization> optionalOrganization = organizationService.findWithEagerByIdOrExternalDbId(elementId);
        if (optionalOrganization.isEmpty()) {
            throw new BadRequestAlertException("There is no organization associated to given id.",
                RecordType.ORGANIZATION.toString(), "id");
        }
        Organization organization = optionalOrganization.get();

        return ResponseEntity.ok().body(recordsService.getRecordDetailsFromOrganization(organization));
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
