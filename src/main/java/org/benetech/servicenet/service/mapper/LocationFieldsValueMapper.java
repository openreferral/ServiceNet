package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.LocationFieldsValue;
import org.benetech.servicenet.service.dto.LocationFieldsValueDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link LocationFieldsValue} and its DTO {@link LocationFieldsValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationFieldsValueMapper extends EntityMapper<LocationFieldsValueDTO, LocationFieldsValue> {

    default LocationFieldsValue fromId(UUID id) {
        if (id == null) {
            return null;
        }
        LocationFieldsValue locationFieldsValue = new LocationFieldsValue();
        locationFieldsValue.setId(id);
        return locationFieldsValue;
    }
}
