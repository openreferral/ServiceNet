package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.benetech.servicenet.adapter.sheltertech.model.AddressRaw;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShelterTechPhysicalAddressMapper {

    ShelterTechPhysicalAddressMapper INSTANCE = Mappers.getMapper(ShelterTechPhysicalAddressMapper.class);

    default PhysicalAddress mapAddressRawToPhysicalAddress(AddressRaw raw) {
        if (raw == null || raw.getStateProvince() == null || raw.getCity() == null || raw.getAddress1() == null) {
            return null;
        }

        return toPhysicalAddress(raw);
    }

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "raw.attention", target = "attention")
    @Mapping(source = "raw.address1", target = "address1")
    @Mapping(source = "raw.address2", target = "address2")
    @Mapping(source = "raw.city", target = "city")
    @Mapping(ignore = true, target = "region")
    @Mapping(source = "raw.stateProvince", target = "stateProvince")
    @Mapping(source = "raw.postalCode", target = "postalCode")
    @Mapping(source = "raw.country", target = "country")
    @Mapping(ignore = true, target = "location")
    PhysicalAddress toPhysicalAddress(AddressRaw raw);
}
