package org.benetech.servicenet.adapter.sheltertech.mapper;

import static org.benetech.servicenet.config.Constants.SHELTER_TECH_PROVIDER;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.adapter.sheltertech.model.AddressRaw;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShelterTechLocationMapper {

    ShelterTechLocationMapper INSTANCE = Mappers.getMapper(ShelterTechLocationMapper.class);

    default Optional<Location> mapToLocation(AddressRaw raw) {
        if (raw == null || raw.getCity() == null || raw.getStateProvince() == null || raw.getAddress1() == null) {
            return Optional.empty();
        }

        Location location = toLocation(raw);
        location.setName(LocationUtils.buildLocationName(raw.getCity(), raw.getStateProvince(), raw.getAddress1()));

        return Optional.of(location);
    }

    default Set<Location> mapToLocations(List<AddressRaw> raws) {
        Set<Location> locations = new HashSet<>();

        for ( AddressRaw addressRaw : raws ) {
            mapToLocation(addressRaw).ifPresent(locations::add);
        }

        return locations;
    }

    @Named("physicalAddressFromAddressRaw")
    default PhysicalAddress physicalAddressFromAddressRaw(AddressRaw raw) {
        return ShelterTechPhysicalAddressMapper.INSTANCE.mapAddressRawToPhysicalAddress(raw);
    }

    @Named("postalAddressFromAddressRaw")
    default PostalAddress postalAddressFromAddressRaw(AddressRaw raw) {
        return ShelterTechPostalAddressMapper.INSTANCE.mapAddressRawToPostalAddress(raw);
    }

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "name")
    @Mapping(ignore = true, target = "alternateName")
    @Mapping(ignore = true, target = "description")
    @Mapping(ignore = true, target = "transportation")
    @Mapping(source = "raw.latitude", target = "latitude")
    @Mapping(source = "raw.longitude", target = "longitude")
    @Mapping(source = "raw.id", target = "externalDbId")
    @Mapping(constant = SHELTER_TECH_PROVIDER, target = "providerName")
    @Mapping(ignore = true, target = "physicalAddress")
    @Mapping(ignore = true, target = "postalAddress")
    @Mapping(ignore = true, target = "regularSchedule")
    @Mapping(ignore = true, target = "holidaySchedules")
    @Mapping(ignore = true, target = "langs")
    @Mapping(ignore = true, target = "accessibilities")
    Location toLocation(AddressRaw raw);
}
