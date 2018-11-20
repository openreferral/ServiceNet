package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Language entity.
 */
public class LanguageDTO implements Serializable {

    private UUID id;

    @NotNull
    private String language;

    private UUID srvcId;

    private String srvcName;

    private UUID locationId;

    private String locationName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

        LanguageDTO languageDTO = (LanguageDTO) o;
        if (languageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), languageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LanguageDTO{" +
            "id=" + getId() +
            ", language='" + getLanguage() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            "}";
    }
}
