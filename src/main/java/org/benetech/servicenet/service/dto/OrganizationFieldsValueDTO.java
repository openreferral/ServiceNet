package org.benetech.servicenet.service.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.enumeration.OrganizationFields;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.OrganizationFieldsValue} entity.
 */
public class OrganizationFieldsValueDTO implements Serializable {

    private UUID id;

    @NotNull
    private OrganizationFields organizationField;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrganizationFields getOrganizationField() {
        return organizationField;
    }

    public void setOrganizationField(OrganizationFields organizationField) {
        this.organizationField = organizationField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationFieldsValueDTO organizationFieldsValueDTO = (OrganizationFieldsValueDTO) o;
        if (organizationFieldsValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationFieldsValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationFieldsValueDTO{" +
            "id=" + getId() +
            ", organizationField='" + getOrganizationField() + "'" +
            "}";
    }
}
