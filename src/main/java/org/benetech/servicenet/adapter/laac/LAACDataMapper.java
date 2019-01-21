package org.benetech.servicenet.adapter.laac;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.laac.model.LAACData;
import org.benetech.servicenet.adapter.laac.utils.PhysicalAddressFormatUtil;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LAACDataMapper {

    LAACDataMapper INSTANCE = Mappers.getMapper(LAACDataMapper.class);

    DateTimeFormatter YEAR_INCORPORATED_FORMATTER = DateTimeFormatter.ofPattern("EEE, MM/dd/yyyy - HH:mm");
    String LANGUAGES_DELIMITER = ",";
    String SERVICE_POSTFIX = " - Service";
    String LOCATION_POSTFIX = " - Location";

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", source = "phone")
    Phone extractPhone(LAACData data);

    default Eligibility extractEligibility(LAACData data) {
        String eligibilityString = data.getEligibility();
        if (StringUtils.isBlank(eligibilityString)) {
            return null;
        }
        Eligibility eligibility = new Eligibility();
        eligibility.setEligibility(eligibilityString);
        return eligibility;
    }

    default Contact extractContact(LAACData data) {
        String contactName = data.getContactName();
        if (StringUtils.isBlank(contactName)) {
            return null;
        }
        Contact contact = new Contact();
        contact.setName(contactName);
        return contact;
    }

    default Organization extractOrganization(LAACData data) {
        Organization organization = new Organization();

        organization.setName(data.getOrganizationName());
        organization.setYearIncorporated(LocalDate.parse(data.getAuthoredOn(), YEAR_INCORPORATED_FORMATTER));
        organization.setUrl(data.getWebsite());
        organization.setDescription(data.getWhoWeAre());
        organization.setExternalDbId(data.getId());
        organization.setActive(true);

        return organization;
    }

    default PhysicalAddress extractPhysicalAddress(LAACData data) {
        return PhysicalAddressFormatUtil.resolveAddress(data.getAddress());
    }

    default Set<Language> extractLanguages(LAACData data) {
        String[] languages = data.getSpokenLanguages().split(LANGUAGES_DELIMITER);
        return Arrays.stream(languages)
            .map(language -> new Language()
            .language(language.trim()))
            .collect(Collectors.toSet());
    }

    default Service extractService(LAACData data) {
       Service service = new Service();

       service.setName(data.getOrganizationName() + SERVICE_POSTFIX);
       service.setType(data.getServiceTypes());
       service.setDescription(data.getDescriptionOfServiceTypes());

       return service;
    }

    default Location extractLocation(LAACData data) {
        Location location = new Location();

        location.name(data.getOrganizationName() + LOCATION_POSTFIX);
        location.setDescription(data.getAreasServed());

        return location;
    }
}
