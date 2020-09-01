package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the AccessibilityForDisabilities entity.
 */
public class AccessibilityForDisabilitiesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private String accessibility;

    private String details;

    private UUID locationId;

    private String locationName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO = (AccessibilityForDisabilitiesDTO) o;
        if (accessibilityForDisabilitiesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accessibilityForDisabilitiesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccessibilityForDisabilitiesDTO{" +
            "id=" + getId() +
            ", accessibility='" + getAccessibility() + "'" +
            ", details='" + getDetails() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            "}";
    }
}
