package org.benetech.servicenet.manager;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;

public interface ImportManager {

    Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId, String providerName,
                                            DataImportReport report);

    Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName);

    Location createOrUpdateLocation(Location filledLocation, String externalDbId, String providerName);

    Service createOrUpdateService(Service filledService, String externalDbId,
                                  String providerName, DataImportReport report);
}
