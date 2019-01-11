package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.BaseData;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HealthleadsDataPersistence {

    private Map<String, HealthleadsLocation> locations = new HashMap<>();
    private Map<String, HealthleadsOrganization> organizations = new HashMap<>();
    private Map<String, HealthleadsService> services = new HashMap<>();
    private Map<String, HealthleadsPhysicalAddress> physicalAddresses = new HashMap<>();
    private Map<String, Set<HealthleadsPhone>> phones = new HashMap<>();
    private Map<String, HealthleadsEligibility> eligibilities = new HashMap<>();
    private Map<String, HealthleadsLanguage> languages = new HashMap<>();
    private Map<String, HealthleadsRequiredDocument> requiredDocuments = new HashMap<>();
    private Map<String, HealthleadsServiceAtLocation> serviceAtLocations = new HashMap<>();
    private Map<String, HealthleadsServiceTaxonomy> serviceTaxonomies = new HashMap<>();
    private Map<String, HealthleadsTaxonomy> taxonomies = new HashMap<>();

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
        if (data instanceof HealthleadsEligibility) {
            eligibilities.put(((HealthleadsEligibility) data).getServiceId(), (HealthleadsEligibility) data);
        } else if (data instanceof HealthleadsLanguage) {
            languages.put(((HealthleadsLanguage) data).getLocationId(), (HealthleadsLanguage) data);
        } else if (data instanceof HealthleadsLocation) {
            locations.put(data.getId(), (HealthleadsLocation) data);
        } else if (data instanceof HealthleadsOrganization) {
            organizations.put(data.getId(), (HealthleadsOrganization) data);
        } else if (data instanceof HealthleadsPhone) {
            if (!phones.containsKey(((HealthleadsPhone) data).getLocationId())) {
                phones.put(((HealthleadsPhone) data).getLocationId(), new HashSet<>());
            }
            phones.get(((HealthleadsPhone) data).getLocationId()).add((HealthleadsPhone) data);
        } else if (data instanceof HealthleadsPhysicalAddress) {
            physicalAddresses.put(((HealthleadsPhysicalAddress) data).getLocationId(), (HealthleadsPhysicalAddress) data);
        } else if (data instanceof HealthleadsRequiredDocument) {
            requiredDocuments.put(((HealthleadsRequiredDocument) data).getServiceId(), (HealthleadsRequiredDocument) data);
        } else if (data instanceof HealthleadsService) {
            services.put(((HealthleadsService) data).getOrganizationId(), (HealthleadsService) data);
        } else if (data instanceof HealthleadsServiceAtLocation) {
            serviceAtLocations.put(((HealthleadsServiceAtLocation) data).getLocationId(), (HealthleadsServiceAtLocation) data);
        } else if (data instanceof HealthleadsServiceTaxonomy) {
            serviceTaxonomies.put(((HealthleadsServiceTaxonomy) data).getServiceId(), (HealthleadsServiceTaxonomy) data);
        } else if (data instanceof HealthleadsTaxonomy) {
            taxonomies.put(data.getId(), (HealthleadsTaxonomy) data);
        }
    }

    public DataImportReport persistData() {
        for (HealthleadsLocation location : locations.values()) {
            HealthleadsOrganization organization = organizations.get(location.getOrganizationId());
            HealthleadsService service = services.get(organization.getId());
            HealthleadsPhysicalAddress physicalAddress = physicalAddresses.get(location.getId());
            Set<HealthleadsPhone> phoneSet = phones.get(location.getId());
            HealthleadsEligibility eligibility = eligibilities.get(service.getId());
            HealthleadsLanguage language = this.languages.get(location.getId());
            HealthleadsServiceTaxonomy serviceTaxonomy = serviceTaxonomies.get(service.getId());
            HealthleadsTaxonomy taxonomy = taxonomies.get(serviceTaxonomy.getTaxonomyId());
            HealthleadsServiceAtLocation serviceAtLocation = serviceAtLocations.get(location.getId());
            HealthleadsRequiredDocument requiredDocument = requiredDocuments.get(service.getId());

            Location savedLocation = saveLocation(mapper.extractLocation(location), location.getId());
            Organization savedOrganization
                = saveOrganization(getOrganization(organization, savedLocation), organization.getId());
            Service savedService = saveService(getService(service, savedOrganization), service.getId());
            savePhysicalAddress(mapper.extractPhysicalAddress(physicalAddress), savedLocation);
            saveServiceAtLocation(mapper.extractServiceAtLocation(serviceAtLocation), serviceAtLocation.getId(), savedService, savedLocation);
            savePhones(mapper.extractPhones(phoneSet), savedService, savedLocation);
            saveEligibility(mapper.extractEligibility(eligibility), savedService);
            saveLanguages(mapper.extractLanguages(language), savedService, savedLocation);
            Taxonomy extractedTaxonomy = saveTaxonomy(mapper.extractTaxonomy(taxonomy), taxonomy.getId());
            saveServiceTaxonomy(mapper.extractServiceTaxonomy(serviceTaxonomy), serviceTaxonomy.getId(), savedService, extractedTaxonomy);
            saveRequiredDocument(mapper.extractRequiredDocument(requiredDocument), requiredDocument.getId(), savedService);
        }
        return report;
    }

    private Location saveLocation(Location location, String externalDbId) {
        return importService.createOrUpdateLocation(location, externalDbId, providerName);
    }

    private Organization saveOrganization(Organization organization, String externalDbId) {
        return importService.createOrUpdateOrganization(organization, externalDbId, providerName, report);
    }

    private Organization getOrganization(HealthleadsOrganization organization, Location savedLocation) {
        return mapper.extractOrganization(organization).location(savedLocation).active(true);
    }

    private Service saveService(Service service, String externalDbId) {
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
        return importService.createOrUpdateTaxonomy(taxonomy, extermalDbId, providerName);
    }

    private void saveServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String externalDbId, Service service, Taxonomy taxonomy) {
        importService.createOrUpdateServiceTaxonomy(serviceTaxonomy, externalDbId, providerName, service, taxonomy);
    }

    private void saveRequiredDocument(RequiredDocument requiredDocument, String externalDbId, Service service) {
        if (requiredDocument != null) {
            importService.createOrUpdateRequiredDocument(requiredDocument, externalDbId, providerName, service);
        }
    }
}
