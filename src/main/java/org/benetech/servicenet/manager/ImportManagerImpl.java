package org.benetech.servicenet.manager;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.builder.ReportErrorMessageBuilder;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.conflict.detector.ConflictDetector;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ImportManagerImpl implements ImportManager {

    @Autowired
    private ImportService importService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ApplicationContext context;

    @Override
    public Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId,
                                                   ImportData importData) {
        try {
            ConflictDetector detector = context.getBean(
                filledOrganization.getClass().getSimpleName() + Constants.CONFLICT_DETECTOR_SUFFIX,
                ConflictDetector.class);
            Optional<Organization> organizationFromDb =
                organizationService.findWithEagerAssociations(externalDbId, importData.getProviderName());

            if (organizationFromDb.isEmpty() || detector.areConflicted(filledOrganization, organizationFromDb.get())) {
                return importService.createOrUpdateOrganization(filledOrganization, externalDbId, importData);
            } else {
                return null;
            }
        } catch (Exception e) {
            handleError(e.getMessage(), importData.getReport());
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
    public Location createOrUpdateLocation(Location filledLocation, String externalDbId, ImportData importData) {
        try {
            return importService.createOrUpdateLocation(filledLocation, externalDbId, importData);
        } catch (Exception e) {
            handleError(e.getMessage(), importData.getReport());
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
        report.setErrorMessage(ReportErrorMessageBuilder.buildForError(message, report));
        log.error(message);
    }
}
