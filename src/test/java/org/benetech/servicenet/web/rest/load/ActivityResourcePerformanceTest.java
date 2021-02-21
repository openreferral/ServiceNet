package org.benetech.servicenet.web.rest.load;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit5Extension;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.service.dto.provider.ProviderFilterDTO;
import org.benetech.servicenet.web.rest.ActivityResource;
import org.benetech.servicenet.web.rest.TestUtil;
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

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
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
@SuppressWarnings("CPD-START")
public class ActivityResourcePerformanceTest {

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
    private GeocodingResultRepository geocodingResultRepository;

    private MockMvc restActivityMockMvc;

    final static int PAGE_SIZE = 20;
    final static int PAGE_SIZE_MAP = 50;

    @BeforeEach
    public void setUp() {
        final ActivityResource activityResource = new ActivityResource(
            activityService, userService, geocodingResultRepository
        );
        MockitoAnnotations.initMocks(this);
        restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    public void getAPageOfUsersRecords() throws Exception {
        restActivityMockMvc.perform(get("/api/provider-records?size=" + PAGE_SIZE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.content", hasSize(PAGE_SIZE)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user")
    public void getAPageOfAllRecords() throws Exception {
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
        restActivityMockMvc.perform(post("/api/all-provider-records-map?size=" + PAGE_SIZE_MAP)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(new ProviderFilterDTO())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(PAGE_SIZE_MAP)));
    }
}
