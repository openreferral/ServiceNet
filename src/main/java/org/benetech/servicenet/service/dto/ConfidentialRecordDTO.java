package org.benetech.servicenet.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the ConfidentialRecord entity.
 */
@Data
public class ConfidentialRecordDTO implements Serializable {

    private UUID id;

    private UUID resourceId;

    private String fields;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConfidentialRecordDTO confidentialRecordDTO = (ConfidentialRecordDTO) o;
        if (confidentialRecordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confidentialRecordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfidentialRecordDTO{" +
            "id=" + getId() +
            ", resourceId='" + getResourceId() + "'" +
            ", fields='" + getFields() + "'" +
            "}";
    }
}
