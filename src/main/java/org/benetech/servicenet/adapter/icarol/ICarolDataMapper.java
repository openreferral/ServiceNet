package org.benetech.servicenet.adapter.icarol;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.icarol.model.ICarolAccessibility;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolContact;
import org.benetech.servicenet.adapter.icarol.model.ICarolContactDetails;
import org.benetech.servicenet.adapter.icarol.model.ICarolDay;
import org.benetech.servicenet.adapter.icarol.model.ICarolHours;
import org.benetech.servicenet.adapter.icarol.model.ICarolName;
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
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface ICarolDataMapper {

    String LISTS_DELIMITER = "; ";

    ICarolDataMapper INSTANCE = Mappers.getMapper(ICarolDataMapper.class);
    String PRIMARY = "Primary";
    String PHONE_NUMBER = "PhoneNumber";
    String EMAIL_ADDRESS = "EmailAddress";
    String WEBSITE = "Website";
    String PHYSICAL_LOCATION = "PhysicalLocation";
    String POSTAL_ADDRESS = "PostalAddress";
    String ACTIVE = "Active";

    @Mapping(source = "disabled", target = "accessibility")
    AccessibilityForDisabilities mapAccessibility(ICarolAccessibility accessibility);

    @Mapping(source = "line1", target = "address1")
    @Mapping(source = "zipPostalCode", target = "postalCode")
    PhysicalAddress mapToPhysicalAddress(ICarolContact contact);

    @Mapping(source = "line1", target = "address1")
    @Mapping(source = "zipPostalCode", target = "postalCode")
    PostalAddress mapToPostalAddress(ICarolContact contact);

    Phone mapContactToPhone(ICarolContact contact);

    default Organization extractOrganization(ICarolAgency agency, String dbId, String providerName) {
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

    default Service extractService(ICarolProgram program, String dbId, String providerName) {
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

    default Set<OpeningHours> extractOpeningHours(ICarolHours hours) {
        if (hours == null || hours.getDays() == null) {
            return new HashSet<>();
        }
        ICarolDay[] days = hours.getDays();
        return Arrays.stream(days).map(day -> new OpeningHours()
            .weekday(getIdByTheWeekday(day.getDayOfWeek()))
            .opensAt(day.getOpens())
            .closesAt(day.getCloses()))
            .collect(Collectors.toSet());
    }

    default Optional<AccessibilityForDisabilities> extractAccessibilityForDisabilities(ICarolSite site) {
        return site.getAccessibility() != null && site.getAccessibility().getDisabled() != null
            ? Optional.of(mapAccessibility(site.getAccessibility()))
            : Optional.empty();
    }

    default int getIdByTheWeekday(String weekday) {
        return ICarolWeekday.valueOf(weekday.toUpperCase(Locale.ROOT)).getNumber();
    }

    default Set<Phone> extractPhones(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHONE_NUMBER))
            .map(entry -> mapContactToPhone(entry.getContact()))
            .collect(Collectors.toSet());
    }

    default Location extractLocation(ICarolContact contact, String dbId, String providerName) {
        Location result = new Location().name(extractLocationName(contact));
        result.setLatitude(contact.getLatitude());
        result.setLongitude(contact.getLongitude());
        result.setExternalDbId(dbId);
        result.setProviderName(providerName);
        return result;
    }

    default Optional<Location> extractLocation(ICarolContactDetails[] contactDetails, String dbId, String providerName) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(entry -> extractLocation(entry.getContact(), dbId, providerName));
    }

    private String extractLocationName(ICarolContact contact) {
        return LocationUtils.buildLocationName(contact.getCity(), contact.getStateProvince(), contact.getLine1());
    }

    default Set<Language> extractLangs(ICarolProgram program) {
        String[] langs = program.getLanguagesOffered().split(LISTS_DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang)).collect(Collectors.toSet());
    }

    default Optional<Eligibility> extractEligibility(ICarolProgram program) {
        return StringUtils.isNotBlank(program.getEligibility())
            ? Optional.of(new Eligibility().eligibility(program.getEligibility()))
            : Optional.empty();
    }

    default Optional<PhysicalAddress> extractPhysicalAddress(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(entry -> mapToPhysicalAddress(entry.getContact()));
    }

    default Optional<PostalAddress> extractPostalAddress(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(POSTAL_ADDRESS))
            .findFirst().map(entry -> mapToPostalAddress(entry.getContact()));
    }

    default String extractEmail(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(EMAIL_ADDRESS))
            .findFirst().map(entry -> entry.getContact().getAddress().replace(" ", ""))
            .orElse(null);
    }

    default String extractUrl(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(WEBSITE))
            .findFirst().map(entry -> entry.getContact().getUrl().replace(" ", ""))
            .orElse(null);
    }

    private String extractName(ICarolName[] names) {
        for (ICarolName name : names) {
            if (name.getPurpose().equals(PRIMARY)) {
                return name.getValue();
            }
        }
        throw new IllegalArgumentException("No primary name found");
    }

    private String extractAlternateName(ICarolName[] names) {
        return Arrays.stream(names)
            .filter(name -> !name.getPurpose().equals(PRIMARY))
            .findFirst().map(ICarolName::getValue)
            .orElse(null);
    }
}
