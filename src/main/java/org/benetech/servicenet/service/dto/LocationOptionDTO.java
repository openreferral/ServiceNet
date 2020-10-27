package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the Location entity.
 */
@Data
@NoArgsConstructor
public class LocationOptionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID organizationId;

    @NotNull
    private String name;

    public LocationOptionDTO(UUID id, String name, UUID organizationId) {
        this.id = id;
        this.name = name;
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

        LocationOptionDTO organizationDTO = (LocationOptionDTO) o;
        if (organizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationOptionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
