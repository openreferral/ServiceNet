package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.provider.ProviderOpeningHoursDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity OpeningHours and its DTO OpeningHoursDTO.
 */
@Mapper(componentModel = "spring", uses = {RegularScheduleMapper.class}, unmappedTargetPolicy = IGNORE)
public interface OpeningHoursMapper extends EntityMapper<OpeningHoursDTO, OpeningHours> {

    @Mapping(source = "regularSchedule.id", target = "regularScheduleId")
    OpeningHoursDTO toDto(OpeningHours openingHours);

    ProviderOpeningHoursDTO toProviderDto(OpeningHours openingHours);

    @Mapping(source = "regularScheduleId", target = "regularSchedule")
    OpeningHours toEntity(OpeningHoursDTO openingHoursDTO);

    default OpeningHours fromId(UUID id) {
        if (id == null) {
            return null;
        }
        OpeningHours openingHours = new OpeningHours();
        openingHours.setId(id);
        return openingHours;
    }
}
