package org.benetech.servicenet.service.mapper;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.service.LocationMatchService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.benetech.servicenet.util.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity OrganizationMatch and its DTO OrganizationMatchDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, unmappedTargetPolicy = IGNORE)
public abstract class OrganizationMatchMapper {

    @Autowired
    private LocationMatchService locationMatchService;

    @Autowired
    private MetadataRepository metadataRepository;

    @Mapping(source = "organizationRecord.id", target = "organizationRecordId")
    @Mapping(source = "organizationRecord.name", target = "organizationRecordName")
    @Mapping(source = "partnerVersion.id", target = "partnerVersionId")
    @Mapping(source = "partnerVersion.name", target = "partnerVersionName")
    @Mapping(source = "dismissedBy.id", target = "dismissedById")
    @Mapping(source = "dismissedBy.login", target = "dismissedByName")
    @Mapping(source = "hiddenBy.id", target = "hiddenById")
    @Mapping(source = "hiddenBy.login", target = "hiddenByName")
    public abstract OrganizationMatchDTO toDto(OrganizationMatch organizationMatch);

    @Mapping(source = "organizationRecordId", target = "organizationRecord")
    @Mapping(source = "partnerVersionId", target = "partnerVersion")
    public abstract OrganizationMatch toEntity(OrganizationMatchDTO organizationMatchDTO);

    public OrganizationMatchDTO toDtoWithLocationMatches(OrganizationMatch organizationMatch) {
        OrganizationMatchDTO dto = toDto(organizationMatch);
        Organization partner = organizationMatch.getPartnerVersion();
        Map<UUID, Set<UUID>> locationMatches = new HashMap<>();
        for (Location location : organizationMatch.getOrganizationRecord().getLocations()) {
            Set<UUID> matches = new HashSet<>();
            for (LocationMatch match : locationMatchService.findAllForLocation(location.getId())) {
                if (partner.getLocations().contains(match.getMatchingLocation())) {
                    matches.add(match.getMatchingLocation().getId());
                }
            }
            if (matches.size() > 0) {
                locationMatches.put(location.getId(), matches);
            }
        }
        dto.setNumberOfLocations(partner.getLocations().size());
        dto.setProviderName(partner.getAccount().getName());
        dto.setLocationMatches(locationMatches);
        dto.setFreshness(getFreshness(partner));
        return dto;
    }

    public OrganizationMatch fromId(UUID id) {
        if (id == null) {
            return null;
        }
        OrganizationMatch organizationMatch = new OrganizationMatch();
        organizationMatch.setId(id);
        return organizationMatch;
    }

    private long getFreshness(Organization organization) {
        long freshness = 0L;
        long numberOfRecords = 1L;
        Set<ZonedDateTime> updateDates = new HashSet<>();

        for (Location location : organization.getLocations()) {
            ZonedDateTime lastUpdate = (location.getLastVerifiedOn() != null) ? location.getLastVerifiedOn() :
                location.getUpdatedAt();
            freshness += daysSince(lastUpdate);
            updateDates.add(lastUpdate);
            numberOfRecords++;
        }

        for (Service service : organization.getServices()) {
            ZonedDateTime lastUpdate = (service.getLastVerifiedOn() != null) ? service.getLastVerifiedOn() :
                service.getUpdatedAt();
            freshness += daysSince(lastUpdate);
            updateDates.add(lastUpdate);
            numberOfRecords++;
        }

        if (organization.getLastVerifiedOn() != null) {
            freshness += daysSince(organization.getLastVerifiedOn());
        } else {
            // calculate partial update for organization
            Set<UUID> resourceIds = new HashSet<>();
            resourceIds.add(organization.getId());
            if (organization.getFunding() != null) {
                resourceIds.add(organization.getFunding().getId());
            }
            resourceIds.addAll(CollectionUtils.getIds(organization.getPrograms()));
            resourceIds.addAll(CollectionUtils.getIds(organization.getContacts()));
            resourceIds.addAll(CollectionUtils.getIds(organization.getLocations()));
            resourceIds.addAll(CollectionUtils.getIds(organization.getServices()));
            updateDates.addAll(metadataRepository.findByResourceIds(resourceIds).stream()
                .map(Metadata::getLastActionDate).collect(Collectors.toSet()));
            ZonedDateTime lastUpdate = updateDates.stream().max(ZonedDateTime::compareTo).get();
            freshness += daysSince(lastUpdate);
        }

        freshness /= numberOfRecords;
        return freshness;
    }

    private long daysSince(ZonedDateTime date) {
        return date.until(ZonedDateTime.now(), ChronoUnit.DAYS);
    }
}
