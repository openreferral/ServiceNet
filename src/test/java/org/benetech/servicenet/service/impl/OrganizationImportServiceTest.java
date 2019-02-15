package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestPersistanceHelper;
import org.benetech.servicenet.domain.Contact;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.service.ContactService;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.OrganizationImportService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.ProgramService;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.benetech.servicenet.TestConstants.EXISTING_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.EXISTING_STRING;
import static org.benetech.servicenet.TestConstants.NEW_BOOLEAN;
import static org.benetech.servicenet.TestConstants.NEW_EXTERNAL_ID;
import static org.benetech.servicenet.TestConstants.NEW_STRING;
import static org.benetech.servicenet.TestConstants.OTHER_STRING;
import static org.benetech.servicenet.TestConstants.PROVIDER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class OrganizationImportServiceTest {
    
    @Autowired
    private OrganizationImportService importService;
    
    @Autowired
    private TestPersistanceHelper helper;
    
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ContactService contactsService;

    @Before
    public void clearDatabase() {
        contactsService.findAll().forEach(x -> contactsService.delete(x.getId()));
        organizationService.findAll().forEach(x -> organizationService.delete(x.getId()));
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateOrganization() {
        Organization organization = helper.generateNewOrganization(helper.generateExistingAccount());
        DataImportReport report = new DataImportReport();

        assertEquals(0, organizationService.findAllDTOs().size());

        importService.createOrUpdateOrganization(organization,
            EXISTING_EXTERNAL_ID, PROVIDER, report);

        List<OrganizationDTO> all = organizationService.findAllDTOs();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertEquals(Integer.valueOf(1), report.getNumberOfCreatedOrgs());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateOrganization() {
        SystemAccount account = helper.generateExistingAccount();
        Organization newOrganization = helper.generateNewOrganization(account);
        helper.generateExistingOrganization(account);
        DataImportReport report = new DataImportReport();

        assertEquals(1, organizationService.findAllDTOs().size());

        importService.createOrUpdateOrganization(newOrganization,
            EXISTING_EXTERNAL_ID, PROVIDER, report);

        List<OrganizationDTO> all = organizationService.findAllDTOs();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertEquals(NEW_BOOLEAN, all.get(0).getActive());
        assertEquals(Integer.valueOf(1), report.getNumberOfUpdatedOrgs());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateFundingForOrganization() {
        Organization organization = helper.generateNewOrganization(helper.generateExistingAccount());
        Funding funding = new Funding().source(NEW_STRING);
        organization.setFunding(funding);

        assertEquals(0, fundingService.findAll().size());
        importService.createOrUpdateOrganization(organization, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getSource());
        assertNotNull(all.get(0).getOrganizationId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldUpdateFundingForOrganization() {
        Organization organization = helper.generateExistingOrganization(helper.generateExistingAccount());
        Funding funding = new Funding().source(EXISTING_STRING).organization(organization);
        helper.persist(funding);
        helper.flushAndRefresh(organization);

        Organization organizationToUpdate = helper.generateExistingOrganizationDoNotPersist();
        Funding newFunding = new Funding().source(NEW_STRING);
        organizationToUpdate.setFunding(newFunding);

        assertEquals(1, fundingService.findAll().size());
        importService.createOrUpdateOrganization(organizationToUpdate,
            EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<FundingDTO> all = fundingService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getSource());
        assertEquals(organization.getId(), all.get(0).getOrganizationId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateProgramsIfOrganizationHasNoneOfThem() {
        Organization organization = helper.generateNewOrganization(helper.generateExistingAccount());
        Program program = new Program().name(NEW_STRING);
        organization.setPrograms(helper.mutableSet(program));

        assertEquals(0, programService.findAll().size());
        importService.createOrUpdateOrganization(organization, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ProgramDTO> all = programService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertNotNull(all.get(0).getOrganizationId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceProgramsIfOrganizationHasFewButNotThisOne() {
        Organization organization = helper.generateExistingOrganization(helper.generateExistingAccount());
        Program program = new Program().name(OTHER_STRING).organization(organization);
        helper.persist(program);
        helper.flushAndRefresh(organization);

        Program newProgram = new Program().name(NEW_STRING);
        Organization organizationToUpdate = helper.generateExistingOrganizationDoNotPersist().programs(helper.mutableSet(newProgram));

        assertEquals(1, programService.findAll().size());
        importService.createOrUpdateOrganization(organizationToUpdate,
            EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ProgramDTO> all = programService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertEquals(organization.getId(), all.get(0).getOrganizationId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldCreateContactsIfOrganizationHasNoneOfThem() {
        Organization organization = helper.generateNewOrganization(helper.generateExistingAccount());
        Contact contact = new Contact().name(NEW_STRING);
        organization.setContacts(helper.mutableSet(contact));

        assertEquals(0, contactsService.findAll().size());
        importService.createOrUpdateOrganization(organization, NEW_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ContactDTO> all = contactsService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertNotNull(all.get(0).getOrganizationId());
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void shouldReplaceContactsIfOrganizationHasFewButNotThisOne() {
        Organization organization = helper.generateExistingOrganization(helper.generateExistingAccount());
        Contact contact = new Contact().name(OTHER_STRING).organization(organization);
        helper.persist(contact);
        helper.flushAndRefresh(organization);

        Contact newContact = new Contact().name(NEW_STRING);
        Organization organizationToUpdate = helper.generateExistingOrganizationDoNotPersist().contacts(helper.mutableSet(newContact));

        assertEquals(1, contactsService.findAll().size());
        importService.createOrUpdateOrganization(organizationToUpdate,
            EXISTING_EXTERNAL_ID, PROVIDER, new DataImportReport());

        List<ContactDTO> all = contactsService.findAll();
        assertEquals(1, all.size());
        assertEquals(NEW_STRING, all.get(0).getName());
        assertEquals(organization.getId(), all.get(0).getOrganizationId());
    }
}
