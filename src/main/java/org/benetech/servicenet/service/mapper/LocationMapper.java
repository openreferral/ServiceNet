package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.service.dto.LocationDTO;

import org.benetech.servicenet.service.dto.LocationRecordDTO;
import org.benetech.servicenet.service.dto.provider.SimpleLocationDTO;
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

    @Mapping(target = "physicalAddress", ignore = true)
    @Mapping(target = "postalAddress", ignore = true)
    @Mapping(target = "regularSchedule", ignore = true)
    @Mapping(target = "holidaySchedules", ignore = true)
    @Mapping(target = "langs", ignore = true)
    @Mapping(target = "accessibilities", ignore = true)
    @Mapping(target = "geocodingResults", ignore = true)
    Location toEntity(SimpleLocationDTO locationDTO);

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    LocationDTO toDto(Location location);

    @Mapping(target = "location", source = "location")
    @Mapping(target = "regularScheduleOpeningHours", source = "regularSchedule.openingHours")
    @Mapping(target = "regularScheduleNotes", source = "regularSchedule.notes")
    LocationRecordDTO toRecord(Location location);

    @Mapping(target = "address1", source = "physicalAddress.address1")
    @Mapping(target = "address2", source = "physicalAddress.address2")
    @Mapping(target = "city", source = "physicalAddress.city")
    @Mapping(target = "ca", source = "physicalAddress.stateProvince")
    @Mapping(target = "zipcode", source = "physicalAddress.postalCode")
    SimpleLocationDTO toSimpleDto(Location location);

    default Location fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }

    default PostalAddress extractPostalAddress(SimpleLocationDTO simpleLocationDTO, Location existingLocation) {
        PostalAddress postalAddress = (existingLocation != null)
            ? existingLocation.getPostalAddress() : new PostalAddress();
        postalAddress.address1(simpleLocationDTO.getAddress1());
        postalAddress.address2(simpleLocationDTO.getAddress2());
        postalAddress.city(simpleLocationDTO.getCity());
        postalAddress.setPostalCode(simpleLocationDTO.getZipcode());
        postalAddress.setStateProvince(simpleLocationDTO.getCa());
        return postalAddress;
    }

    default PhysicalAddress extractPhysicalAddress(SimpleLocationDTO simpleLocationDTO, Location existingLocation) {
        PhysicalAddress physicalAddress = (existingLocation != null)
            ? existingLocation.getPhysicalAddress() : new PhysicalAddress();
        physicalAddress.address1(simpleLocationDTO.getAddress1());
        physicalAddress.address2(simpleLocationDTO.getAddress2());
        physicalAddress.city(simpleLocationDTO.getCity());
        physicalAddress.setPostalCode(simpleLocationDTO.getZipcode());
        physicalAddress.setStateProvince(simpleLocationDTO.getCa());
        return physicalAddress;
    }
}
