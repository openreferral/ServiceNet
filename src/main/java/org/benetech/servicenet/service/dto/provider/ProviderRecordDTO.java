package org.benetech.servicenet.service.dto.provider;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.UserDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRecordDTO {

    private OrganizationDTO organization;

    private ZonedDateTime lastUpdated;

    private Set<SimpleLocationDTO> locations;

    private Set<SimpleServiceDTO> services;

    private String userLogin;

    private UserDTO owner;

    private Set<DailyUpdateDTO> dailyUpdates;

    public ProviderRecordDTO(OrganizationDTO organization, ZonedDateTime lastUpdated,
        Set<SimpleLocationDTO> locations, Set<SimpleServiceDTO> services, UserDTO owner,
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
