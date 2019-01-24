package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.benetech.servicenet.adapter.sheltertech.model.AddressRaw;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import static org.benetech.servicenet.adapter.sheltertech.ShelterTechConstants.PROVIDER_NAME;

@Mapper
public interface ShelterTechLocationMapper {

    ShelterTechLocationMapper INSTANCE = Mappers.getMapper(ShelterTechLocationMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "address1", target = "name", defaultValue = "undefined")
    @Mapping(ignore = true, target = "alternateName")
    @Mapping(ignore = true, target = "description")
    @Mapping(ignore = true, target = "transportation")
    @Mapping(source = "raw.latitude", target = "latitude")
    @Mapping(source = "raw.longitude", target = "longitude")
    @Mapping(source = "raw.id", target = "externalDbId")
    @Mapping(constant = PROVIDER_NAME, target = "providerName")
    @Mapping(ignore = true, target = "physicalAddress")
    @Mapping(ignore = true, target = "postalAddress")
    @Mapping(ignore = true, target = "regularSchedule")
    @Mapping(ignore = true, target = "holidaySchedule")
    @Mapping(ignore = true, target = "langs")
    @Mapping(ignore = true, target = "accessibilities")
    Location mapToLocation(AddressRaw raw);

    @Named("physicalAddressFromAddressRaw")
    default PhysicalAddress physicalAddressFromAddressRaw(AddressRaw raw) {
        return ShelterTechPhysicalAddressMapper.INSTANCE.mapAddressRawToPhysicalAddress(raw);
    }

    @Named("postalAddressFromAddressRaw")
    default PostalAddress postalAddressFromAddressRaw(AddressRaw raw) {
        return ShelterTechPostalAddressMapper.INSTANCE.mapAddressRawToPostalAddress(raw);
    }

}
