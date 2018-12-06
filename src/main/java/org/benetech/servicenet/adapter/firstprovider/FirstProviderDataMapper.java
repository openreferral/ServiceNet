package org.benetech.servicenet.adapter.firstprovider;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.firstprovider.model.RawData;
import org.benetech.servicenet.adapter.shared.model.Coordinates;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
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

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FirstProviderDataMapper {

    String LISTS_DELIMITER = "; ";

    FirstProviderDataMapper INSTANCE = Mappers.getMapper(FirstProviderDataMapper.class);

    PhysicalAddress extractPhysicalAddress(RawData rawData);

    PostalAddress extractPostalAddress(RawData rawData);

    @Mapping(source = "resourceSiteEmail", target = "email")
    @Mapping(source = "organizationName", target = "name")
    Organization extractOrganization(RawData rawData);

    @Mapping(source = "phone", target = "number")
    @Mapping(source = "phoneExtension", target = "extension", qualifiedByName = "parseToInt")
    Phone extractPhone(RawData rawData);

    @Mapping(source = "eligibilityList", target = "eligibility")
    Eligibility extractEligibility(RawData rawData);

    default Service extractService(RawData rawData) {
        return new Service().name(rawData.getServiceName()).url(rawData.getAppURL())
            .applicationProcess("Required items: " + rawData.getRequiredItems())
            .fees("from " + rawData.getCostOfServiceMinimum() + " to " + rawData.getCostOfServiceMaximum())
            .description(rawData.getServiceDescription() + " " + rawData.getServiceAdditionalInfo());
    }

    default Set<Program> extractPrograms(RawData rawData) {
        String[] programs = rawData.getProgramList().split(LISTS_DELIMITER);
        return Arrays.stream(programs).map(p -> new Program().name(p)).collect(Collectors.toSet());
    }

    default Set<Language> extractLangs(RawData rawData) {
        String[] langs = rawData.getLanguageList().split(LISTS_DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang)).collect(Collectors.toSet());
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
        return LocationUtils.buildLocationName(rawData.getCity(), rawData.getStateProvince(), rawData.getAddress1());
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
