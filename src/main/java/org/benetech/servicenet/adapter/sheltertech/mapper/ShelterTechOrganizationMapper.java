package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.sheltertech.model.AddressRaw;
import org.benetech.servicenet.adapter.sheltertech.model.OrganizationRaw;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public interface ShelterTechOrganizationMapper {

    ShelterTechOrganizationMapper INSTANCE = Mappers.getMapper(ShelterTechOrganizationMapper.class);

    Logger LOG = LoggerFactory.getLogger(ShelterTechOrganizationMapper.class);

    default Organization mapToOrganization(OrganizationRaw orgRaw, DocumentUpload documentUpload) {
        if (StringUtils.isBlank(orgRaw.getName())) {
            LOG.warn("Organization name is empty for organization with ID: " + orgRaw.getId());
        }

        return toOrganization(orgRaw, documentUpload);
    }

    @Named("activeFromStatus")
    default Boolean activeFromStatus(String status) {
        final String approved = "approved";
        if (StringUtils.isNotBlank(status)) {
            return StringUtils.equals(status, approved);
        }

        return false;
    }

    @Named("locationFromAddressRaw")
    default Set<Location> locationFromAddressRaw(AddressRaw raw) {
        if (raw == null) {
            return Collections.emptySet();
        }
        return ShelterTechLocationMapper.INSTANCE.mapToLocations(Collections.singletonList(raw));
    }

    @Named("emailFromString")
    default String emailFromString(String emailString) {
        // pattern detects valid e-mail addresses
        Pattern email = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}", Pattern.CASE_INSENSITIVE);

        if (StringUtils.isNotBlank(emailString)) {
            Matcher matcher = email.matcher(emailString);

            // TODO: Consider supporting many e-mail addresses
            if (matcher.find()) {
                return matcher.group();
            }
        }

        return null;
    }

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "orgRaw.name", target = "name")
    @Mapping(source = "orgRaw.alternateName", target = "alternateName")
    @Mapping(source = "orgRaw.longDescription", target = "description")
    @Mapping(source = "orgRaw.email", target = "email", qualifiedByName = "emailFromString")
    @Mapping(source = "orgRaw.website", target = "url")
    @Mapping(ignore = true, target = "taxStatus")
    @Mapping(ignore = true, target = "taxId")
    @Mapping(ignore = true, target = "yearIncorporated")
    @Mapping(source = "orgRaw.legalStatus", target = "legalStatus")
    @Mapping(source = "orgRaw.status", target = "active", qualifiedByName = "activeFromStatus")
    @Mapping(source = "orgRaw.id", target = "externalDbId")
    @Mapping(source = "orgRaw.address", target = "locations", qualifiedByName = "locationFromAddressRaw")
    @Mapping(ignore = true, target = "replacedBy")
    @Mapping(source = "documentUpload", target = "sourceDocument")
    @Mapping(ignore = true, target = "account")
    @Mapping(ignore = true,  target = "funding")
    @Mapping(ignore = true, target = "programs")
    @Mapping(ignore = true, target = "services")
    Organization toOrganization(OrganizationRaw orgRaw, DocumentUpload documentUpload);
}
