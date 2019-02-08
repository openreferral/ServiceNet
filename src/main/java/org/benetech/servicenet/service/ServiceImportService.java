package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Service;

public interface ServiceImportService {

    Service createOrUpdateService(Service filledService, String externalDbId,
                                  String providerName, DataImportReport report);
}
