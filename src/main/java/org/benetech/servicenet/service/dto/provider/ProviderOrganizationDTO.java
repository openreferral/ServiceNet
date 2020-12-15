package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;
import org.benetech.servicenet.service.dto.OpeningHoursRow;

/**
 * A DTO for the Organization entity.
 */
@Data
public class ProviderOrganizationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private String accountName;

    @Getter
    private List<ProviderLocationDTO> locations;

    @Getter
    private List<ProviderServiceDTO> services;

    @Getter
    private List<DailyUpdateDTO> dailyUpdates;

    @Getter
    private Map<Integer, List<String>> datesClosedByLocation;

    @Getter
    private Map<Integer, List<OpeningHoursRow>> openingHoursByLocation;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProviderOrganizationDTO organizationDTO = (ProviderOrganizationDTO) o;
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
