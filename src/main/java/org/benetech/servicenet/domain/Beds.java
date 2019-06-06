package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.PrePersist;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Beds.
 */
@Entity
@Table(name = "beds")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Beds extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "available_beds")
    private Integer availableBeds;

    @Column(name = "waitlist")
    private Integer waitlist;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne
    @JsonIgnoreProperties("beds")
    private Shelter shelter;

    @PrePersist
    public void addTimestamp() {
        updatedAt = ZonedDateTime.now();
    }

    public Integer getAvailableBeds() {
        return availableBeds;
    }

    public Beds availableBeds(Integer availableBeds) {
        this.availableBeds = availableBeds;
        return this;
    }

    public void setAvailableBeds(Integer availableBeds) {
        this.availableBeds = availableBeds;
    }

    public Integer getWaitlist() {
        return waitlist;
    }

    public Beds waitlist(Integer waitlist) {
        this.waitlist = waitlist;
        return this;
    }

    public void setWaitlist(Integer waitlist) {
        this.waitlist = waitlist;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Beds updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public Beds shelter(Shelter shelter) {
        this.shelter = shelter;
        return this;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
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
        Beds beds = (Beds) o;
        if (beds.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), beds.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Beds{" +
            "id=" + getId() +
            ", availableBeds=" + getAvailableBeds() +
            ", waitlist=" + getWaitlist() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
