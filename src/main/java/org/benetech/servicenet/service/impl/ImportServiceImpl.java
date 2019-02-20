package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.OrganizationImportService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class ImportServiceImpl implements ImportService {

    @Autowired
    private TransactionSynchronizationService transactionSynchronizationService;

    @Autowired
    private OrganizationImportService organizationImportService;

    @Autowired
    private ServiceImportService serviceImportService;

    @Autowired
    private LocationImportService locationImportService;

    @Autowired
    private TaxonomyImportService taxonomyImportService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @ConfidentialFilter
    public Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId,
                                                   String providerName, DataImportReport report) {
        Organization organization = organizationImportService.createOrUpdateOrganization(
            filledOrganization, externalDbId, providerName, report);

        importLocations(filledOrganization.getLocations(), organization, providerName, report);
        importServices(filledOrganization.getServices(), organization, providerName, report);

        registerSynchronizationOfMatchingOrganizations(organization);

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
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, String providerName,
                                           DataImportReport report) {
        return locationImportService.createOrUpdateLocation(filledLocation, externalDbId, providerName, report);
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
            savedServices.add(createOrUpdateService(
                service, service.getExternalDbId(), providerName, report));
        }
        org.setServices(savedServices);
    }

    private void importLocations(Set<Location> locations, Organization org, String providerName, DataImportReport report) {
        Set<Location> savedLocations = new HashSet<>();
        for (Location location : locations) {
            location.setOrganization(org);
            savedLocations.add(createOrUpdateLocation(
                location, location.getExternalDbId(), providerName, report));
        }
        org.setLocations(savedLocations);
    }

    private void registerSynchronizationOfMatchingOrganizations(Organization organization) {
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations(organization);
    }
}
