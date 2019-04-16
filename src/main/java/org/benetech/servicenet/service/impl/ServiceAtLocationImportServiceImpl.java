package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.ServiceAtLocationImportService;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

@Component
public class ServiceAtLocationImportServiceImpl implements ServiceAtLocationImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private ServiceAtLocationService serviceAtLocationService;

    @Autowired
    private LocationService locationService;

    @Override
    public ServiceAtLocation createOrUpdateServiceAtLocationForService(ServiceAtLocation serviceAtLocation,
        String providerName, Service service, DataImportReport report) {
        Optional<ServiceAtLocation> serviceAtLocationFromDb = serviceAtLocationService.findForExternalDb(
            serviceAtLocation.getExternalDbId(), providerName);

        if (serviceAtLocation.getLocation() != null) {
            locationService.findWithEagerAssociations(
                serviceAtLocation.getLocation().getExternalDbId(), providerName).ifPresent(serviceAtLocation::setLocation);
        }

        if (serviceAtLocationFromDb.isPresent()) {
            serviceAtLocation.setId(serviceAtLocationFromDb.get().getId());
            return em.merge(serviceAtLocation);
        } else {
            em.persist(serviceAtLocation);
            return serviceAtLocation;
        }
    }
}
