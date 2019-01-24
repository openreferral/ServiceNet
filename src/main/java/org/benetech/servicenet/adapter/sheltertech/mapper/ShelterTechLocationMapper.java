package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.benetech.servicenet.adapter.sheltertech.model.AddressRaw;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;

@Mapper
public interface ShelterTechLocationMapper {

    ShelterTechLocationMapper INSTANCE = Mappers.getMapper(ShelterTechLocationMapper.class);

    default Set<Location> mapToLocation(AddressRaw raw) {
        if (raw == null) {
            return null;
        }

        Location location = new Location();

        location.setLatitude(raw.getLatitude());
        if (raw.getAddress1() != null) {
            location.setName(raw.getAddress1());
        } else {
            location.setName("undefined");
        }
        if (raw.getId() != null) {
            location.setExternalDbId(String.valueOf(raw.getId()));
        }
        location.setLongitude(raw.getLongitude());

        location.setProviderName("ShelterTech");

        return Collections.singleton(location);
    }

    @Named("physicalAddressFromAddressRaw")
    default PhysicalAddress physicalAddressFromAddressRaw(AddressRaw raw) {
        return ShelterTechPhysicalAddressMapper.INSTANCE.mapAddressRawToPhysicalAddress(raw);
    }

    @Named("postalAddressFromAddressRaw")
    default PostalAddress postalAddressFromAddressRaw(AddressRaw raw) {
        return ShelterTechPostalAddressMapper.INSTANCE.mapAddressRawToPostalAddress(raw);
    }

}
