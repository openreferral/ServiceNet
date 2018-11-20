package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity PostalAddress and its DTO PostalAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface PostalAddressMapper extends EntityMapper<PostalAddressDTO, PostalAddress> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    PostalAddressDTO toDto(PostalAddress postalAddress);

    @Mapping(source = "locationId", target = "location")
    PostalAddress toEntity(PostalAddressDTO postalAddressDTO);

    default PostalAddress fromId(Long id) {
        if (id == null) {
            return null;
        }
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setId(id);
        return postalAddress;
    }
}
