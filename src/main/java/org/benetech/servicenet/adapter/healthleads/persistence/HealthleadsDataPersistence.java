package org.benetech.servicenet.adapter.healthleads.persistence;

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
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceAtLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceTaxonomy;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsTaxonomy;
import org.benetech.servicenet.adapter.healthleads.model.LocationRelatedHealthleadsData;
import org.benetech.servicenet.adapter.healthleads.model.ServiceRelatedHealthleadsData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ImportService;

import java.util.Set;

public class HealthleadsDataPersistence {

    private HealthleadsStorage storage = new HealthleadsStorage();

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
        Class<? extends HealthleadsBaseData> clazz = data.getClass();

        if (data instanceof HealthleadsPhone) {
            handlePhones(data, clazz);
        } else if (data instanceof HealthleadsService) {
            handleServices(data, clazz);
        } else {
            if (data instanceof LocationRelatedHealthleadsData) {
                storage.addBaseData(clazz, ((LocationRelatedHealthleadsData) data).getLocationId(), data);
            } else if (data instanceof ServiceRelatedHealthleadsData) {
                storage.addBaseData(clazz, ((ServiceRelatedHealthleadsData) data).getServiceId(), data);
            } else {
                storage.addBaseData(clazz, data.getId(), data);
            }
        }
    }

    public DataImportReport persistData() {
        for (HealthleadsLocation location : storage.getValuesOfClass(HealthleadsLocation.class)) {
            Location savedLocation = saveLocation(mapper.extractLocation(location), location.getId());

            HealthleadsOrganization organization = storage
                .getBaseData(HealthleadsOrganization.class, location.getOrganizationId());
            Organization savedOrganization
                = saveOrganization(getOrganization(organization, savedLocation), organization.getId());

            savePhysicalAddress(mapper.extractPhysicalAddress(storage
                .getBaseData(HealthleadsPhysicalAddress.class, location.getId())), savedLocation);

            for (HealthleadsService service : storage.getBaseDataSet(HealthleadsService.class, organization.getId())) {
                Service savedService = saveService(getService(service, savedOrganization), service.getId());

                saveLocationRelatedData(location.getId(), savedService, savedLocation);
                saveTaxonomyRelatedData(service.getId(), savedService);

                saveEligibility(mapper.extractEligibility(storage
                    .getBaseData(HealthleadsEligibility.class, service.getId())), savedService);

                saveRequiredDocument(storage.getBaseData(HealthleadsRequiredDocument.class, service.getId()), savedService);
            }
        }
        return report;
    }

    private void saveTaxonomyRelatedData(String serviceId, Service savedService) {
        HealthleadsServiceTaxonomy serviceTaxonomy = storage
            .getBaseData(HealthleadsServiceTaxonomy.class, serviceId);
        HealthleadsTaxonomy taxonomy = storage
            .getBaseData(HealthleadsTaxonomy.class, serviceTaxonomy.getTaxonomyId());
        Taxonomy extractedTaxonomy = saveTaxonomy(mapper.extractTaxonomy(taxonomy), taxonomy.getId());
        saveServiceTaxonomy(mapper.extractServiceTaxonomy(serviceTaxonomy),
            serviceTaxonomy.getId(), savedService, extractedTaxonomy);
    }

    private void saveLocationRelatedData(String locationId, Service savedService, Location savedLocation) {
        Set<HealthleadsPhone> phoneSet = storage.getBaseDataSet(HealthleadsPhone.class, locationId);
        savePhones(mapper.extractPhones(phoneSet), savedService, savedLocation);

        HealthleadsLanguage language = storage
            .getBaseData(HealthleadsLanguage.class, locationId);
        saveLanguages(mapper.extractLanguages(language), savedService, savedLocation);

        HealthleadsServiceAtLocation serviceAtLocation = storage
            .getBaseData(HealthleadsServiceAtLocation.class, locationId);
        saveServiceAtLocation(mapper.extractServiceAtLocation(serviceAtLocation),
            serviceAtLocation.getId(), savedService, savedLocation);
    }

    private void handlePhones(HealthleadsBaseData data, Class<? extends HealthleadsBaseData> clazz) {
        storage.addBaseDataToSet(clazz, ((HealthleadsPhone) data).getLocationId(), data);
    }
    
    private void handleServices(HealthleadsBaseData data, Class<? extends HealthleadsBaseData> clazz) {
        storage.addBaseDataToSet(clazz, ((HealthleadsService) data).getOrganizationId(), data);
    }

    private Location saveLocation(Location location, String externalDbId) {
        location.setExternalDbId(externalDbId);
        location.setProviderName(providerName);
        return importService.createOrUpdateLocation(location, externalDbId, providerName);
    }

    private Organization saveOrganization(Organization organization, String externalDbId) {
        organization.setExternalDbId(externalDbId);
        organization.setProviderName(providerName);
        return importService.createOrUpdateOrganization(organization, externalDbId, providerName, report);
    }

    private Organization getOrganization(HealthleadsOrganization organization, Location savedLocation) {
        return mapper.extractOrganization(organization).location(savedLocation).active(true);
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

    private void saveServiceAtLocation(ServiceAtLocation serviceAtLocation, String externalDbId,
                                       Service service, Location location) {
        serviceAtLocation.setExternalDbId(externalDbId);
        serviceAtLocation.setProviderName(providerName);
        importService.createOrUpdateServiceAtLocation(serviceAtLocation, externalDbId, providerName, service, location);
    }

    private void savePhones(Set<Phone> phones,
                            Service service, Location location) {
        importService.createOrUpdatePhones(phones, service, location);
    }

    private void saveEligibility(Eligibility eligibility, Service service) {
        importService.createOrUpdateEligibility(eligibility, service);
    }

    private void saveLanguages(Set<Language> languages,
                               Service service, Location location) {
        importService.createOrUpdateLangs(languages, service, location);
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

    private void saveRequiredDocument(HealthleadsRequiredDocument healthleadsRequiredDocument, Service service) {
        RequiredDocument requiredDocument = mapper.extractRequiredDocument(healthleadsRequiredDocument);
        String externalDbId = healthleadsRequiredDocument.getId();
        if (requiredDocument != null) {
            requiredDocument.setExternalDbId(externalDbId);
            requiredDocument.setProviderName(providerName);
            importService.createOrUpdateRequiredDocument(requiredDocument, externalDbId, providerName, service);
        }
    }
}
