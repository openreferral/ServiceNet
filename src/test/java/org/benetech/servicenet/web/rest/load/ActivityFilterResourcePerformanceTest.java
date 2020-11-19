package org.benetech.servicenet.web.rest.load;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit5Extension;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.repository.SiloRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.web.rest.ActivityResource;
import org.benetech.servicenet.web.rest.ActivityFilterResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@ExtendWith({ SpringExtension.class, ZeroCodeSpringJUnit5Extension.class })
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ActivityFilterResourcePerformanceTest {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restActivityMockMvc;

    @Autowired
    private ActivityFilterResource activityFilterResource;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private SiloRepository siloRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityFilterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
        new LoadTestUtils(userProfileRepository, siloRepository).setTestSilo("user");
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getPostalCodes() throws Exception {
        restActivityMockMvc.perform(get("/api/activity-filter/service-providers/get-postal-codes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getRegions() throws Exception {
        restActivityMockMvc.perform(get("/api/activity-filter/service-providers/get-regions"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getCities() throws Exception {
        restActivityMockMvc.perform(get("/api/activity-filter/service-providers/get-cities"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getTaxonomies() throws Exception {
        restActivityMockMvc.perform(get("/api/activity-filter/service-providers/get-taxonomies"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }
}
