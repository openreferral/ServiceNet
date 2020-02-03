package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.ContactDetailsFieldsValue;
import org.benetech.servicenet.service.dto.ContactDetailsFieldsValueDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ContactDetailsFieldsValue} and its DTO {@link ContactDetailsFieldsValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactDetailsFieldsValueMapper extends
    EntityMapper<ContactDetailsFieldsValueDTO, ContactDetailsFieldsValue> {

    default ContactDetailsFieldsValue fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ContactDetailsFieldsValue contactDetailsFieldsValue = new ContactDetailsFieldsValue();
        contactDetailsFieldsValue.setId(id);
        return contactDetailsFieldsValue;
    }
}
