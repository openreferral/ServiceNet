package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.*;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.ImportService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HealthleadsDataPersistence {

    private Map<String, Location> locations = new HashMap<>();
    private Map<String, Organization> organizations = new HashMap<>();
    private Map<String, Service> services = new HashMap<>();
    private Map<String, PhysicalAddress> physicalAddresses = new HashMap<>();
    private Map<String, Set<Phone>> phones = new HashMap<>();
    private Map<String, Eligibility> eligibilities = new HashMap<>();
    private Map<String, Language> languages = new HashMap<>();
    private Map<String, RequiredDocument> requiredDocuments = new HashMap<>();
    private Map<String, ServiceAtLocation> serviceAtLocations = new HashMap<>();
    private Map<String, ServiceTaxonomy> serviceTaxonomies = new HashMap<>();
    private Map<String, Taxonomy> taxonomies = new HashMap<>();

    private ImportService importService;
    private HealthLeadsDataMapper mapper;
    private final String providerName;
    private DataImportReport report;

    public HealthleadsDataPersistence(ImportService importService, HealthLeadsDataMapper mapper, String providerName, DataImportReport report) {
        this.importService = importService;
        this.mapper = mapper;
        this.providerName = providerName;
        this.report = report;
    }

    public void addData(BaseData data) {
        if (data instanceof Eligibility) {
            eligibilities.put(((Eligibility) data).getServiceId(), (Eligibility) data);
        } else if (data instanceof Language) {
            languages.put(((Language) data).getLocationId(), (Language) data);
        } else if (data instanceof Location) {
            locations.put(data.getId(), (Location) data);
        } else if (data instanceof Organization) {
            organizations.put(data.getId(), (Organization) data);
        } else if (data instanceof Phone) {
            if (!phones.containsKey(((Phone) data).getLocationId())) {
                phones.put(((Phone) data).getLocationId(), new HashSet<>());
            }
            phones.get(((Phone) data).getLocationId()).add((Phone) data);
        } else if (data instanceof PhysicalAddress) {
            physicalAddresses.put(((PhysicalAddress) data).getLocationId(), (PhysicalAddress) data);
        } else if (data instanceof RequiredDocument) {
            requiredDocuments.put(((RequiredDocument) data).getServiceId(), (RequiredDocument) data);
        } else if (data instanceof Service) {
            services.put(((Service) data).getOrganizationId(), (Service) data);
        } else if (data instanceof ServiceAtLocation) {
            serviceAtLocations.put(((ServiceAtLocation) data).getLocationId(), (ServiceAtLocation) data);
        } else if (data instanceof ServiceTaxonomy) {
            serviceTaxonomies.put(((ServiceTaxonomy) data).getServiceId(), (ServiceTaxonomy) data);
        } else if (data instanceof Taxonomy) {
            taxonomies.put(data.getId(), (Taxonomy) data);
        }
    }

    public DataImportReport persistData() {
        for (Location location : locations.values()) {
            Organization organization = organizations.get(location.getOrganizationId());
            Service service = services.get(organization.getId());
            PhysicalAddress physicalAddress = physicalAddresses.get(location.getId());
            Set<Phone> phoneSet = phones.get(location.getId());
            Eligibility eligibility = eligibilities.get(service.getId());
            Language language = this.languages.get(location.getId());
            ServiceTaxonomy serviceTaxonomy = serviceTaxonomies.get(service.getId());
            Taxonomy taxonomy = taxonomies.get(serviceTaxonomy.getTaxonomyId());
            ServiceAtLocation serviceAtLocation = serviceAtLocations.get(location.getId());
            RequiredDocument requiredDocument = requiredDocuments.get(service.getId());

            org.benetech.servicenet.domain.Location savedLocation = saveLocation(mapper.extractLocation(location), location.getId());
            org.benetech.servicenet.domain.Organization savedOrganization
                = saveOrganization(getOrganization(organization, savedLocation), organization.getId());
            org.benetech.servicenet.domain.Service savedService = saveService(getService(service, savedOrganization), service.getId());
            savePhysicalAddress(mapper.extractPhysicalAddress(physicalAddress), savedLocation);
            saveServiceAtLocation(mapper.extractServiceAtLocation(serviceAtLocation), serviceAtLocation.getId(), savedService, savedLocation);
            savePhones(mapper.extractPhones(phoneSet), savedService, savedLocation);
            saveEligibility(mapper.extractEligibility(eligibility), savedService);
            saveLanguages(mapper.extractLanguages(language), savedService, savedLocation);
            org.benetech.servicenet.domain.Taxonomy extractedTaxonomy = saveTaxonomy(mapper.extractTaxonomy(taxonomy), taxonomy.getId());
            saveServiceTaxonomy(mapper.extractServiceTaxonomy(serviceTaxonomy), serviceTaxonomy.getId(), savedService, extractedTaxonomy);
            saveRequiredDocument(mapper.extractRequiredDocument(requiredDocument), requiredDocument.getId(), savedService);
        }
        return report;
    }

    private org.benetech.servicenet.domain.Location saveLocation(org.benetech.servicenet.domain.Location location, String externalDbId) {
        return importService.createOrUpdateLocation(location, externalDbId, providerName);
    }

    private org.benetech.servicenet.domain.Organization saveOrganization(org.benetech.servicenet.domain.Organization organization, String externalDbId) {
        return importService.createOrUpdateOrganization(organization, externalDbId, providerName, report);
    }

    private org.benetech.servicenet.domain.Organization getOrganization(Organization organization, org.benetech.servicenet.domain.Location savedLocation) {
        return mapper.extractOrganization(organization).location(savedLocation).active(true);
    }

    private org.benetech.servicenet.domain.Service saveService(org.benetech.servicenet.domain.Service service, String externalDbId) {
        return importService.createOrUpdateService(service, externalDbId, providerName, report);
    }

    private org.benetech.servicenet.domain.Service getService(Service service, org.benetech.servicenet.domain.Organization extractedOrganization) {
        return mapper.extractService(service)
            .organization(extractedOrganization);
    }

    private void savePhysicalAddress(org.benetech.servicenet.domain.PhysicalAddress physicalAddress, org.benetech.servicenet.domain.Location location) {
        importService.createOrUpdatePhysicalAddress(physicalAddress, location);
    }

    private void saveServiceAtLocation(org.benetech.servicenet.domain.ServiceAtLocation serviceAtLocation, String externalDbId,
                                       org.benetech.servicenet.domain.Service service, org.benetech.servicenet.domain.Location location) {
        importService.createOrUpdateServiceAtLocation(serviceAtLocation, externalDbId, providerName, service, location);
    }

    private void savePhones(Set<org.benetech.servicenet.domain.Phone> phones,
                            org.benetech.servicenet.domain.Service service, org.benetech.servicenet.domain.Location location) {
        importService.createOrUpdatePhones(phones, service, location);
    }

    private void saveEligibility(org.benetech.servicenet.domain.Eligibility eligibility, org.benetech.servicenet.domain.Service service) {
        importService.createOrUpdateEligibility(eligibility, service);
    }

    private void saveLanguages(Set<org.benetech.servicenet.domain.Language> languages,
                               org.benetech.servicenet.domain.Service service, org.benetech.servicenet.domain.Location location) {
        importService.createOrUpdateLangs(languages, service, location);
    }

    private org.benetech.servicenet.domain.Taxonomy saveTaxonomy(org.benetech.servicenet.domain.Taxonomy taxonomy, String extermalDbId) {
        return importService.createOrUpdateTaxonomy(taxonomy, extermalDbId, providerName);
    }

    private void saveServiceTaxonomy(org.benetech.servicenet.domain.ServiceTaxonomy serviceTaxonomy, String externalDbId, org.benetech.servicenet.domain.Service service, org.benetech.servicenet.domain.Taxonomy taxonomy) {
        importService.createOrUpdateServiceTaxonomy(serviceTaxonomy, externalDbId, providerName, service, taxonomy);
    }

    private void saveRequiredDocument(org.benetech.servicenet.domain.RequiredDocument requiredDocument, String externalDbId, org.benetech.servicenet.domain.Service service) {
        if (requiredDocument != null) {
            importService.createOrUpdateRequiredDocument(requiredDocument, externalDbId, providerName, service);
        }
    }
}
