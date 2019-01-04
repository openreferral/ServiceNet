package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.*;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class HealthleadsDataPersistence {

    private Map<String, Location> locations;
    private Map<String, Organization> organizations;
    private Map<String, Service> services;
    private Map<String, PhysicalAddress> physicalAddresses;
    private Map<String, Phone> phones;
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
            this.phones.put(((Phone) data).getLocationId(), (Phone) data);
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

    public void persistData(EntityManager em, HealthLeadsDataMapper mapper) {
        for (Location location : locations.values()) {
            Organization organization = organizations.get(location.getOrganizationId());
            Service service = services.get(organization.getId());
            PhysicalAddress physicalAddress = physicalAddresses.get(location.getId());
            Phone phone = phones.get(location.getId());
            Eligibility eligibility = eligibilities.get(service.getId());
            Language languages = this.languages.get(location.getId());
            ServiceTaxonomy serviceTaxonomy = serviceTaxonomies.get(service.getId());
            Taxonomy taxonomy = taxonomies.get(serviceTaxonomy.getTaxonomyId());
            ServiceAtLocation serviceAtLocation = serviceAtLocations.get(location.getId());
            RequiredDocument requiredDocument = requiredDocuments.get(service.getId());

            org.benetech.servicenet.domain.Location location1 = mapper.extractLocation(location);
            em.persist(location1);

            org.benetech.servicenet.domain.Organization organization1 =
                mapper.extractOrganization(organization)
                    .location(location1)
                    .active(true);
            em.persist(organization1);

            org.benetech.servicenet.domain.Service service1 = mapper.extractService(service)
                .organization(organization1);
            em.persist(service1);

            org.benetech.servicenet.domain.PhysicalAddress physicalAddress1 =
                mapper.extractPhysicalAddress(physicalAddress)
                    .location(location1);
            em.persist(physicalAddress1);

            org.benetech.servicenet.domain.ServiceAtLocation serviceAtLocation1 = mapper.extractServiceAtLocation(serviceAtLocation)
                .srvc(service1)
                .location(location1);
            em.persist(serviceAtLocation1);

            org.benetech.servicenet.domain.Phone phone1 = mapper.extractPhone(phone)
                .location(location1)
                .srvc(service1)
                .serviceAtLocation(serviceAtLocation1);
            em.persist(phone1);

            org.benetech.servicenet.domain.Eligibility eligibility1 = mapper.extractEligibility(eligibility)
                .srvc(service1);
            em.persist(eligibility1);

            mapper.extractLanguages(languages)
                .stream().map(loc -> loc.srvc(service1).location(location1))
                .forEach(em::persist);

            org.benetech.servicenet.domain.Taxonomy taxonomy1 = mapper.extractTaxonomy(taxonomy);
            em.persist(taxonomy1);

            org.benetech.servicenet.domain.ServiceTaxonomy serviceTaxonomy1 = mapper.extractServiceTaxonomy(serviceTaxonomy)
                .srvc(service1);
            em.persist(serviceTaxonomy1);

            org.benetech.servicenet.domain.RequiredDocument requiredDocument1 = mapper.extractRequiredDocument(requiredDocument)
                .srvc(service1);
            em.persist(requiredDocument1);
        }
    }
}
