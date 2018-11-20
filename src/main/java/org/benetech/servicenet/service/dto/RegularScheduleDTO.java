package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the RegularSchedule entity.
 */
public class RegularScheduleDTO implements Serializable {

    private UUID id;

    @NotNull
    private Integer weekday;

    private String opensAt;

    private String closesAt;

    private UUID srvcId;

    private String srvcName;

    private UUID locationId;

    private String locationName;

    private UUID serviceAtlocationId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public UUID getSrvcId() {
        return srvcId;
    }

    public void setSrvcId(UUID serviceId) {
        this.srvcId = serviceId;
    }

    public String getSrvcName() {
        return srvcName;
    }

    public void setSrvcName(String serviceName) {
        this.srvcName = serviceName;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public UUID getServiceAtlocationId() {
        return serviceAtlocationId;
    }

    public void setServiceAtlocationId(UUID serviceAtLocationId) {
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
