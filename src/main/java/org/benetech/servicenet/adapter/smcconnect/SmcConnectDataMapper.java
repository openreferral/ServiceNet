package org.benetech.servicenet.adapter.smcconnect;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.shared.MapperUtils;
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
import org.benetech.servicenet.adapter.smcconnect.model.SmcWeekday;
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
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SmcConnectDataMapper {

    SmcConnectDataMapper INSTANCE = Mappers.getMapper(SmcConnectDataMapper.class);
    String DELIMITER = ",";

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "url", source = "website")
    Organization mapOrganization(SmcOrganization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationProcess", ignore = true)
    @Mapping(target = "eligibility", ignore = true)
    @Mapping(target = "url", source = "website")
    Service mapService(SmcService service);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "longitude", qualifiedByName = "double")
    @Mapping(target = "latitude", qualifiedByName = "double")
    Location extractLocation(SmcLocation location);

    @Mapping(target = "id", ignore = true)
    PhysicalAddress extractPhysicalAddress(SmcAddress address);

    @Mapping(target = "id", ignore = true)
    PostalAddress extractPostalAddress(SmcMailAddress address);

    @Mapping(target = "id", ignore = true)
    Contact extractContact(SmcContact contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "weekday", ignore = true)
    OpeningHours mapOpeningHours(SmcRegularSchedule regularSchedule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    HolidaySchedule mapHolidaySchedule(SmcHolidaySchedule startDate);

    @Mapping(target = "id", ignore = true)
    Program extractProgram(SmcProgram program);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "extension", qualifiedByName = "int")
    Phone mapPhone(SmcPhone phone);

    default HolidaySchedule extractHolidaySchedule(SmcHolidaySchedule source) {
        String dateFormat = "MMMM dd, 00yy";
        HolidaySchedule result = mapHolidaySchedule(source);
        if (StringUtils.isNotBlank(source.getStartDate())) {
            result.setStartDate(LocalDate.parse(source.getStartDate(), DateTimeFormatter.ofPattern(dateFormat)));
        }
        if (StringUtils.isNotBlank(source.getEndDate())) {
            result.setEndDate(LocalDate.parse(source.getEndDate(), DateTimeFormatter.ofPattern(dateFormat)));
        }
        return result;
    }

    default OpeningHours extractOpeningHours(SmcRegularSchedule regularSchedule) {
        OpeningHours result = mapOpeningHours(regularSchedule);
        result.setWeekday(getIdByTheWeekday(regularSchedule.getWeekday()));
        return result;
    }

    default int getIdByTheWeekday(String weekday) {
        return SmcWeekday.valueOf(weekday.toUpperCase(Locale.ROOT)).ordinal();
    }

    default Organization extractOrganization(SmcOrganization source) {
        Organization result = mapOrganization(source);
        if (StringUtils.isNotBlank(source.getDateIncorporated())) {
            result.setYearIncorporated(LocalDate.parse(source.getDateIncorporated(),
                DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        }
        return result;
    }

    default Service extractService(SmcService source) {
        Service result = mapService(source);
        result.setApplicationProcess(MapperUtils.joinNotBlank(" ", source.getApplicationProcess(),
            "Required documents: " + source.getRequiredDocuments()));
        return result;
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
            result.add(mapped);
        }
        return result;
    }

    default Set<Language> extractLangs(SmcService service) {
        String[] langs = service.getLanguages().split(DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang)).collect(Collectors.toSet());
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
