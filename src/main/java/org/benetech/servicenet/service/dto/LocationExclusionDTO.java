package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.LocationExclusion} entity.
 */
public class LocationExclusionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String region;

    private String city;

    private UUID configId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UUID getConfigId() {
        return configId;
    }

    public void setConfigId(UUID exclusionsConfigId) {
        this.configId = exclusionsConfigId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationExclusionDTO locationExclusionDTO = (LocationExclusionDTO) o;
        if (locationExclusionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), locationExclusionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationExclusionDTO{" +
            "id=" + getId() +
            ", region='" + getRegion() + "'" +
            ", city='" + getCity() + "'" +
            ", config=" + getConfigId() +
            "}";
    }
}
