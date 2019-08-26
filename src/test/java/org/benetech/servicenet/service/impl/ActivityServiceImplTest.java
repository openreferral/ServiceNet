package org.benetech.servicenet.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.mother.ConflictMother;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.repository.ActivityRepository;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.dto.ActivityRecordDTO;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.web.rest.ActivityResource;
import org.benetech.servicenet.web.rest.errors.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ActivityServiceImplTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ExclusionsConfigService exclusionsConfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager em;

    private Organization organization;

    private Organization organizationOther;

    private User user;

    private Conflict conflict;

    @Autowired
    private RecordsService recordsService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        activityService = new ActivityServiceImpl(activityRepository, recordsService, exclusionsConfigService);
    }

    @Before
    public void initTest() {
        organization = OrganizationMother.createDefaultAndPersist(em);
        organizationOther = OrganizationMother.createDifferentAndPersist(em);
        SystemAccount systemAccount = organization.getAccount();

        OrganizationMatch orgMatch = new OrganizationMatch()
            .organizationRecord(organization)
            .partnerVersion(organizationOther)
            .hidden(false);
        em.persist(orgMatch);
        em.flush();

        conflict = ConflictMother.createDefaultAndPersist(em);
        conflict.setResourceId(organization.getId());
        conflict.setOwner(systemAccount);
        conflict.setPartner(SystemAccountMother.createDifferentAndPersist(em));
        em.persist(conflict);
        em.flush();

        user = userService.getUserWithAuthoritiesAndAccount()
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        user.setSystemAccount(systemAccount);
        em.persist(user);
        em.flush();
    }

    @Test
    @Transactional
    public void getAllNotHiddenActivities() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<ActivityDTO> activities = activityService.getAllOrganizationActivities(
            pageRequest, user.getSystemAccount().getId(), "", new ActivityFilterDTO());

        assertEquals(1, activities.getTotalElements());
        ActivityDTO actualAct = activities.stream().collect(Collectors.toList()).get(0);

        assertEquals(organization.getId(), actualAct.getOrganizationId());
        assertEquals(organization.getName(), actualAct.getOrganizationName());

        assertNotNull(actualAct);
        assertEquals(1, actualAct.getConflicts().size());
        ConflictDTO actualConflict = actualAct.getConflicts().get(0);
        assertEquals(conflict.getId(), actualConflict.getId());
        assertEquals(conflict.getCurrentValue(), actualConflict.getCurrentValue());
        assertEquals(conflict.getCurrentValueDate(), actualConflict.getCurrentValueDate());
        assertEquals(conflict.getOfferedValue(), actualConflict.getOfferedValue());
        assertEquals(conflict.getOfferedValueDate(), actualConflict.getOfferedValueDate());
        assertEquals(conflict.getFieldName(), actualConflict.getFieldName());
        assertEquals(conflict.getEntityPath(), actualConflict.getEntityPath());
        assertEquals(conflict.getState(), actualConflict.getState());
        assertEquals(conflict.getStateDate(), actualConflict.getStateDate());
        assertEquals(conflict.getCreatedDate(), actualConflict.getCreatedDate());
        assertEquals(conflict.getResourceId(), actualConflict.getResourceId());
        assertEquals(conflict.getPartnerResourceId(), actualConflict.getPartnerResourceId());
        assertEquals(conflict.getOwner().getId(), actualConflict.getOwnerId());
        assertEquals(conflict.getOwner().getName(), actualConflict.getOwnerName());
        assertEquals(conflict.getPartner().getId(), actualConflict.getPartnerId());
        assertEquals(conflict.getPartner().getName(), actualConflict.getPartnerName());
    }

    @Test
    @Transactional
    public void getAllHiddenActivities() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        ActivityFilterDTO activityFilterDTO = new ActivityFilterDTO();
        activityFilterDTO.setHiddenFilter(true);

        Page<ActivityDTO> activities = activityService.getAllOrganizationActivities(
            pageRequest, user.getSystemAccount().getId(), "", activityFilterDTO);

        assertEquals(0, activities.getTotalElements());
    }

    @Test
    @Transactional
    public void getActivityByOrganizationId() {
        Optional<ActivityRecordDTO> activityOpt = activityService.getOneByOrganizationId(organization.getId());

        assertTrue(activityOpt.isPresent());

        ActivityRecordDTO actualAct = activityOpt.get();
        OrganizationDTO actualOrg = actualAct.getOrganization();
        assertNotNull(actualOrg);
        assertEquals(organization.getAccount().getName(), actualOrg.getAccountName());
        assertEquals(organization.getId(), actualOrg.getId());
        assertEquals(organization.getName(), actualOrg.getName());
        assertEquals(organization.getAlternateName(), actualOrg.getAlternateName());
        assertEquals(organization.getDescription(), actualOrg.getDescription());
        assertEquals(organization.getEmail(), actualOrg.getEmail());
        assertEquals(organization.getUrl(), actualOrg.getUrl());
        assertEquals(organization.getTaxStatus(), actualOrg.getTaxStatus());
        assertEquals(organization.getTaxId(), actualOrg.getTaxId());
        assertEquals(organization.getYearIncorporated(), actualOrg.getYearIncorporated());
        assertEquals(organization.getLegalStatus(), actualOrg.getLegalStatus());
        assertEquals(organization.getActive(), actualOrg.isActive());
        assertEquals(organization.getUpdatedAt(), actualOrg.getUpdatedAt());

        assertNotNull(actualAct);
        assertEquals(1, actualAct.getConflicts().size());
        ConflictDTO actualConflict = actualAct.getConflicts().get(0);
        assertEquals(conflict.getId(), actualConflict.getId());
        assertEquals(conflict.getCurrentValue(), actualConflict.getCurrentValue());
        assertEquals(conflict.getCurrentValueDate(), actualConflict.getCurrentValueDate());
        assertEquals(conflict.getOfferedValue(), actualConflict.getOfferedValue());
        assertEquals(conflict.getOfferedValueDate(), actualConflict.getOfferedValueDate());
        assertEquals(conflict.getFieldName(), actualConflict.getFieldName());
        assertEquals(conflict.getEntityPath(), actualConflict.getEntityPath());
        assertEquals(conflict.getState(), actualConflict.getState());
        assertEquals(conflict.getStateDate(), actualConflict.getStateDate());
        assertEquals(conflict.getCreatedDate(), actualConflict.getCreatedDate());
        assertEquals(conflict.getResourceId(), actualConflict.getResourceId());
        assertEquals(conflict.getPartnerResourceId(), actualConflict.getPartnerResourceId());
        assertEquals(conflict.getOwner().getId(), actualConflict.getOwnerId());
        assertEquals(conflict.getOwner().getName(), actualConflict.getOwnerName());
        assertEquals(conflict.getPartner().getId(), actualConflict.getPartnerId());
        assertEquals(conflict.getPartner().getName(), actualConflict.getPartnerName());
    }
}
