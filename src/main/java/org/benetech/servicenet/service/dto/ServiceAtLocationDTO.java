package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the ServiceAtLocation entity.
 */
public class ServiceAtLocationDTO implements Serializable {

    private UUID id;

    @Lob
    private String description;

    private UUID srvcId;

    private String srvcName;

    private UUID locationId;

    private String locationName;

    private String externalDbId;

    private String providerName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceAtLocationDTO serviceAtLocationDTO = (ServiceAtLocationDTO) o;
        if (serviceAtLocationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceAtLocationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceAtLocationDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            "}";
    }
}
