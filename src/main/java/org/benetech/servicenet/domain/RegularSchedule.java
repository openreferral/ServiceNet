package org.benetech.servicenet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RegularSchedule.
 */
@Entity
@Table(name = "regular_schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RegularSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "weekday", nullable = false)
    private Integer weekday;

    @Column(name = "opens_at")
    private String opensAt;

    @Column(name = "closes_at")
    private String closesAt;

    @OneToOne
    @JoinColumn(unique = true)
    private Service srvc;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToOne
    @JoinColumn(unique = true)
    private ServiceAtLocation serviceAtlocation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public RegularSchedule weekday(Integer weekday) {
        this.weekday = weekday;
        return this;
    }

    public String getOpensAt() {
        return opensAt;
    }

    public void setOpensAt(String opensAt) {
        this.opensAt = opensAt;
    }

    public RegularSchedule opensAt(String opensAt) {
        this.opensAt = opensAt;
        return this;
    }

    public String getClosesAt() {
        return closesAt;
    }

    public void setClosesAt(String closesAt) {
        this.closesAt = closesAt;
    }

    public RegularSchedule closesAt(String closesAt) {
        this.closesAt = closesAt;
        return this;
    }

    public Service getSrvc() {
        return srvc;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public RegularSchedule srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public RegularSchedule location(Location location) {
        this.location = location;
        return this;
    }

    public ServiceAtLocation getServiceAtlocation() {
        return serviceAtlocation;
    }

    public void setServiceAtlocation(ServiceAtLocation serviceAtLocation) {
        this.serviceAtlocation = serviceAtLocation;
    }

    public RegularSchedule serviceAtlocation(ServiceAtLocation serviceAtLocation) {
        this.serviceAtlocation = serviceAtLocation;
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
        RegularSchedule regularSchedule = (RegularSchedule) o;
        if (regularSchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), regularSchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegularSchedule{" +
            "id=" + getId() +
            ", weekday=" + getWeekday() +
            ", opensAt='" + getOpensAt() + "'" +
            ", closesAt='" + getClosesAt() + "'" +
            "}";
    }
}
