package org.benetech.servicenet.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.conflict.ConflictDetectionService;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.matching.counter.OrganizationSimilarityCounter;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.repository.MatchSimilarityRepository;
import org.benetech.servicenet.repository.OrganizationMatchRepository;
import org.benetech.servicenet.service.MatchSimilarityService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
@ContextConfiguration(classes = OrganizationMatchServiceImplTest.AsyncConfiguration.class)
public class OrganizationMatchServiceImplTest {

    private static final String ORG_1 = "org 1";
    private static final String ORG_2 = "org 2";
    private static final BigDecimal COMPLETE_MATCH_RATIO = BigDecimal.valueOf(1.0f);
    private static final List<MatchSimilarityDTO> MATCHING_SIMILARITY_DTOS = new ArrayList<>();

    @Mock
    private OrganizationSimilarityCounter organizationSimilarityCounter;

    @Autowired
    private OrganizationMatchRepository organizationMatchRepository;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationMatchMapper organizationMatchMapper;

    @Autowired
    private ConflictDetectionService conflictDetectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MatchSimilarityService matchSimilarityService;

    @Autowired
    private MatchSimilarityRepository matchSimilarityRepository;

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
            organizationMatchMapper, organizationService, organizationSimilarityCounter,
            conflictDetectionService, userService, matchSimilarityService,
            matchSimilarityRepository, BigDecimal.valueOf(0.4f));
        MatchSimilarityDTO completeSimilarity = new MatchSimilarityDTO();
        completeSimilarity.setSimilarity(BigDecimal.ONE);
        MATCHING_SIMILARITY_DTOS.add(completeSimilarity);
    }

    @Test
    @Transactional
    public void shouldCreateMatchAndMirrorMatchForSimilarOrgs() {
        Organization org1 = OrganizationMother.createDefaultAndPersist(em);
        Organization org2 = OrganizationMother.createDifferentAndPersist(em);
        org1.setName(ORG_1);
        org2.setName(ORG_2);
        em.persist(org1);
        em.persist(org2);
        em.flush();

        List<MatchSimilarityDTO> similarities = new ArrayList<>();
        MatchSimilarityDTO similarity = new MatchSimilarityDTO();
        similarity.setSimilarity(BigDecimal.valueOf(1.0f));
        similarities.add(similarity);

        BigDecimal similarityRatio = BigDecimal.valueOf(1.0f);
        BigDecimal totalWeight = BigDecimal.valueOf(3.1f);
        when(organizationSimilarityCounter.countSimilarityRatio(org1, org2)).thenReturn(similarityRatio);
        when(organizationSimilarityCounter.getMatchSimilarityDTOs(org1, org2)).thenReturn(similarities);
        when(organizationSimilarityCounter.getTotalWeight(similarities)).thenReturn(totalWeight);

        int dbSize = organizationMatchService.findAll().size();
        organizationMatchService.createOrUpdateOrganizationMatches(org1);
        List<OrganizationMatchDTO> matches = organizationMatchService.findAll();

        assertEquals(dbSize + 2, matches.size());
        assertTrue(matches.stream().anyMatch(m -> m.getOrganizationRecordName().equals(ORG_1)
            && m.getPartnerVersionName().equals(ORG_2)));
        assertTrue(matches.stream().anyMatch(m -> m.getOrganizationRecordName().equals(ORG_2)
            && m.getPartnerVersionName().equals(ORG_1)));
        assertTrue(matches.stream().anyMatch(m -> m.getSimilarity().equals(similarityRatio.divide(totalWeight, 2, RoundingMode.FLOOR))));
    }

    @Test
    @Transactional
    public void shouldNotCreateMatchesForSimiliarOrgsIfAlreadyExists() {
        Organization org1 = OrganizationMother.createDefaultAndPersist(em);
        org1.setName(ORG_1);
        org1.setActive(true);
        Organization org2 = OrganizationMother.createSimiliarAndPersist(em);
        org2.setName(ORG_2);
        org2.setActive(true);
        em.persist(org1);
        em.persist(org2);
        em.persist(new OrganizationMatch().organizationRecord(org1).partnerVersion(org2));
        em.persist(new OrganizationMatch().organizationRecord(org2).partnerVersion(org1));
        em.flush();

        when(organizationSimilarityCounter.countSimilarityRatio(org1, org2)).thenReturn(COMPLETE_MATCH_RATIO);
        when(organizationSimilarityCounter.getMatchSimilarityDTOs(org1, org2)).thenReturn(MATCHING_SIMILARITY_DTOS);
        when(organizationSimilarityCounter.getTotalWeight(any())).thenReturn(BigDecimal.valueOf(3.1f));

        int dbSize = organizationMatchService.findAll().size();
        organizationMatchService.createOrUpdateOrganizationMatches(org1);

        assertEquals(dbSize, organizationMatchService.findAll().size());
    }

    @Test
    @Transactional
    public void shouldNotCreateMatchesForDifferentOrgs() {
        Organization org1 = OrganizationMother.createDefaultAndPersist(em);
        org1.setName(ORG_1);
        org1.setActive(true);
        Organization org2 = OrganizationMother.createDifferentAndPersist(em);
        org2.setName(ORG_2);
        org2.setActive(true);
        em.persist(org1);
        em.persist(org2);
        em.flush();

        BigDecimal ratioBelowThreshold = BigDecimal.valueOf(0.2f);
        when(organizationSimilarityCounter.countSimilarityRatio(org1, org2)).thenReturn(ratioBelowThreshold);

        organizationMatchService.createOrUpdateOrganizationMatches(org1);

        assertEquals(0, organizationMatchService.findAll().size());
    }
}
