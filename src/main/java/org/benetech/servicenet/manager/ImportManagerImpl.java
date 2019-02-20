package org.benetech.servicenet.manager;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.TmpImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImportManagerImpl implements ImportManager {

    @Autowired
    private TmpImportService importService;

    @Override
    public Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId,
                                                   String providerName, DataImportReport report) {
        try {
            return importService.createOrUpdateOrganization(filledOrganization, externalDbId, providerName, report);
        } catch (Exception e) {
            handleError(e.getMessage(), report);
            return null;
        }
    }

    @Override
    public Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName,
                                           DataImportReport report) {
        try {
            return importService.createOrUpdateTaxonomy(taxonomy, externalDbId, providerName, report);
        } catch (Exception e) {
            handleError(e.getMessage(), report);
            return null;
        }
    }

    @Override
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, String providerName,
                                           DataImportReport report) {
        try {
            return importService.createOrUpdateLocation(filledLocation, externalDbId, providerName, report);
        } catch (Exception e) {
            handleError(e.getMessage(), report);
            return null;
        }
    }

    @Override
    public Service createOrUpdateService(Service filledService, String externalDbId,
                                         String providerName, DataImportReport report) {
        try {
            return importService.createOrUpdateService(filledService, externalDbId, providerName, report);
        } catch (Exception e) {
            handleError(e.getMessage(), report);
            return null;
        }
    }

    private void handleError(String message, DataImportReport report) {
        report.setErrorMessage(report.getErrorMessage() + message + "\n");
        log.error(message);
    }
}
