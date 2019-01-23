package org.benetech.servicenet.service;

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
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;

import java.util.Set;
import java.util.UUID;

public interface ImportService {

    Location createOrUpdateLocation(Location location, String externalDbId, String providerName);

    PhysicalAddress createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress, Location location);

    PostalAddress createOrUpdatePostalAddress(PostalAddress postalAddress, Location location);

    AccessibilityForDisabilities createOrUpdateAccessibility(AccessibilityForDisabilities accessibility, Location location);

    Organization createOrUpdateOrganization(Organization organization, String externalDbId, String providerName,
                                            DataImportReport report);

    Organization createOrUpdateOrganization(Organization organization, String externalDbId, String providerName,
                                            Service service, Set<Location> location, DataImportReport report);

    Service createOrUpdateService(Service service, String externalDbId, String providerName, DataImportReport report);

    Set<Phone> createOrUpdatePhonesForService(Set<Phone> phones, Service service, Location location);

    Set<Phone> createOrUpdatePhonesForOrganization(Set<Phone> phones, UUID orgId);

    Eligibility createOrUpdateEligibility(Eligibility eligibility, Service service);

    Set<Language> createOrUpdateLangsForService(Set<Language> langs, Service service, Location location);

    Funding createOrUpdateFundingForOrganization(Funding funding, Organization organization);

    Funding createOrUpdateFundingForService(Funding funding, Service service);

    Set<OpeningHours> createOrUpdateOpeningHoursForService(Set<OpeningHours> openingHours, Service service);

    Set<OpeningHours> createOrUpdateOpeningHoursForLocation(Set<OpeningHours> openingHours, Location location);

    ServiceAtLocation createOrUpdateServiceAtLocation(ServiceAtLocation serviceAtLocation, String externalDbId,
                                                      String providerName, Service service, Location location);

    Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName);

    ServiceTaxonomy createOrUpdateServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String externalDbId,
                                                  String providerName, Service service, Taxonomy taxonomy);

    RequiredDocument createOrUpdateRequiredDocument(RequiredDocument requiredDocument, String externalDbId,
                                                    String providerName, Service service);

    Set<Contact> createOrUpdateContactsForService(Set<Contact> contacts, Service service);

    Set<Contact> createOrUpdateContactsForOrganization(Set<Contact> contacts, Organization organization);

    HolidaySchedule createOrUpdateHolidayScheduleForLocation(HolidaySchedule schedule, Location location);

    HolidaySchedule createOrUpdateHolidayScheduleForService(HolidaySchedule schedule, Service service);

    Set<Program> createOrUpdateProgramsForOrganization(Set<Program> programs, Organization organization);

    /**
     * @param schedule which is respected by the service.
     * @param service with set database uuid.
     * @return a saved regular schedule.
     */
    RegularSchedule createOrUpdateRegularSchedule(RegularSchedule schedule, Service service);

}
