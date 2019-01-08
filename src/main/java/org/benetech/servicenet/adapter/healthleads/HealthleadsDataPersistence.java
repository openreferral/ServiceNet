package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.*;
import org.benetech.servicenet.service.ImportService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HealthleadsDataPersistence {

    private Map<String, Location> locations;
    private Map<String, Organization> organizations;
    private Map<String, Service> services;
    private Map<String, PhysicalAddress> physicalAddresses;
    private Map<String, Set<Phone>> phones;
    private Map<String, Eligibility> eligibilities;
    private Map<String, Language> languages;
    private Map<String, RequiredDocument> requiredDocuments;
    private Map<String, ServiceAtLocation> serviceAtLocations;
    private Map<String, ServiceTaxonomy> serviceTaxonomies;
    private Map<String, Taxonomy> taxonomies;

    public HealthleadsDataPersistence() {
        this.locations = new HashMap<>();
        this.organizations = new HashMap<>();
        this.serviceAtLocations = new HashMap<>();
        this.services = new HashMap<>();
        this.phones = new HashMap<>();
        this.physicalAddresses = new HashMap<>();
        this.eligibilities = new HashMap<>();
        this.languages = new HashMap<>();
        this.requiredDocuments = new HashMap<>();
        this.serviceTaxonomies = new HashMap<>();
        this.taxonomies = new HashMap<>();
    }

    public void addData(BaseData data) {
        if (data instanceof Eligibility) {
            this.eligibilities.put(((Eligibility) data).getServiceId(), (Eligibility) data);
        } else if (data instanceof Language) {
            languages.put(((Language) data).getLocationId(), (Language) data);
        } else if (data instanceof Location) {
            this.locations.put(data.getId(), (Location) data);
        } else if (data instanceof Organization) {
            this.organizations.put(data.getId(), (Organization) data);
        } else if (data instanceof Phone) {
            if (!this.phones.containsKey(((Phone) data).getLocationId())) {
                this.phones.put(((Phone) data).getLocationId(), new HashSet<>());
            }
            this.phones.get(((Phone) data).getLocationId()).add((Phone) data);
        } else if (data instanceof PhysicalAddress) {
            this.physicalAddresses.put(((PhysicalAddress) data).getLocationId(), (PhysicalAddress) data);
        } else if (data instanceof RequiredDocument) {
            this.requiredDocuments.put(((RequiredDocument) data).getServiceId(), (RequiredDocument) data);
        } else if (data instanceof Service) {
            this.services.put(((Service) data).getOrganizationId(), (Service) data);
        } else if (data instanceof ServiceAtLocation) {
            this.serviceAtLocations.put(((ServiceAtLocation) data).getLocationId(), (ServiceAtLocation) data);
        } else if (data instanceof ServiceTaxonomy) {
            this.serviceTaxonomies.put(((ServiceTaxonomy) data).getServiceId(), (ServiceTaxonomy) data);
        } else if (data instanceof Taxonomy) {
            this.taxonomies.put(data.getId(), (Taxonomy) data);
        }
    }

    public void persistData(ImportService importService, HealthLeadsDataMapper mapper, String providerName) {
        for (Location location : locations.values()) {
            Organization organization = organizations.get(location.getOrganizationId());
            Service service = services.get(organization.getId());
            PhysicalAddress physicalAddress = physicalAddresses.get(location.getId());
            Set<Phone> phones = this.phones.get(location.getId());
            Eligibility eligibility = eligibilities.get(service.getId());
            Language languages = this.languages.get(location.getId());
            ServiceTaxonomy serviceTaxonomy = serviceTaxonomies.get(service.getId());
            Taxonomy taxonomy = taxonomies.get(serviceTaxonomy.getTaxonomyId());
            ServiceAtLocation serviceAtLocation = serviceAtLocations.get(location.getId());
            RequiredDocument requiredDocument = requiredDocuments.get(service.getId());

            org.benetech.servicenet.domain.Location extractedLocaton = mapper.extractLocation(location);
            org.benetech.servicenet.domain.Location savedLocation
                = importService.createOrUpdateLocation(extractedLocaton, location.getId(), providerName);

            org.benetech.servicenet.domain.Organization extractedOrganization
                = mapper.extractOrganization(organization)
                    .location(savedLocation)
                    .active(true);
            extractedOrganization = importService
                .createOrUpdateOrganization(extractedOrganization, organization.getId(), providerName);

            org.benetech.servicenet.domain.Service extractedService = mapper.extractService(service)
                .organization(extractedOrganization);
            org.benetech.servicenet.domain.Service savedService
                = importService.createOrUpdateService(extractedService, service.getId(), providerName);

            org.benetech.servicenet.domain.PhysicalAddress extractedPhysicalAdress
                = mapper.extractPhysicalAddress(physicalAddress);
            importService.createOrUpdatePhysicalAddress(extractedPhysicalAdress, savedLocation);

            org.benetech.servicenet.domain.ServiceAtLocation extractedServiceAtLocation
                = mapper.extractServiceAtLocation(serviceAtLocation);
            importService.createOrUpdateServiceAtLocation(extractedServiceAtLocation, serviceAtLocation.getId(),
                providerName, savedService, savedLocation);

            Set<org.benetech.servicenet.domain.Phone> extractedPhones = mapper.extractPhones(phones);
            importService.createOrUpdatePhones(extractedPhones, savedService, savedLocation);

            org.benetech.servicenet.domain.Eligibility extractedEligibility
                = mapper.extractEligibility(eligibility);
            importService.createOrUpdateEligibility(extractedEligibility, savedService);

            importService.createOrUpdateLangs(mapper.extractLanguages(languages), savedService, savedLocation);

            org.benetech.servicenet.domain.Taxonomy extractedTaxonomy = mapper.extractTaxonomy(taxonomy);
            extractedTaxonomy = importService.createOrUpdateTaxonomy(extractedTaxonomy, taxonomy.getId(),
                providerName);

            org.benetech.servicenet.domain.ServiceTaxonomy extractedServiceTaxonomy
                = mapper.extractServiceTaxonomy(serviceTaxonomy);
            importService.createOrUpdateServiceTaxonomy(extractedServiceTaxonomy, serviceTaxonomy.getId(),
                providerName, savedService, extractedTaxonomy);

            org.benetech.servicenet.domain.RequiredDocument extractedRequiredDocument
                = mapper.extractRequiredDocument(requiredDocument);
            if (extractedRequiredDocument != null) {
                importService.createOrUpdateRequiredDocument(extractedRequiredDocument, requiredDocument.getId(),
                    providerName, savedService);
            }
        }
    }
}
