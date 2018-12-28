package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.UserService;

import org.benetech.servicenet.web.rest.errors.ExceptionTranslator;

import org.benetech.servicenet.web.rest.errors.InternalServerErrorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.benetech.servicenet.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ActivityResourceIntTest {

    private static final String DEFAULT_NAME = "account_name";

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restActivityMockMvc;

    private Organization organization;

    private User user;

    private Conflict conflict;

    private SystemAccount systemAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityResource activityResource = new ActivityResource(activityService, userService);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        systemAccount = SystemAccount.builder()
            .name(DEFAULT_NAME)
            .build();
        em.persist(systemAccount);
        em.flush();

        organization = OrganizationResourceIntTest.createEntity(em);
        organization.setAccount(systemAccount);
        em.persist(organization);
        em.flush();

        conflict = ConflictResourceIntTest.createEntity(em);
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
    public void getAllActivities() throws Exception {
        // Get all the activityList
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            // Organization
            .andExpect(jsonPath("$.[*].organization.accountName")
                .value(hasItem(systemAccount.getName())))
            .andExpect(jsonPath("$.[*].organization.id").value(hasItem(organization.getId().toString())))
            .andExpect(jsonPath("$.[*].organization.name").value(hasItem(organization.getName())))
            .andExpect(jsonPath("$.[*].organization.alternateName")
                .value(hasItem(organization.getAlternateName())))
            .andExpect(jsonPath("$.[*].organization.description").value(hasItem(organization.getDescription())))
            .andExpect(jsonPath("$.[*].organization.email").value(hasItem(organization.getEmail())))
            .andExpect(jsonPath("$.[*].organization.url").value(hasItem(organization.getUrl())))
            .andExpect(jsonPath("$.[*].organization.taxStatus").value(hasItem(organization.getTaxStatus())))
            .andExpect(jsonPath("$.[*].organization.taxId").value(hasItem(organization.getTaxId())))
            .andExpect(jsonPath("$.[*].organization.yearIncorporated")
                .value(hasItem(organization.getYearIncorporated().toString())))
            .andExpect(jsonPath("$.[*].organization.legalStatus").value(hasItem(organization.getLegalStatus())))
            .andExpect(jsonPath("$.[*].organization.active").value(hasItem(organization.isActive())))
            .andExpect(jsonPath("$.[*].organization.updatedAt")
                .value(hasItem(sameInstant(organization.getUpdatedAt()))))
            // Conflict
            .andExpect(jsonPath("$.[*].conflicts.[*].ownerId").value(hasItem(systemAccount.getId().toString())))
            .andExpect(jsonPath("$.[*].conflicts.[*].id").value(hasItem(conflict.getId().toString())))
            .andExpect(jsonPath("$.[*].conflicts.[*].currentValue").value(hasItem(conflict.getCurrentValue())))
            .andExpect(jsonPath("$.[*].conflicts.[*].currentValueDate")
                .value(hasItem(sameInstant(conflict.getCurrentValueDate()))))
            .andExpect(jsonPath("$.[*].conflicts.[*].offeredValue").value(hasItem(conflict.getOfferedValue())))
            .andExpect(jsonPath("$.[*].conflicts.[*].offeredValueDate")
                .value(hasItem(sameInstant(conflict.getOfferedValueDate()))))
            .andExpect(jsonPath("$.[*].conflicts.[*].fieldName").value(hasItem(conflict.getFieldName())))
            .andExpect(jsonPath("$.[*].conflicts.[*].entityPath").value(hasItem(conflict.getEntityPath())))
            .andExpect(jsonPath("$.[*].conflicts.[*].state").value(hasItem(conflict.getState().toString())))
            .andExpect(jsonPath("$.[*].conflicts.[*].stateDate")
                .value(hasItem(sameInstant(conflict.getStateDate()))))
            .andExpect(jsonPath("$.[*].conflicts.[*].createdDate")
                .value(hasItem(sameInstant(conflict.getCreatedDate()))))
            .andExpect(jsonPath("$.[*].conflicts.[*].resourceId")
                .value(hasItem(conflict.getResourceId().toString())));
    }
}
