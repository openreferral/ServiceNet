package org.benetech.servicenet.adapter.smcconnect;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.shared.MapperUtils;
import org.benetech.servicenet.adapter.shared.util.OpeningHoursUtils;
import org.benetech.servicenet.adapter.smcconnect.model.SmcAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcContact;
import org.benetech.servicenet.adapter.smcconnect.model.SmcHolidaySchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcLocation;
import org.benetech.servicenet.adapter.smcconnect.model.SmcMailAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcOrganization;
import org.benetech.servicenet.adapter.smcconnect.model.SmcPhone;
import org.benetech.servicenet.adapter.smcconnect.model.SmcProgram;
import org.benetech.servicenet.adapter.smcconnect.model.SmcRegularSchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcService;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SmcConnectDataMapper {

    String PROVIDER_NAME = "SMCConnect";
    SmcConnectDataMapper INSTANCE = Mappers.getMapper(SmcConnectDataMapper.class);
    String DELIMITER = ",";

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", source = "website")
    @Mapping(target = "externalDbId", source = "id")
    Organization mapOrganization(SmcOrganization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationProcess", ignore = true)
    @Mapping(target = "eligibility", ignore = true)
    @Mapping(target = "url", source = "website")
    @Mapping(target = "externalDbId", source = "id")
    @Mapping(target = "providerName", constant = PROVIDER_NAME)
    Service mapService(SmcService service);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "longitude", qualifiedByName = "double")
    @Mapping(target = "latitude", qualifiedByName = "double")
    @Mapping(target = "externalDbId", source = "id")
    @Mapping(target = "providerName", constant = PROVIDER_NAME)
    Location mapLocation(SmcLocation location);

    @Mapping(target = "id", ignore = true)
    PhysicalAddress mapPhysicalAddress(SmcAddress address);

    @Mapping(target = "id", ignore = true)
    PostalAddress mapPostalAddress(SmcMailAddress address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    @Mapping(target = "providerName", constant = PROVIDER_NAME)
    Contact mapContact(SmcContact contact);

    @Mapping(target = "weekday", source = "weekday", qualifiedByName = "weekday")
    @Mapping(target = "opensAt", source = "opensAt", qualifiedByName = "mapTime")
    @Mapping(target = "closesAt", source = "closesAt", qualifiedByName = "mapTime")
    @Mapping(target = "id", ignore = true)
    OpeningHours mapOpeningHours(SmcRegularSchedule regularSchedule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    HolidaySchedule mapHolidaySchedule(SmcHolidaySchedule startDate);

    @Mapping(target = "id", ignore = true)
    Program mapProgram(SmcProgram program);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "extension", qualifiedByName = "int")
    Phone mapPhone(SmcPhone phone);

    @Named("mapTime")
    default String mapTime(String time) {
        return OpeningHoursUtils.normalizeTime(time);
    }

    @Named("weekday")
    default int getIdByTheWeekday(String weekday) {
        if (StringUtils.isBlank(weekday)) {
            throw new IllegalArgumentException("Day of the week cannot be empty");
        }

        return OpeningHoursUtils.getWeekday(weekday);
    }

    default Optional<HolidaySchedule> extractHolidaySchedule(SmcHolidaySchedule source) {
        if (StringUtils.isBlank(source.getStartDate()) || StringUtils.isBlank(source.getEndDate())) {
            return Optional.empty();
        }

        String dateFormat = "MMMM dd, 00yy";
        HolidaySchedule result = mapHolidaySchedule(source);
        if (StringUtils.isNotBlank(source.getStartDate())) {
            result.setStartDate(LocalDate.parse(source.getStartDate(), DateTimeFormatter.ofPattern(dateFormat)));
        }
        if (StringUtils.isNotBlank(source.getEndDate())) {
            result.setEndDate(LocalDate.parse(source.getEndDate(), DateTimeFormatter.ofPattern(dateFormat)));
        }
        return Optional.of(result);
    }

    default OpeningHours extractOpeningHours(SmcRegularSchedule regularSchedule) {
        if (StringUtils.isBlank(regularSchedule.getWeekday())) {
            return null;
        }

        return mapOpeningHours(regularSchedule);
    }

    default Organization extractOrganization(SmcOrganization source) {
        if (StringUtils.isBlank(source.getName())) {
            throw new IllegalArgumentException("Organization name cannot be empty");
        }

        Organization result = mapOrganization(source);
        result.setFunding(extractFunding(source).orElse(null));
        result.setActive(true);
        if (StringUtils.isNotBlank(source.getDateIncorporated())) {
            result.setYearIncorporated(LocalDate.parse(source.getDateIncorporated(),
                DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        }
        return result;
    }

    default Service extractService(SmcService source) {
        Service result = mapService(source);
        if (result.getName() == null) {
            result.setName("");
        }
        result.setFunding(extractFunding(source).orElse(null));
        result.setApplicationProcess(MapperUtils.joinNotBlank(" ", source.getApplicationProcess(),
            "Required documents: " + source.getRequiredDocuments()));
        return result;
    }

    default Location extractLocation(SmcLocation source) {
        if (StringUtils.isBlank(source.getName())) {
            throw new IllegalArgumentException("Location name cannot be empty");
        }

        return mapLocation(source);
    }

    default Optional<Eligibility> extractEligibility(SmcService service) {
        return StringUtils.isNotBlank(service.getEligibility())
            ? Optional.of(new Eligibility().eligibility(service.getEligibility()))
            : Optional.empty();
    }

    default Optional<Funding> extractFunding(SmcService service) {
        return StringUtils.isNotBlank(service.getFundingSources())
            ? Optional.of(new Funding().source(service.getFundingSources()))
            : Optional.empty();
    }

    default Optional<Funding> extractFunding(SmcOrganization organization) {
        return StringUtils.isNotBlank(organization.getFundingSources())
            ? Optional.of(new Funding().source(organization.getFundingSources()))
            : Optional.empty();
    }

    default Set<Phone> extractPhones(Set<SmcPhone> phones) {
        Set<Phone> result = new HashSet<>();
        for (SmcPhone phone : phones) {
            Phone mapped = mapPhone(phone);
            mapped.setNumber(MapperUtils.joinNotBlank(" ", phone.getCountryPrefix(),
                phone.getNumber()));
            if (StringUtils.isNotBlank(mapped.getNumber())) {
                result.add(mapped);
            }
        }
        return result;
    }

    default Set<Language> extractLangs(SmcService service) {
        if (StringUtils.isBlank(service.getLanguages())) {
            return Collections.emptySet();
        }
        String[] langs = service.getLanguages().split(DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang)).collect(Collectors.toSet());
    }

    default Set<Language> extractLangs(SmcLocation location) {
        if (StringUtils.isBlank(location.getLanguages())) {
            return Collections.emptySet();
        }
        String[] langs = location.getLanguages().split(DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang)).collect(Collectors.toSet());
    }

    default Program extractProgram(SmcProgram program) {
        return StringUtils.isNotBlank(program.getName()) ? mapProgram(program) : null;
    }

    default Contact extractContact(SmcContact contact) {
        return StringUtils.isNotBlank(contact.getName()) ? mapContact(contact) : null;
    }

    default Optional<PhysicalAddress> extractPhysicalAddress(SmcAddress address) {
        return StringUtils.isNotBlank(address.getAddress1()) ?
            Optional.of(mapPhysicalAddress(address)) :
            Optional.empty();
    }

    default Optional<PostalAddress> extractPostalAddress(SmcMailAddress mailAddress) {
        return StringUtils.isNotBlank(mailAddress.getAddress1()) ?
            Optional.of(mapPostalAddress(mailAddress)) :
            Optional.empty();
    }

    @Named("double")
    default Double stringToDouble(String source) {
        return MapperUtils.stringToDouble(source);
    }

    @Named("int")
    default Integer stringToInteger(String source) {
        return MapperUtils.stringToInteger(source);
    }
}
