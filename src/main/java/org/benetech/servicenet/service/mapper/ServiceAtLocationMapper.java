package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.service.dto.ServiceAtLocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity ServiceAtLocation and its DTO ServiceAtLocationDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class, LocationMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ServiceAtLocationMapper extends EntityMapper<ServiceAtLocationDTO, ServiceAtLocation> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    ServiceAtLocationDTO toDto(ServiceAtLocation serviceAtLocation);

    @Mapping(source = "srvcId", target = "srvc")
    @Mapping(source = "locationId", target = "location")
    ServiceAtLocation toEntity(ServiceAtLocationDTO serviceAtLocationDTO);

    default ServiceAtLocation fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ServiceAtLocation serviceAtLocation = new ServiceAtLocation();
        serviceAtLocation.setId(id);
        return serviceAtLocation;
    }
}
