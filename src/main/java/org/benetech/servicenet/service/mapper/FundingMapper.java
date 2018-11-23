package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity Funding and its DTO FundingDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, ServiceMapper.class})
public interface FundingMapper extends EntityMapper<FundingDTO, Funding> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    FundingDTO toDto(Funding funding);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "srvcId", target = "srvc")
    Funding toEntity(FundingDTO fundingDTO);

    default Funding fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Funding funding = new Funding();
        funding.setId(id);
        return funding;
    }
}
