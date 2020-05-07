package org.benetech.servicenet.service.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderActivityRecordDTO {

    private OrganizationDTO organization;

    private ZonedDateTime lastUpdated;

    private Set<LocationRecordDTO> locations;

    private Set<ServiceRecordDTO> services;

    public ProviderActivityRecordDTO(ActivityRecordDTO activityRecordDTO) {
        this.organization = activityRecordDTO.getOrganization();
        this.lastUpdated = activityRecordDTO.getLastUpdated();
        this.locations = activityRecordDTO.getLocations();
        this.services = activityRecordDTO.getServices();
    }
}
