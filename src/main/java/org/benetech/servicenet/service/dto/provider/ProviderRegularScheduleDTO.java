package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the RegularSchedule entity.
 */
public class ProviderRegularScheduleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private Set<ProviderOpeningHoursDTO> openingHours;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<ProviderOpeningHoursDTO> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(
        Set<ProviderOpeningHoursDTO> openingHours) {
        this.openingHours = openingHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProviderRegularScheduleDTO regularScheduleDTO = (ProviderRegularScheduleDTO) o;
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
            "}";
    }
}
