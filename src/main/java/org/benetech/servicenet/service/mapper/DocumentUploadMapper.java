package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity DocumentUpload and its DTO DocumentUploadDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentUploadMapper extends EntityMapper<DocumentUploadDTO, DocumentUpload> {

    default DocumentUpload fromId(Long id) {
        if (id == null) {
            return null;
        }
        DocumentUpload documentUpload = new DocumentUpload();
        documentUpload.setId(id);
        return documentUpload;
    }
}
