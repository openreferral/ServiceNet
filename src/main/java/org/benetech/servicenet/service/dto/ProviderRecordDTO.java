package org.benetech.servicenet.service.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
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

    private String userLogin;

    private UserDTO owner;

    private Set<DailyUpdateDTO> dailyUpdates;

    public ProviderRecordDTO(OrganizationDTO organization, ZonedDateTime lastUpdated,
        Set<LocationRecordDTO> locations, Set<ServiceRecordDTO> services, UserDTO owner,
        Set<DailyUpdateDTO> dailyUpdates) {
        this.organization = organization;
        this.lastUpdated = lastUpdated;
        this.locations = locations;
        this.services = services;
        this.owner = owner;
        this.dailyUpdates = dailyUpdates;
    }

    public ProviderRecordDTO(UUID orgId, String orgName, UUID orgAccountId,
        String userLogin, ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
        this.userLogin = userLogin;
        this.organization = new OrganizationDTO(orgId, orgName, orgAccountId);
    }
}
