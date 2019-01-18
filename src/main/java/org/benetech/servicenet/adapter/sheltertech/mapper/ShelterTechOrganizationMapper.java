package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.sheltertech.model.OrganizationRaw;
import org.benetech.servicenet.domain.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import static org.benetech.servicenet.adapter.sheltertech.ShelterTechConstants.PROVIDER_NAME;

@Mapper
public interface ShelterTechOrganizationMapper {

    ShelterTechOrganizationMapper INSTANCE = Mappers.getMapper(ShelterTechOrganizationMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "alternateName", target = "alternateName")
    @Mapping(source = "longDescription", target = "description")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "website", target = "url")
    @Mapping(ignore = true, target = "taxStatus")
    @Mapping(ignore = true, target = "taxId")
    @Mapping(ignore = true, target = "yearIncorporated")
    @Mapping(source = "legalStatus", target = "legalStatus")
    @Mapping(source = "status", target = "active", qualifiedByName = "activeFromStatus")
    @Mapping(source = "id", target = "externalDbId")
    @Mapping(constant = PROVIDER_NAME, target = "providerName")
//    @Mapping(source = "address", target = "location") // TODO AddressRaw
    @Mapping(ignore = true, target = "location")
    @Mapping(ignore = true, target = "replacedBy") // TODO
    @Mapping(ignore = true, target = "sourceDocument") // TODO
    @Mapping(ignore = true, target = "account") // TODO
    @Mapping(ignore = true,  target = "funding")
    @Mapping(ignore = true, target = "programs")
//    @Mapping(source = "services", target = "services") // TODO ServiceRaw
    @Mapping(ignore = true, target = "services")
    Organization mapToOrganization(OrganizationRaw orgRaw);

    @Named("activeFromStatus")
    default Boolean activeFromStatus(String status) {
        final String APPROVED = "approved";
        if (StringUtils.isNotBlank(status)) {
            if (StringUtils.equals(status, APPROVED)) {
                return true;
            }
        }

        return false;
    }
}
