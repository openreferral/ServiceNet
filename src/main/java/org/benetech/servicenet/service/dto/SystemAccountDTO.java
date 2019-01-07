package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the SystemAccount entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemAccountDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SystemAccountDTO systemAccountDTO = (SystemAccountDTO) o;
        if (systemAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
