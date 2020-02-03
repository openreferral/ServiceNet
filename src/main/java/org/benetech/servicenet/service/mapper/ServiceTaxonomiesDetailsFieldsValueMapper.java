package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.ServiceTaxonomiesDetailsFieldsValue;
import org.benetech.servicenet.service.dto.ServiceTaxonomiesDetailsFieldsValueDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ServiceTaxonomiesDetailsFieldsValue} and its DTO
 * {@link ServiceTaxonomiesDetailsFieldsValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceTaxonomiesDetailsFieldsValueMapper extends
    EntityMapper<ServiceTaxonomiesDetailsFieldsValueDTO, ServiceTaxonomiesDetailsFieldsValue> {

    default ServiceTaxonomiesDetailsFieldsValue fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsFieldsValue = new ServiceTaxonomiesDetailsFieldsValue();
        serviceTaxonomiesDetailsFieldsValue.setId(id);
        return serviceTaxonomiesDetailsFieldsValue;
    }
}
