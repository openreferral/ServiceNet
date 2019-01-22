package org.benetech.servicenet.service.dto;

import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Organization entity.
 */
@Data
public class OrganizationDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private String alternateName;

    @Lob
    private String description;

    @Size(max = 50)
    private String email;

    private String url;

    private String taxStatus;

    private String taxId;

    private LocalDate yearIncorporated;

    private String legalStatus;

    @NotNull
    private Boolean active;

    private ZonedDateTime updatedAt;

    private UUID locationId;

    private String locationName;

    private UUID replacedById;

    private UUID sourceDocumentId;

    private String sourceDocumentDateUploaded;

    private UUID accountId;

    private String accountName;

    private String externalDbId;

    private String providerName;

    public Boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationDTO organizationDTO = (OrganizationDTO) o;
        if (organizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            ", description='" + getDescription() + "'" +
            ", email='" + getEmail() + "'" +
            ", url='" + getUrl() + "'" +
            ", taxStatus='" + getTaxStatus() + "'" +
            ", taxId='" + getTaxId() + "'" +
            ", yearIncorporated='" + getYearIncorporated() + "'" +
            ", legalStatus='" + getLegalStatus() + "'" +
            ", active='" + isActive() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            ", replacedBy=" + getReplacedById() +
            ", sourceDocument=" + getSourceDocumentId() +
            ", sourceDocument='" + getSourceDocumentDateUploaded() + "'" +
            ", account=" + getAccountId() +
            ", account='" + getAccountName() + "'" +
            "}";
    }
}
