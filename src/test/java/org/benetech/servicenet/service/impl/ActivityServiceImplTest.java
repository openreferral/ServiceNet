package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.mother.ConflictMother;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityDTO;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    private ConflictService conflictService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Autowired
    private EntityManager em;

    private Organization organization;

    private User user;

    private Conflict conflict;

    private SystemAccount systemAccount;

    @Autowired
    private RecordsService recordsService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        activityService = new ActivityServiceImpl(organizationService, conflictService, organizationMatchService,
            recordsService);
    }

    @Before
    public void initTest() {
        organization = OrganizationMother.createDefaultAndPersist(em);
        systemAccount = organization.getAccount();

        conflict = ConflictMother.createDefaultAndPersist(em);
        conflict.setResourceId(organization.getId());
        conflict.setOwner(systemAccount);
        em.persist(conflict);
        em.flush();

        user = userService.getUserWithAuthorities()
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        user.setSystemAccount(systemAccount);
        em.persist(user);
        em.flush();
    }

    @Test
    @Transactional
    public void getAllActivities() {
        List<ActivityDTO> activities = activityService.getAllOrganizationActivities(user.getSystemAccount().getId());

        assertEquals(1, activities.size());
        OrganizationDTO actualOrg = activities.get(0).getRecord().getOrganization();
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

        ActivityDTO actualAct = activities.get(0);
        assertNotNull(actualAct);
        assertEquals(1, actualAct.getRecord().getConflicts().size());
        ConflictDTO actualConflict = actualAct.getRecord().getConflicts().get(0);
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
        assertEquals(conflict.getOwner().getId(), actualConflict.getOwnerId());
        assertEquals(conflict.getOwner().getName(), actualConflict.getOwnerName());
        assertEquals(conflict.getAcceptedThisChange(), actualConflict.getAcceptedThisChange());
    }

}
