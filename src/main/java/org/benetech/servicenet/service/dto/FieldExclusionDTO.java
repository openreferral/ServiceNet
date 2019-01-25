package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the FieldExclusion entity.
 */
public class FieldExclusionDTO implements Serializable {

    private UUID id;

    private String fields;

    private String entity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

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
