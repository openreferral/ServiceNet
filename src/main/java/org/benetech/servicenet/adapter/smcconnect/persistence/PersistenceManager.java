package org.benetech.servicenet.adapter.smcconnect.persistence;

import org.benetech.servicenet.adapter.shared.model.ImportData;
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
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.manager.ImportManager;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class PersistenceManager {

    private static final String PROVIDER_NAME = "SMCConnect";
    private final SmcStorage storage;
    private SmcConnectDataMapper mapper;
    private ImportManager importManager;
    private ImportData importData;

    PersistenceManager(ImportManager importManager, MultipleImportData data, SmcStorage storage) {
        this.storage = storage;
        this.importManager = importManager;
        this.mapper = SmcConnectDataMapper.INSTANCE;
        this.importData = data;
    }

    void saveOrganizationsAndRelatedData(DocumentUpload sourceDocument) {
        for (SmcOrganization smcOrganization : storage.getEntitiesOfClass(SmcOrganization.class)) {

            Organization organizationToSave = mapper.extractOrganization(smcOrganization);
            organizationToSave.setSourceDocument(sourceDocument);
            organizationToSave.setContacts(getOrgBasedContactsToPersist(smcOrganization.getId()));
            organizationToSave.setPrograms(getProgramsToPersist(smcOrganization.getId()));
            organizationToSave.setServices(getServicesToPersist(smcOrganization.getId()));
            organizationToSave.setLocations(getLocationsToPersist(smcOrganization.getId()));

            importManager.createOrUpdateOrganization(
                organizationToSave, smcOrganization.getId(), importData);
        }
    }

    private Set<Location> getLocationsToPersist(String relatedTo) {
        Set<Location> result = new HashSet<>();
        for (SmcLocation smcLocation : storage.getRelatedEntities(SmcLocation.class, relatedTo, SmcOrganization.class)) {
            Location location = mapper.extractLocation(smcLocation);

            location.setRegularSchedule(getLocationBasedRegularScheduleToPersist(smcLocation.getId()));
            location.setHolidaySchedule(getLocationBasedHolidayScheduleToPersist(smcLocation.getId()));
            location.setPhysicalAddress(getPhysicalAddressToPersist(smcLocation.getId()));
            location.setPostalAddress(getPostalAddressToPersist(smcLocation.getId()));

            result.add(location);
        }
        return result;
    }

    private Set<Service> getServicesToPersist(String relatedTo) {
        Set<Service> result = new HashSet<>();
        for (SmcService smcService : storage.getRelatedEntities(SmcService.class, relatedTo, SmcLocation.class)) {
            Service service = mapper.extractService(smcService);

            service.setEligibility(getEligibilityToPersist(smcService));
            service.setLangs(getLanguagesToPersist(smcService));
            service.setContacts(getServiceBasedContactsToPersist(smcService.getId()));
            service.setRegularSchedule(getServiceBasedRegularScheduleToPersist(smcService.getId()));
            service.setHolidaySchedule(getServiceBasedHolidayScheduleToPersist(smcService.getId()));
            service.setPhones(getPhonesToPersist(smcService.getId()));

            result.add(service);
        }
        return result;
    }

    private Set<Language> getLanguagesToPersist(SmcService smcService) {
        return mapper.extractLangs(smcService);
    }

    private Set<Phone> getPhonesToPersist(String relatedTo) {
            return mapper.extractPhones(
                storage.getRelatedEntities(SmcPhone.class, relatedTo, SmcService.class));
    }

    private Eligibility getEligibilityToPersist(SmcService smcService) {
        return mapper.extractEligibility(smcService).orElse(null);
    }

    private RegularSchedule getLocationBasedRegularScheduleToPersist(String relatedTo) {
        Set<OpeningHours> openingHours = storage.getRelatedEntities(SmcRegularSchedule.class, relatedTo, SmcLocation.class)
            .stream().map(o -> mapper.extractOpeningHours(o)).collect(Collectors.toSet());
        if (openingHours.isEmpty()) {
            return null;
        }
        return new RegularSchedule().openingHours(openingHours);
    }

    private HolidaySchedule getLocationBasedHolidayScheduleToPersist(String relatedTo) {
        // TODO: currently the relation is one to one, so only one is persisted
        return storage.getRelatedEntities(SmcHolidaySchedule.class, relatedTo, SmcLocation.class)
            .stream().map(s -> mapper.extractHolidaySchedule(s)).findFirst().orElse(null);
    }

    private PhysicalAddress getPhysicalAddressToPersist(String relatedTo) {
        //TODO: Only first one is persisted
        return storage.getRelatedEntities(SmcAddress.class, relatedTo, SmcLocation.class).stream()
            .map(a -> mapper.extractPhysicalAddress(a)).findFirst().orElse(null);
    }

    private PostalAddress getPostalAddressToPersist(String relatedTo) {
        //TODO: Only first one is persisted
        return storage.getRelatedEntities(SmcMailAddress.class, relatedTo, SmcLocation.class).stream()
            .map(a -> mapper.extractPostalAddress(a)).findFirst().orElse(null);
    }

    private Funding getFundingToPersist(SmcService smcService) {
        return mapper.extractFunding(smcService).orElse(null);
    }

    private Set<Contact> getServiceBasedContactsToPersist(String relatedTo) {
        return storage.getRelatedEntities(SmcContact.class, relatedTo, SmcService.class).stream()
            .map(c -> mapper.extractContact(c)).collect(Collectors.toSet());
    }

    private RegularSchedule getServiceBasedRegularScheduleToPersist(String relatedTo) {
        Set<OpeningHours> openingHours = storage.getRelatedEntities(SmcRegularSchedule.class, relatedTo, SmcService.class)
            .stream().map(o -> mapper.extractOpeningHours(o)).collect(Collectors.toSet());
        if (openingHours.isEmpty()) {
            return null;
        }
        return new RegularSchedule().openingHours(openingHours);
    }

    private HolidaySchedule getServiceBasedHolidayScheduleToPersist(String relatedTo) {
        // TODO: currently the relation is one to one, so only one is persisted
        return storage.getRelatedEntities(SmcHolidaySchedule.class, relatedTo, SmcService.class).stream()
                .map(s -> mapper.extractHolidaySchedule(s)).findFirst().orElse(null);
    }

    private Set<Program> getProgramsToPersist(String relatedTo) {
        return storage.getRelatedEntities(SmcProgram.class, relatedTo, SmcOrganization.class).stream()
            .map(p -> mapper.extractProgram(p)).collect(Collectors.toSet());
    }

    private Set<Contact> getOrgBasedContactsToPersist(String relatedTo) {
        return storage.getRelatedEntities(SmcContact.class, relatedTo, SmcOrganization.class).stream()
            .map(c -> mapper.extractContact(c)).collect(Collectors.toSet());
    }

    private Funding getFundingToPersist(SmcOrganization smcOrganization) {
        return mapper.extractFunding(smcOrganization).orElse(null);
    }
}
