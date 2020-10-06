package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the HolidaySchedule entity.
 */
@Getter
@Setter
public class HolidayScheduleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private Boolean closed;

    private String opensAt;

    private String closesAt;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private UUID srvcId;

    private String srvcName;

    private UUID locationId;

    private String locationName;

    public Boolean isClosed() {
        return closed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HolidayScheduleDTO holidayScheduleDTO = (HolidayScheduleDTO) o;
        if (holidayScheduleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), holidayScheduleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HolidayScheduleDTO{" +
            "id=" + getId() +
            ", closed='" + isClosed() + "'" +
            ", opensAt='" + getOpensAt() + "'" +
            ", closesAt='" + getClosesAt() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            "}";
    }
}
