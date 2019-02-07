package org.benetech.servicenet.adapter.healthleads.persistence;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.healthleads.HealthLeadsDataMapper;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsBaseData;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsEligibility;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLanguage;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsOrganization;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhone;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhysicalAddress;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsRequiredDocument;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsService;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceTaxonomy;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsTaxonomy;
import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.shared.model.storage.EntryDictionary;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ImportService;

import java.util.HashSet;
import java.util.Set;

public class RelationManager {

    private EntryDictionary<HealthleadsBaseData> dictionary = new EntryDictionary<>();
    private PersistenceManager persistence;

    public RelationManager(ImportService importService, MultipleImportData importData) {
        this.persistence = new PersistenceManager(importService, importData, HealthLeadsDataMapper.INSTANCE);
    }

    public void addData(HealthleadsBaseData data) {
        if (isLocationBased(data)) {
            dictionary.addData(data.getLocationId(), data, HealthleadsLocation.class);
        } else if (isServiceBased(data)) {
            dictionary.addData(data.getServiceId(), data, HealthleadsService.class);
        } else if (isOrganizationBased(data)) {
            dictionary.addData(data.getOrganizationId(), data, HealthleadsOrganization.class);
        } else {
            dictionary.addData(data.getId(), data, HealthleadsBaseData.class);
        }
    }

    public DataImportReport persistData() {
        Set<HealthleadsService> processedServices = saveOrganizationsAndServices();
        saveServicesWithoutOrganization(processedServices);
        return persistence.getReport();
    }

    private void saveServicesWithoutOrganization(Set<HealthleadsService> processedServices) {
        Set<HealthleadsService> services = dictionary.getEntitiesOfClass(HealthleadsService.class);
        services.removeAll(processedServices);
        saveServicesAndRelatedData(services, null);
    }

    private Set<HealthleadsService> saveOrganizationsAndServices() {
        Set<HealthleadsService> processedServices = new HashSet<>();
        for (HealthleadsOrganization organization : dictionary.getEntitiesOfClass(HealthleadsOrganization.class)) {
            String organizationId = organization.getId();
            Organization savedOrg = persistence.importOrganization(organization);
            saveLocationsAndRelatedData(savedOrg, organizationId);
            processedServices.addAll(saveServicesAndRelatedData(savedOrg, organizationId));
        }
        return processedServices;
    }

    private Set<HealthleadsService> saveServicesAndRelatedData(Organization savedOrg, String organizationId) {
        Set<HealthleadsService> services = dictionary.getRelatedEntities(
            HealthleadsService.class, organizationId, HealthleadsOrganization.class);
        return saveServicesAndRelatedData(services, savedOrg);
    }

    private Set<HealthleadsService> saveServicesAndRelatedData(Set<HealthleadsService> services, Organization savedOrg) {
        Set<HealthleadsService> processedServices = new HashSet<>();
        for (HealthleadsService service : services) {
            String externalServiceId = service.getId();
            Service savedService = persistence.importService(service, savedOrg);
            processedServices.add(service);
            saveTaxonomyRelatedData(externalServiceId, savedService);
            savePhonesForService(savedService, externalServiceId);
            saveLanguagesForService(savedService, externalServiceId);
            saveEligibilities(savedService, externalServiceId);
            saveRequiredDocuments(savedService, externalServiceId);
        }
        return processedServices;
    }

    private void saveEligibilities(Service savedService, String externalServiceId) {
        persistence.importEligibilities(dictionary.getRelatedEntities(
            HealthleadsEligibility.class, externalServiceId, HealthleadsService.class),
            savedService);
    }

    private void saveRequiredDocuments(Service savedService, String externalServiceId) {
        persistence.importRequiredDocuments(dictionary.getRelatedEntities(
            HealthleadsRequiredDocument.class, externalServiceId, HealthleadsService.class),
            savedService);
    }

    private void saveLanguagesForService(Service savedService, String externalServiceId) {
        persistence.importLanguagesForService(dictionary.getRelatedEntities(
            HealthleadsLanguage.class, externalServiceId, HealthleadsService.class), 
            savedService);
    }

    private void saveLocationsAndRelatedData(Organization savedOrg, String organizationId) {
        Set<HealthleadsLocation> locations = dictionary.getRelatedEntities(
            HealthleadsLocation.class, organizationId, HealthleadsOrganization.class);
        for (HealthleadsLocation healthleadsLocation : locations) {
            String externalLocationId = healthleadsLocation.getId();
            Location savedLocation = persistence.importLocation(healthleadsLocation, savedOrg);
            if (savedLocation != null) {
                savePhysicalAddresses(savedLocation, externalLocationId);
                savePhonesForLocation(savedLocation, externalLocationId);
                saveLanguagesForLocation(savedLocation, externalLocationId);
            }
        }
    }

    private void saveLanguagesForLocation(Location savedLocation, String externalLocationId) {
        persistence.importLanguagesForLocation(dictionary.getRelatedEntities(
            HealthleadsLanguage.class, externalLocationId, HealthleadsLocation.class),
            savedLocation);
    }

    private void savePhonesForService(Service savedService, String externalServiceId) {
        persistence.importPhonesForService(dictionary.getRelatedEntities(
            HealthleadsPhone.class, externalServiceId, HealthleadsService.class),
            savedService);
    }

    private void savePhonesForLocation(Location savedLocation, String externalLocationId) {
        persistence.importPhonesForLocation(dictionary.getRelatedEntities(
            HealthleadsPhone.class, externalLocationId, HealthleadsLocation.class),
            savedLocation);
    }

    private void savePhysicalAddresses(Location savedLocation, String locationId) {
        persistence.importPhysicalAddresses(dictionary.getRelatedEntities(
            HealthleadsPhysicalAddress.class, locationId, HealthleadsLocation.class), savedLocation);
    }

    private void saveTaxonomyRelatedData(String serviceId, Service savedService) {
        Set<HealthleadsServiceTaxonomy> serviceTaxonomies = dictionary.getRelatedEntities(
            HealthleadsServiceTaxonomy.class, serviceId, HealthleadsService.class);

        for (HealthleadsServiceTaxonomy serviceTaxonomy : serviceTaxonomies) {
            Set<HealthleadsTaxonomy> taxonomies = dictionary.getRelatedEntities(
                HealthleadsTaxonomy.class, serviceTaxonomy.getTaxonomyId(), HealthleadsBaseData.class);
            for (HealthleadsTaxonomy taxonomy : taxonomies) {
                Taxonomy savedTaxonomy = persistence.importTaxonomy(taxonomy);
                persistence.importServiceTaxonomy(serviceTaxonomy, savedService, savedTaxonomy);
            }
        }
    }

    private boolean isServiceBased(HealthleadsBaseData data) {
        return StringUtils.isNotBlank(data.getServiceId());
    }

    private boolean isLocationBased(HealthleadsBaseData data) {
        return StringUtils.isNotBlank(data.getLocationId());
    }

    private boolean isOrganizationBased(HealthleadsBaseData data) {
        return StringUtils.isNotBlank(data.getOrganizationId());
    }
}
