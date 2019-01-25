package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.mapstruct.Mapper;

import java.util.UUID;

/**
 * Mapper for the entity FieldExclusion and its DTO FieldExclusionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FieldExclusionMapper extends EntityMapper<FieldExclusionDTO, FieldExclusion> {

    default FieldExclusion fromId(UUID id) {
        if (id == null) {
            return null;
        }
        FieldExclusion fieldExclusion = new FieldExclusion();
        fieldExclusion.setId(id);
        return fieldExclusion;
    }
}
