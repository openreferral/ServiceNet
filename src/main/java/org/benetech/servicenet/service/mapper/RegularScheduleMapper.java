package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity RegularSchedule and its DTO RegularScheduleDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class, LocationMapper.class, ServiceAtLocationMapper.class})
public interface RegularScheduleMapper extends EntityMapper<RegularScheduleDTO, RegularSchedule> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "serviceAtlocation.id", target = "serviceAtlocationId")
    RegularScheduleDTO toDto(RegularSchedule regularSchedule);

    @Mapping(source = "srvcId", target = "srvc")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "serviceAtlocationId", target = "serviceAtlocation")
    RegularSchedule toEntity(RegularScheduleDTO regularScheduleDTO);

    default RegularSchedule fromId(Long id) {
        if (id == null) {
            return null;
        }
        RegularSchedule regularSchedule = new RegularSchedule();
        regularSchedule.setId(id);
        return regularSchedule;
    }
}
