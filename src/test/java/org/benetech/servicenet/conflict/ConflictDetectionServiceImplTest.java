package org.benetech.servicenet.conflict;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.matching.service.impl.OrganizationEquivalentsService;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.repository.ConflictRepository;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.MetadataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@ContextConfiguration(classes = ConflictDetectionServiceImplTest.AsyncConfiguration.class)
public class ConflictDetectionServiceImplTest {

    private static final int NUM_OF_CONFL = 9;
    private static final int NUM_OF_MIRROR_CONFL = 9;

    @Autowired
    private ConflictDetectionService conflictDetectionService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private EntityManager em;

    @Autowired
    private OrganizationEquivalentsService organizationEquivalentsService;

    @Autowired
    private ConflictRepository conflictRepository;

    @Autowired
    private ConflictService conflictService;

    @Configuration
    static class AsyncConfiguration {
        @Bean(name = "taskExecutor")
        @Primary
        public TaskExecutor taskExecutor() {
            return new SyncTaskExecutor();
        }
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        conflictDetectionService = new ConflictDetectionServiceImpl(context, em,
            organizationEquivalentsService, conflictService, metadataService);
    }

    @Test
    @Transactional
    public void shouldFindAllOrganizationConflictsWithoutDuplicates() {
        Organization org1 = OrganizationMother.createDefaultAndPersist(em);
        Organization org2 = getConflictingOrganization();
        em.flush();
        OrganizationMatch match = createMatch(org1, org2);
        OrganizationMatch match2 = createMatch(org2, org1);

        int dbSize = conflictRepository.findAll().size();

        conflictDetectionService.detect(org1, Arrays.asList(match, match2));

        assertEquals(dbSize + NUM_OF_CONFL + NUM_OF_MIRROR_CONFL, conflictRepository.findAll().size());
    }

    @Test
    @Transactional
    public void shouldCreateMirrorConflict() {
        Organization org = OrganizationMother.createDefaultAndPersist(em);
        Organization theSameOrg = OrganizationMother.createDefaultAndPersist(em);
        theSameOrg.setEmail("user@example.example");
        em.flush();
        OrganizationMatch match = createMatch(org, theSameOrg);
        OrganizationMatch match2 = createMatch(theSameOrg, org);

        int dbSize = conflictRepository.findAll().size();
        int numberOfConflicts = 1;
        int numberOfMirrorConflicts = 1;

        conflictDetectionService.detect(org, Arrays.asList(match, match2));

        assertEquals(dbSize + numberOfConflicts + numberOfMirrorConflicts, conflictRepository.findAll().size());
        assertEquals(conflictRepository.findAll().get(0).getOfferedValue(),
            conflictRepository.findAll().get(1).getCurrentValue());
    }

    @Test
    @Transactional
    public void shouldCreateConflictWithDates() {
        // Arrange
        // Organization with one metadata of type ALL_FIELDS
        Organization org = OrganizationMother.createDefaultAndPersist(em);
        // Organization with metadata of type ALL_FIELDS and with email changed one
        Organization theSameOrg = OrganizationMother.createDefaultAndPersist(em);
        theSameOrg.setEmail("user@example.example");
        em.persist(theSameOrg);
        em.flush();

        OrganizationMatch match = createMatch(org, theSameOrg);
        OrganizationMatch match2 = createMatch(theSameOrg, org);

        // Act
        conflictDetectionService.detect(org, Arrays.asList(match, match2));

        // Assert
        assertFalse(CollectionUtils.isEmpty(conflictRepository.findAllPendingWithResourceId(org.getId())));
        assertFalse(CollectionUtils.isEmpty(conflictRepository.findAllPendingWithResourceId(theSameOrg.getId())));

        Conflict conflictOrg = conflictRepository.findAllPendingWithResourceId(org.getId()).get(0);
        Conflict conflictTheSameOrg = conflictRepository.findAllPendingWithResourceId(theSameOrg.getId()).get(0);
        assertEquals(conflictTheSameOrg.getOfferedValueDate(), conflictOrg.getCurrentValueDate());
        assertEquals(conflictOrg.getOfferedValueDate(), conflictTheSameOrg.getCurrentValueDate());
    }

    private static OrganizationMatch createMatch(Organization org, Organization org2) {
        return new OrganizationMatch()
            .organizationRecord(org)
            .partnerVersion(org2)
            .timestamp(ZonedDateTime.now())
            .dismissed(false);
    }

    private Organization getConflictingOrganization() {
        SystemAccount account = SystemAccountMother.createDefaultAndPersist(em);
        Organization org = OrganizationMother.createDifferent();
        org.setAccount(account);
        em.persist(org);
        return org;
    }

}
