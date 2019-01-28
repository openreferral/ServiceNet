package org.benetech.servicenet.adapter.icarol;

import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolDataToPersist;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ImportService;

import java.util.List;
import java.util.Optional;

import static org.benetech.servicenet.adapter.icarol.ICarolDataCollector.findRelatedEntities;

class RelationManager {

    private static final String SITE = "Site";
    private static final String PROGRAM = "Program";
    private final PersistenceManager persistence;

    RelationManager(ImportService importService) {
        this.persistence = new PersistenceManager(importService);
    }

    DataImportReport persist(ICarolDataToPersist data, ImportData importData) {
        saveOrganizationsAndRelatedData(data, importData);
        saveEntitiesWithoutOrganization(data, importData);
        return importData.getReport();
    }

    private void saveLocationsAndRelatedData(List<ICarolSite> relatedSites, ICarolDataToPersist dataToPersist,
                                             ImportData importData, Organization savedOrg, ICarolAgency relatedAgency) {
        for (ICarolSite site : relatedSites) {
            Optional<Location> optLocation = persistence.importLocation(importData, site, savedOrg);
            if (optLocation.isPresent()) {
                saveLocationRelatedData(site, optLocation.get());

                if (savedOrg != null) {
                    saveOrganizationRelatedData(
                        findRelatedEntities(dataToPersist.getPrograms(), relatedAgency, PROGRAM),
                        optLocation.get(), importData, savedOrg);
                }
            }
        }
    }

    private void saveLocationRelatedData(ICarolSite site, Location savedLocation) {
        persistence.importPhysicalAddress(site, savedLocation);
        persistence.importPostalAddress(site, savedLocation);
        persistence.importAccessibility(site, savedLocation);
    }

    private void saveOrganizationsAndRelatedData(ICarolDataToPersist dataToPersist, ImportData importData) {
        for (ICarolAgency agency : dataToPersist.getAgencies()) {
            persistence.importOrganization(importData, agency).ifPresent(org -> {
                saveLocationsAndRelatedData(
                    findRelatedEntities(dataToPersist.getSites(), agency, SITE),
                    dataToPersist, importData, org, agency);
            });
        }
    }

    private void saveOrganizationRelatedData(List<ICarolProgram> relatedPrograms, Location location, ImportData importData,
                                             Organization savedOrganization) {
        saveServicesAndRelatedData(location, savedOrganization, relatedPrograms, importData);
    }

    private void saveServicesAndRelatedData(Location location, Organization organization,
                                            List<ICarolProgram> relatedPrograms, ImportData importData) {
        for (ICarolProgram program : relatedPrograms) {
            persistence.importService(organization, importData, program).ifPresent(service ->
                saveServiceRelatedData(location, program, service));
        }
    }

    private void saveServiceRelatedData(Location location, ICarolProgram program,
                                        Service savedService) {
        persistence.importEligibility(program, savedService);

        persistence.importPhones(location, program, savedService);
        persistence.importLangs(location, program, savedService);
        persistence.importOpeningHours(program, savedService);
    }

    private void saveEntitiesWithoutOrganization(ICarolDataToPersist data, ImportData importData) {
        saveLocationsAndRelatedData(data.getSites(), data, importData, null, null);
        saveServicesAndRelatedData(null, null, data.getPrograms(), importData);
    }
}
