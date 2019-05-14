package org.benetech.servicenet.adapter.anonymous;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.anonymous.model.RawData;
import org.benetech.servicenet.adapter.shared.MapperUtils;
import org.benetech.servicenet.adapter.shared.model.Coordinates;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.adapter.shared.util.OpeningHoursUtils;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Eligibility;
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
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface AnonymousDataMapper {

    String LISTS_DELIMITER = "; ";

    AnonymousDataMapper INSTANCE = Mappers.getMapper(AnonymousDataMapper.class);

    @Mapping(source = "resourceSiteEmail", target = "email")
    @Mapping(source = "organizationName", target = "name")
    Organization extractOrganization(RawData rawData);

    @Mapping(source = "phone", target = "number")
    @Mapping(source = "phoneExtension", target = "extension", qualifiedByName = "parseToInt")
    Phone extractPhone(RawData rawData);

    @Mapping(source = "eligibilityList", target = "eligibility")
    Eligibility extractEligibility(RawData rawData);

    default PhysicalAddress extractPhysicalAddress(RawData rawData) {
        return new PhysicalAddress()
            .city(rawData.getCity())
            .stateProvince(rawData.getStateProvince())
            .address1(MapperUtils.joinNotBlank(" ", rawData.getAddress1(), rawData.getAddress2()));
    }

    default PostalAddress extractPostalAddress(RawData rawData) {
        return new PostalAddress()
            .city(rawData.getCity())
            .stateProvince(rawData.getStateProvince())
            .address1(MapperUtils.joinNotBlank(" ", rawData.getAddress1(), rawData.getAddress2()));
    }

    default Service extractService(RawData rawData) {
        //TODO: extract fees from the description
        //TODO: extract application procedure from the description
        return new Service().name(rawData.getServiceName()).url(rawData.getWebsite())
            .applicationProcess("Required items: " + rawData.getRequiredItems())
            .fees("from " + rawData.getCostOfServiceMinimum() + " to " + rawData.getCostOfServiceMaximum())
            .description(MapperUtils.joinNotBlank(" ", rawData.getServiceDescription(),
                rawData.getServiceAdditionalInfo()));
    }

    default Set<Program> extractPrograms(RawData rawData) {
        String[] programs = rawData.getProgramList().split(LISTS_DELIMITER);
        return Arrays.stream(programs).map(p -> new Program().name(p)).collect(Collectors.toSet());
    }

    default Set<Language> extractLangs(RawData rawData) {
        String[] langs = rawData.getLanguageList().split(LISTS_DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang)).collect(Collectors.toSet());
    }

    default List<OpeningHours> extractOpeningHours(RawData rawData) {
        String[] rawOpeningHours = rawData.getWeeklyOpenHoursRaw().split(", ");
        List<OpeningHours> result = new ArrayList<>();
        int weekday = 0;
        for (String openingHoursString : rawOpeningHours) {
            OpeningHours openingHours = new OpeningHours().weekday(weekday++);
            OpeningHoursUtils.getHoursFromStringWithDelimiter(openingHoursString, "-").ifPresent(hours -> {
                openingHours.setOpensAt(hours.getOpen());
                openingHours.setClosesAt(hours.getClose());
            });
            result.add(openingHours);
        }
        return result;
    }

    default AccessibilityForDisabilities extractAccessibilityForDisabilities(RawData rawData) {
        return new AccessibilityForDisabilities()
            .accessibility("Is Wheel Chair Accessible")
            .details(rawData.getIsWheelChairAccessible());
    }

    default Location extractLocation(RawData rawData) {
        Location result = new Location().name(extractLocationName(rawData));

        Optional<Coordinates> coordinates = LocationUtils.getCoordinatesFromString(rawData.getGeoLocation(), ",");
        return coordinates.map(
            c -> result.latitude(c.getLatitude()).longitude(c.getLongitude()))
            .orElse(result);
    }

    private String extractLocationName(RawData rawData) {
        return LocationUtils.buildLocationName(rawData.getCity(), rawData.getStateProvince(),
            MapperUtils.joinNotBlank(" ", rawData.getAddress1(), rawData.getAddress2()));
    }

    @Named("parseToInt")
    default Integer parseToInt(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        } else {
            return Integer.valueOf(input);
        }
    }
}
