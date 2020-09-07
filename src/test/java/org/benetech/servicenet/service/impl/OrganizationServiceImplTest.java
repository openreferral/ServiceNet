package org.benetech.servicenet.service.impl;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.PaymentAccepted;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceArea;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceMetadata;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.mother.AccessibilityForDisabilitiesMother;
import org.benetech.servicenet.mother.ContactMother;
import org.benetech.servicenet.mother.EligibilityMother;
import org.benetech.servicenet.mother.FundingMother;
import org.benetech.servicenet.mother.HolidayScheduleMother;
import org.benetech.servicenet.mother.LocationMother;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.mother.PhoneMother;
import org.benetech.servicenet.mother.PhysicalAddressMother;
import org.benetech.servicenet.mother.PostalAddressMother;
import org.benetech.servicenet.mother.RegularScheduleMother;
import org.benetech.servicenet.mother.ServiceMother;
import org.benetech.servicenet.mother.ServiceTaxonomyMother;
import org.benetech.servicenet.repository.AccessibilityForDisabilitiesRepository;
import org.benetech.servicenet.repository.ContactRepository;
import org.benetech.servicenet.repository.EligibilityRepository;
import org.benetech.servicenet.repository.FundingRepository;
import org.benetech.servicenet.repository.HolidayScheduleRepository;
import org.benetech.servicenet.repository.LanguageRepository;
import org.benetech.servicenet.repository.LocationRepository;
import org.benetech.servicenet.repository.OpeningHoursRepository;
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
import org.benetech.servicenet.repository.TaxonomyRepository;
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
        Organization org = OrganizationMother.createDefaultAndPersist(em);

        Funding orgFunding = FundingMother.createDefault();
        orgFunding.setOrganization(org);
        em.persist(orgFunding);

        Phone orgPhone = PhoneMother.createDefault();
        orgPhone.setOrganization(org);
        em.persist(orgPhone);

        Program program = new Program().name("program").organization(org);
        em.persist(program);

        Service programService = ServiceMother.createDefault();
        programService.setProgram(program);
        em.persist(programService);

        Contact orgContact = ContactMother.createDefault();
        orgContact.setOrganization(org);
        em.persist(orgContact);

        Phone contactPhone = PhoneMother.createDefault();
        contactPhone.setContact(orgContact);
        em.persist(contactPhone);

        Location location = LocationMother.createDefault();
        location.setOrganization(org);
        em.persist(location);

        AccessibilityForDisabilities accessibilityForDisabilities = AccessibilityForDisabilitiesMother.createDefault();
        accessibilityForDisabilities.setLocation(location);
        em.persist(accessibilityForDisabilities);

        PostalAddress postalAddress = PostalAddressMother.createDefault();
        postalAddress.setLocation(location);
        em.persist(postalAddress);

        PhysicalAddress physicalAddress = PhysicalAddressMother.createDefault();
        physicalAddress.setLocation(location);
        em.persist(physicalAddress);

        createRegularSchedule(location, null);

        HolidaySchedule locationHolidaySchedule = HolidayScheduleMother.createDefault();
        locationHolidaySchedule.setLocation(location);
        em.persist(locationHolidaySchedule);

        Language locationLanguage = new Language().language("eng").location(location);
        em.persist(locationLanguage);

        Phone locationPhone = PhoneMother.createDefault();
        locationPhone.setLocation(location);
        em.persist(locationPhone);

        Service service = ServiceMother.createDefault();
        service.setOrganization(org);
        em.persist(service);

        Taxonomy taxonomy = new Taxonomy().name("taxonomy");
        em.persist(taxonomy);

        ServiceTaxonomy serviceTaxonomy = ServiceTaxonomyMother.createDefault();
        serviceTaxonomy.setTaxonomy(taxonomy);
        serviceTaxonomy.setSrvc(service);
        em.persist(serviceTaxonomy);

        ServiceAtLocation serviceAtLocation = new ServiceAtLocation().location(location).srvc(service);
        em.persist(serviceAtLocation);

        Eligibility eligibility = EligibilityMother.createDefault();
        eligibility.setSrvc(service);
        em.persist(eligibility);

        Funding serviceFunding = FundingMother.createDefault();
        serviceFunding.setSrvc(service);
        em.persist(serviceFunding);

        createRegularSchedule(null, service);

        HolidaySchedule serviceHolidaySchedule = HolidayScheduleMother.createDefault();
        serviceHolidaySchedule.setSrvc(service);
        em.persist(serviceHolidaySchedule);

        ServiceArea serviceArea = new ServiceArea().description("area").srvc(service);
        em.persist(serviceArea);

        ServiceMetadata serviceMetadata = new ServiceMetadata();
        serviceMetadata.setSrvc(service);
        serviceMetadata.setLastActionType("create");
        serviceMetadata.setUpdatedBy("admin");
        em.persist(serviceMetadata);

        RequiredDocument requiredDocument = new RequiredDocument().document("doc").srvc(service);
        em.persist(requiredDocument);

        PaymentAccepted paymentAccepted = new PaymentAccepted().payment("payment").srvc(service);
        em.persist(paymentAccepted);

        Language serviceLanguage = new Language().language("eng").srvc(service);
        em.persist(serviceLanguage);

        Phone servicePhone = PhoneMother.createDefault();
        servicePhone.setSrvc(service);
        em.persist(servicePhone);

        Contact serviceContact = ContactMother.createDefault();
        serviceContact.setSrvc(service);
        em.persist(serviceContact);

        em.flush();

        em.refresh(orgContact);
        em.refresh(program);
        em.refresh(service);
        em.refresh(location);
        em.refresh(org);

        assertEquals(1, organizationService.findAll().size());
        assertEquals(2, fundingRepository.findAll().size());
        assertEquals(4, phoneRepository.findAll().size());
        assertEquals(1, programRepository.findAll().size());
        assertEquals(2, serviceRepository.findAll().size());
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
        assertEquals(0, fundingRepository.findAll().size());
        assertEquals(0, phoneRepository.findAll().size());
        assertEquals(0, programRepository.findAll().size());
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
    }

    private RegularSchedule createRegularSchedule(Location location, Service service) {
        RegularSchedule regularSchedule = RegularScheduleMother.createWithTwoOpeningHours();
        regularSchedule.setLocation(location);
        regularSchedule.setSrvc(service);
        em.persist(regularSchedule);

        regularSchedule.getOpeningHours().forEach(openingHours -> {
            openingHours.setRegularSchedule(regularSchedule);
            em.persist(openingHours);
        });

        return regularSchedule;
    }
}
