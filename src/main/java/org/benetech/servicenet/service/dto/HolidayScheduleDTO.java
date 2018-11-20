package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the HolidaySchedule entity.
 */
public class HolidayScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean closed;

    private String opensAt;

    private String closesAt;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Long srvcId;

    private String srvcName;

    private Long locationId;

    private String locationName;

    private Long serviceAtlocationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isClosed() {
        return closed;
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

    public String getClosesAt() {
        return closesAt;
    }

    public void setClosesAt(String closesAt) {
        this.closesAt = closesAt;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getSrvcId() {
        return srvcId;
    }

    public void setSrvcId(Long serviceId) {
        this.srvcId = serviceId;
    }

    public String getSrvcName() {
        return srvcName;
    }

    public void setSrvcName(String serviceName) {
        this.srvcName = serviceName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getServiceAtlocationId() {
        return serviceAtlocationId;
    }

    public void setServiceAtlocationId(Long serviceAtLocationId) {
        this.serviceAtlocationId = serviceAtLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HolidayScheduleDTO holidayScheduleDTO = (HolidayScheduleDTO) o;
        if (holidayScheduleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), holidayScheduleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HolidayScheduleDTO{" +
            "id=" + getId() +
            ", closed='" + isClosed() + "'" +
            ", opensAt='" + getOpensAt() + "'" +
            ", closesAt='" + getClosesAt() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            ", serviceAtlocation=" + getServiceAtlocationId() +
            "}";
    }
}
