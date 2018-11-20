package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.service.dto.MetadataDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Metadata and its DTO MetadataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetadataMapper extends EntityMapper<MetadataDTO, Metadata> {

    default Metadata fromId(Long id) {
        if (id == null) {
            return null;
        }
        Metadata metadata = new Metadata();
        metadata.setId(id);
        return metadata;
    }
}
