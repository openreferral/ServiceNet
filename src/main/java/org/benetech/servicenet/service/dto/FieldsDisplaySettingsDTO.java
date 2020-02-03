package org.benetech.servicenet.service.dto;

import java.util.List;
import java.util.UUID;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.FieldsDisplaySettings} entity.
 */
@Getter
@Setter
public class FieldsDisplaySettingsDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private List<String> locationFields;

    private List<String> organizationFields;

    private List<String> physicalAddressFields;

    private List<String> postalAddressFields;

    private List<String> serviceFields;

    private List<String> serviceTaxonomiesDetailsFields;

    private List<String> contactDetailsFields;

    private UUID userId;

    private String userLogin;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO = (FieldsDisplaySettingsDTO) o;
        if (fieldsDisplaySettingsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fieldsDisplaySettingsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FieldsDisplaySettingsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", locationFields='" + getLocationFields() + "'" +
            ", organizationFields='" + getOrganizationFields() + "'" +
            ", physicalAddressFields='" + getPhysicalAddressFields() + "'" +
            ", postalAddressFields='" + getPostalAddressFields() + "'" +
            ", serviceFields='" + getServiceFields() + "'" +
            ", serviceTaxonomiesDetailsFields='" + getServiceTaxonomiesDetailsFields() + "'" +
            ", contactDetailsFields='" + getContactDetailsFields() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
