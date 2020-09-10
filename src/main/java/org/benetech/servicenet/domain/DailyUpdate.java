package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DailyUpdate.
 */
@Entity
@Table(name = "daily_update")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DailyUpdate extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "update", nullable = false, columnDefinition = "clob")
    private String update;

    @Column(name = "expiry")
    private ZonedDateTime expiry;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @ManyToOne
    @JsonIgnoreProperties("dailyUpdates")
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getUpdate() {
        return update;
    }

    public DailyUpdate update(String update) {
        this.update = update;
        return this;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public DailyUpdate expiry(ZonedDateTime expiry) {
        this.expiry = expiry;
        return this;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public DailyUpdate createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Organization getOrganization() {
        return organization;
    }

    public DailyUpdate organization(Organization organization) {
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
        if (!(o instanceof DailyUpdate)) {
            return false;
        }
        return getId() != null && getId().equals(((DailyUpdate) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DailyUpdate{" +
            "id=" + getId() +
            ", update='" + getUpdate() + "'" +
            ", expiry='" + getExpiry() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
