package org.benetech.servicenet.service.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.enumeration.PostalAddressFields;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.PostalAddressFieldsValue} entity.
 */
public class PostalAddressFieldsValueDTO implements Serializable {

    private UUID id;

    @NotNull
    private PostalAddressFields postalAddressField;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PostalAddressFields getPostalAddressField() {
        return postalAddressField;
    }

    public void setPostalAddressField(PostalAddressFields postalAddressField) {
        this.postalAddressField = postalAddressField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostalAddressFieldsValueDTO postalAddressFieldsValueDTO = (PostalAddressFieldsValueDTO) o;
        if (postalAddressFieldsValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postalAddressFieldsValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostalAddressFieldsValueDTO{" +
            "id=" + getId() +
            ", postalAddressField='" + getPostalAddressField() + "'" +
            "}";
    }
}
