package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity HolidaySchedule and its DTO HolidayScheduleDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class, LocationMapper.class},
    unmappedTargetPolicy = IGNORE)
public interface HolidayScheduleMapper extends EntityMapper<HolidayScheduleDTO, HolidaySchedule> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    HolidayScheduleDTO toDto(HolidaySchedule holidaySchedule);

    @Mapping(source = "srvcId", target = "srvc")
    @Mapping(source = "locationId", target = "location")
    HolidaySchedule toEntity(HolidayScheduleDTO holidayScheduleDTO);

    default HolidaySchedule fromId(UUID id) {
        if (id == null) {
            return null;
        }
        HolidaySchedule holidaySchedule = new HolidaySchedule();
        holidaySchedule.setId(id);
        return holidaySchedule;
    }
}
