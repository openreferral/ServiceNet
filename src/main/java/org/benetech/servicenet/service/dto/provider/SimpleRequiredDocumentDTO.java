package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * A DTO for the SimpleRequiredDocument entity.
 */
@Data
public class SimpleRequiredDocumentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private String document;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimpleRequiredDocumentDTO requiredDocumentDTO = (SimpleRequiredDocumentDTO) o;
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
        return "SimpleRequiredDocumentDTO{" +
            "id=" + getId() +
            ", document='" + getDocument() + "'" +
            "}";
    }
}
