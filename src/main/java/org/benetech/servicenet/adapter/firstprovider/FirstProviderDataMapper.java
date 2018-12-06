package org.benetech.servicenet.adapter.firstprovider;

import org.benetech.servicenet.adapter.firstprovider.model.RawData;
import org.benetech.servicenet.adapter.shared.model.Coordinates;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface FirstProviderDataMapper {

    FirstProviderDataMapper INSTANCE = Mappers.getMapper(FirstProviderDataMapper.class);

    PhysicalAddress extractPhysicalAddress(RawData rawData);

    PostalAddress extractPostalAddress(RawData rawData);

    default Location extractLocation(RawData rawData) {
        Location result = new Location().name(extractLocationName(rawData));

        Optional<Coordinates> coordinates = LocationUtils.getCoordinatesFromString(rawData.getGeoLocation(), ", ");
        return coordinates.map(
            c -> result.latitude(c.getLatitude()).longitude(c.getLongitude()))
            .orElse(result);
    }

    default Phone extractPhone(RawData rawData) {
        return new Phone().number(rawData.getPhone());
    }

    private String extractLocationName(RawData rawData) {
        return LocationUtils.buildLocationName(rawData.getCity(), rawData.getStateProvince(), rawData.getAddress1());
    }
}
