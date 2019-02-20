package org.benetech.servicenet.service;

import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;

import java.util.Optional;
import java.util.Set;

public interface ServiceBasedImportService {

    void createOrUpdateEligibility(Eligibility eligibility, Service service);

    void createOrUpdateLangsForService(Set<Language> langs, Service service);

    void createOrUpdatePhonesForService(Set<Phone> phones, Service service);

    void createOrUpdateFundingForService(Funding funding, Service service);

    void createOrUpdateRegularScheduleForService(RegularSchedule schedule, Service service);

    void createOrUpdateServiceTaxonomy(Set<ServiceTaxonomy> serviceTaxonomies, String providerName,
                                               Service service);

    Optional<ServiceTaxonomy> persistServiceTaxonomy(ServiceTaxonomy serviceTaxonomy, String providerName, Service service);

    void createOrUpdateRequiredDocuments(Set<RequiredDocument> requiredDocuments, String providerName, Service service);

    RequiredDocument persistRequiredDocument(RequiredDocument document, String externalDbId, String providerName,
                                             Service service);

    void createOrUpdateContactsForService(Set<Contact> contacts, Service service);

    void createOrUpdateHolidayScheduleForService(HolidaySchedule schedule, Service service);
}
