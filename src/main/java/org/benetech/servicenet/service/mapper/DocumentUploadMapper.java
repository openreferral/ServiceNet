package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity DocumentUpload and its DTO DocumentUploadDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = IGNORE)
public interface DocumentUploadMapper extends EntityMapper<DocumentUploadDTO, DocumentUpload> {

    @Mapping(source = "uploader.id", target = "uploaderId")
    @Mapping(source = "uploader.login", target = "uploaderLogin")
    DocumentUploadDTO toDto(DocumentUpload documentUpload);

    @Mapping(source = "uploaderId", target = "uploader")
    DocumentUpload toEntity(DocumentUploadDTO documentUploadDTO);

    default DocumentUpload fromId(UUID id) {
        if (id == null) {
            return null;
        }
        DocumentUpload documentUpload = new DocumentUpload();
        documentUpload.setId(id);
        return documentUpload;
    }
}
