package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the HolidaySchedule entity.
 */
@Getter
@Setter
public class ProviderHolidayScheduleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private Boolean closed;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

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

        ProviderHolidayScheduleDTO holidayScheduleDTO = (ProviderHolidayScheduleDTO) o;
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
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
