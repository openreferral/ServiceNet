package org.benetech.servicenet.manager;

import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;

public interface ImportManager {

    Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId, ImportData importData);

    Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName, DataImportReport report);

    Location createOrUpdateLocation(Location filledLocation, String externalDbId, ImportData importData);

    Service createOrUpdateService(Service filledService, String externalDbId,
                                  String providerName, DataImportReport report);
}
