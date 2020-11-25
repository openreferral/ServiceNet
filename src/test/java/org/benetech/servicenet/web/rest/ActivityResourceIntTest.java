package org.benetech.servicenet.web.rest;

import java.util.Collections;
import java.util.List;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit5Extension;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Organization_;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.scheduler.ReferenceDataGenerator;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.dto.provider.ProviderFilterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ActivityResource REST controller.
 *
 * @see org.benetech.servicenet.web.rest.ActivityResource
 */
@ExtendWith({ SpringExtension.class, ZeroCodeSpringJUnit5Extension.class })
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class ActivityResourceIntTest {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private MockMvc restActivityMockMvc;

    final static int PAGE_SIZE = 5;
    final static int ORG_COUNT = PAGE_SIZE * 2;
    static List<Organization> createdOrganizations;
    static Organization org;
    static Organization inactiveOrg;

    @BeforeAll
    public static void loadData(@Autowired ReferenceDataGenerator referenceDataGenerator,
        @Autowired OrganizationService organizationService) {
        createdOrganizations = referenceDataGenerator.createReferenceData("admin", ORG_COUNT);
        org = createdOrganizations.get(0);
        inactiveOrg = createdOrganizations.get(1);
        inactiveOrg.setActive(false);
        organizationService.save(inactiveOrg);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ActivityResource activityResource = new ActivityResource(
            activityService, userService
        );
        restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    public void getAPageOfUsersActivityRecords() throws Exception {
        restActivityMockMvc.perform(get("/api/provider-records?size=" + PAGE_SIZE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.content", hasSize(PAGE_SIZE)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getAPageOfAllActivityRecords() throws Exception {
        restActivityMockMvc.perform(post("/api/all-provider-records?size=" + PAGE_SIZE)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(new ProviderFilterDTO())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(PAGE_SIZE)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getRecordsForMap() throws Exception {
        restActivityMockMvc.perform(post("/api/all-provider-records-map?size=" + PAGE_SIZE)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(new ProviderFilterDTO())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(PAGE_SIZE)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void searchASpecificRecordFromAllActivityRecords() throws Exception {
        restActivityMockMvc.perform(post("/api/all-provider-records?search=" + org.getName())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(new ProviderFilterDTO())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[*].organization.id").value(hasItem(org.getId().toString())));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getSelectedRecord() throws Exception {
        restActivityMockMvc.perform(get("/api/select-record/" + org.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("organization.id").value(org.getId().toString()))
            .andExpect(jsonPath("locations", hasSize(org.getLocations().size())));
    }

    @Test
    @Transactional
    public void getRecordOptions() throws Exception {
        restActivityMockMvc.perform(get("/api/provider-record-options"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(ORG_COUNT - 1)))
            .andExpect(jsonPath("$.[*].id").value(hasItem(createdOrganizations.get(0).getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(createdOrganizations.get(0).getName())));
    }

    @Test
    @Transactional
    public void getActivitySuggestionForOrgName() throws Exception {
        UserProfile userProfile = userService.getCurrentUserProfile();
        userProfile.setSystemAccount(org.getAccount());
        userProfileRepository.save(userProfile);
        ActivityFilterDTO activityFilterDTO = new ActivityFilterDTO();
        activityFilterDTO.setSearchFields(Collections.singletonList(Organization_.NAME));
        restActivityMockMvc.perform(post("/api/activity-suggestions?search=" + org.getName())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(activityFilterDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("organizations.[*]").value(hasItem(org.getName())))
            .andExpect(jsonPath("services.[*]").value(containsInAnyOrder(
                org.getServices().stream().map(Service::getName).toArray())));
    }

    @Test
    @Transactional
    public void shouldGetDeactivatedRecords() throws Exception {
        restActivityMockMvc.perform(get("/api/deactivated-provider-records"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inactiveOrg.getId().toString())));
    }
}
