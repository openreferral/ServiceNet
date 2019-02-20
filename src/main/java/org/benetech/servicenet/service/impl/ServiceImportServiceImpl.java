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
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.ServiceService;
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

import static org.benetech.servicenet.service.util.EntityManagerUtils.safeRemove;
import static org.benetech.servicenet.validator.EntityValidator.isValid;

@Component
public class ServiceImportServiceImpl implements ServiceImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private SharedImportService sharedImportService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private TaxonomyImportService taxonomyImportService;

    @Override
    public Service createOrUpdateService(Service filledService, String externalDbId, String providerName,
                                         DataImportReport report) {
        if (EntityValidator.isNotValid(filledService, report, externalDbId)) {
            return null;
        }
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

        createOrUpdateEligibility(filledService.getEligibility(), service, report);
        createOrUpdateLangsForService(filledService.getLangs(), service, report);
        createOrUpdatePhonesForService(filledService.getPhones(), service, report);
        createOrUpdateFundingForService(filledService.getFunding(), service, report);
        createOrUpdateRegularScheduleForService(filledService.getRegularSchedule(), service, report);
        createOrUpdateServiceTaxonomy(filledService.getTaxonomies(), providerName, service, report);
        createOrUpdateRequiredDocuments(filledService.getDocs(), providerName, service, report);
        createOrUpdateContactsForService(filledService.getContacts(), service, report);
        createOrUpdateHolidayScheduleForService(filledService.getHolidaySchedule(), service, report);

        return service;
    }

    @ConfidentialFilter
    private void createOrUpdateEligibility(Eligibility eligibility, Service service, DataImportReport report) {
        if (EntityValidator.isNotValid(eligibility, report, service.getExternalDbId())) {
            return;
        }
        eligibility.setSrvc(service);
        if (service.getEligibility() != null) {
            eligibility.setId(service.getEligibility().getId());
            em.merge(eligibility);
        } else {
            em.persist(eligibility);
        }
        service.setEligibility(eligibility);
    }

    private void createOrUpdateLangsForService(Set<Language> langs, Service service, DataImportReport report) {
        Set<Language> filtered = langs.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, service.getExternalDbId()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredLangsForService(filtered, service);
    }

    private void createOrUpdateFilteredLangsForService(Set<Language> langs, Service service) {
        if (service != null) {
            Set<Language> common = new HashSet<>(langs);
            common.retainAll(service.getLangs());
            service.getLangs().stream().filter(lang -> !common.contains(lang)).forEach(lang -> safeRemove(em, lang));
            langs.stream().filter(lang -> !common.contains(lang)).forEach(lang -> {
                lang.setSrvc(service);
                em.persist(lang);
            });
        }
    }

    private void createOrUpdatePhonesForService(Set<Phone> phones, Service service, DataImportReport report) {
        Set<Phone> filtered = phones.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, service.getExternalDbId()))
            .collect(Collectors.toSet());
        filtered.forEach(p -> p.setSrvc(service));
        createOrUpdateFilteredPhonesForService(filtered, service);
    }

    private void createOrUpdateFilteredPhonesForService(Set<Phone> phones, @Nonnull Service service) {
        service.setPhones(sharedImportService.persistPhones(phones, service.getPhones()));
    }

    @ConfidentialFilter
    private void createOrUpdateFundingForService(Funding funding, Service service, DataImportReport report) {
        if (EntityValidator.isNotValid(funding, report, service.getExternalDbId())) {
            return;
        }
        funding.setSrvc(service);
        if (service.getFunding() != null) {
            funding.setId(service.getFunding().getId());
            em.merge(funding);
        } else {
            em.persist(funding);
        }

        service.setFunding(funding);
    }

    private void createOrUpdateRegularScheduleForService(RegularSchedule schedule, Service service,
                                                         DataImportReport report) {
        if (schedule != null) {
            sharedImportService.createOrUpdateOpeningHours(schedule.getOpeningHours().stream()
                .filter(x -> isValid(x, report, service.getExternalDbId())).collect(Collectors.toSet()), service, schedule);
        }
    }

    private void createOrUpdateServiceTaxonomy(Set<ServiceTaxonomy> serviceTaxonomies, String providerName,
                                               Service service, DataImportReport report) {
        Set<ServiceTaxonomy> savedServiceTaxonomies = new HashSet<>();
        for (ServiceTaxonomy serviceTaxonomy : serviceTaxonomies) {
            persistServiceTaxonomy(serviceTaxonomy, providerName, service, report)
                .ifPresent(savedServiceTaxonomies::add);
        }
        service.setTaxonomies(savedServiceTaxonomies);
    }

    @ConfidentialFilter
    private Optional<ServiceTaxonomy> persistServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String providerName,
                                                             Service service, DataImportReport report) {
        if (EntityValidator.isNotValid(serviceTaxonomy, report, service.getExternalDbId())) {
            return Optional.empty();
        }
        serviceTaxonomy.setSrvc(service);

        if (serviceTaxonomy.getTaxonomy() != null) {
            taxonomyImportService.createOrUpdateTaxonomy(
                serviceTaxonomy.getTaxonomy(), serviceTaxonomy.getTaxonomy().getExternalDbId(), providerName, report);
        }

        Optional<ServiceTaxonomy> serviceTaxonomyFromDb
            = serviceTaxonomyService.findForExternalDb(service.getExternalDbId(), providerName);

        if (serviceTaxonomyFromDb.isPresent()) {
            serviceTaxonomy.setId(serviceTaxonomyFromDb.get().getId());
            em.merge(serviceTaxonomy);
        } else {
            em.persist(serviceTaxonomy);
        }
        return Optional.of(serviceTaxonomy);
    }

    @ConfidentialFilter
    private void createOrUpdateRequiredDocuments(Set<RequiredDocument> requiredDocuments,
                                                 String providerName, Service service, DataImportReport report) {
        Set<RequiredDocument> savedDocs = new HashSet<>();
        for (RequiredDocument doc : requiredDocuments) {
            persistRequiredDocument(doc, doc.getExternalDbId(), providerName, service, report)
                .ifPresent(savedDocs::add);
        }
        service.setDocs(savedDocs);
    }

    @ConfidentialFilter
    private Optional<RequiredDocument> persistRequiredDocument(RequiredDocument document, String externalDbId,
                                                     String providerName, Service service, DataImportReport report) {
        if (EntityValidator.isNotValid(document, report, externalDbId)) {
            return Optional.empty();
        }
        document.setSrvc(service);
        Optional<RequiredDocument> requiredDocumentFromDb
            = requiredDocumentService.findForExternalDb(externalDbId, providerName);

        if (requiredDocumentFromDb.isPresent()) {
            document.setId(requiredDocumentFromDb.get().getId());
            return Optional.of(em.merge(document));
        } else {
            em.persist(document);
            return Optional.of(document);
        }
    }

    private void createOrUpdateContactsForService(Set<Contact> contacts, Service service, DataImportReport report) {
        contacts.forEach(c -> c.setSrvc(service));
        Set<Contact> common = new HashSet<>(contacts);
        common.retainAll(service.getContacts());
        createOrUpdateContacts(contacts, common, service.getContacts(), report, service.getExternalDbId());
        service.setContacts(contacts);
    }

    private void createOrUpdateContacts(Set<Contact> contacts, Set<Contact> common,
                                        Set<Contact> source, DataImportReport report, String serviceExternalId) {
        Set<Contact> filtered = contacts.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential())
            && isValid(x, report, serviceExternalId))
            .collect(Collectors.toSet());
        createOrUpdateFilteredContacts(filtered, common, source);
    }

    private void createOrUpdateFilteredContacts(Set<Contact> contacts, Set<Contact> common, Set<Contact> source) {
        source.stream().filter(c -> !common.contains(c)).forEach(c -> safeRemove(em, c));
        contacts.stream().filter(c -> !common.contains(c)).forEach(c -> em.persist(c));
    }

    @ConfidentialFilter
    private void createOrUpdateHolidayScheduleForService(HolidaySchedule schedule,
                                                         Service service, DataImportReport report) {
        if (EntityValidator.isNotValid(schedule, report, service.getExternalDbId())) {
            return;
        }
        schedule.setSrvc(service);
        if (service.getHolidaySchedule() != null) {
            schedule.setId(service.getHolidaySchedule().getId());
            em.merge(schedule);
        } else {
            em.persist(schedule);
        }
        service.setHolidaySchedule(schedule);
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
