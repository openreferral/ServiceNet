package org.benetech.servicenet.service.mapper;


import java.util.UUID;
import org.benetech.servicenet.domain.*;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DailyUpdate} and its DTO {@link DailyUpdateDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class})
public interface DailyUpdateMapper extends EntityMapper<DailyUpdateDTO, DailyUpdate> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    DailyUpdateDTO toDto(DailyUpdate dailyUpdate);

    @Mapping(source = "organizationId", target = "organization")
    DailyUpdate toEntity(DailyUpdateDTO dailyUpdateDTO);

    default DailyUpdate fromId(UUID id) {
        if (id == null) {
            return null;
        }
        DailyUpdate dailyUpdate = new DailyUpdate();
        dailyUpdate.setId(id);
        return dailyUpdate;
    }
}
