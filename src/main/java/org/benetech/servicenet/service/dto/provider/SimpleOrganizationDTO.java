package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;

/**
 * A DTO for the Organization entity.
 */
@Data
public class SimpleOrganizationDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    @Lob
    private String description;

    @Size(max = 50)
    private String email;

    private String url;

    private String update;

    private ZonedDateTime updatedAt;

    @Getter
    private List<SimpleLocationDTO> locations;

    @Getter
    private List<SimpleServiceDTO> services;

    @Getter
    private List<DailyUpdateDTO> dailyUpdates;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimpleOrganizationDTO organizationDTO = (SimpleOrganizationDTO) o;
        if (organizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
