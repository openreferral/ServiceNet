package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.service.dto.LocationMatchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity LocationMatch and its DTO LocationMatchDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class}, unmappedTargetPolicy = IGNORE)
public interface LocationMatchMapper extends EntityMapper<LocationMatchDto, LocationMatch> {

    @Mapping(target = "organizationName", source = "matchingLocation.organization.name")
    @Mapping(target = "orgId", source = "matchingLocation.organization.id")
    @Mapping(target = "locationName", source = "matchingLocation.name")
    @Mapping(source = "location.id", target = "location")
    @Mapping(source = "matchingLocation.id", target = "matchingLocation")
    LocationMatchDto toDto(LocationMatch locationMatch);

    @Mapping(source = "location", target = "location")
    @Mapping(source = "matchingLocation", target = "matchingLocation")
    LocationMatch toEntity(LocationMatchDto locationMatchDto);

    default LocationMatch fromId(UUID id) {
        if (id == null) {
            return null;
        }
        LocationMatch locationMatch = new LocationMatch();
        locationMatch.setId(id);
        return locationMatch;
    }
}
