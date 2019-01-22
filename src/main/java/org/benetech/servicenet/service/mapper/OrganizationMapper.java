package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity Organization and its DTO OrganizationDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, DocumentUploadMapper.class, SystemAccountMapper.class},
    unmappedTargetPolicy = IGNORE)
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "replacedBy.id", target = "replacedById")
    @Mapping(source = "sourceDocument.id", target = "sourceDocumentId")
    @Mapping(source = "sourceDocument.dateUploaded", target = "sourceDocumentDateUploaded")
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.name", target = "accountName")
    OrganizationDTO toDto(Organization organization);

    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "replacedById", target = "replacedBy")
    @Mapping(source = "sourceDocumentId", target = "sourceDocument")
    @Mapping(source = "accountId", target = "account")
    @Mapping(target = "funding", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "services", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);

    default Organization fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}
