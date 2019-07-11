package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.benetech.servicenet.service.dto.ServiceRecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity Service and its DTO ServiceDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, ProgramMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ServiceMapper extends EntityMapper<ServiceDTO, Service> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "program.id", target = "programId")
    @Mapping(source = "program.name", target = "programName")
    ServiceDTO toDto(Service service);

    @Mapping(target = "service", source = "service")
    @Mapping(target = "regularScheduleOpeningHours", source = "regularSchedule.openingHours")
    ServiceRecordDTO toRecord(Service service);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "programId", target = "program")
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "regularSchedule", ignore = true)
    @Mapping(target = "holidaySchedules", ignore = true)
    @Mapping(target = "funding", ignore = true)
    @Mapping(target = "eligibility", ignore = true)
    @Mapping(target = "areas", ignore = true)
    @Mapping(target = "docs", ignore = true)
    @Mapping(target = "paymentsAccepteds", ignore = true)
    @Mapping(target = "langs", ignore = true)
    @Mapping(target = "taxonomies", ignore = true)
    @Mapping(target = "phones", ignore = true)
    Service toEntity(ServiceDTO serviceDTO);

    default Service fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Service service = new Service();
        service.setId(id);
        return service;
    }
}
