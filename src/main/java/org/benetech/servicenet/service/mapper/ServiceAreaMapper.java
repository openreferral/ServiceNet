package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.ServiceArea;
import org.benetech.servicenet.service.dto.ServiceAreaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity ServiceArea and its DTO ServiceAreaDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface ServiceAreaMapper extends EntityMapper<ServiceAreaDTO, ServiceArea> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    ServiceAreaDTO toDto(ServiceArea serviceArea);

    @Mapping(source = "srvcId", target = "srvc")
    ServiceArea toEntity(ServiceAreaDTO serviceAreaDTO);

    default ServiceArea fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ServiceArea serviceArea = new ServiceArea();
        serviceArea.setId(id);
        return serviceArea;
    }
}
