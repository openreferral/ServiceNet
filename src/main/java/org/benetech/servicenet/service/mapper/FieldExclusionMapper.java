package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity FieldExclusion and its DTO FieldExclusionDTO.
 */
@Mapper(componentModel = "spring", uses = { ExclusionsConfigMapper.class })
public interface FieldExclusionMapper extends EntityMapper<FieldExclusionDTO, FieldExclusion> {

    @Mapping(source = "config.id", target = "configId")
    FieldExclusionDTO toDto(FieldExclusion fieldExclusion);

    @Mapping(source = "configId", target = "config")
    FieldExclusion toEntity(FieldExclusionDTO fieldExclusionDTO);

    default FieldExclusion fromId(UUID id) {
        if (id == null) {
            return null;
        }
        FieldExclusion fieldExclusion = new FieldExclusion();
        fieldExclusion.setId(id);
        return fieldExclusion;
    }
}
