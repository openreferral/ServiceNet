package org.benetech.servicenet.service;

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

import java.util.Set;

public interface ServiceBasedImportService {

    void createOrUpdateEligibility(Eligibility eligibility, Service service, DataImportReport report);

    void createOrUpdateLangsForService(Set<Language> langs, Service service, DataImportReport report);

    void createOrUpdatePhonesForService(Set<Phone> phones, Service service, DataImportReport report);

    void createOrUpdateFundingForService(Funding funding, Service service, DataImportReport report);

    void createOrUpdateRegularScheduleForService(RegularSchedule schedule, Service service, DataImportReport report);

    void createOrUpdateServiceTaxonomy(Set<ServiceTaxonomy> serviceTaxonomies, String providerName,
                                               Service service, DataImportReport report);

    ServiceTaxonomy persistServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String providerName, Service service,
                                                     DataImportReport report);

    void createOrUpdateRequiredDocuments(Set<RequiredDocument> requiredDocuments, String providerName,
                                         Service service, DataImportReport report);

    RequiredDocument persistRequiredDocument(RequiredDocument document, String externalDbId, String providerName,
                                             Service service, DataImportReport report);

    void createOrUpdateContactsForService(Set<Contact> contacts, Service service, DataImportReport report);

    void createOrUpdateHolidayScheduleForService(HolidaySchedule schedule, Service service, DataImportReport report);

    void createOrUpdateServiceAtLocationForService(ServiceAtLocation serviceAtLocation, String providerName,
        Service service, DataImportReport report);
}
