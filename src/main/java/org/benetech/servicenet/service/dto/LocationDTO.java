package org.benetech.servicenet.service.dto;

import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Location entity.
 */
@Data
public class LocationDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private String alternateName;

    @Lob
    private String description;

    private String transportation;

    private Double latitude;

    private Double longitude;

    private String externalDbId;

    private String providerName;

    private UUID organizationId;

    private String organizationName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (locationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), locationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            ", description='" + getDescription() + "'" +
            ", transportation='" + getTransportation() + "'" +
            ", latitude=" + getLatitude() +
            ", Longitude=" + getLongitude() +
            ", organization=" + getOrganizationId() +
            "}";
    }
}
