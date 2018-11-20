package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ServiceAtLocation entity.
 */
public class ServiceAtLocationDTO implements Serializable {

    private Long id;

    @Lob
    private String description;

    private Long srvcId;

    private String srvcName;

    private Long locationId;

    private String locationName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
