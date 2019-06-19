package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.LocationExclusion;
import org.benetech.servicenet.service.dto.LocationExclusionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity {@link LocationExclusion} and its DTO {@link LocationExclusionDTO}.
 */
@Mapper(componentModel = "spring", uses = {ExclusionsConfigMapper.class})
public interface LocationExclusionMapper extends EntityMapper<LocationExclusionDTO, LocationExclusion> {

    @Mapping(source = "config.id", target = "configId")
    LocationExclusionDTO toDto(LocationExclusion locationExclusion);

    @Mapping(source = "configId", target = "config")
    LocationExclusion toEntity(LocationExclusionDTO locationExclusionDTO);

    default LocationExclusion fromId(UUID id) {
        if (id == null) {
            return null;
        }
        LocationExclusion locationExclusion = new LocationExclusion();
        locationExclusion.setId(id);
        return locationExclusion;
    }
}
