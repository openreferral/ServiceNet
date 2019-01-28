package org.benetech.servicenet.adapter.icarol;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.icarol.model.ICarolAccessibility;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolContact;
import org.benetech.servicenet.adapter.icarol.model.ICarolContactDetails;
import org.benetech.servicenet.adapter.icarol.model.ICarolDay;
import org.benetech.servicenet.adapter.icarol.model.ICarolHours;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.adapter.icarol.model.ICarolWeekday;
import org.benetech.servicenet.adapter.shared.MapperUtils;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for ICarolData objects
 *
 * IMPORTANT: Some objects might be confidential and we should not lose that information.
 *            Each mapping method should use auto-generated version, or should set this information manually.
 *            @see ICarolConfidentialFieldsMapper for mapping fields that might be confidential as well as the whole object.
 */
@Mapper(unmappedTargetPolicy = IGNORE)
public interface ICarolDataMapper extends ICarolConfidentialFieldsMapper {

    String LISTS_DELIMITER = "; ";

    ICarolDataMapper INSTANCE = Mappers.getMapper(ICarolDataMapper.class);
    String PRIMARY = "Primary";
    String PHONE_NUMBER = "PhoneNumber";
    String PHYSICAL_LOCATION = "PhysicalLocation";
    String POSTAL_ADDRESS = "PostalAddress";
    String ACTIVE = "Active";
    String NAME = "name";
    String URL = "url";

    @Mapping(source = "disabled", target = "accessibility")
    @Mapping(target = "id", ignore = true)
    AccessibilityForDisabilities mapAccessibility(ICarolAccessibility accessibility);

    @Mapping(source = "contact.line1", target = "address1")
    @Mapping(source = "contact.zipPostalCode", target = "postalCode")
    @Mapping(source = "contact.country", target = "country")
    @Mapping(source = "contact.stateProvince", target = "stateProvince")
    @Mapping(source = "contact.city", target = "city")
    @Mapping(target = "id", ignore = true)
    PhysicalAddress mapToPhysicalAddress(ICarolContactDetails details);

    @Mapping(source = "contact.line1", target = "address1")
    @Mapping(source = "contact.zipPostalCode", target = "postalCode")
    @Mapping(source = "contact.country", target = "country")
    @Mapping(source = "contact.stateProvince", target = "stateProvince")
    @Mapping(source = "contact.city", target = "city")
    @Mapping(target = "id", ignore = true)
    PostalAddress mapToPostalAddress(ICarolContactDetails details);

    @Mapping(target = "name", source = "contact", qualifiedByName = "locationName")
    @Mapping(target = "description", source = "contact.description")
    @Mapping(target = "longitude", source = "contact.longitude")
    @Mapping(target = "latitude", source = "contact.latitude")
    @Mapping(target = "id", ignore = true)
    Location mapToLocation(ICarolContactDetails details);

    @Mapping(target = "number", source = "contact.number")
    @Mapping(target = "type", source = "contact.type")
    @Mapping(target = "description", source = "contact.description")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    Phone mapContactToPhone(ICarolContactDetails details);

    @Mapping(target = "name", source = "names", qualifiedByName = "name")
    @Mapping(target = "alternateName", source = "names", qualifiedByName = "alternateName")
    @Mapping(target = "email", source = "contactDetails", qualifiedByName = "email")
    @Mapping(target = "url", source = "contactDetails", qualifiedByName = "url")
    @Mapping(target = "externalDbId", source = "id")
    @Mapping(target = "id", ignore = true)
    Organization mapOrganization(ICarolAgency agency);

    @Mapping(target = "name", source = "names", qualifiedByName = "name")
    @Mapping(target = "alternateName", source = "names", qualifiedByName = "alternateName")
    @Mapping(target = "email", source = "contactDetails", qualifiedByName = "email")
    @Mapping(target = "url", source = "contactDetails", qualifiedByName = "url")
    @Mapping(target = "externalDbId", source = "id")
    @Mapping(target = "description", source = "descriptionText")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eligibility", ignore = true)
    Service mapService(ICarolProgram program);

    @Mapping(target = "id", ignore = true)
    Eligibility mapEligibility(ICarolProgram program);

    @Mapping(target = "weekday", source = "dayOfWeek", qualifiedByName = "weekday")
    @Mapping(target = "opensAt", source = "opens")
    @Mapping(target = "closesAt", source = "closes")
    @Mapping(target = "id", ignore = true)
    OpeningHours mapOpeningHours(ICarolDay day);

    default Location mapToLocation(ICarolContactDetails details, String dbId, String providerName) {
        Location result = mapToLocation(details);
        result.setExternalDbId(dbId);
        result.setProviderName(providerName);
        return result;
    }

    @Named("locationName")
    default String extractLocationNameIfNotConfidential(ICarolContact contact) {
        return LocationUtils.buildLocationName(contact.getCity(), contact.getStateProvince(), contact.getLine1());
    }

    default Organization extractOrganization(ICarolAgency agency, String providerName) {
        Organization result = mapOrganization(agency);
        result.setActive(agency.getStatus().equals(ACTIVE));
        result.setProviderName(providerName);
        return result;
    }

    default Service extractService(ICarolProgram program, String providerName) {
        Service result = mapService(program);
        result.setApplicationProcess(
            MapperUtils.joinNotBlank(" ", program.getApplicationProcess(),
            "Required items: " + program.getRequiredDocumentation()));
        result.setProviderName(providerName);
        return result;
    }

    default Set<OpeningHours> extractOpeningHours(ICarolHours hours) {
        if (hours == null || hours.getDays() == null || BooleanUtils.isTrue(hours.getIsConfidential())) {
            return new HashSet<>();
        }
        ICarolDay[] days = hours.getDays();
        return Arrays.stream(days)
            .map(this::mapOpeningHours)
            .collect(Collectors.toSet());
    }

    default Optional<AccessibilityForDisabilities> extractAccessibilityForDisabilities(ICarolSite site) {
        return site.getAccessibility() != null && site.getAccessibility().getDisabled() != null
            ? Optional.of(mapAccessibility(site.getAccessibility()))
            : Optional.empty();
    }

    default Set<Phone> extractPhones(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHONE_NUMBER))
            .map(this::mapContactToPhone)
            .collect(Collectors.toSet());
    }

    default Optional<Location> extractLocation(ICarolContactDetails[] contactDetails, String dbId, String providerName) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(entry -> mapToLocation(entry, dbId, providerName));
    }

    default Set<Language> extractLangs(ICarolProgram program) {
        if (BooleanUtils.isTrue(program.getIsConfidential())) {
            return new HashSet<>();
        }
        String[] langs = program.getLanguagesOffered().split(LISTS_DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang))
            .collect(Collectors.toSet());
    }

    default Optional<Eligibility> extractEligibility(ICarolProgram program) {
        return StringUtils.isNotBlank(program.getEligibility())
            ? Optional.of(mapEligibility(program))
            : Optional.empty();
    }

    default Optional<PhysicalAddress> extractPhysicalAddress(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(this::mapToPhysicalAddress);
    }

    default Optional<PostalAddress> extractPostalAddress(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(POSTAL_ADDRESS))
            .findFirst().map(this::mapToPostalAddress);
    }

    @Named("weekday")
    default int getIdByTheWeekday(String weekday) {
        return ICarolWeekday.valueOf(weekday.toUpperCase(Locale.ROOT)).getNumber();
    }
}
