package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Organization entity.
 */
public class OrganizationDTO implements Serializable {

    private Long id;

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

    private Long locationId;

    private String locationName;

    private Long replacedById;

    private Long sourceDocumentId;

    private String sourceDocumentDateUploaded;

    private Long accountId;

    private String accountName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTaxStatus() {
        return taxStatus;
    }

    public void setTaxStatus(String taxStatus) {
        this.taxStatus = taxStatus;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public LocalDate getYearIncorporated() {
        return yearIncorporated;
    }

    public void setYearIncorporated(LocalDate yearIncorporated) {
        this.yearIncorporated = yearIncorporated;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getReplacedById() {
        return replacedById;
    }

    public void setReplacedById(Long organizationId) {
        this.replacedById = organizationId;
    }

    public Long getSourceDocumentId() {
        return sourceDocumentId;
    }

    public void setSourceDocumentId(Long documentUploadId) {
        this.sourceDocumentId = documentUploadId;
    }

    public String getSourceDocumentDateUploaded() {
        return sourceDocumentDateUploaded;
    }

    public void setSourceDocumentDateUploaded(String documentUploadDateUploaded) {
        this.sourceDocumentDateUploaded = documentUploadDateUploaded;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long systemAccountId) {
        this.accountId = systemAccountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String systemAccountName) {
        this.accountName = systemAccountName;
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
