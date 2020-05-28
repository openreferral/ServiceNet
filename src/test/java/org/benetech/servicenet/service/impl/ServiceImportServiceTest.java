package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestDatabaseManagement;
import org.benetech.servicenet.TestPersistanceHelper;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.HolidayScheduleService;
import org.benetech.servicenet.service.LanguageService;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.PhoneService;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.benetech.servicenet.TestConstants.EXISTING_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.EXISTING_INT;
import static org.benetech.servicenet.TestConstants.EXISTING_STRING;
import static org.benetech.servicenet.TestConstants.NEW_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.NEW_INT;
import static org.benetech.servicenet.TestConstants.NEW_STRING;
import static org.benetech.servicenet.TestConstants.OTHER_INT;
import static org.benetech.servicenet.TestConstants.OTHER_STRING;
import static org.benetech.servicenet.TestConstants.PROVIDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ServiceImportServiceTest {

    private static final int THREE = 3;

    private static final int YEAR_2018 = 2018;

    private static final int YEAR_2019 = 2019;

    @Autowired
    private ServiceImportService importService;

    @Autowired
    private TestPersistanceHelper helper;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private HolidayScheduleService holidayScheduleService;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private TaxonomyService taxonomyService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private TestDatabaseManagement testDatabaseManagement;

    @Before
    public void clearDb() {
        testDatabaseManagement.clearDb();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateService() {
        Service service = helper.generateNewService();
        DataImportReport report = new DataImportReport();
        assertEquals(0, serviceService.findAll().size());

        Service srvc = importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, report);

        assertEquals(1, serviceService.findAll().size());
        assertEquals(NEW_STRING, srvc.getName());
        assertEquals(Integer.valueOf(1), report.getNumberOfCreatedServices());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateService() {
        helper.generateExistingService();
        Service newService = helper.generateNewService();
        DataImportReport report = new DataImportReport();

        assertEquals(1, serviceService.findAll().size());

        Service updated = importService.createOrUpdateService(newService,
            EXISTING_EXTERNAL_ID, PROVIDER, report);

        assertEquals(1, serviceService.findAll().size());
        assertEquals(NEW_STRING, updated.getName());
        assertEquals(Integer.valueOf(1), report.getNumberOfUpdatedServices());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateEligibility() {
        Service service = helper.generateNewService();
        Eligibility eligibility = new Eligibility().eligibility(NEW_STRING);
        service.setEligibility(eligibility);

        assertEquals(0, eligibilityService.findAll().size());

        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<EligibilityDTO> all = eligibilityService.findAll();
        assertEquals(1, all.size());
        assertNotNull(all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getEligibility());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateEligibility() {
        Service service = helper.generateExistingService();
        Eligibility existingEligibility = new Eligibility().eligibility(EXISTING_STRING).srvc(service);
        helper.persist(existingEligibility);
        helper.flushAndRefresh(service);

        Eligibility newEligibility = new Eligibility().eligibility(NEW_STRING);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setEligibility(newEligibility);

        assertEquals(1, eligibilityService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<EligibilityDTO> all = eligibilityService.findAll();
        assertEquals(1, all.size());
        assertEquals(service.getId(), all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getEligibility());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreatePhonesIfServiceHasNoneOfThem() {
        Service service = helper.generateNewService();
        Phone phone = new Phone().number(NEW_STRING);
        service.setPhones(helper.mutableSet(phone));

        assertEquals(0, phoneService.findAll().size());

        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertNotNull(all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getNumber());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplacePhonesIfServiceHasFewOfThemButNotThisOne() {
        Service service = helper.generateExistingService();
        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        helper.persist(otherPhone);
        helper.flushAndRefresh(service);

        Phone newPhone = new Phone().number(NEW_STRING);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setPhones(helper.mutableSet(newPhone));

        assertEquals(1, phoneService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertEquals(service.getId(), all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getNumber());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplacePhonesForService() {
        Service service = helper.generateExistingService();
        Phone phoneToBeUpdated = new Phone().number(EXISTING_STRING).srvc(service);
        helper.persist(phoneToBeUpdated);
        Phone otherPhone = new Phone().number(OTHER_STRING).srvc(service);
        helper.persist(otherPhone);
        helper.flushAndRefresh(service);

        Phone newPhone = new Phone().number(NEW_STRING);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setPhones(helper.mutableSet(newPhone));

        assertEquals(2, phoneService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<PhoneDTO> all = phoneService.findAll();
        assertEquals(1, all.size());
        assertEquals(service.getId(), all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getNumber());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateFundingForService() {
        Service service = helper.generateNewService();
        Funding funding = new Funding().source(NEW_STRING);
        service.setFunding(funding);

        assertEquals(0, fundingService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getSource());
        assertNotNull(all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateFundingForService() {
        Service service = helper.generateExistingService();
        Funding funding = new Funding().source(EXISTING_STRING).srvc(service);
        helper.persist(funding);
        helper.flushAndRefresh(service);

        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        Funding newFunding = new Funding().source(NEW_STRING);
        serviceToUpdate.setFunding(newFunding);

        assertEquals(1, fundingService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getSource());
        assertEquals(service.getId(), all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateOpeningHoursIfServiceHasNoneOfThem() {
        Service service = helper.generateNewService();
        OpeningHours openingHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        service.setRegularSchedule(new RegularSchedule().openingHours(helper.mutableSet(openingHours)));

        assertEquals(0, regularScheduleService.findAll().size());
        assertEquals(0, openingHoursService.findAll().size());
        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertNotNull(allSchedules.get(0).getSrvcId());
        assertEquals(1, allHours.size());
        OpeningHoursDTO hoursDTO = allHours.get(0);
        assertEquals(NEW_INT, hoursDTO.getWeekday());
        assertEquals(NEW_STRING, hoursDTO.getOpensAt());
        assertEquals(NEW_STRING, hoursDTO.getClosesAt());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceOpeningHoursIfServiceHasFewOfThemButNotThisOne() {
        Service service = helper.generateExistingService();
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT).opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        helper.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule().openingHours(helper.mutableSet(otherOpeningHours)).srvc(service);
        helper.persist(schedule);
        helper.flushAndRefresh(service);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setRegularSchedule(new RegularSchedule().openingHours(helper.mutableSet(newOpeningHours)));

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(1, openingHoursService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertEquals(service.getId(), allSchedules.get(0).getSrvcId());
        assertEquals(1, allHours.size());
        OpeningHoursDTO hoursDTO = allHours.get(0);
        assertEquals(NEW_INT, hoursDTO.getWeekday());
        assertEquals(NEW_STRING, hoursDTO.getOpensAt());
        assertEquals(NEW_STRING, hoursDTO.getClosesAt());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceOpeningHoursForService() {
        Service service = helper.generateExistingService();
        OpeningHours openingHoursToBeUpdated = new OpeningHours().weekday(EXISTING_INT)
            .opensAt(EXISTING_STRING).closesAt(EXISTING_STRING);
        helper.persist(openingHoursToBeUpdated);
        OpeningHours otherOpeningHours = new OpeningHours().weekday(OTHER_INT)
            .opensAt(OTHER_STRING).closesAt(OTHER_STRING);
        helper.persist(otherOpeningHours);
        RegularSchedule schedule = new RegularSchedule()
            .openingHours(helper.mutableSet(openingHoursToBeUpdated, otherOpeningHours))
            .srvc(service);
        helper.persist(schedule);
        helper.flushAndRefresh(service);

        OpeningHours newOpeningHours = new OpeningHours().weekday(NEW_INT).opensAt(NEW_STRING).closesAt(NEW_STRING);
        RegularSchedule newSchedule = new RegularSchedule().openingHours(helper.mutableSet(newOpeningHours));
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setRegularSchedule(newSchedule);

        assertEquals(1, regularScheduleService.findAll().size());
        assertEquals(2, openingHoursService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RegularScheduleDTO> allSchedules = regularScheduleService.findAll();
        List<OpeningHoursDTO> allHours = openingHoursService.findAll();
        assertEquals(1, allSchedules.size());
        assertEquals(service.getId(), allSchedules.get(0).getSrvcId());
        assertEquals(1, allHours.size());
        OpeningHoursDTO hoursDTO = allHours.get(0);
        assertEquals(NEW_INT, hoursDTO.getWeekday());
        assertEquals(NEW_STRING, hoursDTO.getOpensAt());
        assertEquals(NEW_STRING, hoursDTO.getClosesAt());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateServiceTaxonomy() {
        Taxonomy taxonomy = helper.generateExistingTaxonomy();
        Service service = helper.generateNewService();
        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy().taxonomy(taxonomy).srvc(service);
        service.setTaxonomies(helper.mutableSet(serviceTaxonomy));

        assertEquals(1, taxonomyService.findAll().size());
        assertEquals(0, serviceTaxonomyService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ServiceTaxonomyDTO> all = serviceTaxonomyService.findAll();
        assertEquals(1, all.size());
        assertEquals(taxonomy.getId(), all.get(0).getTaxonomyId());
        assertNotNull(all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateServiceTaxonomy() {
        Taxonomy taxonomy = helper.generateOtherTaxonomy();
        Service service = helper.generateExistingService();
        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy().taxonomy(taxonomy).srvc(service)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER);
        helper.persist(serviceTaxonomy);
        helper.flushAndRefresh(service);

        Taxonomy newTaxonomy = helper.generateNewTaxonomy();
        helper.persist(newTaxonomy);
        helper.flushAndRefresh(newTaxonomy);
        ServiceTaxonomy newServiceTaxonomy = new ServiceTaxonomy().taxonomy(newTaxonomy)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist().taxonomies(
            helper.mutableSet(newServiceTaxonomy));

        assertEquals(2, taxonomyService.findAll().size());
        assertEquals(1, serviceTaxonomyService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ServiceTaxonomyDTO> all = serviceTaxonomyService.findAll();
        assertEquals(1, all.size());
        assertNotNull(all.get(0).getTaxonomyId());
        assertEquals(service.getId(), all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateRequiredDocument() {
        Service service = helper.generateNewService();
        RequiredDocument document = new RequiredDocument().document(NEW_STRING)
            .externalDbId(NEW_EXTERNAL_ID).providerName(PROVIDER);
        service.setDocs(helper.mutableSet(document));

        assertEquals(0, requiredDocumentService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RequiredDocumentDTO> all = requiredDocumentService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getDocument());
        assertNotNull(all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateRequiredDocument() {
        Service service = helper.generateExistingService();
        RequiredDocument requiredDocument = new RequiredDocument().document(EXISTING_STRING)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER).srvc(service);
        helper.persist(requiredDocument);
        helper.flushAndRefresh(service);

        RequiredDocument newDocument = new RequiredDocument().document(NEW_STRING)
            .externalDbId(EXISTING_EXTERNAL_ID).providerName(PROVIDER);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setDocs(helper.mutableSet(newDocument));

        assertEquals(1, requiredDocumentService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<RequiredDocumentDTO> all = requiredDocumentService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getDocument());
        assertEquals(service.getId(), all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateContactsForService() {
        Service service = helper.generateNewService();
        Contact contact = new Contact().name(NEW_STRING);
        service.setContacts(helper.mutableSet(contact));

        assertEquals(0, contactService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ContactDTO> all = contactService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertNotNull(all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateContactsForService() {
        Service service = helper.generateExistingService();
        Contact contact = new Contact().name(EXISTING_STRING).srvc(service);
        helper.persist(contact);
        helper.flushAndRefresh(service);

        Contact newContact = new Contact().name(NEW_STRING);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setContacts(helper.mutableSet(newContact));

        assertEquals(1, contactService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ContactDTO> all = contactService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertEquals(service.getId(), all.get(0).getSrvcId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateHolidayScheduleForService() {
        Service service = helper.generateNewService();
        LocalDate start = LocalDate.of(YEAR_2019, 1, 1);
        LocalDate end = LocalDate.of(YEAR_2019, 1, THREE);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(start).endDate(end).providerName(PROVIDER).externalDbId(NEW_EXTERNAL_ID);
        service.setHolidaySchedules(Collections.singleton(schedule));

        assertEquals(0, holidayScheduleService.findAll().size());
        importService.createOrUpdateService(service, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        HolidayScheduleDTO scheduleDTO = all.get(0);
        assertEquals(NEW_STRING, scheduleDTO.getClosesAt());
        assertFalse(scheduleDTO.isClosed());
        assertNotNull(scheduleDTO.getSrvcId());
        assertEquals(start, scheduleDTO.getStartDate());
        assertEquals(end, scheduleDTO.getEndDate());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateHolidayScheduleForService() {
        Service service = helper.generateExistingService();
        LocalDate start = LocalDate.of(YEAR_2018, 1, 1);
        LocalDate end = LocalDate.of(YEAR_2018, 1, THREE);
        HolidaySchedule schedule = new HolidaySchedule().closesAt(EXISTING_STRING).closed(true).srvc(service)
            .startDate(start).endDate(end).providerName(PROVIDER).externalDbId(EXISTING_EXTERNAL_ID);
        helper.persist(schedule);
        helper.flushAndRefresh(service);

        LocalDate newStart = LocalDate.of(YEAR_2019, 1, 1);
        LocalDate newEnd = LocalDate.of(YEAR_2019, 1, THREE);
        HolidaySchedule newSchedule = new HolidaySchedule().closesAt(NEW_STRING).closed(false)
            .startDate(newStart).endDate(newEnd).providerName(PROVIDER).externalDbId(EXISTING_EXTERNAL_ID);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setHolidaySchedules(Collections.singleton(newSchedule));

        assertEquals(1, holidayScheduleService.findAll().size());
        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<HolidayScheduleDTO> all = holidayScheduleService.findAll();
        assertEquals(1, all.size());
        HolidayScheduleDTO scheduleDTO = all.get(0);
        assertEquals(NEW_STRING, scheduleDTO.getClosesAt());
        assertFalse(scheduleDTO.isClosed());
        assertNotNull(scheduleDTO.getSrvcId());
        assertEquals(newStart, scheduleDTO.getStartDate());
        assertEquals(newEnd, scheduleDTO.getEndDate());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateLangsIfServiceHasNoneOfThem() {
        Service service = helper.generateNewService();
        Language language = new Language().language(NEW_STRING);
        service.setLangs(helper.mutableSet(language));

        assertEquals(0, languageService.findAll().size());

        importService.createOrUpdateService(service, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertNotNull(all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceLangsIfServiceHasFewOfThemButNotThisOne() {
        Service service = helper.generateExistingService();
        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        helper.persist(otherLanguage);
        helper.flushAndRefresh(service);

        Language newLanguage = new Language().language(NEW_STRING);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setLangs(helper.mutableSet(newLanguage));

        assertEquals(1, languageService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertEquals(service.getId(), all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getLanguage());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceLangsForService() {
        Service service = helper.generateExistingService();

        Language languageToBeUpdated = new Language().language(EXISTING_STRING).srvc(service);
        helper.persist(languageToBeUpdated);

        Language otherLanguage = new Language().language(OTHER_STRING).srvc(service);
        helper.persist(otherLanguage);
        helper.flushAndRefresh(service);

        Language newLanguage = new Language().language(NEW_STRING);
        Service serviceToUpdate = helper.generateExistingServiceDoNotPersist();
        serviceToUpdate.setLangs(helper.mutableSet(newLanguage));

        assertEquals(2, languageService.findAll().size());

        importService.createOrUpdateService(serviceToUpdate, EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<LanguageDTO> all = languageService.findAll();
        assertEquals(1, all.size());
        assertEquals(service.getId(), all.get(0).getSrvcId());
        assertEquals(NEW_STRING, all.get(0).getLanguage());
    }
}
