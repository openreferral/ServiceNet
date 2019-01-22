package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.service.dto.LocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = IGNORE)
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {

    @Mapping(target = "physicalAddress", ignore = true)
    @Mapping(target = "postalAddress", ignore = true)
    @Mapping(target = "regularSchedule", ignore = true)
    @Mapping(target = "holidaySchedule", ignore = true)
    @Mapping(target = "langs", ignore = true)
    @Mapping(target = "accessibilities", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    default Location fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
