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
public class ProviderRecordDTO {

    private OrganizationDTO organization;

    private ZonedDateTime lastUpdated;

    private Set<LocationRecordDTO> locations;

    private Set<ServiceRecordDTO> services;
}
