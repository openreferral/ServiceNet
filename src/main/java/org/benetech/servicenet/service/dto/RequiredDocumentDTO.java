package org.benetech.servicenet.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the RequiredDocument entity.
 */
@Data
public class RequiredDocumentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private String document;

    private UUID srvcId;

    private String srvcName;

    private String externalDbId;

    private String providerName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequiredDocumentDTO requiredDocumentDTO = (RequiredDocumentDTO) o;
        if (requiredDocumentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requiredDocumentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequiredDocumentDTO{" +
            "id=" + getId() +
            ", document='" + getDocument() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            "}";
    }
}
