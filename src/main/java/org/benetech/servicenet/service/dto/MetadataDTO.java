package org.benetech.servicenet.service.dto;

import org.benetech.servicenet.domain.enumeration.ActionType;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Metadata entity.
 */
public class MetadataDTO implements Serializable {

    private Long id;

    @NotNull
    private String resourceId;

    @NotNull
    private ZonedDateTime lastActionDate;

    @NotNull
    private ActionType lastActionType;

    @NotNull
    private String fieldName;

    @Lob
    private String previousValue;

    @Lob
    private String replacementValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public ZonedDateTime getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(ZonedDateTime lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public ActionType getLastActionType() {
        return lastActionType;
    }

    public void setLastActionType(ActionType lastActionType) {
        this.lastActionType = lastActionType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MetadataDTO metadataDTO = (MetadataDTO) o;
        if (metadataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metadataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MetadataDTO{" +
            "id=" + getId() +
            ", resourceId='" + getResourceId() + "'" +
            ", lastActionDate='" + getLastActionDate() + "'" +
            ", lastActionType='" + getLastActionType() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", previousValue='" + getPreviousValue() + "'" +
            ", replacementValue='" + getReplacementValue() + "'" +
            "}";
    }
}
