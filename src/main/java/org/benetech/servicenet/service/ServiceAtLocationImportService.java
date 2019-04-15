package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;

public interface ServiceAtLocationImportService {

    ServiceAtLocation createOrUpdateServiceAtLocationForService(ServiceAtLocation serviceAtLocation,
        String providerName, Service service, DataImportReport report);
}
