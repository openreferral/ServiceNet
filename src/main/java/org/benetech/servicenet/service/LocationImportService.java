package org.benetech.servicenet.service;

import org.benetech.servicenet.adapter.shared.model.ImportData;
import org.benetech.servicenet.domain.Location;

public interface LocationImportService {

    Location createOrUpdateLocation(Location filledLocation, String externalDbId, ImportData importData);
}
