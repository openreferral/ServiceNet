package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the OrganizationMatch entity.
 */
public class OrganizationMatchDTO implements Serializable {

    private UUID id;

    private String fieldName;

    private ZonedDateTime timestamp;

    private Boolean deleted;

    private String fieldPath;

    @Lob
    private String matchedValue;

    private UUID organizationRecordId;

    private String organizationRecordName;

    private UUID partnerVersionId;

    private String partnerVersionName;

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

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean isDeleted() {
        return deleted;
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

    public String getMatchedValue() {
        return matchedValue;
    }

    public void setMatchedValue(String matchedValue) {
        this.matchedValue = matchedValue;
    }

    public UUID getOrganizationRecordId() {
        return organizationRecordId;
    }

    public void setOrganizationRecordId(UUID organizationId) {
        this.organizationRecordId = organizationId;
    }

    public String getOrganizationRecordName() {
        return organizationRecordName;
    }

    public void setOrganizationRecordName(String organizationName) {
        this.organizationRecordName = organizationName;
    }

    public UUID getPartnerVersionId() {
        return partnerVersionId;
    }

    public void setPartnerVersionId(UUID organizationId) {
        this.partnerVersionId = organizationId;
    }

    public String getPartnerVersionName() {
        return partnerVersionName;
    }

    public void setPartnerVersionName(String organizationName) {
        this.partnerVersionName = organizationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationMatchDTO organizationMatchDTO = (OrganizationMatchDTO) o;
        if (organizationMatchDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationMatchDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationMatchDTO{" +
            "id=" + getId() +
            ", fieldName='" + getFieldName() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", fieldPath='" + getFieldPath() + "'" +
            ", matchedValue='" + getMatchedValue() + "'" +
            ", organizationRecord=" + getOrganizationRecordId() +
            ", organizationRecord='" + getOrganizationRecordName() + "'" +
            ", partnerVersion=" + getPartnerVersionId() +
            ", partnerVersion='" + getPartnerVersionName() + "'" +
            "}";
    }
}
