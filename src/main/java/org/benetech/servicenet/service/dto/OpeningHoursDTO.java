package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the OpeningHours entity.
 */
public class OpeningHoursDTO implements Serializable {

    private UUID id;

    @NotNull
    private Integer weekday;

    private String opensAt;

    private String closesAt;

    private UUID regularScheduleId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public String getOpensAt() {
        return opensAt;
    }

    public void setOpensAt(String opensAt) {
        this.opensAt = opensAt;
    }

    public String getClosesAt() {
        return closesAt;
    }

    public void setClosesAt(String closesAt) {
        this.closesAt = closesAt;
    }

    public UUID getRegularScheduleId() {
        return regularScheduleId;
    }

    public void setRegularScheduleId(UUID regularScheduleId) {
        this.regularScheduleId = regularScheduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OpeningHoursDTO openingHoursDTO = (OpeningHoursDTO) o;
        if (openingHoursDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), openingHoursDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OpeningHoursDTO{" +
            "id=" + getId() +
            ", weekday=" + getWeekday() +
            ", opensAt='" + getOpensAt() + "'" +
            ", closesAt='" + getClosesAt() + "'" +
            ", regularSchedule=" + getRegularScheduleId() +
            "}";
    }
}
