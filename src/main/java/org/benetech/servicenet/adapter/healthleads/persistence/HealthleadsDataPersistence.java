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
import org.benetech.servicenet.adapter.shared.model.storage.EntryDictionary;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ImportService;

import java.util.HashSet;
import java.util.Set;

public class HealthleadsDataPersistence {

    private EntryDictionary<HealthleadsBaseData> dictionary = new EntryDictionary<>();

    private ImportService importService;
    private HealthLeadsDataMapper mapper;
    private final String providerName;
    private DataImportReport report;

    public HealthleadsDataPersistence(ImportService importService,
                                      HealthLeadsDataMapper mapper,
                                      String providerName,
                                      DataImportReport report) {
        this.importService = importService;
        this.mapper = mapper;
        this.providerName = providerName;
        this.report = report;
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
        return report;
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

            Set<HealthleadsLocation> locations = dictionary.getRelatedEntities(
                HealthleadsLocation.class, organizationId, HealthleadsOrganization.class);

            Organization savedOrg = saveOrganization(getOrganization(organization), organizationId);

            saveLocationsAndRelatedData(locations, savedOrg);

            Set<HealthleadsService> services = dictionary.getRelatedEntities(
                HealthleadsService.class, organizationId, HealthleadsOrganization.class);

            processedServices.addAll(saveServicesAndRelatedData(services, savedOrg));
        }
        return processedServices;
    }

    private Set<HealthleadsService> saveServicesAndRelatedData(Set<HealthleadsService> services, Organization savedOrg) {
        Set<HealthleadsService> processedServices = new HashSet<>();
        for (HealthleadsService service : services) {
            String externalServiceId = service.getId();
            Service savedService = saveService(getService(service, savedOrg), externalServiceId);
            processedServices.add(service);

            saveTaxonomyRelatedData(externalServiceId, savedService);
            savePhonesForService(savedService, externalServiceId);
            saveLanguagesForService(savedService, externalServiceId);

            saveEligibilities(dictionary.getRelatedEntities(
                HealthleadsEligibility.class, service.getId(), HealthleadsService.class),
                savedService);

            saveRequiredDocuments(dictionary.getRelatedEntities(
                HealthleadsRequiredDocument.class, externalServiceId, HealthleadsService.class),
                savedService);
        }
        return processedServices;
    }

    private void saveLanguagesForService(Service savedService, String externalServiceId) {
        saveLanguagesForService(dictionary.getRelatedEntities(
            HealthleadsLanguage.class, externalServiceId, HealthleadsService.class), 
            savedService);
    }

    private void saveLanguagesForService(Set<HealthleadsLanguage> healthleadsLanguages, Service savedService) {
        for (HealthleadsLanguage language : healthleadsLanguages) {
            saveLanguages(mapper.extractLanguages(language), savedService, null);   
        }
    }

    private void saveLanguagesForLocation(Location savedLocation, String externalLocationId) {
        saveLanguagesForLocation(dictionary.getRelatedEntities(
            HealthleadsLanguage.class, externalLocationId, HealthleadsLocation.class),
            savedLocation);
    }

    private void saveLanguagesForLocation(Set<HealthleadsLanguage> healthleadsLanguages, Location savedLocation) {
        for (HealthleadsLanguage language : healthleadsLanguages) {
            saveLanguages(mapper.extractLanguages(language), null, savedLocation);
        }
    }

    private void savePhonesForService(Service savedService, String externalServiceId) {
        savePhonesForService(dictionary.getRelatedEntities(
            HealthleadsPhone.class, externalServiceId, HealthleadsService.class),
            savedService);
    }

    private void savePhonesForService(Set<HealthleadsPhone> phonesForService, Service savedService) {
        savePhones(mapper.extractPhones(phonesForService), savedService, null);
    }

    private void savePhonesForLocation(Location savedLocation, String externalLocationId) {
        savePhonesForLocation(dictionary.getRelatedEntities(
            HealthleadsPhone.class, externalLocationId, HealthleadsLocation.class),
            savedLocation);
    }

    private void savePhonesForLocation(Set<HealthleadsPhone> phonesForService, Location savedLocation) {
        savePhones(mapper.extractPhones(phonesForService), null, savedLocation);
    }

    private void savePhysicalAddresses(Location savedLocation, String locationId) {
        dictionary.getRelatedEntities(
            HealthleadsPhysicalAddress.class, locationId, HealthleadsLocation.class)
            .forEach(pa -> savePhysicalAddress(mapper.extractPhysicalAddress(pa), savedLocation));
    }

    private void saveTaxonomyRelatedData(String serviceId, Service savedService) {
        Set<HealthleadsServiceTaxonomy> serviceTaxonomies = dictionary.getRelatedEntities(
            HealthleadsServiceTaxonomy.class, serviceId, HealthleadsService.class);

        for (HealthleadsServiceTaxonomy serviceTaxonomy : serviceTaxonomies) {
            Set<HealthleadsTaxonomy> taxonomies = dictionary.getRelatedEntities(
                HealthleadsTaxonomy.class, serviceTaxonomy.getTaxonomyId(), HealthleadsBaseData.class);
            for (HealthleadsTaxonomy taxonomy : taxonomies) {
                Taxonomy savedTaxonomy = saveTaxonomy(mapper.extractTaxonomy(taxonomy), taxonomy.getId());
                saveServiceTaxonomy(mapper.extractServiceTaxonomy(serviceTaxonomy),
                    serviceTaxonomy.getId(), savedService, savedTaxonomy);
            }
        }
    }

    private void saveLocationsAndRelatedData(Set<HealthleadsLocation> healthleadsLocations,
                                                      Organization savedOrg) {
        for (HealthleadsLocation healthleadsLocation : healthleadsLocations) {
            String externalLocationId = healthleadsLocation.getId();
            Location savedLocation = saveLocation(mapper.extractLocation(healthleadsLocation),
                externalLocationId, savedOrg);
            if (savedLocation != null) {
                savePhysicalAddresses(savedLocation, externalLocationId);
                savePhonesForLocation(savedLocation, externalLocationId);
                saveLanguagesForLocation(savedLocation, externalLocationId);
            }
        }
    }

    private Location saveLocation(Location location, String externalLocationId, Organization savedOrg) {
        location.setExternalDbId(externalLocationId);
        location.setProviderName(providerName);
        location.setOrganization(savedOrg);
        return importService.createOrUpdateLocation(
            location, externalLocationId, providerName);
    }

    private Organization saveOrganization(Organization organization, String externalDbId) {
        organization.setExternalDbId(externalDbId);
        organization.setProviderName(providerName);
        return importService.createOrUpdateOrganization(organization, externalDbId, providerName, report);
    }

    private Organization getOrganization(HealthleadsOrganization organization) {
        return mapper.extractOrganization(organization).active(true);
    }

    private Service saveService(Service service, String externalDbId) {
        service.setExternalDbId(externalDbId);
        service.setProviderName(providerName);
        return importService.createOrUpdateService(service, externalDbId, providerName, report);
    }

    private Service getService(HealthleadsService service, Organization extractedOrganization) {
        return mapper.extractService(service)
            .organization(extractedOrganization);
    }

    private void savePhysicalAddress(PhysicalAddress physicalAddress, Location location) {
        importService.createOrUpdatePhysicalAddress(physicalAddress, location);
    }

    private void savePhones(Set<Phone> phones, Service service, Location location) {
        importService.createOrUpdatePhonesForService(phones, service, location);
    }

    private void saveEligibilities(Set<HealthleadsEligibility> healthleadsEligibilities, Service service) {
        for (HealthleadsEligibility healthleadsEligibility : healthleadsEligibilities) {
            Eligibility eligibility = mapper.extractEligibility(healthleadsEligibility);
            importService.createOrUpdateEligibility(eligibility, service);
        }
    }

    private void saveLanguages(Set<Language> languages,
                               Service service, Location location) {
        importService.createOrUpdateLangsForService(languages, service, location);
    }

    private Taxonomy saveTaxonomy(Taxonomy taxonomy, String extermalDbId) {
        taxonomy.setExternalDbId(extermalDbId);
        taxonomy.setProviderName(providerName);
        return importService.createOrUpdateTaxonomy(taxonomy, extermalDbId, providerName);
    }

    private void saveServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String externalDbId,
                                     Service service, Taxonomy taxonomy) {
        serviceTaxonomy.setExternalDbId(externalDbId);
        serviceTaxonomy.setProviderName(providerName);
        importService.createOrUpdateServiceTaxonomy(serviceTaxonomy, externalDbId, providerName, service, taxonomy);
    }

    private void saveRequiredDocuments(Set<HealthleadsRequiredDocument> healthleadsRequiredDocuments, Service service) {
        for (HealthleadsRequiredDocument healthleadsRequiredDocument : healthleadsRequiredDocuments) {
            RequiredDocument requiredDocument = mapper.extractRequiredDocument(healthleadsRequiredDocument);
            String externalDbId = healthleadsRequiredDocument.getId();
            requiredDocument.setExternalDbId(externalDbId);
            requiredDocument.setProviderName(providerName);
            importService.createOrUpdateRequiredDocument(requiredDocument, externalDbId, providerName, service);
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
