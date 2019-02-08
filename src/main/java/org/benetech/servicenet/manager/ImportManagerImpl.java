package org.benetech.servicenet.manager;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.TmpImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImportManagerImpl implements ImportManager {

    private final Logger log = LoggerFactory.getLogger(ImportManagerImpl.class);

    @Autowired
    private TmpImportService importService;

    @Override
    public Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId,
                                                   String providerName, DataImportReport report) {
        try {
            return importService.createOrUpdateOrganization(filledOrganization, externalDbId, providerName, report);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName) {
        try {
            return importService.createOrUpdateTaxonomy(taxonomy, externalDbId, providerName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, String providerName) {
        try {
            return importService.createOrUpdateLocation(filledLocation, externalDbId, providerName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Service createOrUpdateService(Service filledService, String externalDbId,
                                         String providerName, DataImportReport report) {
        try {
            return importService.createOrUpdateService(filledService, externalDbId, providerName, report);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
