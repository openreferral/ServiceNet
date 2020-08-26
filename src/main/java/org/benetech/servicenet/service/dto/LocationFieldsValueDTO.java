package org.benetech.servicenet.service.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.enumeration.LocationFields;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.LocationFieldsValue} entity.
 */
public class LocationFieldsValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private LocationFields locationField;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocationFields getLocationField() {
        return locationField;
    }

    public void setLocationField(LocationFields locationField) {
        this.locationField = locationField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationFieldsValueDTO locationFieldsValueDTO = (LocationFieldsValueDTO) o;
        if (locationFieldsValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), locationFieldsValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationFieldsValueDTO{" +
            "id=" + getId() +
            ", locationField='" + getLocationField() + "'" +
            "}";
    }
}
