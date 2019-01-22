package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.benetech.servicenet.adapter.shared.model.MultipleImportData;
import org.benetech.servicenet.adapter.smcconnect.SmcConnectDataMapper;
import org.benetech.servicenet.adapter.smcconnect.model.SmcAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcContact;
import org.benetech.servicenet.adapter.smcconnect.model.SmcHolidaySchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcLocation;
import org.benetech.servicenet.adapter.smcconnect.model.SmcMailAddress;
import org.benetech.servicenet.adapter.smcconnect.model.SmcOrganization;
import org.benetech.servicenet.adapter.smcconnect.model.SmcPhone;
import org.benetech.servicenet.adapter.smcconnect.model.SmcProgram;
import org.benetech.servicenet.adapter.smcconnect.model.SmcRegularSchedule;
import org.benetech.servicenet.adapter.smcconnect.model.SmcService;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ImportService;

import java.util.Set;
import java.util.stream.Collectors;

class PersistenceManager {

    private final String providerName;
    private SmcStorage storage;
    private ImportService importService;
    private SmcConnectDataMapper mapper;
    private DataImportReport report;

    PersistenceManager(ImportService importService, MultipleImportData data, SmcStorage storage) {
        this.importService = importService;
        this.storage = storage;
        this.mapper = SmcConnectDataMapper.INSTANCE;
        this.providerName = data.getProviderName();
        this.report = data.getReport();
    }

    Location importLocation(SmcLocation smcLocation, Organization savedOrganization) {
        Location locationToSave = mapper.extractLocation(smcLocation);
        locationToSave.setExternalDbId(smcLocation.getId());
        locationToSave.setProviderName(providerName);
        locationToSave.setOrganization(savedOrganization);
        return importService.createOrUpdateLocation(locationToSave, smcLocation.getId(), providerName);
    }

    Organization importOrganization(SmcOrganization smcOrganization, DocumentUpload sourceDocument) {
        Organization organizationToSave = mapper.extractOrganization(smcOrganization);
        organizationToSave.setExternalDbId(smcOrganization.getId());
        organizationToSave.setProviderName(providerName);
        organizationToSave.setActive(true);
        organizationToSave.setSourceDocument(sourceDocument);
        return importService.createOrUpdateOrganization(organizationToSave, smcOrganization.getId(), providerName, report);
    }

    Service importService(SmcService smcService, Organization savedOrganization) {
        Service serviceToSave = mapper.extractService(smcService);
        serviceToSave.setExternalDbId(smcService.getId());
        serviceToSave.setProviderName(providerName);
        serviceToSave.setOrganization(savedOrganization);
        return importService.createOrUpdateService(serviceToSave, smcService.getId(), providerName, report);
    }

    void importPhysicalAddress(String relatedTo, Location location) {
        storage.getRelatedEntities(SmcAddress.class, relatedTo, SmcLocation.class)
            .forEach(a -> importService.createOrUpdatePhysicalAddress(
                mapper.extractPhysicalAddress(a), location));
    }

    void importPostalAddress(String relatedTo, Location location) {
        storage.getRelatedEntities(SmcMailAddress.class, relatedTo, SmcLocation.class)
            .forEach(a -> importService.createOrUpdatePostalAddress(
                mapper.extractPostalAddress(a), location));
    }

    void importPhones(String relatedTo, Service service) {
        importService.createOrUpdatePhonesForService(
            mapper.extractPhones(
                storage.getRelatedEntities(SmcPhone.class, relatedTo, SmcService.class)), service, null);
    }

    void importEligibility(SmcService smcService, Service service) {
        mapper.extractEligibility(smcService)
            .ifPresent(e -> importService.createOrUpdateEligibility(e, service));
    }

    void importLanguages(SmcService smcService, Service service, Location location) {
        importService.createOrUpdateLangsForService(
            mapper.extractLangs(smcService), service, location);
    }

    void importFunding(SmcOrganization smcOrganization, Organization organization) {
        mapper.extractFunding(smcOrganization)
            .ifPresent(f -> importService.createOrUpdateFundingForOrganization(f, organization));
    }

    void importFunding(SmcService smcService, Service service) {
        mapper.extractFunding(smcService)
            .ifPresent(f -> importService.createOrUpdateFundingForService(f, service));
    }

    void importContacts(String relatedTo, Organization organization) {
        Set<Contact> contacts = storage.getRelatedEntities(SmcContact.class, relatedTo, SmcOrganization.class).stream()
            .map(c -> mapper.extractContact(c)).collect(Collectors.toSet());
        importService.createOrUpdateContactsForOrganization(contacts, organization);
    }

    void importContacts(String relatedTo, Service service) {
        Set<Contact> contacts = storage.getRelatedEntities(SmcContact.class, relatedTo, SmcService.class).stream()
            .map(c -> mapper.extractContact(c)).collect(Collectors.toSet());
        importService.createOrUpdateContactsForService(contacts, service);
    }

    void importRegularSchedule(String relatedTo, Service service) {
        Set<OpeningHours> openingHours = storage.getRelatedEntities(SmcRegularSchedule.class, relatedTo, SmcService.class)
            .stream().map(o -> mapper.extractOpeningHours(o)).collect(Collectors.toSet());
        if (!openingHours.isEmpty()) {
            importService.createOrUpdateOpeningHoursForService(openingHours, service);
        }
    }

    void importRegularSchedule(String relatedTo, Location location) {
        Set<OpeningHours> openingHours = storage.getRelatedEntities(SmcRegularSchedule.class, relatedTo, SmcLocation.class)
            .stream().map(o -> mapper.extractOpeningHours(o)).collect(Collectors.toSet());
        if (!openingHours.isEmpty()) {
            importService.createOrUpdateOpeningHoursForLocation(openingHours, location);
        }
    }

    void importHolidaySchedule(String relatedTo, Location location) {
        Set<HolidaySchedule> schedules = storage.getRelatedEntities(SmcHolidaySchedule.class, relatedTo, SmcLocation.class)
            .stream().map(s -> mapper.extractHolidaySchedule(s)).collect(Collectors.toSet());
        // TODO: currently the relation is one to one, so only one is persisted
        if (!schedules.isEmpty()) {
            importService.createOrUpdateHolidayScheduleForLocation(schedules.iterator().next(), location);
        }
    }

    void importHolidaySchedule(String relatedTo, Service service) {
        Set<HolidaySchedule> schedules =
            storage.getRelatedEntities(SmcHolidaySchedule.class, relatedTo, SmcService.class).stream()
            .map(s -> mapper.extractHolidaySchedule(s)).collect(Collectors.toSet());
        // TODO: currently the relation is one to one, so only one is persisted
        if (!schedules.isEmpty()) {
            importService.createOrUpdateHolidayScheduleForService(schedules.iterator().next(), service);
        }
    }

    void importPrograms(String relatedTo, Organization organization) {
        importService.createOrUpdateProgramsForOrganization(
            storage.getRelatedEntities(SmcProgram.class, relatedTo, SmcOrganization.class).stream()
                .map(p -> mapper.extractProgram(p)).collect(Collectors.toSet()), organization);
    }
}
