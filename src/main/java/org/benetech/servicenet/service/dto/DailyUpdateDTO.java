package org.benetech.servicenet.service.dto;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import lombok.NoArgsConstructor;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.DailyUpdate} entity.
 */
@NoArgsConstructor
public class DailyUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @Lob
    private String update;

    private ZonedDateTime expiry;

    private ZonedDateTime createdAt;

    private UUID organizationId;

    private String organizationName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public ZonedDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(ZonedDateTime expiry) {
        this.expiry = expiry;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public DailyUpdateDTO(UUID id, String update, ZonedDateTime expiry, ZonedDateTime createdAt,
        UUID organizationId) {
        this.id = id;
        this.update = update;
        this.expiry = expiry;
        this.createdAt = createdAt;
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

        DailyUpdateDTO dailyUpdateDTO = (DailyUpdateDTO) o;
        if (dailyUpdateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dailyUpdateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DailyUpdateDTO{" +
            "id=" + getId() +
            ", update='" + getUpdate() + "'" +
            ", expiry='" + getExpiry() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", organizationId=" + getOrganizationId() +
            ", organizationName='" + getOrganizationName() + "'" +
            "}";
    }
}
