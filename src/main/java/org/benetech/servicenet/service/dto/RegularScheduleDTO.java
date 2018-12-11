package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the RegularSchedule entity.
 */
public class RegularScheduleDTO implements Serializable {

    private UUID id;

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
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            ", serviceAtlocation=" + getServiceAtlocationId() +
            "}";
    }
}
