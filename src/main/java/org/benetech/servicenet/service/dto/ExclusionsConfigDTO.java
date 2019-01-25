package org.benetech.servicenet.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
public class ExclusionsConfigDTO implements Serializable {

    private UUID id;

    private UUID accountId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExclusionsConfigDTO exclusionsConfigDTO = (ExclusionsConfigDTO) o;
        if (exclusionsConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exclusionsConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExclusionsConfigDTO{" +
            "id=" + getId() +
            ", account=" + getAccountId() +
            "}";
    }
}
