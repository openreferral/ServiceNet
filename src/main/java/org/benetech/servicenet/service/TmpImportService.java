package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;

public interface TmpImportService {

    Organization createOrUpdateOrganization(Organization filledOrganization, String externalDbId, String providerName,
                                            DataImportReport report);

    Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName, DataImportReport report);

    Location createOrUpdateLocation(Location filledLocation, String externalDbId, String providerName,
                                    DataImportReport report);

    Service createOrUpdateService(Service filledService, String externalDbId,
                                  String providerName, DataImportReport report);
}
