package org.benetech.servicenet.adapter.icarol;

import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ImportService;

import java.util.Optional;

class PersistenceManager {

    private final ICarolDataMapper mapper;
    private final ImportService importService;
    
    PersistenceManager(ImportService importService) {
        this.importService = importService;
        this.mapper = ICarolDataMapper.INSTANCE;
    }

    void importEligibility(ICarolProgram program, Service service) {
        mapper.extractEligibility(program).ifPresent(
            x -> importService.createOrUpdateEligibility(x, service));
    }

    void importOpeningHours(ICarolProgram program, Service service) {
        importService.createOrUpdateOpeningHoursForService(
            mapper.extractOpeningHours(program.getHours()), service);
    }

    void importLangs(Location location, ICarolProgram program, Service service) {
        importService.createOrUpdateLangsForService(
            mapper.extractLangs(program), service, location);
    }

    void importPhones(Location location, ICarolProgram program, Service service) {
        importService.createOrUpdatePhonesForService(
            mapper.extractPhones(program.getContactDetails()), service, location);
    }
    
    Optional<Location> importLocation(ImportData importData, ICarolSite site, Organization savedOrg) {
        return mapper.extractLocation(
            site.getContactDetails(), site.getId(), importData.getProviderName())
            .flatMap(extractedLocation -> {
                extractedLocation.setOrganization(savedOrg);
                return Optional.ofNullable(importService.createOrUpdateLocation(
                    extractedLocation, site.getId(), importData.getProviderName()));
            });
    }

    void importPhysicalAddress(ICarolSite site, Location location) {
        mapper.extractPhysicalAddress(site.getContactDetails()).ifPresent(
            x -> importService.createOrUpdatePhysicalAddress(x, location));
    }

    void importPostalAddress(ICarolSite site, Location location) {
        mapper.extractPostalAddress(site.getContactDetails()).ifPresent(
            x -> importService.createOrUpdatePostalAddress(x, location));
    }

    void importAccessibility(ICarolSite site, Location location) {
        mapper.extractAccessibilityForDisabilities(site).ifPresent(
            x -> importService.createOrUpdateAccessibility(x, location));
    }

    Optional<Organization> importOrganization(ImportData importData, ICarolAgency agency) {
        Organization extractedOrganization = mapper
            .extractOrganization(agency, importData.getProviderName())
            .sourceDocument(importData.getReport().getDocumentUpload());

        return Optional.ofNullable(importService
            .createOrUpdateOrganization(extractedOrganization, agency.getId(), importData.getProviderName(),
                importData.getReport()));
    }

    Optional<Service> importService(Organization organization, ImportData importData, ICarolProgram program) {
        Service extractedService = mapper
            .extractService(program, importData.getProviderName())
            .organization(organization);

        return Optional.ofNullable(importService
            .createOrUpdateService(extractedService, program.getId(), importData.getProviderName(),
                importData.getReport()));
    }
}
