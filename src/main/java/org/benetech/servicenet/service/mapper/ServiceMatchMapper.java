package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.ServiceMatch;
import org.benetech.servicenet.service.dto.ServiceMatchDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity ServiceMatch and its DTO ServiceMatchDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ServiceMatchMapper extends EntityMapper<ServiceMatchDto, ServiceMatch> {

    @Mapping(target = "orgId", source = "matchingService.organization.id")
    @Mapping(target = "serviceName", source = "matchingService.name")
    @Mapping(target = "organizationName", source = "matchingService.organization.name")
    @Mapping(source = "service.id", target = "service")
    @Mapping(source = "matchingService.id", target = "matchingService")
    ServiceMatchDto toDto(ServiceMatch serviceMatch);

    @Mapping(source = "service", target = "service")
    @Mapping(source = "matchingService", target = "matchingService")
    ServiceMatch toEntity(ServiceMatchDto serviceMatchDto);

    default ServiceMatch fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ServiceMatch serviceMatch = new ServiceMatch();
        serviceMatch.setId(id);
        return serviceMatch;
    }
}
