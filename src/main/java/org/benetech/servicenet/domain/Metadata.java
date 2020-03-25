package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.benetech.servicenet.domain.enumeration.ActionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A Metadata.
 */
@Entity
@Table(name = "metadata")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Metadata extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "resource_id", nullable = false)
    private UUID resourceId;

    @NotNull
    @Column(name = "last_action_date", nullable = false)
    private ZonedDateTime lastActionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "last_action_type", nullable = false)
    private ActionType lastActionType;

    @NotNull
    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "previous_value", columnDefinition = "clob")
    private String previousValue;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "replacement_value", columnDefinition = "clob")
    private String replacementValue;

    @NotNull
    @Column(name = "resource_class", nullable = false)
    private String resourceClass;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public Metadata resourceId(UUID resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public ZonedDateTime getLastActionDate() {
        return lastActionDate;
    }

    public void setLastActionDate(ZonedDateTime lastActionDate) {
        this.lastActionDate = lastActionDate;
    }

    public Metadata lastActionDate(ZonedDateTime lastActionDate) {
        this.lastActionDate = lastActionDate;
        return this;
    }

    public ActionType getLastActionType() {
        return lastActionType;
    }

    public void setLastActionType(ActionType lastActionType) {
        this.lastActionType = lastActionType;
    }

    public Metadata lastActionType(ActionType lastActionType) {
        this.lastActionType = lastActionType;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Metadata fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public Metadata previousValue(String previousValue) {
        this.previousValue = previousValue;
        return this;
    }

    public String getReplacementValue() {
        return replacementValue;
    }

    public Metadata replacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
        return this;
    }

    public void setReplacementValue(String replacementValue) {
        this.replacementValue = replacementValue;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public Metadata resourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
        return this;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Metadata user(UserProfile userProfile) {
        this.userProfile = userProfile;
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
        Metadata metadata = (Metadata) o;
        if (metadata.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metadata.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Metadata{" +
            "id=" + getId() +
            ", resourceId='" + getResourceId() + "'" +
            ", lastActionDate='" + getLastActionDate() + "'" +
            ", lastActionType='" + getLastActionType() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", previousValue='" + getPreviousValue() + "'" +
            ", replacementValue='" + getReplacementValue() + "'" +
            ", resourceClass='" + getResourceClass() + "'" +
            "}";
    }
}
