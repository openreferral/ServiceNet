package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.PostalAddressFieldsValue;
import org.benetech.servicenet.service.dto.PostalAddressFieldsValueDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PostalAddressFieldsValue} and its DTO {@link PostalAddressFieldsValueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostalAddressFieldsValueMapper extends EntityMapper<PostalAddressFieldsValueDTO, PostalAddressFieldsValue> {

    default PostalAddressFieldsValue fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PostalAddressFieldsValue postalAddressFieldsValue = new PostalAddressFieldsValue();
        postalAddressFieldsValue.setId(id);
        return postalAddressFieldsValue;
    }
}
