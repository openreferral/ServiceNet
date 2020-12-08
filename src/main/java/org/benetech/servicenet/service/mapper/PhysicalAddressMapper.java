package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.service.dto.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity PhysicalAddress and its DTO PhysicalAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class}, unmappedTargetPolicy = IGNORE)
public interface PhysicalAddressMapper extends EntityMapper<AddressDTO, PhysicalAddress> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    AddressDTO toDto(PhysicalAddress physicalAddress);

    @Mapping(source = "locationId", target = "location")
    PhysicalAddress toEntity(AddressDTO physicalAddressDTO);

    default PhysicalAddress fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PhysicalAddress physicalAddress = new PhysicalAddress();
        physicalAddress.setId(id);
        return physicalAddress;
    }
}
