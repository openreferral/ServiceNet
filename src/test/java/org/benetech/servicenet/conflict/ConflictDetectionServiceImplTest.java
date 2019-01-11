package org.benetech.servicenet.conflict;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.matching.service.impl.OrganizationEquivalentsService;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.repository.ConflictRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.service.ConflictService;
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

import javax.persistence.EntityManager;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@ContextConfiguration(classes = ConflictDetectionServiceImplTest.AsyncConfiguration.class)
public class ConflictDetectionServiceImplTest {

    @Autowired
    private ConflictDetectionService conflictDetectionService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EntityManager em;

    @Autowired
    private OrganizationRepository organizationRepository;

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
            organizationRepository, organizationEquivalentsService, conflictService);
    }

    @Test
    @Transactional
    public void shouldFindAllOrganizationConflicts() {
        Organization org1 = OrganizationMother.createDefaultAndPersist(em);
        Organization org2 = OrganizationMother.createDifferentAndPersist(em);
        em.flush();
        OrganizationMatch match = new OrganizationMatch()
            .organizationRecord(org1)
            .partnerVersion(org2)
            .timestamp(ZonedDateTime.now())
            .deleted(false);

        int dbSize = conflictRepository.findAll().size();
        int numberOfConflicts = 10;
        int numberOfMirrorConflicts = 10;

        conflictDetectionService.detect(Collections.singletonList(match));

        assertEquals(dbSize + numberOfConflicts + numberOfMirrorConflicts, conflictRepository.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotDuplicateConflicts() {
        Organization org1 = OrganizationMother.createDefaultAndPersist(em);
        Organization org2 = OrganizationMother.createDifferentAndPersist(em);
        em.flush();
        OrganizationMatch match = new OrganizationMatch()
            .organizationRecord(org1)
            .partnerVersion(org2)
            .timestamp(ZonedDateTime.now())
            .deleted(false);

        int dbSize = conflictRepository.findAll().size();
        int numberOfConflicts = 10;
        int numberOfMirrorConflicts = 10;

        conflictDetectionService.detect(Collections.singletonList(match));
        conflictDetectionService.detect(Collections.singletonList(match));

        assertEquals(dbSize + numberOfConflicts + numberOfMirrorConflicts, conflictRepository.findAll().size());
    }

    @Test
    @Transactional
    public void shouldAddToAcceptedThisChangeInsteadOfDuplicatingConflict() {
        Organization org = OrganizationMother.createDefaultAndPersist(em);
        Organization theSameOrg = OrganizationMother.createDefaultAndPersist(em);
        theSameOrg.setEmail("user@example.com");
        em.flush();
        OrganizationMatch match = new OrganizationMatch()
            .organizationRecord(org)
            .partnerVersion(theSameOrg)
            .timestamp(ZonedDateTime.now())
            .deleted(false);

        int dbSize = conflictRepository.findAll().size();
        int numberOfConflicts = 1;
        int numberOfMirrorConflicts = 1;

        conflictDetectionService.detect(Collections.singletonList(match));
        conflictDetectionService.detect(Collections.singletonList(match));

        assertEquals(dbSize + numberOfConflicts + numberOfMirrorConflicts, conflictRepository.findAll().size());
        assertEquals(conflictRepository.findAll().get(0).getAcceptedThisChanges().size(), 1);
    }

    @Test
    @Transactional
    public void shouldCreateMirrorConflictConflict() {
        Organization org = OrganizationMother.createDefaultAndPersist(em);
        Organization theSameOrg = OrganizationMother.createDefaultAndPersist(em);
        theSameOrg.setEmail("user@example.com");
        em.flush();
        OrganizationMatch match = new OrganizationMatch()
            .organizationRecord(org)
            .partnerVersion(theSameOrg)
            .timestamp(ZonedDateTime.now())
            .deleted(false);

        int dbSize = conflictRepository.findAll().size();
        int numberOfConflicts = 1;
        int numberOfMirrorConflicts = 1;

        conflictDetectionService.detect(Collections.singletonList(match));

        assertEquals(dbSize + numberOfConflicts + numberOfMirrorConflicts, conflictRepository.findAll().size());
        assertEquals(conflictRepository.findAll().get(0).getOfferedValue(),
            conflictRepository.findAll().get(1).getCurrentValue());
    }

}
