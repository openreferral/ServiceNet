package org.benetech.servicenet.adapter.icarol;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolDataToPersist;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.manager.ImportManager;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.benetech.servicenet.adapter.icarol.ICarolDataCollector.findRelatedEntities;

@Slf4j
class RelationManager {

    private static final String SITE = "Site";
    private static final String PROGRAM = "Program";
    private final ICarolDataMapper mapper;
    private final ImportManager importManager;

    RelationManager(ImportManager importManager) {
        this.importManager = importManager;
        this.mapper = ICarolDataMapper.INSTANCE;
    }

    DataImportReport persist(ICarolDataToPersist data, ImportData importData) {
        saveOrganizationsAndRelatedData(data, importData);
        return importData.getReport();
    }

    private Set<Location> getLocationsToPersist(List<ICarolSite> relatedSites, ImportData importData) {
        Set<Location> result = new HashSet<>();
        for (ICarolSite site : relatedSites) {
            Optional<Location> location = getLocationToPersist(importData, site);
            location.ifPresent(result::add);
        }
        return result;
    }

    private void saveOrganizationsAndRelatedData(ICarolDataToPersist dataToPersist, ImportData importData) {
        int i = 0;
        for (ICarolAgency agency : dataToPersist.getAgencies()) {
            logProgress(i++, dataToPersist.getAgencies().size());

            Set<Location> locations = getLocationsToPersist(
                findRelatedEntities(dataToPersist.getSites(), agency, SITE), importData);

            Set<Service> services = getServicesToPersist(
                findRelatedEntities(dataToPersist.getPrograms(), agency, PROGRAM), importData);

            importOrganization(importData, agency, locations, services);

        }
    }

    private static void logProgress(int itemNr, int size) {
        int maxPercentage = 100;
        log.info("Saving data for ICarol: " + maxPercentage * itemNr / size + "% completed");
    }

    private Set<Service> getServicesToPersist(List<ICarolProgram> relatedPrograms, ImportData importData) {

        Set<Service> result = new HashSet<>();
        for (ICarolProgram program : relatedPrograms) {
            Optional<Service> service = getServiceToPersist(importData, program);
            service.ifPresent(result::add);
        }
        return result;
    }

    private Optional<Organization> importOrganization(ImportData importData, ICarolAgency agency,
                                              Set<Location> locations, Set<Service> services) {
        Organization extractedOrganization = mapper
            .extractOrganization(agency, importData.getProviderName())
            .sourceDocument(importData.getReport().getDocumentUpload());

        extractedOrganization.setLocations(locations);
        extractedOrganization.setServices(services);

        return Optional.ofNullable(importManager
            .createOrUpdateOrganization(extractedOrganization, agency.getId(), importData));
    }

    private Optional<Location> getLocationToPersist(ImportData importData, ICarolSite site) {
        return mapper.extractLocation(
            site.getContactDetails(), site.getId(), importData.getProviderName())
            .flatMap(extractedLocation -> {
                extractedLocation.setPhysicalAddress(
                    mapper.extractPhysicalAddress(site.getContactDetails()).orElse(null));
                extractedLocation.setPostalAddress(
                    mapper.extractPostalAddress(site.getContactDetails()).orElse(null));
                mapper.extractAccessibilityForDisabilities(site)
                    .ifPresent(a -> extractedLocation.setAccessibilities(Set.of(a)));

                return Optional.of(extractedLocation);
            });
    }

    private Optional<Service> getServiceToPersist(ImportData importData, ICarolProgram program) {
        Service extractedService = mapper
            .extractService(program, importData.getProviderName());

        extractedService.setEligibility(mapper.extractEligibility(program).orElse(null));
        extractedService.setPhones(mapper.extractPhones(program.getContactDetails()));
        extractedService.setLangs(mapper.extractLangs(program));
        extractedService.setRegularSchedule(new RegularSchedule().openingHours(
            mapper.extractOpeningHours(program.getHours())));

        return Optional.of(extractedService);
    }
}
