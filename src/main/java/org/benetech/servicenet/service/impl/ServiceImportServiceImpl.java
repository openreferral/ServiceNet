package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ServiceBasedImportService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

@Component
public class ServiceImportServiceImpl implements ServiceImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceBasedImportService serviceBasedImportService;

    @Override
    public Service createOrUpdateService(Service filledService, String externalDbId, String providerName,
                                         DataImportReport report) {
        Service service = new Service(filledService);
        Optional<Service> serviceFromDb = serviceService.findWithEagerAssociations(externalDbId, providerName);
        if (serviceFromDb.isPresent()) {
            fillDataFromDb(service, serviceFromDb.get());
            em.merge(service);
            report.incrementNumberOfUpdatedServices();
        } else {
            em.persist(service);
            report.incrementNumberOfCreatedServices();
        }

        serviceBasedImportService.createOrUpdateEligibility(filledService.getEligibility(), service);
        serviceBasedImportService.createOrUpdateLangsForService(filledService.getLangs(), service);
        serviceBasedImportService.createOrUpdatePhonesForService(filledService.getPhones(), service);
        serviceBasedImportService.createOrUpdateFundingForService(filledService.getFunding(), service);
        serviceBasedImportService.createOrUpdateRegularScheduleForService(filledService.getRegularSchedule(), service);
        serviceBasedImportService.createOrUpdateServiceTaxonomy(filledService.getTaxonomies(), providerName, service);
        serviceBasedImportService.createOrUpdateRequiredDocuments(filledService.getDocs(), providerName, service);
        serviceBasedImportService.createOrUpdateContactsForService(filledService.getContacts(), service);
        serviceBasedImportService.createOrUpdateHolidayScheduleForService(filledService.getHolidaySchedule(), service);

        return service;
    }

    private void fillDataFromDb(Service newService, Service serviceFromDb) {
        newService.setPhones(serviceFromDb.getPhones());
        newService.setEligibility(serviceFromDb.getEligibility());
        newService.setId(serviceFromDb.getId());
        newService.setRegularSchedule(serviceFromDb.getRegularSchedule());
        newService.setFunding(serviceFromDb.getFunding());
        newService.setHolidaySchedule(serviceFromDb.getHolidaySchedule());
        newService.setContacts(serviceFromDb.getContacts());
        newService.setLangs(serviceFromDb.getLangs());
    }
}
