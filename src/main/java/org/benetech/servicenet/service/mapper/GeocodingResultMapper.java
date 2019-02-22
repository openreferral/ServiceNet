package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.service.dto.GeocodingResultDTO;
import org.mapstruct.Mapper;

import java.util.UUID;

/**
 * Mapper for the entity GeocodingResult and its DTO GeocodingResultDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeocodingResultMapper extends EntityMapper<GeocodingResultDTO, GeocodingResult> {

    default GeocodingResult fromId(UUID id) {
        if (id == null) {
            return null;
        }
        GeocodingResult geocodingResult = new GeocodingResult();
        geocodingResult.setId(id);
        return geocodingResult;
    }
}
