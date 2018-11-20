package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A OrganizationMatch.
 */
@Entity
@Table(name = "organization_match")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationMatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "jhi_timestamp")
    private ZonedDateTime timestamp;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "field_path")
    private String fieldPath;

    @Lob
    @Column(name = "matched_value")
    private String matchedValue;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization organizationRecord;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization partnerVersion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public OrganizationMatch fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public OrganizationMatch timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public OrganizationMatch deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public OrganizationMatch fieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
        return this;
    }

    public String getMatchedValue() {
        return matchedValue;
    }

    public void setMatchedValue(String matchedValue) {
        this.matchedValue = matchedValue;
    }

    public OrganizationMatch matchedValue(String matchedValue) {
        this.matchedValue = matchedValue;
        return this;
    }

    public Organization getOrganizationRecord() {
        return organizationRecord;
    }

    public void setOrganizationRecord(Organization organization) {
        this.organizationRecord = organization;
    }

    public OrganizationMatch organizationRecord(Organization organization) {
        this.organizationRecord = organization;
        return this;
    }

    public Organization getPartnerVersion() {
        return partnerVersion;
    }

    public void setPartnerVersion(Organization organization) {
        this.partnerVersion = organization;
    }

    public OrganizationMatch partnerVersion(Organization organization) {
        this.partnerVersion = organization;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrganizationMatch organizationMatch = (OrganizationMatch) o;
        if (organizationMatch.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationMatch.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationMatch{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", fieldPath='" + getFieldPath() + "'" +
            ", matchedValue='" + getMatchedValue() + "'" +
            "}";
    }
}
