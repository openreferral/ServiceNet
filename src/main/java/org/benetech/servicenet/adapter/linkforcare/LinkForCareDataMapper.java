package org.benetech.servicenet.adapter.linkforcare;

import static org.benetech.servicenet.config.Constants.LINK_FOR_CARE_PROVIDER;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.linkforcare.model.LinkForCareData;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LinkForCareDataMapper {

    LinkForCareDataMapper INSTANCE = Mappers.getMapper(LinkForCareDataMapper.class);

    Logger LOG = LoggerFactory.getLogger(LinkForCareDataMapper.class);

    String YES = "YES";
    String[] TRUE_VALUES = {"TRUE", "1"};
    String[] FALSE_VALUES = {"FALSE", "0"};
    String AVAILABLE = "available";
    String SEMICOLON_REPLACEMENT = "__SEMICOLON__";

    default Optional<Phone> extractPhone(LinkForCareData data) {
        if (StringUtils.isBlank(data.getOrganizationPhoneNumber())) {
            return Optional.empty();
        }

        return Optional.of(toPhone(data));
    }

    default Optional<Phone> extractTollFreePhone(LinkForCareData data) {
        if (StringUtils.isBlank(data.getOrganizationPhoneNumberTollFree())) {
            return Optional.empty();
        }

        return Optional.of(toTollFreePhone(data));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", source = "organizationPhoneNumber")
    @Mapping(target = "type", constant = Phone.TYPE_PUBLIC)
    Phone toPhone(LinkForCareData data);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", source = "organizationPhoneNumberTollFree")
    @Mapping(target = "type", constant = Phone.TYPE_TOLL_FREE)
    Phone toTollFreePhone(LinkForCareData data);

    default Optional<Eligibility> extractEligibility(LinkForCareData data) {
        String eligibilityString = Stream.of(
            data.getServiceEligibility(),
            StringUtils.isNotBlank(data.getServiceEligibilityMinAge()) ? "Minimum Age Accepted: " + data.getServiceEligibilityMinAge() : null,
            isTrue(data.getServiceEligibilityMustBeAmbulatory()) ? "Must be ambulatory" : null,
            isTrue(data.getServiceEligibilityMustMeetAge()) ? "Must meet age guidelines" : null,
            isTrue(data.getServiceEligibilityMustMeetDisability()) ? "Must meet disability guidelines" : null,
            isTrue(data.getServiceEligibilityMustMeetIncome()) ? "Must meet income guidelines" : null,
            isTrue(data.getServiceEligibilityMustBeVeteran()) ? "Must be a veteran" : null,
            data.getServiceEligibilityOtherRequirements(),
            isTrue(data.getServiceEligibilitySocialSecurityCard()) ? "Social Security Card Required" : null
        ).filter(StringUtils::isNotBlank).collect(Collectors.joining(", "));
        if (StringUtils.isBlank(eligibilityString)) {
            return Optional.empty();
        }
        return Optional.of(new Eligibility().eligibility(eligibilityString));
    }

    default Organization extractOrganization(LinkForCareData data) {
        Organization organization = new Organization();

        if (StringUtils.isBlank(data.getOrganizationName())) {
            LOG.warn("Name is empty for organization with ID: " + data.getOrganizationId());
        } else {
            organization.setName(data.getOrganizationName());
        }
        if (StringUtils.isNotBlank(data.getOrganizationCreatedDate())) {
            organization.setYearIncorporated(ZonedDateTime.parse(data.getOrganizationCreatedDate()).toLocalDate());
        }
        if (StringUtils.isNotBlank(data.getOrganizationModifiedDate())) {
            organization.setUpdatedAt(ZonedDateTime.parse(data.getOrganizationModifiedDate()));
        }
        organization.setUrl(data.getOrganizationWebsite());
        organization.setDescription(data.getOrganizationDescription());
        organization.setExternalDbId(data.getOrganizationId());
        organization.setActive(true);
        organization.setEmail(data.getOrganizationMainEmailAddress());
        String covid19Protocols = Stream.of(data.getCovid19Protocols(), data.getCovid19ProtocolsFomula())
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.joining(", "));
        organization.setCovidProtocols(covid19Protocols);

        return organization;
    }

    default Location extractLocation(LinkForCareData data) {
        Location location = new Location();

        location.name(String.join(", ", data.getLocationAddress1(),
            data.getLocationCity(), data.getLocationState()));

        location.setExternalDbId(data.getOrganizationId());
        location.setProviderName(LINK_FOR_CARE_PROVIDER);
        location.setOpen247(StringUtils.equalsIgnoreCase(YES, data.getService247()));
        RegularSchedule regularSchedule = extractRegularSchedule(data);
        location.setRegularSchedule(regularSchedule);
        // TODO: set location remote service checkbox once #1321 is done

        return location;
    }

    default RegularSchedule extractRegularSchedule(LinkForCareData data) {
        RegularSchedule regularSchedule = new RegularSchedule();
        Set<OpeningHours> openingHours = Stream.of(
            extractOpeningHours(0, data.getLocationOpeningHoursSundayOpeningAt(), data.getLocationOpeningHoursSundayClosingAt()),
            extractOpeningHours(1, data.getLocationOpeningHoursMondayOpeningAt(), data.getLocationOpeningHoursMondayClosingAt()),
            extractOpeningHours(2, data.getLocationOpeningHoursTuesdayOpeningAt(), data.getLocationOpeningHoursTuesdayClosingAt()),
            extractOpeningHours(3, data.getLocationOpeningHoursWednesdayOpeningAt(), data.getLocationOpeningHoursWednesdayClosingAt()),
            extractOpeningHours(4, data.getLocationOpeningHoursThursDayOpeningAt(), data.getLocationOpeningHoursThursdayClosingAt()),
            extractOpeningHours(5, data.getLocationOpeningHoursFridayOpeningAt(), data.getLocationOpeningHoursFridayClosingAt()),
            extractOpeningHours(6, data.getLocationOpeningHoursSaturdayOpeningAt(), data.getLocationOpeningHoursSaturdayClosingAt())
        ).filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
        regularSchedule.setOpeningHours(openingHours);
        return regularSchedule;
    }

    default Optional<OpeningHours> extractOpeningHours(Integer weekday, String opensAt, String closesAt) {
        if (StringUtils.isNotBlank(opensAt) || StringUtils.isNotBlank(closesAt)) {
            OpeningHours openingHours = new OpeningHours();
            openingHours.setOpensAt(opensAt);
            openingHours.setClosesAt(closesAt);
            openingHours.setWeekday(weekday);
            return Optional.of(openingHours);
        }
        return Optional.empty();
    }

    default Optional<PhysicalAddress> extractPhysicalAddress(LinkForCareData data) {
        PhysicalAddress physicalAddress = new PhysicalAddress();
        physicalAddress.setAddress1(data.getLocationAddress1());
        physicalAddress.setAddress2(data.getLocationAddress2());
        physicalAddress.setCity(data.getLocationCity());
        physicalAddress.setCountry(data.getLocationCounty());
        physicalAddress.setStateProvince(data.getLocationState());
        physicalAddress.setPostalCode(data.getLocationZipCode());
        return Optional.of(physicalAddress);
    }

    default Service extractService(LinkForCareData data, Location location) {
        Service service = new Service();

        ServiceAtLocation serviceAtLocation = new ServiceAtLocation();
        serviceAtLocation.setLocation(location);
        serviceAtLocation.setProviderName(LINK_FOR_CARE_PROVIDER);
        service.setLocations(Set.of(serviceAtLocation));
        service.setExternalDbId(data.getOrganizationId());
        service.setProviderName(LINK_FOR_CARE_PROVIDER);

        service.setName(data.getServiceName());
        service.setApplicationProcess(data.getServiceApplicationProcess());
        service.setDocs(extractRequiredDocuments(data));
        service.setDescription(extractServiceDescription(data));

        service.setEligibility(extractEligibility(data).orElse(null));

        return service;
    }

    default Set<RequiredDocument> extractRequiredDocuments(LinkForCareData data) {
        return Stream.of(data.getServiceRequiredDocuments(), data.getServiceRequiredDocumentOthers())
            .filter(StringUtils::isNotBlank)
            .map(doc -> {
                RequiredDocument document = new RequiredDocument();
                document.setDocument(doc);
                return document;
            })
            .collect(Collectors.toSet());
    }

    default Set<String> extractServiceTypes(LinkForCareData data) {
        return Stream.of(
            data.getServiceTypeOther(),
            data.getServiceTypesProvided(),
            data.getServiceTypesProvidedHealthClinics(),
            data.getServiceTypesProvidedOmh(),
            isTrue(data.getServiceTypeDialysis()) ? "Renal Dialysis" : null,
            isTrue(data.getServiceTypeResidential()) ? "Residential Health Care" : null,
            isTrue(data.getServiceTypeRespiratory()) ? "Respiratory Therapists" : null,
            isTrue(data.getServiceTypeRespite()) ? "Respite Care" : null,
            isTrue(data.getServiceTypeSkilledNursing()) ? "Skilled Nursing and Therapy Services" : null,
            isTrue(data.getServiceTypeTransportation()) ? "Transportation" : null,
            isTrue(data.getServiceTypeVeterans()) ? "Veterans" : null,
            isTrue(data.getServiceTypeVocational()) ? "Vocational Work Program" : null,
            isTrue(data.getServiceTypeWellness()) ? "Wellness Facility" : null
        ).filter(StringUtils::isNotBlank)
            .flatMap(s -> Arrays.stream(s.split(";")))
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
    }

    default String extractServiceDescription(LinkForCareData data) {
        return Stream.of(
            data.getServiceDescriptionAge(),
            data.getServiceDescriptionAgeExceptions(),
            prefixIfNotBlank(data.getServiceDescriptionConditionsTreated(), "Conditions Treated"),
            prefixIfNotBlank(data.getServiceDescriptionContiesServed(), "Counties Served"),
            availableIfTrue(data.getServiceDescriptionInCenterHemodialysis(), "In Center Hemodialysis"),
            availableIfTrue(data.getServiceDescriptionInCenterNocturnalDialysis(), "In Center Nocturnal Dialysis"),
            availableIfTrue(data.getServiceDescriptionInHome(), "In Home Non Medical Services"),
            availableIfTrue(data.getServiceDescriptionInUnit(), "In Unit Washer and Dryer"),
            prefixIfNotBlank(data.getServiceDescriptionKansasCountiesServed(), "Kansas Counties Served"),
            prefixIfNotBlank(data.getServiceDescriptionLanguagesSpoken(), "Languages Spoken By Staff"),
            availableIfNotBlank(data.getServiceDescriptionLaundry(), "Laundry Services"),
            availableIfNotBlank(data.getServiceDescriptionLengthOfService()),
            availableIfNotBlank(data.getServiceDescriptionLicensedNurses(), "Licensed Practical Nurses"),
            availableIfNotBlank(data.getServiceDescriptionProfessionalCounselor(), "Licensed Professional Counselor"),
            availableIfNotBlank(data.getServiceDescriptionLicensedStaff(), "Licensed Staff on Duty 24/7"),
            availableIfNotBlank(data.getServiceDescriptionLightHousekeeping(), "Light Housekeeping"),
            availableIfNotBlank(data.getServiceDescriptionLiveInCare(), "Live In Care"),
            availableIfNotBlank(data.getServiceDescriptionMealsServed()),
            availableIfNotBlank(data.getServiceDescriptionMissouriCounties(), "Missouri Counties Served"),
            isTrue(data.getServiceDescriptionMustSchedule24h()) ? "Must Schedule 24 Hrs Or More In Advance" : null,
            isTrue(data.getServiceDescriptionMustSchedule48h()) ? "Must Schedule 48 Hrs Or More In Advance" : null,
            data.getServiceDescriptionOtherLanguages(),
            data.getServiceDescriptionOtherOptions(),
            data.getServiceDescriptionOtherProvidedSpecialityCare(),
            data.getServiceDescriptionOtherProvided(),
            availableIfTrue(data.getServiceDescriptionPetCare(), "Pet Care"),
            availableIfTrue(data.getServiceDescriptionPetTherapy(), "Pet Therapy"),
            availableIfTrue(data.getServiceDescriptionPharmacyServices(), "Pharmacy Services"),
            availableIfTrue(data.getServiceDescriptionPhysicalTherapists(), "Physical Therapists"),
            availableIfTrue(data.getServiceDescriptionPhysicians(), "Physicians"),
            availableIfTrue(data.getServiceDescriptionPsychiatricNurse(), "Psychiatric Nurse"),
            availableIfTrue(data.getServiceDescriptionPsychiatrist(), "Psychiatrist"),
            availableIfTrue(data.getServiceDescriptionPsychoanalyst(), "Psychoanalyst"),
            availableIfTrue(data.getServiceDescriptionPsychologist(), "Psychologist"),
            availableIfTrue(data.getServiceDescriptionPsychotherapist(), "Psychotherapist"),
            isTrue(data.getServiceDescriptionDieticians()) ? "Registered Dietitians" : null,
            isTrue(data.getServiceDescriptionNurseOnCall()) ? "Registered Nurse On Call" : null,
            prefixIfNotBlankOrFalse(data.getServiceDescriptionSatopClasses(), "Services Provided"),
            data.getServiceDescriptionAppointmentFormula(),
            data.getServiceDescriptionHoursDetails(),
            prefixIfNotBlankOrFalse(data.getServiceDescriptionInhomeNonmedFormula(), "Services Provided"),
            isTrue(data.getServiceDescriptionSingleFamily1Bedroom()) ? "Single Family Homes/Villas 1 Bedroom" : null,
            isTrue(data.getServiceDescriptionSingleFamily2Bedrooms()) ? "Single Family Homes/Villas 2 Bedrooms" : null,
            isTrue(data.getServiceDescriptionSingleFamily3Bedrooms()) ? "Single Family Homes/Villas 3 Bedrooms" : null,
            isTrue(data.getServiceDescriptionSkilledNursingFacility()) ? "Skilled Nursing Facility" : null,
            isTrue(data.getServiceDescriptionSnacks()) ? "Snacks Provided" : null,
            isTrue(data.getServiceDescriptionSocialWorkers()) ? "Social Workers" : null,
            isTrue(data.getServiceDescriptionSpanish()) ? "Spanish" : null,
            data.getServiceDescriptionSpecialityCareFormula(),
            isTrue(data.getServiceDescriptionSpeechTherapists()) ? "Speech Language Therapists" : null,
            isTrue(data.getServiceDescriptionStateLicensed()) ? "State Licensed" : null,
            isTrue(data.getServiceDescriptionStress()) ? "Stress and Trauma" : null,
            isTrue(data.getServiceDescriptionStroke()) ? "Stroke Neurological Rehabilitation" : null,
            isTrue(data.getServiceDescription247MultipleShift()) ? "24/7 Multiple Shift Care" : null,
            isTrue(data.getServiceDescription24HourCrisis()) ? "24 Hour Crisis Services" : null,
            isTrue(data.getServiceDescription24HourPersonnel()) ? "24 Hour Security Personnel" : null,
            isTrue(data.getServiceDescription24HourSupportive()) ? "24 Hour Supportive Services" : null,
            StringUtils.isNotBlank(data.getServiceDescriptionVisitationRestrictions())
                ? data.getServiceDescriptionVisitationRestrictions()
                .replaceFirst(";", SEMICOLON_REPLACEMENT)
                .replaceAll(";", ",")
                .replaceFirst(SEMICOLON_REPLACEMENT, ";")
                : null
            ).filter(StringUtils::isNotBlank)
            // remove the leading ; and trim the section
            .map(section -> section.charAt(0) == ';' ? section.substring(1).trim() : section.trim())
            .collect(Collectors.joining(", "));
    }

    private String availableIfTrue(String s, String label) {
        return isTrue(s) ? StringUtils.join(label, " ", AVAILABLE) : null;
    }

    private String availableIfNotBlank(String s) {
        return StringUtils.isNotBlank(s) ? StringUtils.join(s, " ", AVAILABLE) : null;
    }

    private String availableIfNotBlank(String s, String label) {
        String l = StringUtils.isNotBlank(label) ? label : s;
        return StringUtils.isNotBlank(s) ? StringUtils.join(l, " ", AVAILABLE) : null;
    }

    private String prefixIfNotBlank(String s, String prefix) {
        return StringUtils.isNotBlank(s) ? StringUtils.join(prefix + ": ", s) : null;
    }

    private String prefixIfNotBlankOrFalse(String s, String prefix) {
        return StringUtils.isNotBlank(s) && !isFalse(s) ? StringUtils.join(prefix + ": ", s) : null;
    }

    private boolean isTrue(String value) {
        return Arrays.stream(TRUE_VALUES).anyMatch(t -> t.equalsIgnoreCase(value));
    }

    private boolean isFalse(String value) {
        return Arrays.stream(FALSE_VALUES).anyMatch(t -> t.equalsIgnoreCase(value));
    }
}
