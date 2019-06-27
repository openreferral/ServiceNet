package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.ServiceAtLocation;

public interface ServiceAtLocationImportService {

    void createOrUpdateServiceAtLocationForService(ServiceAtLocation serviceAtLocation, String providerName);
}
