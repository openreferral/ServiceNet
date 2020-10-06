package org.benetech.servicenet.service.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.enumeration.ServiceTaxonomiesDetailsFields;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.ServiceTaxonomiesDetailsFieldsValue} entity.
 */
public class ServiceTaxonomiesDetailsFieldsValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private ServiceTaxonomiesDetailsFields serviceTaxonomiesDetailsField;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServiceTaxonomiesDetailsFields getServiceTaxonomiesDetailsField() {
        return serviceTaxonomiesDetailsField;
    }

    public void setServiceTaxonomiesDetailsField(ServiceTaxonomiesDetailsFields serviceTaxonomiesDetailsField) {
        this.serviceTaxonomiesDetailsField = serviceTaxonomiesDetailsField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO =
            (ServiceTaxonomiesDetailsFieldsValueDTO) o;
        if (serviceTaxonomiesDetailsFieldsValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceTaxonomiesDetailsFieldsValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceTaxonomiesDetailsFieldsValueDTO{" +
            "id=" + getId() +
            ", serviceTaxonomiesDetailsField='" + getServiceTaxonomiesDetailsField() + "'" +
            "}";
    }
}
