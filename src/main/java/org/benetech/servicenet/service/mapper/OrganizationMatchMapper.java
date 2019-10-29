package org.benetech.servicenet.service.mapper;

import java.util.HashMap;
import java.util.Map;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.service.LocationMatchService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity OrganizationMatch and its DTO OrganizationMatchDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, unmappedTargetPolicy = IGNORE)
public abstract class OrganizationMatchMapper {

    @Autowired
    private LocationMatchService locationMatchService;

    @Mapping(source = "organizationRecord.id", target = "organizationRecordId")
    @Mapping(source = "organizationRecord.name", target = "organizationRecordName")
    @Mapping(source = "partnerVersion.id", target = "partnerVersionId")
    @Mapping(source = "partnerVersion.name", target = "partnerVersionName")
    @Mapping(source = "dismissedBy.id", target = "dismissedById")
    @Mapping(source = "dismissedBy.login", target = "dismissedByName")
    @Mapping(source = "hiddenBy.id", target = "hiddenById")
    @Mapping(source = "hiddenBy.login", target = "hiddenByName")
    public abstract OrganizationMatchDTO toDto(OrganizationMatch organizationMatch);

    @Mapping(source = "organizationRecordId", target = "organizationRecord")
    @Mapping(source = "partnerVersionId", target = "partnerVersion")
    public abstract OrganizationMatch toEntity(OrganizationMatchDTO organizationMatchDTO);

    public OrganizationMatchDTO toDtoWithLocationMatches(OrganizationMatch organizationMatch) {
        OrganizationMatchDTO dto = toDto(organizationMatch);
        Map<UUID, UUID> locationMatches = new HashMap<>();
        for (Location location : organizationMatch.getOrganizationRecord().getLocations()) {
            for (LocationMatch match : locationMatchService.findAllForLocation(location.getId())) {
                locationMatches.put(match.getLocation().getId(), match.getMatchingLocation().getId());
            }
        }
        dto.setLocationMatches(locationMatches);
        return dto;
    }

    public OrganizationMatch fromId(UUID id) {
        if (id == null) {
            return null;
        }
        OrganizationMatch organizationMatch = new OrganizationMatch();
        organizationMatch.setId(id);
        return organizationMatch;
    }
}
