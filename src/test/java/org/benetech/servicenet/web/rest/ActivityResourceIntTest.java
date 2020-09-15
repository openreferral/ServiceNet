package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit4Runner;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.scheduler.ReferenceDataGenerator;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ActivityResource REST controller.
 *
 * @see org.benetech.servicenet.web.rest.ActivityResource
 */
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class ActivityResourceIntTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ReferenceDataGenerator referenceDataGenerator;

    private MockMvc restActivityMockMvc;

    final static int PAGE_SIZE = 20;
    final static int ORG_COUNT = PAGE_SIZE;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ActivityResource activityResource = new ActivityResource(
            activityService, userService
        );
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void loadData() {
        UserProfile userProfile = userService.getCurrentUserProfile();
        referenceDataGenerator.createReferenceData(userProfile.getLogin(), ORG_COUNT);
    }

    @Test
    @Transactional
    public void getAPageOfUsersActivityRecords() throws Exception {
        restActivityMockMvc.perform(get("/api/provider-records?size=" + PAGE_SIZE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.content", hasSize(PAGE_SIZE)));
    }
}
