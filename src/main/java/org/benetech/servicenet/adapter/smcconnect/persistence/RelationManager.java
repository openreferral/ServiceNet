package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.smcconnect.model.SmcLocation;
import org.benetech.servicenet.adapter.smcconnect.model.SmcOrganization;
import org.benetech.servicenet.adapter.smcconnect.model.SmcService;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ImportService;

class RelationManager {

    private final SmcStorage storage;
    private final PersistenceManager persistence;

    RelationManager(ImportService importService, MultipleImportData data, SmcStorage storage) {
        this.storage = storage;
        this.persistence = new PersistenceManager(importService, data, storage);
    }

    void saveOrganizationsAndRelatedData(DocumentUpload sourceDocument) {
        for (SmcOrganization smcOrganization : storage.getEntitiesOfClass(SmcOrganization.class)) {
            Organization savedOrganization = persistence.importOrganization(smcOrganization, sourceDocument);
            saveOrganizationRelatedData(smcOrganization, savedOrganization);
        }
    }

    private void saveLocationsAndLocationRelatedData(String relatedTo, Organization savedOrganization) {
        for (SmcLocation smcLocation : storage.getRelatedEntities(SmcLocation.class, relatedTo, SmcOrganization.class)) {
            Location savedLocation = persistence.importLocation(smcLocation, savedOrganization);
            saveLocationRelatedData(smcLocation, savedLocation, savedOrganization);
        }
    }

    private void saveServicesAndServiceRelatedData(String relatedTo, Location location, Organization savedOrganization) {
        for (SmcService smcService : storage.getRelatedEntities(SmcService.class, relatedTo, SmcLocation.class)) {
            Service savedService = persistence.importService(smcService, savedOrganization);
            saveServiceRelatedData(smcService, savedService, location);
        }
    }

    private void saveOrganizationRelatedData(SmcOrganization smcOrganization, Organization savedOrganization) {
        persistence.importFunding(smcOrganization, savedOrganization);
        persistence.importContacts(smcOrganization.getId(), savedOrganization);
        persistence.importPrograms(smcOrganization.getId(), savedOrganization);
        saveLocationsAndLocationRelatedData(smcOrganization.getId(), savedOrganization);
    }

    private void saveLocationRelatedData(SmcLocation smcLocation, Location savedLocation, Organization savedOrganization) {
        persistence.importRegularSchedule(smcLocation.getId(), savedLocation);
        persistence.importHolidaySchedule(smcLocation.getId(), savedLocation);
        persistence.importPhysicalAddress(smcLocation.getId(), savedLocation);
        persistence.importPostalAddress(smcLocation.getId(), savedLocation);
        saveServicesAndServiceRelatedData(smcLocation.getId(), savedLocation, savedOrganization);
    }

    private void saveServiceRelatedData(SmcService smcService, Service savedService, Location location) {
        persistence.importFunding(smcService, savedService);
        persistence.importEligibility(smcService, savedService);
        persistence.importLanguages(smcService, savedService, location);
        persistence.importContacts(smcService.getId(), savedService);
        persistence.importRegularSchedule(smcService.getId(), savedService);
        persistence.importHolidaySchedule(smcService.getId(), savedService);
        persistence.importPhones(smcService.getId(), savedService);
    }
}
