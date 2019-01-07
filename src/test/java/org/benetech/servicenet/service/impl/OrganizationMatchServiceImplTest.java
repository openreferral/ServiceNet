package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.matching.OrganizationSimilarityCounter;
import org.benetech.servicenet.repository.OrganizationMatchRepository;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.benetech.servicenet.service.mapper.OrganizationMatchMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@ContextConfiguration(classes = OrganizationMatchServiceImplTest.AsyncConfiguration.class)
public class OrganizationMatchServiceImplTest {

    private static final String ORG_1 = "org 1";
    private static final String ORG_2 = "org 2";
    private static final float COMPLETE_MATCH_RATIO = 1.0f;

    @Mock
    private OrganizationSimilarityCounter organizationSimilarityCounter;

    @Autowired
    private OrganizationMatchRepository organizationMatchRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMatchMapper organizationMatchMapper;

    private OrganizationMatchService organizationMatchService;

    @Autowired
    private EntityManager em;

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

        organizationMatchService = new OrganizationMatchServiceImpl(organizationMatchRepository,
            organizationMatchMapper, organizationService, organizationSimilarityCounter, 0.4f);
    }

    @Test
    @Transactional
    public void shouldCreateMatchAndMirrorMatchForSimilarOrgs() {
        Organization org1 = new Organization().name(ORG_1).active(true);
        Organization org2 = new Organization().name(ORG_2).active(true);
        em.persist(org1);
        em.persist(org2);
        em.flush();

        when(organizationSimilarityCounter.countSimilarityRatio(org1, org2)).thenReturn(1.0f);

        int dbSize = organizationMatchService.findAll().size();
        organizationMatchService.createOrUpdateOrganizationMatches(org1);
        List<OrganizationMatchDTO> matches = organizationMatchService.findAll();

        assertEquals(dbSize + 2, matches.size());
        assertTrue(matches.stream().anyMatch(m -> m.getOrganizationRecordName().equals(ORG_1)
            && m.getPartnerVersionName().equals(ORG_2)));
        assertTrue(matches.stream().anyMatch(m -> m.getOrganizationRecordName().equals(ORG_2)
            && m.getPartnerVersionName().equals(ORG_1)));
    }

    @Test
    @Transactional
    public void shouldNotCreateMatchesForSimiliarOrgsIfAlreadyExists() {
        Organization org1 = new Organization().name(ORG_1).active(true);
        Organization org2 = new Organization().name(ORG_2).active(true);
        em.persist(org1);
        em.persist(org2);
        em.persist(new OrganizationMatch().organizationRecord(org1).partnerVersion(org2));
        em.persist(new OrganizationMatch().organizationRecord(org2).partnerVersion(org1));
        em.flush();

        when(organizationSimilarityCounter.countSimilarityRatio(org1, org2)).thenReturn(COMPLETE_MATCH_RATIO);

        int dbSize = organizationMatchService.findAll().size();
        organizationMatchService.createOrUpdateOrganizationMatches(org1);

        assertEquals(dbSize, organizationMatchService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotCreateMatchesForDifferentOrgs() {
        Organization org1 = new Organization().name(ORG_1).active(true);
        Organization org2 = new Organization().name(ORG_2).active(true);
        em.persist(org1);
        em.persist(org2);
        em.flush();

        float ratioBelowThreshold = 0.2f;
        when(organizationSimilarityCounter.countSimilarityRatio(org1, org2)).thenReturn(ratioBelowThreshold);

        organizationMatchService.createOrUpdateOrganizationMatches(org1);

        assertEquals(0, organizationMatchService.findAll().size());
    }
}
