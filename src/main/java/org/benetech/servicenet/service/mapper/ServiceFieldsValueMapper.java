package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.ServiceFieldsValue;
import org.benetech.servicenet.service.dto.ServiceFieldsValueDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ServiceFieldsValue} and its DTO {@link ServiceFieldsValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceFieldsValueMapper extends EntityMapper<ServiceFieldsValueDTO, ServiceFieldsValue> {

    default ServiceFieldsValue fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ServiceFieldsValue serviceFieldsValue = new ServiceFieldsValue();
        serviceFieldsValue.setId(id);
        return serviceFieldsValue;
    }
}
