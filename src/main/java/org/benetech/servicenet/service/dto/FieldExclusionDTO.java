package org.benetech.servicenet.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
public class FieldExclusionDTO implements Serializable {

    private UUID id;

    private String fields;

    private String entity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FieldExclusionDTO fieldExclusionDTO = (FieldExclusionDTO) o;
        if (fieldExclusionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fieldExclusionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FieldExclusionDTO{" +
            "id=" + getId() +
            ", fields='" + getFields() + "'" +
            ", entity='" + getEntity() + "'" +
            "}";
    }
}
