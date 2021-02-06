package org.benetech.servicenet.service.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OrganizationImportService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class ImportServiceImpl implements ImportService {

    @Autowired
    private OrganizationImportService organizationImportService;

    @Autowired
    private ServiceImportService serviceImportService;

    @Autowired
    private LocationImportService locationImportService;

    @Autowired
    private TaxonomyImportService taxonomyImportService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @ConfidentialFilter
    public Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId,
                                                   ImportData importData, boolean overwriteLastUpdated) {
        Optional<Organization> organizationFromDb =
            organizationRepository.findOneWithEagerAssociationsByExternalDbIdAndProviderName(externalDbId, importData.getProviderName());
        Set<UUID> locIds = new HashSet<>();
        organizationFromDb.ifPresent(
            organization -> organization.getLocations().stream()
                .map(Location::getId)
                .filter(Objects::nonNull)
                .forEach(locIds::add));
        Organization organization = organizationImportService.createOrUpdateOrganization(
            filledOrganization, organizationFromDb.orElse(null), externalDbId, importData.getProviderName(), importData.getReport(), overwriteLastUpdated);

        if (organization != null) {
            importLocations(filledOrganization.getLocations(), organization, locIds, importData);
            importServices(filledOrganization.getServices(), organization,
                importData.getProviderName(), importData.getReport());
        }
        return organization;
    }

    @Override
    @ConfidentialFilter
    @Transactional(propagation = Propagation.REQUIRED)
    public Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName,
                                           DataImportReport report) {
        return taxonomyImportService.createOrUpdateTaxonomy(taxonomy, externalDbId, providerName, report);
    }

    @Override
    @ConfidentialFilter
    @Transactional(propagation = Propagation.REQUIRED)
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, ImportData importData) {
        return locationImportService.createOrUpdateLocation(filledLocation, externalDbId, importData);
    }

    @Override
    @ConfidentialFilter
    @Transactional(propagation = Propagation.REQUIRED)
    public Service createOrUpdateService(Service filledService, String externalDbId, String providerName,
                                         DataImportReport report) {
        return serviceImportService.createOrUpdateService(filledService, externalDbId, providerName, report);
    }

    private void importServices(Set<Service> services, Organization org, String providerName, DataImportReport report) {
        Set<Service> savedServices = new HashSet<>();
        for (Service service : services) {
            service.setOrganization(org);
            savedServices.add(createOrUpdateService(service, service.getExternalDbId(), providerName, report));
        }
        org.setServices(savedServices);
    }

    private void importLocations(Set<Location> locations, Organization org, Set<UUID> existingLocations, ImportData importData) {
        Set<Location> savedLocations = new HashSet<>();
        for (Location location : locations) {
            location.setOrganization(org);
            savedLocations.add(createOrUpdateLocation(location, location.getExternalDbId(), importData));
        }
        existingLocations.stream()
            .filter(id -> savedLocations.stream().noneMatch(sl -> sl.getId().equals(id)))
            .forEach(id -> locationService.delete(id));
        org.setLocations(savedLocations);
    }
}
