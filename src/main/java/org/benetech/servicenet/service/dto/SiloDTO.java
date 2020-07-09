package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.Silo} entity.
 */
public class SiloDTO implements Serializable {

    private UUID id;

    private String name;

    private boolean isPublic = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SiloDTO siloDTO = (SiloDTO) o;
        if (siloDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), siloDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SiloDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
