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

import static org.benetech.servicenet.adapter.icarol.ICarolDataCollector.findRelatedEntities;

class RelationManager {

    private static final String AGENCY = "Agency";
    private static final String PROGRAM = "Program";
    private final PersistenceManager persistence;

    RelationManager(ImportService importService) {
        this.persistence = new PersistenceManager(importService);
    }

    DataImportReport persist(ICarolDataToPersist data, ImportData importData) {
        saveLocationsAndRelatedData(data, importData);
        saveEntitiesWithoutLocation(data, importData);
        return importData.getReport();
    }

    private void saveLocationsAndRelatedData(ICarolDataToPersist dataToPersist, ImportData importData) {
        for (ICarolSite site : dataToPersist.getSites()) {
            persistence.importLocation(importData, site).ifPresent(location ->
                saveLocationRelatedData(dataToPersist, importData, site, location));
        }
    }

    private void saveLocationRelatedData(ICarolDataToPersist dataToPersist, ImportData importData
        , ICarolSite site, Location savedLocation) {
        persistence.importPhysicalAddress(site, savedLocation);
        persistence.importPostalAddress(site, savedLocation);
        persistence.importAccessibility(site, savedLocation);
        saveOrganizationsAndRelatedData(findRelatedEntities(dataToPersist.getAgencies(), site, AGENCY),
            dataToPersist, importData, savedLocation);
    }

    private void saveOrganizationsAndRelatedData(List<ICarolAgency> relatedAgencies, ICarolDataToPersist dataToPersist,
                                                 ImportData importData, Location location) {
        for (ICarolAgency agency : relatedAgencies) {
            persistence.importOrganization(importData, agency).ifPresent(org ->
                saveOrganizationRelatedData(findRelatedEntities(dataToPersist.getPrograms(), agency, PROGRAM),
                location, importData, org));
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

    private void saveEntitiesWithoutLocation(ICarolDataToPersist data, ImportData importData) {
        saveOrganizationsAndRelatedData(data.getAgencies(), data, importData, null);
        saveServicesAndRelatedData(null, null, data.getPrograms(), importData);
    }
}
