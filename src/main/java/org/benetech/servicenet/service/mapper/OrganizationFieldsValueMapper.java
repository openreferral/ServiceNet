package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.OrganizationFieldsValue;
import org.benetech.servicenet.service.dto.OrganizationFieldsValueDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link OrganizationFieldsValue} and its DTO {@link OrganizationFieldsValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrganizationFieldsValueMapper extends EntityMapper<OrganizationFieldsValueDTO, OrganizationFieldsValue> {

    default OrganizationFieldsValue fromId(UUID id) {
        if (id == null) {
            return null;
        }
        OrganizationFieldsValue organizationFieldsValue = new OrganizationFieldsValue();
        organizationFieldsValue.setId(id);
        return organizationFieldsValue;
    }
}
