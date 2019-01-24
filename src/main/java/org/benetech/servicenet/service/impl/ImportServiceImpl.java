package org.benetech.servicenet.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
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
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.ImportService;
import org.benetech.servicenet.service.LocationService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.SystemAccountService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.annotation.ConfidentialFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Import service for data from all providers
 *
 * IMPORTANT: Some objects might have isConfidential flag set to true
 *            and we should not persist those objects in the database.
 *            Each single-entity import method should be annotated with @ConfidentialFilter to filter it automatically.
 *            Each collection import method should verify the flag manually.
 */
@Component
public class ImportServiceImpl implements ImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Autowired
    private ServiceAtLocationService serviceAtLocationService;

    @Autowired
    private TaxonomyService taxonomyService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private ContactService contactService;

    @Override
    @ConfidentialFilter
    public Location createOrUpdateLocation(Location location, String externalDbId, String providerName) {
        Optional<Location> locationFromDb = locationService.findForExternalDb(externalDbId, providerName);
        if (locationFromDb.isPresent()) {
            location.setPhysicalAddress(locationFromDb.get().getPhysicalAddress());
            location.setPostalAddress(locationFromDb.get().getPostalAddress());
            location.setAccessibilities(locationFromDb.get().getAccessibilities());
            location.setId(locationFromDb.get().getId());
            location.setRegularSchedule(locationFromDb.get().getRegularSchedule());
            location.setHolidaySchedule(locationFromDb.get().getHolidaySchedule());
            em.merge(location);
        } else {
            em.persist(location);
        }
        return location;
    }

    @Override
    @ConfidentialFilter
    public PhysicalAddress createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress, Location location) {
        if (location.getPhysicalAddress() != null) {
            physicalAddress.setId(location.getPhysicalAddress().getId());
            em.merge(physicalAddress);
        } else {
            physicalAddress.setLocation(location);
            em.persist(physicalAddress);
        }

        return physicalAddress;
    }

    @Override
    @ConfidentialFilter
    public PostalAddress createOrUpdatePostalAddress(PostalAddress postalAddress, Location location) {
        if (location.getPostalAddress() != null) {
            postalAddress.setId(location.getPostalAddress().getId());
            em.merge(postalAddress);
        } else {
            postalAddress.setLocation(location);
            em.persist(postalAddress);
        }

        return postalAddress;
    }

    @Override
    @ConfidentialFilter
    public AccessibilityForDisabilities createOrUpdateAccessibility(AccessibilityForDisabilities accessibility,
                                                                    Location location) {
        Optional<AccessibilityForDisabilities> existingAccessibility = getExistingAccessibility(accessibility, location);
        if (existingAccessibility.isPresent()) {
            accessibility.setId(existingAccessibility.get().getId());
            em.merge(accessibility);
        } else {
            accessibility.setLocation(location);
            em.persist(accessibility);
        }

        return accessibility;
    }

    @Override
    @ConfidentialFilter
    public Organization createOrUpdateOrganization(Organization organization, String externalDbId, String providerName,
                                                   DataImportReport report) {
        Optional<Organization> organizationFromDb = organizationService.findForExternalDb(externalDbId, providerName);
        if (organizationFromDb.isPresent()) {
            organization.setId(organizationFromDb.get().getId());
            organization.setAccount(organizationFromDb.get().getAccount());
            organization.setFunding(organizationFromDb.get().getFunding());
            organization.setContacts(organizationFromDb.get().getContacts());
            em.merge(organization);
            report.incrementNumberOfUpdatedOrgs();
        } else {
            Optional<SystemAccount> systemAccount = systemAccountService.findByName(providerName);
            organization.setAccount(systemAccount.orElse(null));
            em.persist(organization);
            report.incrementNumberOfCreatedOrgs();
        }

        registerSynchronizationOfMatchingOrganizations(organization);

        return organization;
    }

    @Override
    @ConfidentialFilter
    public Organization createOrUpdateOrganization(Organization organization, String externalDbId, String providerName,
                                                   Service service, Location location, DataImportReport report) {
        organization.setServices(Collections.singleton(service));
        organization.setLocation(location);
        return createOrUpdateOrganization(organization, externalDbId, providerName, report);
    }

    @Override
    @ConfidentialFilter
    public Service createOrUpdateService(Service service, String externalDbId, String providerName,
                                         DataImportReport report) {
        Optional<Service> serviceFromDb = serviceService.findForExternalDb(externalDbId, providerName);
        if (serviceFromDb.isPresent()) {
            service.setPhones(serviceFromDb.get().getPhones());
            service.setEligibility(serviceFromDb.get().getEligibility());
            service.setId(serviceFromDb.get().getId());
            service.setRegularSchedule(serviceFromDb.get().getRegularSchedule());
            service.setFunding(serviceFromDb.get().getFunding());
            service.setHolidaySchedule(serviceFromDb.get().getHolidaySchedule());
            service.setContacts(serviceFromDb.get().getContacts());
            em.merge(service);
            report.incrementNumberOfUpdatedServices();
        } else {
            em.persist(service);
            report.incrementNumberOfCreatedServices();
        }
        return service;
    }

    @Override
    public Set<Phone> createOrUpdatePhonesForService(Set<Phone> phones, Service service, Location location) {
        Set<Phone> filtered = phones.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        return createOrUpdateFilteredPhonesForService(filtered, service, location);
    }

    private Set<Phone> createOrUpdateFilteredPhonesForService(Set<Phone> phones, Service service, Location location) {
        phones.forEach(phone ->  {
            phone.setSrvc(service);
            phone.setLocation(location);
        });

        Set<Phone> common = new HashSet<>(phones);
        common.retainAll(service.getPhones());

        service.getPhones().stream().filter(phone -> !common.contains(phone)).forEach(phone -> em.remove(phone));
        phones.stream().filter(phone -> !common.contains(phone)).forEach(phone -> em.persist(phone));

        return phones;
    }

    @Override
    @ConfidentialFilter
    public Eligibility createOrUpdateEligibility(Eligibility eligibility, Service service) {
        if (service.getEligibility() != null) {
            eligibility.setId(service.getEligibility().getId());
            em.merge(eligibility);
        } else {
            eligibility.setSrvc(service);
            em.persist(eligibility);
        }

        return eligibility;
    }

    @Override
    @ConfidentialFilter
    public Funding createOrUpdateFundingForOrganization(Funding funding, Organization organization) {
        if (organization.getFunding() != null) {
            funding.setId(organization.getFunding().getId());
            em.merge(funding);
        } else {
            funding.setOrganization(organization);
            em.persist(funding);
        }

        return funding;
    }

    @Override
    @ConfidentialFilter
    public Funding createOrUpdateFundingForService(Funding funding, Service service) {
        if (service.getFunding() != null) {
            funding.setId(service.getFunding().getId());
            em.merge(funding);
        } else {
            funding.setSrvc(service);
            em.persist(funding);
        }

        return funding;
    }

    @Override
    public Set<Language> createOrUpdateLangsForService(Set<Language> langs, Service service, Location location) {
        Set<Language> filtered = langs.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        return createOrUpdateFilteredLangsForService(filtered, service, location);
    }

    private Set<Language> createOrUpdateFilteredLangsForService(Set<Language> langs, Service service, Location location) {
        langs.forEach(lang ->  {
            lang.setSrvc(service);
            lang.setLocation(location);
        });

        Set<Language> common = new HashSet<>(langs);
        common.retainAll(service.getLangs());

        service.getLangs().stream().filter(lang -> !common.contains(lang)).forEach(lang -> em.remove(lang));
        langs.stream().filter(lang -> !common.contains(lang)).forEach(lang -> em.persist(lang));

        return langs;
    }

    @Override
    public Set<Program> createOrUpdateProgramsForOrganization(Set<Program> programs, Organization organization) {
        Set<Program> filtered = programs.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        return createOrUpdateFilteredProgramsForOrganization(filtered, organization);
    }

    private Set<Program> createOrUpdateFilteredProgramsForOrganization(Set<Program> programs, Organization organization) {
        programs.forEach(p -> p.setOrganization(organization));

        Set<Program> common = new HashSet<>(programs);
        common.retainAll(organization.getPrograms());

        organization.getPrograms().stream().filter(p -> !common.contains(p)).forEach(p -> em.remove(p));
        programs.stream().filter(p -> !common.contains(p)).forEach(p -> em.persist(p));

        return programs;
    }

    @Override
    public Set<OpeningHours> createOrUpdateOpeningHoursForService(Set<OpeningHours> openingHours, Service service) {
        RegularSchedule schedule = service.getRegularSchedule();
        createOrUpdateOpeningHours(openingHours, service, null, schedule);
        return openingHours;
    }

    @Override
    public Set<OpeningHours> createOrUpdateOpeningHoursForLocation(Set<OpeningHours> openingHours,
                                                                   Location location) {
        RegularSchedule schedule = location.getRegularSchedule();
        createOrUpdateOpeningHours(openingHours, null, location, schedule);
        return openingHours;
    }

    private void createOrUpdateOpeningHours(Set<OpeningHours> openingHours, Service service, Location location,
                                            RegularSchedule schedule) {
        Set<OpeningHours> filtered = openingHours.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredOpeningHours(filtered, service, location, schedule);
    }

    private void createOrUpdateFilteredOpeningHours(Set<OpeningHours> openingHours, Service service, Location location,
                                            RegularSchedule schedule) {
        if (schedule != null) {
            Set<OpeningHours> common = new HashSet<>(openingHours);
            common.retainAll(schedule.getOpeningHours());

            schedule.getOpeningHours().stream().filter(o -> !common.contains(o)).forEach(o -> em.remove(o));
            openingHours.stream().filter(o -> !common.contains(o)).forEach(o -> em.persist(o));

            em.merge(schedule.openingHours(new HashSet<>(openingHours)).location(location).srvc(service));
        } else {
            openingHours.forEach(o -> em.persist(o));
            em.persist(new RegularSchedule().openingHours(new HashSet<>(openingHours)).location(location).srvc(service));
        }
    }

    @Override
    @ConfidentialFilter
    public ServiceAtLocation createOrUpdateServiceAtLocation(ServiceAtLocation serviceAtLocation, String externalDbId,
                                                             String providerName, Service service, Location location) {
        serviceAtLocation.setSrvc(service);
        serviceAtLocation.setLocation(location);

        Optional<ServiceAtLocation> serviceAtLocationFromDb
            = serviceAtLocationService.findForExternalDb(externalDbId, providerName);

        if (serviceAtLocationFromDb.isPresent()) {
            serviceAtLocation.setId(serviceAtLocationFromDb.get().getId());
            return em.merge(serviceAtLocation);
        } else {
            em.persist(serviceAtLocation);
            return serviceAtLocation;
        }
    }

    @Override
    @ConfidentialFilter
    public Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName) {
        Optional<Taxonomy> taxonomyFromDb = taxonomyService.findForExternalDb(externalDbId, providerName);

        if (taxonomyFromDb.isPresent()) {
            taxonomy.setId(taxonomyFromDb.get().getId());
            return em.merge(taxonomy);
        } else {
            em.persist(taxonomy);
            return taxonomy;
        }
    }

    @Override
    @ConfidentialFilter
    public ServiceTaxonomy createOrUpdateServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String externalDbId,
                                                         String providerName, Service service, Taxonomy taxonomy) {
        serviceTaxonomy.setSrvc(service);
        serviceTaxonomy.setTaxonomy(taxonomy);

        Optional<ServiceTaxonomy> serviceTaxonomyFromDb
            = serviceTaxonomyService.findForExternalDb(externalDbId, providerName);

        if (serviceTaxonomyFromDb.isPresent()) {
            serviceTaxonomy.setId(serviceTaxonomyFromDb.get().getId());
            return em.merge(serviceTaxonomy);
        } else {
            em.persist(serviceTaxonomy);
            return serviceTaxonomy;
        }
    }

    @Override
    @ConfidentialFilter
    public RequiredDocument createOrUpdateRequiredDocument(RequiredDocument requiredDocument, String externalDbId,
                                                           String providerName, Service service) {
        requiredDocument.setSrvc(service);

        Optional<RequiredDocument> requiredDocumentFromDb
            = requiredDocumentService.findForExternalDb(externalDbId, providerName);

        if (requiredDocumentFromDb.isPresent()) {
            requiredDocument.setId(requiredDocumentFromDb.get().getId());
            return em.merge(requiredDocument);
        } else {
            em.persist(requiredDocument);
            return requiredDocument;
        }
    }

    @Override
    public Set<Contact> createOrUpdateContactsForService(Set<Contact> contacts, Service service) {
        contacts.forEach(c -> c.setSrvc(service));
        Set<Contact> common = new HashSet<>(contacts);
        common.retainAll(service.getContacts());
        createOrUpdateContacts(contacts, common, service.getContacts());
        return contacts;
    }

    @Override
    public Set<Contact> createOrUpdateContactsForOrganization(Set<Contact> contacts, Organization organization) {
        contacts.forEach(c -> c.setOrganization(organization));
        Set<Contact> common = new HashSet<>(contacts);
        common.retainAll(organization.getContacts());
        createOrUpdateContacts(contacts, common, organization.getContacts());
        return contacts;
    }

    private void createOrUpdateContacts(Set<Contact> contacts, Set<Contact> common, Set<Contact> source) {
        Set<Contact> filtered = contacts.stream().filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .collect(Collectors.toSet());
        createOrUpdateFilteredContacts(filtered, common, source);
    }

    private void createOrUpdateFilteredContacts(Set<Contact> contacts, Set<Contact> common, Set<Contact> source) {
        source.stream().filter(c -> !common.contains(c)).forEach(c -> em.remove(c));
        contacts.stream().filter(c -> !common.contains(c)).forEach(c -> em.persist(c));
    }

    @Override
    @ConfidentialFilter
    public HolidaySchedule createOrUpdateHolidayScheduleForLocation(HolidaySchedule schedule, Location location) {
        if (location.getHolidaySchedule() != null) {
            schedule.setId(location.getHolidaySchedule().getId());
            em.merge(schedule);
        } else {
            schedule.setLocation(location);
            em.persist(schedule);
        }

        return schedule;
    }

    @Override
    @ConfidentialFilter
    public HolidaySchedule createOrUpdateHolidayScheduleForService(HolidaySchedule schedule, Service service) {
        if (service.getHolidaySchedule() != null) {
            schedule.setId(service.getHolidaySchedule().getId());
            em.merge(schedule);
        } else {
            schedule.setSrvc(service);
            em.persist(schedule);
        }

        return schedule;
    }

    private Optional<AccessibilityForDisabilities> getExistingAccessibility(AccessibilityForDisabilities accessibility,
                                                                            Location location) {
        return location.getAccessibilities().stream()
            .filter(a -> a.getAccessibility().equals(accessibility.getAccessibility()))
            .findFirst();
    }

    private void registerSynchronizationOfMatchingOrganizations(Organization organization) {
         TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                organizationMatchService.createOrUpdateOrganizationMatches(organization);
            }
        });
    }
}
