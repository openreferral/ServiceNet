package org.benetech.servicenet.service.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.enumeration.ServiceFields;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.ServiceFieldsValue} entity.
 */
public class ServiceFieldsValueDTO implements Serializable {

    private UUID id;

    @NotNull
    private ServiceFields serviceField;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServiceFields getServiceField() {
        return serviceField;
    }

    public void setServiceField(ServiceFields serviceField) {
        this.serviceField = serviceField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceFieldsValueDTO serviceFieldsValueDTO = (ServiceFieldsValueDTO) o;
        if (serviceFieldsValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceFieldsValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceFieldsValueDTO{" +
            "id=" + getId() +
            ", serviceField='" + getServiceField() + "'" +
            "}";
    }
}
