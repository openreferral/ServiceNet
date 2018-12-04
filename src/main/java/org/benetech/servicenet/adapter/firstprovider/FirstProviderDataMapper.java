package org.benetech.servicenet.adapter.firstprovider;

import org.benetech.servicenet.adapter.firstprovider.model.RawData;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FirstProviderDataMapper {

    FirstProviderDataMapper INSTANCE = Mappers.getMapper(FirstProviderDataMapper.class);

    PhysicalAddress toPhysicalAddress(RawData rawData);

    PostalAddress toPostalAddress(RawData rawData);

    default PhysicalAddress extractPhysicalAddress(RawData rawData) {
        return FirstProviderDataMapper.INSTANCE.toPhysicalAddress(rawData)
            .postalCode("").country("");
    }

    default PostalAddress extractPostalAddress(RawData rawData) {
        return FirstProviderDataMapper.INSTANCE.toPostalAddress(rawData)
            .postalCode("").country("");
    }

    default Location extractLocation(RawData rawData, PhysicalAddress physicalAddress, PostalAddress postalAddress) {
        return new Location().name(extractLocationName(rawData))
            .physicalAddress(physicalAddress).postalAddress(postalAddress);
    }

    default Phone extractPhone(RawData rawData, Location location) {
        return new Phone().number(rawData.getPhone()).location(location);
    }

    private String extractLocationName(RawData rawData) {
        return rawData.getCity() + " " + rawData.getStateProvince() + " " + rawData.getAddress1();
    }
}
