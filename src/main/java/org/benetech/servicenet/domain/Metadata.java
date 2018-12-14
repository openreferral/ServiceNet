package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.benetech.servicenet.domain.enumeration.ActionType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Column(name = "resource_id", nullable = false)
    private String resourceId;

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
    @Column(name = "previous_value")
    private String previousValue;

    @Lob
    @Column(name = "replacement_value")
    private String replacementValue;

    @NotNull
    @Column(name = "resource_class", nullable = false)
    private String resourceClass;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Metadata resourceId(String resourceId) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Metadata user(User user) {
        this.user = user;
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
