package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity OrganizationMatch and its DTO OrganizationMatchDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, unmappedTargetPolicy = IGNORE)
public interface OrganizationMatchMapper extends EntityMapper<OrganizationMatchDTO, OrganizationMatch> {

    @Mapping(source = "organizationRecord.id", target = "organizationRecordId")
    @Mapping(source = "organizationRecord.name", target = "organizationRecordName")
    @Mapping(source = "partnerVersion.id", target = "partnerVersionId")
    @Mapping(source = "partnerVersion.name", target = "partnerVersionName")
    OrganizationMatchDTO toDto(OrganizationMatch organizationMatch);

    @Mapping(source = "organizationRecordId", target = "organizationRecord")
    @Mapping(source = "partnerVersionId", target = "partnerVersion")
    OrganizationMatch toEntity(OrganizationMatchDTO organizationMatchDTO);

    default OrganizationMatch fromId(UUID id) {
        if (id == null) {
            return null;
        }
        OrganizationMatch organizationMatch = new OrganizationMatch();
        organizationMatch.setId(id);
        return organizationMatch;
    }
}
