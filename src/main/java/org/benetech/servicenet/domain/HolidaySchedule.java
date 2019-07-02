package org.benetech.servicenet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A HolidaySchedule.
 */
@Entity
@Table(name = "holiday_schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HolidaySchedule extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "closed", nullable = false)
    private Boolean closed = true;

    @Column(name = "opens_at")
    @Size(max = 255, message = "Field value is too long.")
    private String opensAt;

    @Column(name = "closes_at")
    @Size(max = 255, message = "Field value is too long.")
    private String closesAt;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

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

    public Boolean isClosed() {
        return closed;
    }

    public HolidaySchedule closed(Boolean closed) {
        this.closed = closed;
        return this;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getOpensAt() {
        return opensAt;
    }

    public void setOpensAt(String opensAt) {
        this.opensAt = opensAt;
    }

    public HolidaySchedule opensAt(String opensAt) {
        this.opensAt = opensAt;
        return this;
    }

    public String getClosesAt() {
        return closesAt;
    }

    public void setClosesAt(String closesAt) {
        this.closesAt = closesAt;
    }

    public HolidaySchedule closesAt(String closesAt) {
        this.closesAt = closesAt;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public HolidaySchedule startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public HolidaySchedule endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Service getSrvc() {
        return srvc;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public HolidaySchedule srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public HolidaySchedule location(Location location) {
        this.location = location;
        return this;
    }

    public ServiceAtLocation getServiceAtlocation() {
        return serviceAtlocation;
    }

    public void setServiceAtlocation(ServiceAtLocation serviceAtLocation) {
        this.serviceAtlocation = serviceAtLocation;
    }

    public HolidaySchedule serviceAtlocation(ServiceAtLocation serviceAtLocation) {
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
        HolidaySchedule holidaySchedule = (HolidaySchedule) o;
        if (holidaySchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), holidaySchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HolidaySchedule{" +
            "id=" + getId() +
            ", closed='" + isClosed() + "'" +
            ", opensAt='" + getOpensAt() + "'" +
            ", closesAt='" + getClosesAt() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
