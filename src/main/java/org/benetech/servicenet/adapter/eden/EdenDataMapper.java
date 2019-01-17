package org.benetech.servicenet.adapter.eden;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.eden.model.EdenAccessibility;
import org.benetech.servicenet.adapter.eden.model.EdenAgency;
import org.benetech.servicenet.adapter.eden.model.EdenContact;
import org.benetech.servicenet.adapter.eden.model.EdenContactDetails;
import org.benetech.servicenet.adapter.eden.model.EdenDay;
import org.benetech.servicenet.adapter.eden.model.EdenHours;
import org.benetech.servicenet.adapter.eden.model.EdenName;
import org.benetech.servicenet.adapter.eden.model.EdenProgram;
import org.benetech.servicenet.adapter.eden.model.EdenSite;
import org.benetech.servicenet.adapter.eden.model.EdenWeekday;
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
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface EdenDataMapper {

    String LISTS_DELIMITER = "; ";

    EdenDataMapper INSTANCE = Mappers.getMapper(EdenDataMapper.class);
    String PRIMARY = "Primary";
    String PHONE_NUMBER = "PhoneNumber";
    String EMAIL_ADDRESS = "EmailAddress";
    String WEBSITE = "Website";
    String PHYSICAL_LOCATION = "PhysicalLocation";
    String POSTAL_ADDRESS = "PostalAddress";
    String ACTIVE = "Active";

    @Mapping(source = "disabled", target = "accessibility")
    AccessibilityForDisabilities mapAccessibility(EdenAccessibility accessibility);

    @Mapping(source = "line1", target = "address1")
    @Mapping(source = "zipPostalCode", target = "postalCode")
    PhysicalAddress mapToPhysicalAddress(EdenContact contact);

    @Mapping(source = "line1", target = "address1")
    @Mapping(source = "zipPostalCode", target = "postalCode")
    PostalAddress mapToPostalAddress(EdenContact contact);

    Phone mapContactToPhone(EdenContact contact);

    default Organization extractOrganization(EdenAgency agency, String dbId, String providerName) {
        Organization result = new Organization();

        result.setName(extractName(agency.getNames()));
        result.setAlternateName(extractAlternateName(agency.getNames()));
        result.setEmail(extractEmail(agency.getContactDetails()));
        result.setUrl(extractUrl(agency.getContactDetails()));
        result.setActive(agency.getStatus().equals(ACTIVE));
        result.setExternalDbId(dbId);
        result.setProviderName(providerName);

        return result;
    }

    default Service extractService(EdenProgram program, String dbId, String providerName) {
        Service result = new Service();

        result.setName(extractName(program.getNames()));
        result.setAlternateName(extractAlternateName(program.getNames()));
        result.setDescription(program.getDescriptionText());
        result.setEmail(extractEmail(program.getContactDetails()));
        result.setUrl(extractUrl(program.getContactDetails()));
        result.setFees(program.getFees());
        result.setApplicationProcess(MapperUtils.joinNotBlank(" ", program.getApplicationProcess(),
            "Required items: " + program.getRequiredDocumentation()));
        result.setExternalDbId(dbId);
        result.setProviderName(providerName);

        return result;
    }

    default Set<OpeningHours> extractOpeningHours(EdenHours hours) {
        if (hours == null || hours.getDays() == null) {
            return new HashSet<>();
        }
        EdenDay[] days = hours.getDays();
        return Arrays.stream(days).map(day -> new OpeningHours()
            .weekday(getIdByTheWeekday(day.getDayOfWeek()))
            .opensAt(day.getOpens())
            .closesAt(day.getCloses()))
            .collect(Collectors.toSet());
    }

    default Optional<AccessibilityForDisabilities> extractAccessibilityForDisabilities(EdenSite site) {
        return site.getAccessibility() != null && site.getAccessibility().getDisabled() != null
            ? Optional.of(mapAccessibility(site.getAccessibility()))
            : Optional.empty();
    }

    default int getIdByTheWeekday(String weekday) {
        return EdenWeekday.valueOf(weekday.toUpperCase(Locale.ROOT)).getNumber();
    }

    default Set<Phone> extractPhones(EdenContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHONE_NUMBER))
            .map(entry -> mapContactToPhone(entry.getContact()))
            .collect(Collectors.toSet());
    }

    default Location extractLocation(EdenContact contact, String dbId, String providerName) {
        Location result = new Location().name(extractLocationName(contact));
        result.setLatitude(contact.getLatitude());
        result.setLongitude(contact.getLongitude());
        result.setExternalDbId(dbId);
        result.setProviderName(providerName);
        return result;
    }

    default Optional<Location> extractLocation(EdenContactDetails[] contactDetails, String dbId, String providerName) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(entry -> extractLocation(entry.getContact(), dbId, providerName));
    }

    private String extractLocationName(EdenContact contact) {
        return LocationUtils.buildLocationName(contact.getCity(), contact.getStateProvince(), contact.getLine1());
    }

    default Set<Language> extractLangs(EdenProgram program) {
        String[] langs = program.getLanguagesOffered().split(LISTS_DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang)).collect(Collectors.toSet());
    }

    default Optional<Eligibility> extractEligibility(EdenProgram program) {
        return StringUtils.isNotBlank(program.getEligibility())
            ? Optional.of(new Eligibility().eligibility(program.getEligibility()))
            : Optional.empty();
    }

    default Optional<PhysicalAddress> extractPhysicalAddress(EdenContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(entry -> mapToPhysicalAddress(entry.getContact()));
    }

    default Optional<PostalAddress> extractPostalAddress(EdenContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(POSTAL_ADDRESS))
            .findFirst().map(entry -> mapToPostalAddress(entry.getContact()));
    }

    default String extractEmail(EdenContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(EMAIL_ADDRESS))
            .findFirst().map(entry -> entry.getContact().getAddress().replace(" ", ""))
            .orElse(null);
    }

    default String extractUrl(EdenContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(WEBSITE))
            .findFirst().map(entry -> entry.getContact().getUrl().replace(" ", ""))
            .orElse(null);
    }

    private String extractName(EdenName[] names) {
        for (EdenName name : names) {
            if (name.getPurpose().equals(PRIMARY)) {
                return name.getValue();
            }
        }
        throw new IllegalArgumentException("No primary name found");
    }

    private String extractAlternateName(EdenName[] names) {
        return Arrays.stream(names)
            .filter(name -> !name.getPurpose().equals(PRIMARY))
            .findFirst().map(EdenName::getValue)
            .orElse(null);
    }
}
