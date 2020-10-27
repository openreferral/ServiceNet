package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.service.dto.LocationDTO;

import org.benetech.servicenet.service.dto.LocationOptionDTO;
import org.benetech.servicenet.service.dto.LocationRecordDTO;
import org.benetech.servicenet.service.dto.provider.SimpleLocationDTO;
import org.benetech.servicenet.service.dto.provider.ProviderLocationDTO;
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
    Location toEntity(ProviderLocationDTO locationDTO);

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    LocationDTO toDto(Location location);

    @Mapping(target = "organizationId", source = "organization.id")
    SimpleLocationDTO toSimpleDto(Location location);

    @Mapping(target = "organizationId", source = "organization.id")
    LocationOptionDTO toOptionDto(Location location);

    @Mapping(target = "address1", source = "physicalAddress.address1")
    @Mapping(target = "address2", source = "physicalAddress.address2")
    @Mapping(target = "city", source = "physicalAddress.city")
    @Mapping(target = "ca", source = "physicalAddress.stateProvince")
    @Mapping(target = "zipcode", source = "physicalAddress.postalCode")
    ProviderLocationDTO toProviderLocation(Location location);

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

    default PostalAddress extractPostalAddress(ProviderLocationDTO providerLocationDTO, Location existingLocation) {
        PostalAddress postalAddress = (existingLocation != null)
            ? existingLocation.getPostalAddress() : new PostalAddress();
        postalAddress.address1(providerLocationDTO.getAddress1());
        postalAddress.address2(providerLocationDTO.getAddress2());
        postalAddress.city(providerLocationDTO.getCity());
        postalAddress.setPostalCode(providerLocationDTO.getZipcode());
        postalAddress.setStateProvince(providerLocationDTO.getCa());
        return postalAddress;
    }

    default PhysicalAddress extractPhysicalAddress(ProviderLocationDTO providerLocationDTO, Location existingLocation) {
        PhysicalAddress physicalAddress = (existingLocation != null)
            ? existingLocation.getPhysicalAddress() : new PhysicalAddress();
        physicalAddress.address1(providerLocationDTO.getAddress1());
        physicalAddress.address2(providerLocationDTO.getAddress2());
        physicalAddress.city(providerLocationDTO.getCity());
        physicalAddress.setPostalCode(providerLocationDTO.getZipcode());
        physicalAddress.setStateProvince(providerLocationDTO.getCa());
        return physicalAddress;
    }
}
