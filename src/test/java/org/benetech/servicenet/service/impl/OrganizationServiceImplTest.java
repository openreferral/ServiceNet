package org.benetech.servicenet.service.impl;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.repository.AccessibilityForDisabilitiesRepository;
import org.benetech.servicenet.repository.ContactRepository;
import org.benetech.servicenet.repository.DailyUpdateRepository;
import org.benetech.servicenet.repository.EligibilityRepository;
import org.benetech.servicenet.repository.FundingRepository;
import org.benetech.servicenet.repository.HolidayScheduleRepository;
import org.benetech.servicenet.repository.LanguageRepository;
import org.benetech.servicenet.repository.LocationRepository;
import org.benetech.servicenet.repository.OpeningHoursRepository;
import org.benetech.servicenet.repository.OrganizationErrorRepository;
import org.benetech.servicenet.repository.PaymentAcceptedRepository;
import org.benetech.servicenet.repository.PhoneRepository;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.repository.PostalAddressRepository;
import org.benetech.servicenet.repository.ProgramRepository;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.repository.RequiredDocumentRepository;
import org.benetech.servicenet.repository.ServiceAreaRepository;
import org.benetech.servicenet.repository.ServiceAtLocationRepository;
import org.benetech.servicenet.repository.ServiceMetadataRepository;
import org.benetech.servicenet.repository.ServiceRepository;
import org.benetech.servicenet.repository.ServiceTaxonomyRepository;
import org.benetech.servicenet.repository.SiloRepository;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.OrganizationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class OrganizationServiceImplTest {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private FundingRepository fundingRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AccessibilityForDisabilitiesRepository accessibilityForDisabilitiesRepository;

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private PhysicalAddressRepository physicalAddressRepository;

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Autowired
    private OpeningHoursRepository openingHoursRepository;

    @Autowired
    private HolidayScheduleRepository holidayScheduleRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TaxonomyRepository taxonomyRepository;

    @Autowired
    private ServiceTaxonomyRepository serviceTaxonomyRepository;

    @Autowired
    private ServiceAtLocationRepository serviceAtLocationRepository;

    @Autowired
    private EligibilityRepository eligibilityRepository;

    @Autowired
    private ServiceAreaRepository serviceAreaRepository;

    @Autowired
    private ServiceMetadataRepository serviceMetadataRepository;

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private PaymentAcceptedRepository paymentAcceptedRepository;

    @Autowired
    private DailyUpdateRepository dailyUpdateRepository;

    @Autowired
    private OrganizationErrorRepository organizationErrorRepository;

    @Autowired
    private SiloRepository siloRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void setUp() {
        testDatabaseManagement.clearDb();
    }

    @SuppressWarnings({"PMD.ExcessiveMethodLength","PMD.NcssMethodCount"})
    @Test
    @Transactional
    public void shouldDeleteOrganizationAndAllRelatedEntities() {
        Organization org = OrganizationMother.createDefaultWithAllRelationsAndPersist(em);
        int siloCount = siloRepository.findAll().size();
        int userProfileCount = userProfileRepository.findAll().size();
        int systemAccountCount = systemAccountRepository.findAll().size();

        assertEquals(1, organizationService.findAll().size());
        assertEquals(1, dailyUpdateRepository.findAll().size());
        assertEquals(1, organizationErrorRepository.findAll().size());
        assertEquals(2, fundingRepository.findAll().size());
        assertEquals(5, phoneRepository.findAll().size());
        assertEquals(2, programRepository.findAll().size());
        assertEquals(1, serviceRepository.findAll().size());
        assertEquals(2, contactRepository.findAll().size());
        assertEquals(1, locationRepository.findAll().size());
        assertEquals(1, accessibilityForDisabilitiesRepository.findAll().size());
        assertEquals(1, physicalAddressRepository.findAll().size());
        assertEquals(1, postalAddressRepository.findAll().size());
        assertEquals(2, regularScheduleRepository.findAll().size());
        assertEquals(4, openingHoursRepository.findAll().size());
        assertEquals(2, holidayScheduleRepository.findAll().size());
        assertEquals(2, languageRepository.findAll().size());
        assertEquals(1, serviceAtLocationRepository.findAll().size());
        assertEquals(1, eligibilityRepository.findAll().size());
        assertEquals(1, serviceAreaRepository.findAll().size());
        assertEquals(1, serviceMetadataRepository.findAll().size());
        assertEquals(1, requiredDocumentRepository.findAll().size());
        assertEquals(1, paymentAcceptedRepository.findAll().size());

        assertEquals(1, serviceTaxonomyRepository.findAll().size());
        assertEquals(1, taxonomyRepository.findAll().size());

        organizationService.delete(org.getId());

        assertEquals(0, organizationService.findAll().size());
        assertEquals(0, dailyUpdateRepository.findAll().size());
        assertEquals(0, organizationErrorRepository.findAll().size());
        assertEquals(0, fundingRepository.findAll().size());
        assertEquals(0, phoneRepository.findAll().size());
        assertEquals(0, serviceRepository.findAll().size());
        assertEquals(0, contactRepository.findAll().size());
        assertEquals(0, locationRepository.findAll().size());
        assertEquals(0, accessibilityForDisabilitiesRepository.findAll().size());
        assertEquals(0, physicalAddressRepository.findAll().size());
        assertEquals(0, postalAddressRepository.findAll().size());
        assertEquals(0, regularScheduleRepository.findAll().size());
        assertEquals(0, openingHoursRepository.findAll().size());
        assertEquals(0, holidayScheduleRepository.findAll().size());
        assertEquals(0, languageRepository.findAll().size());
        assertEquals(0, serviceAtLocationRepository.findAll().size());
        assertEquals(0, eligibilityRepository.findAll().size());
        assertEquals(0, serviceAreaRepository.findAll().size());
        assertEquals(0, serviceMetadataRepository.findAll().size());
        assertEquals(0, requiredDocumentRepository.findAll().size());
        assertEquals(0, paymentAcceptedRepository.findAll().size());
        assertEquals(0, serviceTaxonomyRepository.findAll().size());

        assertEquals(1, taxonomyRepository.findAll().size());
        assertEquals(1, programRepository.findAll().size());
        assertEquals(siloCount, siloRepository.findAll().size());
        assertEquals(userProfileCount, userProfileRepository.findAll().size());
        assertEquals(systemAccountCount, systemAccountRepository.findAll().size());
    }
}
