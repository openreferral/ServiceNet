package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.service.dto.LocationDTO;

import org.benetech.servicenet.service.dto.LocationRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {

    @Mapping(target = "physicalAddress", ignore = true)
    @Mapping(target = "postalAddress", ignore = true)
    @Mapping(target = "regularSchedule", ignore = true)
    @Mapping(target = "holidaySchedules", ignore = true)
    @Mapping(target = "langs", ignore = true)
    @Mapping(target = "accessibilities", ignore = true)
    @Mapping(target = "geocodingResults", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    LocationDTO toDto(Location location);

    @Mapping(target = "location", source = "location")
    @Mapping(target = "regularScheduleOpeningHours", source = "regularSchedule.openingHours")
    @Mapping(target = "regularScheduleNotes", source = "regularSchedule.notes")
    LocationRecordDTO toRecord(Location location);

    default Location fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
