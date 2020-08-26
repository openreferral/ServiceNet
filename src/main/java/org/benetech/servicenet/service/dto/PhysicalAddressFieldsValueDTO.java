package org.benetech.servicenet.service.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.enumeration.PhysicalAddressFields;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.PhysicalAddressFieldsValue} entity.
 */
public class PhysicalAddressFieldsValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private PhysicalAddressFields physicalAddressField;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PhysicalAddressFields getPhysicalAddressField() {
        return physicalAddressField;
    }

    public void setPhysicalAddressField(PhysicalAddressFields physicalAddressField) {
        this.physicalAddressField = physicalAddressField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO = (PhysicalAddressFieldsValueDTO) o;
        if (physicalAddressFieldsValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), physicalAddressFieldsValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PhysicalAddressFieldsValueDTO{" +
            "id=" + getId() +
            ", physicalAddressField='" + getPhysicalAddressField() + "'" +
            "}";
    }
}
