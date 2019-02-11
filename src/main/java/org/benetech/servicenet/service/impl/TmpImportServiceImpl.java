package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.LocationImportService;
import org.benetech.servicenet.service.OrganizationImportService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.TmpImportService;
import org.benetech.servicenet.service.TransactionSynchronizationService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class TmpImportServiceImpl implements TmpImportService {

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
    public Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId,
                                                   String providerName, DataImportReport report) {
        Organization organization = organizationImportService.createOrUpdateOrganization(
            filledOrganization, externalDbId, providerName, report);

        importLocations(filledOrganization.getLocations(), organization, providerName);
        importServices(filledOrganization.getServices(), organization, providerName, report);

        registerSynchronizationOfMatchingOrganizations(organization);

        return organization;
    }

    @Override
    @ConfidentialFilter
    @Transactional(propagation = Propagation.REQUIRED)
    public Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName) {
        return taxonomyImportService.createOrUpdateTaxonomy(taxonomy, externalDbId, providerName);
    }

    @Override
    @ConfidentialFilter
    @Transactional(propagation = Propagation.REQUIRED)
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, String providerName) {
        return locationImportService.createOrUpdateLocation(filledLocation, externalDbId, providerName);
    }

    @Override
    @ConfidentialFilter
    @Transactional(propagation = Propagation.REQUIRED)
    public Service createOrUpdateService(Service filledService, String externalDbId, String providerName,
                                         DataImportReport report) {
        return serviceImportService.createOrUpdateService(filledService, externalDbId, providerName, report);
    }

    @ConfidentialFilter
    private Service createOrUpdateServiceExistingTransaction(Service filledService, String externalDbId,
                                                            String providerName, DataImportReport report) {
        return serviceImportService.createOrUpdateService(filledService, externalDbId, providerName, report);
    }

    @ConfidentialFilter
    private Location createOrUpdateLocationExistingTransaction(Location filledLocation, String externalDbId,
                                                               String providerName) {
        return locationImportService.createOrUpdateLocation(filledLocation, externalDbId, providerName);
    }

    private void importServices(Set<Service> services, Organization org, String providerName, DataImportReport report) {
        Set<Service> savedServices = new HashSet<>();
        for (Service service : services) {
            service.setOrganization(org);
            savedServices.add(createOrUpdateServiceExistingTransaction(
                service, service.getExternalDbId(), providerName, report));
        }
        org.setServices(savedServices);
    }

    private void importLocations(Set<Location> locations, Organization org, String providerName) {
        Set<Location> savedLocations = new HashSet<>();
        for (Location location : locations) {
            location.setOrganization(org);
            savedLocations.add(createOrUpdateLocationExistingTransaction(
                location, location.getExternalDbId(), providerName));
        }
        org.setLocations(savedLocations);
    }

    private void registerSynchronizationOfMatchingOrganizations(Organization organization) {
        transactionSynchronizationService.registerSynchronizationOfMatchingOrganizations(organization);
    }
}
