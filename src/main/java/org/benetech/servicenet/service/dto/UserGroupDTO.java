package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.UserGroup} entity.
 */
@Getter
@Setter
public class UserGroupDTO implements Serializable {

    private UUID id;

    private String name;

    private UUID siloId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGroupDTO)) {
            return false;
        }

        return id != null && id.equals(((UserGroupDTO) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", siloId=" + getSiloId() +
            "}";
    }
}
