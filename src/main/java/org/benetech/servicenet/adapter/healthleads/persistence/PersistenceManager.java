package org.benetech.servicenet.adapter.healthleads.persistence;

import org.benetech.servicenet.adapter.healthleads.HealthLeadsDataMapper;
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

import java.util.Set;

public class PersistenceManager {

    private ImportService importService;
    private HealthLeadsDataMapper mapper;
    private final String providerName;
    private DataImportReport report;
    
    PersistenceManager(ImportService importService, MultipleImportData importData,
                       HealthLeadsDataMapper mapper) {
        this.importService = importService;
        this.mapper = mapper;
        this.providerName = importData.getProviderName();
        this.report = importData.getReport();
    }

    DataImportReport getReport() {
        return report;
    }

    Organization importOrganization(HealthleadsOrganization organization) {
        return importService.createOrUpdateOrganization(getOrganization(organization),
            organization.getId(), providerName, report);
    }

    Location importLocation(HealthleadsLocation location, Organization savedOrg) {
        return importService.createOrUpdateLocation(getLocation(location, savedOrg), location.getId(), providerName);
    }

    void importPhysicalAddresses(Set<HealthleadsPhysicalAddress> physicalAddresses, Location savedLocation) {
        physicalAddresses.forEach(pa ->
            importService.createOrUpdatePhysicalAddress(getPhysicalAddress(pa), savedLocation));
    }

    void importLanguagesForService(Set<HealthleadsLanguage> healthleadsLanguages, Service savedService) {
        healthleadsLanguages.forEach(l ->
            importService.createOrUpdateLangsForService(getLanguages(l), savedService, null));
    }

    void importLanguagesForLocation(Set<HealthleadsLanguage> healthleadsLanguages, Location savedLocation) {
        healthleadsLanguages.forEach(l ->
            importService.createOrUpdateLangsForService(getLanguages(l), null, savedLocation));
    }

    void importPhonesForService(Set<HealthleadsPhone> phonesForService, Service savedService) {
        importService.createOrUpdatePhonesForService(getPhones(phonesForService), savedService, null);
    }

    void importPhonesForLocation(Set<HealthleadsPhone> phonesForService, Location savedLocation) {
        importService.createOrUpdatePhonesForService(getPhones(phonesForService), null, savedLocation);
    }

    Taxonomy importTaxonomy(HealthleadsTaxonomy taxonomy) {
        return importService.createOrUpdateTaxonomy(getTaxonomy(taxonomy), taxonomy.getId(), providerName);
    }

    void importServiceTaxonomy(HealthleadsServiceTaxonomy serviceTaxonomy, Service savedService,
                               Taxonomy savedTaxonomy) {
        importService.createOrUpdateServiceTaxonomy(getServiceTaxonomy(serviceTaxonomy),
            serviceTaxonomy.getId(), providerName, savedService, savedTaxonomy);
    }

    Service importService(HealthleadsService service, Organization savedOrg) {
        return importService.createOrUpdateService(getService(service, savedOrg),
            service.getId(), providerName, report);
    }

    void importEligibilities(Set<HealthleadsEligibility> eligibilities, Service service) {
        eligibilities.forEach(e -> importService.createOrUpdateEligibility(getEligibility(e), service));
    }

    void importRequiredDocuments(Set<HealthleadsRequiredDocument> requiredDocuments, Service service) {
        requiredDocuments.forEach(rd ->
            importService.createOrUpdateRequiredDocument(getRequiredDocument(rd), rd.getId(), providerName, service));
    }

    private Organization getOrganization(HealthleadsOrganization organization) {
        return mapper.extractOrganization(organization)
            .active(true)
            .externalDbId(organization.getId())
            .providerName(providerName);
    }

    private Taxonomy getTaxonomy(HealthleadsTaxonomy taxonomy) {
        return mapper.extractTaxonomy(taxonomy)
            .externalDbId(taxonomy.getId())
            .providerName(providerName);
    }

    private ServiceTaxonomy getServiceTaxonomy(HealthleadsServiceTaxonomy serviceTaxonomy) {
        return mapper.extractServiceTaxonomy(serviceTaxonomy)
            .externalDbId(serviceTaxonomy.getId())
            .providerName(providerName);
    }

    private Service getService(HealthleadsService service, Organization savedOrg) {
        return mapper.extractService(service)
            .organization(savedOrg)
            .externalDbId(service.getId())
            .providerName(providerName);
    }

    private Location getLocation(HealthleadsLocation location, Organization savedOrg) {
        return mapper.extractLocation(location)
            .organization(savedOrg)
            .externalDbId(location.getId())
            .providerName(providerName);
    }

    private PhysicalAddress getPhysicalAddress(HealthleadsPhysicalAddress pa) {
        return mapper.extractPhysicalAddress(pa);
    }

    private Set<Language> getLanguages(HealthleadsLanguage language) {
        return mapper.extractLanguages(language);
    }

    private Set<Phone> getPhones(Set<HealthleadsPhone> phonesForService) {
        return mapper.extractPhones(phonesForService);
    }

    private Eligibility getEligibility(HealthleadsEligibility eligibility) {
        return mapper.extractEligibility(eligibility);
    }

    private RequiredDocument getRequiredDocument(HealthleadsRequiredDocument requiredDocument) {
        return mapper.extractRequiredDocument(requiredDocument)
            .externalDbId(requiredDocument.getId())
            .providerName(providerName);
    }
}
