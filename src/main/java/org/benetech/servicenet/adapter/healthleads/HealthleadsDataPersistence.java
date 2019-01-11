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
import org.benetech.servicenet.adapter.healthleads.model.LocationRelatedData;
import org.benetech.servicenet.adapter.healthleads.model.ServiceRelatedData;
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

    private Map<Class<? extends BaseData>, Map<String, BaseData>> entitiesMap = new HashMap<>();
    private Map<Class<? extends BaseData>, Map<String, Set<BaseData>>> enitiiesSetsMap = new HashMap<>();

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

    public void addData(BaseData data) {
        Class<? extends BaseData> clazz = data.getClass();

        if (data instanceof HealthleadsPhone) {
            handlePhones(data, clazz);
        } else if (data instanceof HealthleadsService) {
            handleServices(data, clazz);
        } else {
            if (!entitiesMap.containsKey(clazz)) {
                entitiesMap.put(clazz, new HashMap<>());
            }

            if (data instanceof LocationRelatedData) {
                entitiesMap.get(clazz).put(((LocationRelatedData) data).getLocationId(), data);
            } else if (data instanceof ServiceRelatedData) {
                entitiesMap.get(clazz).put(((ServiceRelatedData) data).getServiceId(), data);
            } else {
                entitiesMap.get(clazz).put(data.getId(), data);
            }
        }
    }

    public DataImportReport persistData() {
        for (BaseData baseData : entitiesMap.get(HealthleadsLocation.class).values()) {
            HealthleadsLocation location = (HealthleadsLocation) baseData;
            HealthleadsOrganization organization = (HealthleadsOrganization) entitiesMap
                .get(HealthleadsOrganization.class).get(location.getOrganizationId());
            Set<BaseData> services = enitiiesSetsMap.get(HealthleadsService.class)
                .get(organization.getId());
            HealthleadsPhysicalAddress physicalAddress = (HealthleadsPhysicalAddress) entitiesMap
                .get(HealthleadsPhysicalAddress.class).get(location.getId());
            Set<BaseData> phoneSet = enitiiesSetsMap.get(HealthleadsPhone.class).get(location.getId());
            HealthleadsLanguage language = (HealthleadsLanguage) entitiesMap
                .get(HealthleadsLanguage.class).get(location.getId());
            HealthleadsServiceAtLocation serviceAtLocation = (HealthleadsServiceAtLocation) entitiesMap
                .get(HealthleadsServiceAtLocation.class).get(location.getId());
            Location savedLocation = saveLocation(mapper.extractLocation(location), location.getId());
            Organization savedOrganization
                = saveOrganization(getOrganization(organization, savedLocation), organization.getId());

            savePhysicalAddress(mapper.extractPhysicalAddress(physicalAddress), savedLocation);

            for (BaseData baseService : services) {
                HealthleadsService service = (HealthleadsService) baseService;

                HealthleadsEligibility eligibility = (HealthleadsEligibility) entitiesMap
                    .get(HealthleadsEligibility.class).get(service.getId());

                HealthleadsServiceTaxonomy serviceTaxonomy = (HealthleadsServiceTaxonomy) entitiesMap
                    .get(HealthleadsServiceTaxonomy.class).get(service.getId());

                HealthleadsTaxonomy taxonomy = (HealthleadsTaxonomy) entitiesMap
                    .get(HealthleadsTaxonomy.class).get(serviceTaxonomy.getTaxonomyId());

                HealthleadsRequiredDocument requiredDocument = (HealthleadsRequiredDocument) entitiesMap
                    .get(HealthleadsRequiredDocument.class).get(service.getId());

                Service savedService = saveService(getService(service, savedOrganization), service.getId());

                saveServiceAtLocation(mapper.extractServiceAtLocation(serviceAtLocation),
                    serviceAtLocation.getId(), savedService, savedLocation);
                savePhones(mapper.extractPhones(phoneSet), savedService, savedLocation);
                saveEligibility(mapper.extractEligibility(eligibility), savedService);
                saveLanguages(mapper.extractLanguages(language), savedService, savedLocation);
                Taxonomy extractedTaxonomy = saveTaxonomy(mapper.extractTaxonomy(taxonomy), taxonomy.getId());
                saveServiceTaxonomy(mapper.extractServiceTaxonomy(serviceTaxonomy),
                    serviceTaxonomy.getId(), savedService, extractedTaxonomy);
                saveRequiredDocument(mapper.extractRequiredDocument(requiredDocument),
                    requiredDocument.getId(), savedService);
            }
        }
        return report;
    }

    private void handlePhones(BaseData data, Class<? extends BaseData> clazz) {
        if (!enitiiesSetsMap.containsKey(clazz)) {
            enitiiesSetsMap.put(clazz, new HashMap<>());
        }
        Map<String, Set<BaseData>> map = enitiiesSetsMap.get(clazz);
        if (!map.containsKey(((HealthleadsPhone) data).getLocationId())) {
            map.put(((HealthleadsPhone) data).getLocationId(), new HashSet<>());
        }
        map.get(((HealthleadsPhone) data).getLocationId()).add(data);
        enitiiesSetsMap.put(clazz, map);
    }
    
    private void handleServices(BaseData data, Class<? extends BaseData> clazz) {
        if (!enitiiesSetsMap.containsKey(clazz)) {
            enitiiesSetsMap.put(clazz, new HashMap<>());
        }
        Map<String, Set<BaseData>> map = enitiiesSetsMap.get(clazz);
        if (!map.containsKey(((HealthleadsService) data).getOrganizationId())) {
            map.put(((HealthleadsService) data).getOrganizationId(), new HashSet<>());
        }
        map.get(((HealthleadsService) data).getOrganizationId()).add(data);
        enitiiesSetsMap.put(clazz, map);
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

    private void saveServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String externalDbId,
                                     Service service, Taxonomy taxonomy) {
        importService.createOrUpdateServiceTaxonomy(serviceTaxonomy, externalDbId, providerName, service, taxonomy);
    }

    private void saveRequiredDocument(RequiredDocument requiredDocument, String externalDbId, Service service) {
        if (requiredDocument != null) {
            importService.createOrUpdateRequiredDocument(requiredDocument, externalDbId, providerName, service);
        }
    }
}
