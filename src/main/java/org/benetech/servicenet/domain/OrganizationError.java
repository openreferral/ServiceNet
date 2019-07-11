package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A OrganizationError.
 */
@Entity
@Table(name = "organization_error")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationError extends AbstractEntity {

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "external_db_id")
    private String externalDbId;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "invalid_value", columnDefinition = "clob")
    private String invalidValue;

    @Column(name = "cause")
    private String cause;

    @ManyToOne
    private Organization organization;

    @Getter
    @Setter
    @JsonIgnoreProperties({"documentUpload", "organizationErrors"})
    @ManyToOne
    private DataImportReport dataImportReport;

    public String getEntityName() {
        return entityName;
    }

    public OrganizationError entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public OrganizationError fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getExternalDbId() {
        return externalDbId;
    }

    public OrganizationError externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public void setExternalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    public OrganizationError invalidValue(String invalidValue) {
        this.invalidValue = invalidValue;
        return this;
    }

    public void setInvalidValue(String invalidValue) {
        this.invalidValue = invalidValue;
    }

    public String getCause() {
        return cause;
    }

    public OrganizationError cause(String cause) {
        this.cause = cause;
        return this;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Organization getOrganization() {
        return organization;
    }

    public OrganizationError organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationError)) {
            return false;
        }
        return getId() != null && getId().equals(((OrganizationError) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrganizationError{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", externalDbId='" + getExternalDbId() + "'" +
            ", invalidValue='" + getInvalidValue() + "'" +
            ", cause='" + getCause() + "'" +
            "}";
    }
}
