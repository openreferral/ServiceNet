package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.service.dto.MetadataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity Metadata and its DTO MetadataDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MetadataMapper extends EntityMapper<MetadataDTO, Metadata> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    MetadataDTO toDto(Metadata metadata);

    @Mapping(source = "userId", target = "user")
    Metadata toEntity(MetadataDTO metadataDTO);

    default Metadata fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Metadata metadata = new Metadata();
        metadata.setId(id);
        return metadata;
    }
}
