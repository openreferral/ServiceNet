package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.Location} entity.
 */
@NoArgsConstructor
public class LocationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private UUID id;

    @NotNull
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String alternateName;

    @Lob
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String transportation;

    @Getter
    @Setter
    private Double latitude;

    @Getter
    @Setter
    private Double longitude;

    @Getter
    @Setter
    private String externalDbId;

    @Getter
    @Setter
    private String providerName;

    @Getter
    @Setter
    private UUID organizationId;

    @Getter
    @Setter
    private String organizationName;

    @Getter
    @Setter
    private ZonedDateTime updatedAt;

    @Getter
    @Setter
    private ZonedDateTime lastVerifiedOn;

    @Getter
    @Setter
    private List<GeocodingResultDTO> geocodingResults = new ArrayList<>();

    public LocationDTO(UUID organizationId) {
        this.organizationId = organizationId;
    }

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
            ", longitude=" + getLongitude() +
            ", externalDbId='" + getExternalDbId() + "'" +
            ", providerName='" + getProviderName() + "'" +
            "}";
    }
}
