package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.service.dto.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity PostalAddress and its DTO AddressDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class}, unmappedTargetPolicy = IGNORE)
public interface PostalAddressMapper extends EntityMapper<AddressDTO, PostalAddress> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    AddressDTO toDto(PostalAddress postalAddress);

    @Mapping(source = "locationId", target = "location")
    PostalAddress toEntity(AddressDTO postalAddressDTO);

    default PostalAddress fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setId(id);
        return postalAddress;
    }
}
