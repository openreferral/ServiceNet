package org.benetech.servicenet;

import org.benetech.servicenet.repository.AccessibilityForDisabilitiesRepository;
import org.benetech.servicenet.repository.ContactRepository;
import org.benetech.servicenet.repository.EligibilityRepository;
import org.benetech.servicenet.repository.HolidayScheduleRepository;
import org.benetech.servicenet.repository.LanguageRepository;
import org.benetech.servicenet.repository.LocationRepository;
import org.benetech.servicenet.repository.OpeningHoursRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.repository.PhoneRepository;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.repository.PostalAddressRepository;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.repository.RequiredDocumentRepository;
import org.benetech.servicenet.repository.ServiceAtLocationRepository;
import org.benetech.servicenet.repository.ServiceRepository;
import org.benetech.servicenet.repository.ServiceTaxonomyRepository;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDatabaseManagement {

    @Autowired
    private EligibilityRepository eligibilityRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PhysicalAddressRepository physicalAddressRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Autowired
    private OpeningHoursRepository openingHoursRepository;

    @Autowired
    private HolidayScheduleRepository holidayScheduleRepository;

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private ServiceTaxonomyRepository serviceTaxonomyRepository;

    @Autowired
    private AccessibilityForDisabilitiesRepository accessibilityForDisabilitiesRepository;

    @Autowired
    private TaxonomyRepository taxonomyRepository;

    @Autowired
    private ServiceAtLocationRepository serviceAtLocationRepository;

    public void clearDb() {
        contactRepository.deleteAll();
        requiredDocumentRepository.deleteAll();
        openingHoursRepository.deleteAll();
        regularScheduleRepository.deleteAll();
        holidayScheduleRepository.deleteAll();
        languageRepository.deleteAll();
        eligibilityRepository.deleteAll();
        phoneRepository.deleteAll();
        postalAddressRepository.deleteAll();
        physicalAddressRepository.deleteAll();
        serviceTaxonomyRepository.deleteAll();
        taxonomyRepository.deleteAll();
        serviceAtLocationRepository.deleteAll();
        serviceRepository.deleteAll();
        accessibilityForDisabilitiesRepository.deleteAll();
        locationRepository.deleteAll();
        organizationRepository.deleteAll();
    }
}
