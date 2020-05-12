package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.external.RecordDetailsOrganizationDTO;
import org.benetech.servicenet.service.dto.provider.SimpleOrganizationDTO;
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

    @Mapping(source = "replacedBy.id", target = "replacedById")
    @Mapping(source = "sourceDocument.id", target = "sourceDocumentId")
    @Mapping(source = "sourceDocument.dateUploaded", target = "sourceDocumentDateUploaded")
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.name", target = "accountName")
    OrganizationDTO toDto(Organization organization);

    @Mapping(source = "replacedById", target = "replacedBy")
    @Mapping(source = "sourceDocumentId", target = "sourceDocument")
    @Mapping(source = "accountId", target = "account")
    @Mapping(target = "funding", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "userProfiles", ignore = true)
    Organization toEntity(OrganizationDTO organizationDTO);

    @Mapping(target = "funding", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "locations", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "userProfiles", ignore = true)
    Organization toEntity(SimpleOrganizationDTO organizationDTO);

    RecordDetailsOrganizationDTO toRecordDetailsDto(Organization organization);

    default Organization fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Organization organization = new Organization();
        organization.setId(id);
        return organization;
    }
}
