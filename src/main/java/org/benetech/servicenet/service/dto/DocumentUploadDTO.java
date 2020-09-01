package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the DocumentUpload entity.
 */
public class DocumentUploadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private ZonedDateTime dateUploaded;

    @NotNull
    private String originalDocumentId;

    private String parsedDocumentId;

    private UUID uploaderId;

    private String uploaderLogin;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(ZonedDateTime dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getOriginalDocumentId() {
        return originalDocumentId;
    }

    public void setOriginalDocumentId(String originalDocumentId) {
        this.originalDocumentId = originalDocumentId;
    }

    public String getParsedDocumentId() {
        return parsedDocumentId;
    }

    public void setParsedDocumentId(String parsedDocumentId) {
        this.parsedDocumentId = parsedDocumentId;
    }

    public UUID getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(UUID userId) {
        this.uploaderId = userId;
    }

    public String getUploaderLogin() {
        return uploaderLogin;
    }

    public void setUploaderLogin(String userLogin) {
        this.uploaderLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentUploadDTO documentUploadDTO = (DocumentUploadDTO) o;
        if (documentUploadDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentUploadDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentUploadDTO{" +
            "id=" + getId() +
            ", dateUploaded='" + getDateUploaded() + "'" +
            ", originalDocumentId='" + getOriginalDocumentId() + "'" +
            ", parsedDocumentId='" + getParsedDocumentId() + "'" +
            ", uploader=" + getUploaderId() +
            ", uploader='" + getUploaderLogin() + "'" +
            "}";
    }
}
