package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity RequiredDocument and its DTO RequiredDocumentDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class})
public interface RequiredDocumentMapper extends EntityMapper<RequiredDocumentDTO, RequiredDocument> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    RequiredDocumentDTO toDto(RequiredDocument requiredDocument);

    @Mapping(source = "srvcId", target = "srvc")
    RequiredDocument toEntity(RequiredDocumentDTO requiredDocumentDTO);

    default RequiredDocument fromId(UUID id) {
        if (id == null) {
            return null;
        }
        RequiredDocument requiredDocument = new RequiredDocument();
        requiredDocument.setId(id);
        return requiredDocument;
    }
}
