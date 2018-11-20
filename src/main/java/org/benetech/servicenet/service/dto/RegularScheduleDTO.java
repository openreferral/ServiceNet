package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RegularSchedule entity.
 */
public class RegularScheduleDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer weekday;

    private String opensAt;

    private String closesAt;

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

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
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

        RegularScheduleDTO regularScheduleDTO = (RegularScheduleDTO) o;
        if (regularScheduleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), regularScheduleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegularScheduleDTO{" +
            "id=" + getId() +
            ", weekday=" + getWeekday() +
            ", opensAt='" + getOpensAt() + "'" +
            ", closesAt='" + getClosesAt() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            ", serviceAtlocation=" + getServiceAtlocationId() +
            "}";
    }
}
