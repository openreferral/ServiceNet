package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.service.dto.MetadataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity Metadata and its DTO MetadataDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = IGNORE)
public interface MetadataMapper extends EntityMapper<MetadataDTO, Metadata> {

    @Mapping(source = "userProfile.id", target = "userId")
    @Mapping(source = "userProfile.login", target = "userLogin")
    MetadataDTO toDto(Metadata metadata);

    @Mapping(source = "userId", target = "userProfile")
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
