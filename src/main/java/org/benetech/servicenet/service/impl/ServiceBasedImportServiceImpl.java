package org.benetech.servicenet.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceAtLocationImportService;
import org.benetech.servicenet.service.ServiceBasedImportService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.SharedImportService;
import org.benetech.servicenet.service.TaxonomyImportService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.benetech.servicenet.util.CollectionUtils.filterNulls;
import static org.benetech.servicenet.validator.EntityValidator.isValid;

@Component
public class ServiceBasedImportServiceImpl implements ServiceBasedImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private SharedImportService sharedImportService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private TaxonomyImportService taxonomyImportService;

    @Autowired
    private ServiceAtLocationImportService serviceAtLocationImportService;

    @Override
    @ConfidentialFilter
    public void createOrUpdateEligibility(Eligibility eligibility, Service service, DataImportReport report) {
        if (eligibility == null) {
            return;
        }
        EntityValidator.validateAndFix(eligibility, report, service.getExternalDbId());
        
        eligibility.setSrvc(service);
        if (service.getEligibility() != null) {
            eligibility.setId(service.getEligibility().getId());
            em.merge(eligibility);
        } else {
            em.persist(eligibility);
        }
        service.setEligibility(eligibility);
    }

    @Override
    public void createOrUpdateLangsForService(Set<Language> langs, Service service, DataImportReport report) {
        Set<Language> filtered = langs.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, service.getExternalDbId()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredLangsForService(filtered, service);
    }

    @Override
    public void createOrUpdatePhonesForService(Set<Phone> phones, Service service, DataImportReport report) {
        Set<Phone> filtered = phones.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        filtered.forEach(p -> p.setSrvc(service));
        createOrUpdateFilteredPhonesForService(filtered, service);
    }

    @Override
    @ConfidentialFilter
    public void createOrUpdateFundingForService(Funding funding, Service service, DataImportReport report) {
        if (funding == null) {
            return;
        }
        EntityValidator.validateAndFix(funding, report, service.getExternalDbId());
        
        funding.setSrvc(service);
        if (service.getFunding() != null) {
            funding.setId(service.getFunding().getId());
            em.merge(funding);
        } else {
            em.persist(funding);
        }

        service.setFunding(funding);
    }

    @Override
    public void createOrUpdateRegularScheduleForService(RegularSchedule schedule, Service service, DataImportReport report) {
        if (schedule != null) {
            sharedImportService.createOrUpdateOpeningHours(schedule.getOpeningHours()
                .stream().filter(x -> isValid(x, report, service.getExternalDbId()))
                .collect(Collectors.toSet()), service, schedule);
        }
    }

    @Override
    public void createOrUpdateServiceTaxonomy(Set<ServiceTaxonomy> serviceTaxonomies, String providerName,
                                               Service service, DataImportReport report) {
        Set<ServiceTaxonomy> savedServiceTaxonomies = new HashSet<>();
        for (ServiceTaxonomy serviceTaxonomy : serviceTaxonomies) {
            savedServiceTaxonomies.add(persistServiceTaxonomy(serviceTaxonomy, providerName, service, report));
        }
        service.setTaxonomies(filterNulls(savedServiceTaxonomies));
    }

    @Override
    @ConfidentialFilter
    public ServiceTaxonomy persistServiceTaxonomy(ServiceTaxonomy serviceTaxonomy,
                                                            String providerName, Service service, DataImportReport report) {
        if (serviceTaxonomy == null) {
            return null;
        }
        EntityValidator.validateAndFix(serviceTaxonomy, report, service.getExternalDbId());
        
        serviceTaxonomy.setSrvc(service);

        if (serviceTaxonomy.getTaxonomy() != null) {
            taxonomyImportService.createOrUpdateTaxonomy(
                serviceTaxonomy.getTaxonomy(), serviceTaxonomy.getTaxonomy().getExternalDbId(), providerName, report);
        }

        Optional<ServiceTaxonomy> serviceTaxonomyFromDb
            = serviceTaxonomyService.findForExternalDb(serviceTaxonomy.getExternalDbId(), providerName);

        if (serviceTaxonomyFromDb.isPresent()) {
            serviceTaxonomy.setId(serviceTaxonomyFromDb.get().getId());
            em.merge(serviceTaxonomy);
        } else {
            em.persist(serviceTaxonomy);
        }
        return serviceTaxonomy;
    }

    @Override
    @ConfidentialFilter
    public void createOrUpdateRequiredDocuments(Set<RequiredDocument> requiredDocuments,
                                                 String providerName, Service service, DataImportReport report) {
        Set<RequiredDocument> savedDocs = new HashSet<>();
        for (RequiredDocument doc : requiredDocuments) {
            savedDocs.add(persistRequiredDocument(doc, doc.getExternalDbId(), providerName, service, report));
        }
        service.setDocs(filterNulls(savedDocs));
    }

    @Override
    @ConfidentialFilter
    public RequiredDocument persistRequiredDocument(RequiredDocument document, String externalDbId,
                                                     String providerName, Service service, DataImportReport report) {
        if (document == null) {
            return null;
        }
        EntityValidator.validateAndFix(document, report, externalDbId);
        
        document.setSrvc(service);
        Optional<RequiredDocument> requiredDocumentFromDb
            = requiredDocumentService.findForExternalDb(externalDbId, providerName);

        if (requiredDocumentFromDb.isPresent()) {
            document.setId(requiredDocumentFromDb.get().getId());
            return em.merge(document);
        } else {
            em.persist(document);
            return document;
        }
    }

    @Override
    public void createOrUpdateContactsForService(Set<Contact> contacts, Service service, DataImportReport report) {
        contacts.forEach(c -> c.setSrvc(service));
        createOrUpdateContacts(contacts, service, report, service.getExternalDbId());
    }

    @Override
    @ConfidentialFilter
    public void createOrUpdateHolidayScheduleForService(HolidaySchedule schedule, Service service, DataImportReport report) {
        if (schedule == null) {
            return;
        }
        EntityValidator.validateAndFix(schedule, report, service.getExternalDbId());
        
        schedule.setSrvc(service);
        if (service.getHolidaySchedule() != null) {
            schedule.setId(service.getHolidaySchedule().getId());
            em.merge(schedule);
        } else {
            em.persist(schedule);
        }
        service.setHolidaySchedule(schedule);
    }

    @Override
    @ConfidentialFilter
    public void createOrUpdateServiceAtLocationsForService(Set<ServiceAtLocation> serviceAtLocations, String providerName,
        Service service, DataImportReport report) {
        if (serviceAtLocations != null) {
            serviceAtLocations.forEach(serviceAtLocation -> {
                serviceAtLocation.setSrvc(service);

                serviceAtLocationImportService.createOrUpdateServiceAtLocationForService(serviceAtLocation, providerName);
            });

            service.setLocations(serviceAtLocations);
        }
    }

    private void createOrUpdateContacts(Set<Contact> contacts, Service service,
                                        DataImportReport report, String serviceExternalId) {
        Set<Contact> filtered = contacts.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, serviceExternalId))
            .collect(Collectors.toSet());
        createOrUpdateFilteredContacts(filtered, service);
    }

    private void createOrUpdateFilteredContacts(Set<Contact> contacts, Service service) {
        contacts.forEach(contact -> {
            EntityValidator.validateAndFix(contact, null, "");
            contact.setSrvc(service);
        });
        service.setContacts(sharedImportService.createOrUpdateContacts(contacts));
    }

    private void createOrUpdateFilteredPhonesForService(Set<Phone> phones, @Nonnull Service service) {
        phones.forEach(phone -> {
            EntityValidator.validateAndFix(phone, null, "");
            phone.setSrvc(service);
        });
        sharedImportService.persistPhones(service.getPhones(), phones);
    }

    private void createOrUpdateFilteredLangsForService(Set<Language> langs, Service service) {
        langs.forEach(lang -> {
            EntityValidator.validateAndFix(lang, null, "");
            lang.setSrvc(service);
        });
        sharedImportService.persistLangs(service.getLangs(), langs);
    }
}
