package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.PhysicalAddressFieldsValue;
import org.benetech.servicenet.service.dto.PhysicalAddressFieldsValueDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PhysicalAddressFieldsValue} and its DTO {@link PhysicalAddressFieldsValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PhysicalAddressFieldsValueMapper extends
    EntityMapper<PhysicalAddressFieldsValueDTO, PhysicalAddressFieldsValue> {

    default PhysicalAddressFieldsValue fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PhysicalAddressFieldsValue physicalAddressFieldsValue = new PhysicalAddressFieldsValue();
        physicalAddressFieldsValue.setId(id);
        return physicalAddressFieldsValue;
    }
}
