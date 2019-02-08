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

@Deprecated
public interface ImportService {

    @Deprecated
    Location createOrUpdateLocation(Location location, String externalDbId, String providerName);

    @Deprecated
    PhysicalAddress createOrUpdatePhysicalAddress(PhysicalAddress physicalAddress, Location location);

    @Deprecated
    PostalAddress createOrUpdatePostalAddress(PostalAddress postalAddress, Location location);

    @Deprecated
    AccessibilityForDisabilities createOrUpdateAccessibility(AccessibilityForDisabilities accessibility, Location location);

    @Deprecated
    Organization createOrUpdateOrganization(Organization organization, String externalDbId, String providerName,
                                            DataImportReport report);

    @Deprecated
    Service createOrUpdateService(Service service, String externalDbId, String providerName, DataImportReport report);

    @Deprecated
    Set<Phone> createOrUpdatePhonesForService(Set<Phone> phones, Service service, Location location);

    @Deprecated
    Set<Phone> createOrUpdatePhonesForOrganization(Set<Phone> phones, UUID orgId);

    @Deprecated
    Eligibility createOrUpdateEligibility(Eligibility eligibility, Service service);

    @Deprecated
    Set<Language> createOrUpdateLangsForService(Set<Language> langs, Service service, Location location);

    @Deprecated
    Funding createOrUpdateFundingForOrganization(Funding funding, Organization organization);

    @Deprecated
    Funding createOrUpdateFundingForService(Funding funding, Service service);

    @Deprecated
    Set<OpeningHours> createOrUpdateOpeningHoursForService(Set<OpeningHours> openingHours, Service service);

    @Deprecated
    Set<OpeningHours> createOrUpdateOpeningHoursForLocation(Set<OpeningHours> openingHours, Location location);

    @Deprecated
    ServiceAtLocation createOrUpdateServiceAtLocation(ServiceAtLocation serviceAtLocation, String externalDbId,
                                                      String providerName, Service service, Location location);

    @Deprecated
    Taxonomy createOrUpdateTaxonomy(Taxonomy taxonomy, String externalDbId, String providerName);

    @Deprecated
    ServiceTaxonomy createOrUpdateServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String externalDbId,
                                                  String providerName, Service service, Taxonomy taxonomy);

    @Deprecated
    RequiredDocument createOrUpdateRequiredDocument(RequiredDocument requiredDocument, String externalDbId,
                                                    String providerName, Service service);

    @Deprecated
    Set<Contact> createOrUpdateContactsForService(Set<Contact> contacts, Service service);

    @Deprecated
    Set<Contact> createOrUpdateContactsForOrganization(Set<Contact> contacts, Organization organization);

    @Deprecated
    HolidaySchedule createOrUpdateHolidayScheduleForLocation(HolidaySchedule schedule, Location location);

    @Deprecated
    HolidaySchedule createOrUpdateHolidayScheduleForService(HolidaySchedule schedule, Service service);

    @Deprecated
    Set<Program> createOrUpdateProgramsForOrganization(Set<Program> programs, Organization organization);

    /**
     * @param schedule which is respected by the service.
     * @param service with set database uuid.
     * @return a saved regular schedule.
     */
    @Deprecated
    RegularSchedule createOrUpdateRegularSchedule(RegularSchedule schedule, Service service);
}
