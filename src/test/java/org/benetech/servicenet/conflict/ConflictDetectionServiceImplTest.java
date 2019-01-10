package org.benetech.servicenet.conflict;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.conflict.detector.OrganizationConflictDetector;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.repository.ConflictRepository;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@ContextConfiguration(classes = ConflictDetectionServiceImplTest.AsyncConfiguration.class)
public class ConflictDetectionServiceImplTest {

    @Autowired
    private ConflictDetectionServiceImpl conflictDetectionService;

    @Autowired
    private EntityManager em;

    @Autowired
    private OrganizationConflictDetector organizationConflictDetector;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ConflictRepository conflictRepository;

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

        conflictDetectionService = new ConflictDetectionServiceImpl(em, organizationConflictDetector,
            organizationRepository);
    }

    @Test
    @Transactional
    public void shouldFindAllOrganizationConflicts() {
        Organization org1 = OrganizationMother.createDefault();
        SystemAccount sys1 = SystemAccountMother.createDefault();
        org1.setAccount(sys1);
        Organization org2 = OrganizationMother.createDifferent();
        SystemAccount sys2 = SystemAccountMother.createDifferent();
        org2.setAccount(sys2);
        em.persist(sys1);
        em.persist(sys2);
        em.persist(org1);
        em.persist(org2);
        em.flush();

        int dbSize = conflictRepository.findAll().size();
        conflictDetectionService.detect(org1.getId(), org2.getId());

        assertEquals(dbSize + 10, conflictRepository.findAll().size());
    }

}
