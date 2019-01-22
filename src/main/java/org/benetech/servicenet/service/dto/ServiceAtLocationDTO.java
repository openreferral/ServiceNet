package org.benetech.servicenet.service.dto;

import lombok.Data;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the ServiceAtLocation entity.
 */
@Data
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
