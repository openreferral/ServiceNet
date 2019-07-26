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
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceAtLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceTaxonomy;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsTaxonomy;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.adapter.shared.model.storage.EntryDictionary;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.manager.ImportManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PersistanceManager {

    private final Logger log = LoggerFactory.getLogger(PersistanceManager.class);

    private EntryDictionary<HealthleadsBaseData> dictionary = new EntryDictionary<>();
    private HealthLeadsDataMapper mapper;
    private ImportManager importManager;

    private static final String PROVIDER_NAME = "healthleads";

    public PersistanceManager(ImportManager importManager) {
        this.mapper = HealthLeadsDataMapper.INSTANCE;
        this.importManager = importManager;
    }

    public void addData(HealthleadsBaseData data) {
        if (isServiceBased(data)) {
            dictionary.addData(data.getServiceId(), data, HealthleadsService.class);
        } else if (isLocationBased(data)) {
            dictionary.addData(data.getLocationId(), data, HealthleadsLocation.class);
        } else if (isOrganizationBased(data)) {
            dictionary.addData(data.getOrganizationId(), data, HealthleadsOrganization.class);
        } else {
            dictionary.addData(data.getId(), data, HealthleadsBaseData.class);
        }
    }

    public DataImportReport persistData(ImportData importData) {
        for (HealthleadsOrganization healthleadsOrganization :
            dictionary.getEntitiesOfClass(HealthleadsOrganization.class)) {
            try {
                Organization organization = getOrganizationToPersist(healthleadsOrganization);
                importManager.createOrUpdateOrganization(
                    organization, organization.getExternalDbId(), importData);
            } catch (Exception e) {
                log.warn("Skipping organization with name: " + healthleadsOrganization.getName(), e);
            }
        }
        return importData.getReport();
    }

    private Organization getOrganizationToPersist(HealthleadsOrganization healthleadsOrganization) {
        String organizationId = healthleadsOrganization.getId();

        Organization organization = mapper.extractOrganization(healthleadsOrganization);
        Set<Location> locations = getLocationsToPersist(organizationId);

        organization.setActive(true);
        organization.setLocations(locations);
        organization.setServices(getServicesToPersist(organizationId, locations));

        return organization;
    }

    private Set<Service> getServicesToPersist(String organizationId, Set<Location> locations) {

        Set<Service> result = new HashSet<>();
        for (HealthleadsService healthleadsService : dictionary.getRelatedEntities(
            HealthleadsService.class, organizationId, HealthleadsOrganization.class)) {

            String externalServiceId = healthleadsService.getId();
            Service service = mapper.extractService(healthleadsService);

            service.setProviderName(PROVIDER_NAME);
            service.setTaxonomies(getTaxonomiesToPersist(externalServiceId));
            service.setPhones(getPhonesForServiceToPersist(externalServiceId));
            service.setLangs(getLanguagesForServiceToPersist(externalServiceId));
            getEligibilityToPersist(externalServiceId).ifPresent(service::setEligibility);
            service.setDocs(getRequiredDocumentsToPersist(externalServiceId));
            service.setLocations(getServiceAtLocationsToPersist(externalServiceId, locations));

            result.add(service);
        }
        return result;
    }

    //TODO: What if there are multiple eligibilities
    private Optional<Eligibility> getEligibilityToPersist(String externalServiceId) {
        Set<HealthleadsEligibility> eligibilities = dictionary.getRelatedEntities(
            HealthleadsEligibility.class, externalServiceId, HealthleadsService.class);
        if (eligibilities.isEmpty()) {
            return Optional.empty();
        }

        return mapper.extractEligibility(eligibilities.iterator().next());
    }

    private Set<RequiredDocument> getRequiredDocumentsToPersist(String externalServiceId) {
        return dictionary.getRelatedEntities(HealthleadsRequiredDocument.class, externalServiceId, HealthleadsService.class)
            .stream().map(x -> mapper.extractRequiredDocument(x))
            .filter(Optional::isPresent)
            .map(requiredDocument -> requiredDocument.get().providerName(PROVIDER_NAME))
            .collect(Collectors.toSet());
    }

    private Set<Language> getLanguagesForServiceToPersist(String externalServiceId) {
        return mapper.extractLanguages(dictionary.getRelatedEntities(
            HealthleadsLanguage.class, externalServiceId, HealthleadsService.class));
    }

    private Set<Location> getLocationsToPersist(String organizationId) {
        Set<HealthleadsLocation> locations = dictionary.getRelatedEntities(
            HealthleadsLocation.class, organizationId, HealthleadsOrganization.class);
        Set<Location> result = new HashSet<>();
        for (HealthleadsLocation healthleadsLocation : locations) {
            String externalLocationId = healthleadsLocation.getId();
            String schedule = healthleadsLocation.getSchedule();

            Location location = mapper.extractLocation(healthleadsLocation);

            location.setProviderName(PROVIDER_NAME);
            getPhysicalAddressesToPersist(externalLocationId).ifPresent(location::setPhysicalAddress);
            location.setPhones(getPhonesForLocationToPersist(externalLocationId));
            location.setLangs(getLanguagesForLocationToPersist(externalLocationId));
            if (StringUtils.isNotBlank(schedule)) {
                location.setRegularSchedule(new RegularSchedule().notes(schedule));
            }
            result.add(location);
        }
        return result;
    }

    private Set<Language> getLanguagesForLocationToPersist(String externalLocationId) {
        return mapper.extractLanguages(dictionary.getRelatedEntities(
            HealthleadsLanguage.class, externalLocationId, HealthleadsLocation.class));
    }

    private Set<Phone> getPhonesForServiceToPersist(String externalServiceId) {
        return mapper.extractPhones(dictionary.getRelatedEntities(
            HealthleadsPhone.class, externalServiceId, HealthleadsService.class));
    }

    private Set<Phone> getPhonesForLocationToPersist(String externalLocationId) {
        return mapper.extractPhones(dictionary.getRelatedEntities(
            HealthleadsPhone.class, externalLocationId, HealthleadsLocation.class));
    }

    //TODO: What if there are multiple addresses
    private Optional<PhysicalAddress> getPhysicalAddressesToPersist(String locationId) {
        Set<HealthleadsPhysicalAddress> addresses = dictionary.getRelatedEntities(
            HealthleadsPhysicalAddress.class, locationId, HealthleadsLocation.class);
        if (addresses.isEmpty()) {
            return Optional.empty();
        }
        return mapper.extractPhysicalAddress(addresses.iterator().next());
    }

    private Set<ServiceTaxonomy> getTaxonomiesToPersist(String serviceId) {
        Set<HealthleadsServiceTaxonomy> serviceTaxonomies = dictionary.getRelatedEntities(
            HealthleadsServiceTaxonomy.class, serviceId, HealthleadsService.class);

        Set<ServiceTaxonomy> result = new HashSet<>();
        for (HealthleadsServiceTaxonomy healthleadsServiceTaxonomy : serviceTaxonomies) {
            ServiceTaxonomy serviceTaxonomy = mapper.extractServiceTaxonomy(healthleadsServiceTaxonomy);
            Set<HealthleadsTaxonomy> taxonomies = dictionary.getRelatedEntities(
                HealthleadsTaxonomy.class, healthleadsServiceTaxonomy.getTaxonomyId(), HealthleadsBaseData.class);

            if (!taxonomies.isEmpty()) {
                mapper.extractTaxonomy(taxonomies.iterator().next()).ifPresent(taxonomy ->
                    serviceTaxonomy.setTaxonomy(taxonomy.providerName(PROVIDER_NAME)));
            }

            result.add(serviceTaxonomy.providerName(PROVIDER_NAME));
        }

        return result;
    }

    private Set<ServiceAtLocation> getServiceAtLocationsToPersist(String serviceId, Set<Location> locations) {
        Set<HealthleadsServiceAtLocation> serviceAtLocationSet = dictionary.getRelatedEntities(
            HealthleadsServiceAtLocation.class, serviceId, HealthleadsService.class);

        Set<ServiceAtLocation> serviceAtLocations = new HashSet<>();

        serviceAtLocationSet.forEach(healthleadsServiceAtLocation -> {
            Set<Location> serviceLocations = locations.stream().
                filter(l -> l.getExternalDbId().equals(healthleadsServiceAtLocation.getLocationId()))
                .collect(Collectors.toSet());

            ServiceAtLocation serviceAtLocation = mapper.extractServiceAtLocation(healthleadsServiceAtLocation);

            if (!serviceLocations.isEmpty()) {
                serviceAtLocation.setLocation(serviceLocations.iterator().next());
            }

            serviceAtLocations.add(serviceAtLocation.providerName(PROVIDER_NAME));
        });

        return serviceAtLocations;
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
