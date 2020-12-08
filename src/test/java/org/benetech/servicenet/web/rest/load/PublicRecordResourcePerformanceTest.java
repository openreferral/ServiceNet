package org.benetech.servicenet.web.rest.load;

import static org.benetech.servicenet.config.Constants.SERVICE_PROVIDER;
import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.benetech.servicenet.web.rest.load.LoadTestUtils.TEST_SILO_NAME;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit5Extension;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.repository.SiloRepository;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.dto.provider.ProviderFilterDTO;
import org.benetech.servicenet.web.rest.ActivityResource;
import org.benetech.servicenet.web.rest.TestUtil;
import org.benetech.servicenet.web.rest.unauthorized.PublicRecordResource;
import org.junit.jupiter.api.BeforeAll;
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
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class PublicRecordResourcePerformanceTest {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restActivityMockMvc;

    @Autowired
    private PublicRecordResource publicRecordResource;

    final static int PAGE_SIZE = 20;
    final static int PAGE_SIZE_MAP = 50;

    private static Organization org;

    @BeforeAll
    public static void beforeAll(@Autowired OrganizationRepository organizationRepository,
        @Autowired UserProfileRepository userProfileRepository, @Autowired SiloRepository siloRepository) {
        org = organizationRepository.findAll(
            new PageableWithOneItem()).stream().findFirst().get();
        new LoadTestUtils(userProfileRepository, siloRepository).setTestSilo("user");
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restActivityMockMvc = MockMvcBuilders.standaloneSetup(publicRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    public void getAPageOfAllRecords() throws Exception {
        restActivityMockMvc.perform(post("/public-api/all-provider-records?size=" + PAGE_SIZE)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(new ProviderFilterDTO())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(PAGE_SIZE)));
    }

    @Test
    @Transactional
    public void getRecordsForMap() throws Exception {
        restActivityMockMvc.perform(post("/public-api/all-provider-records-map?size=" + PAGE_SIZE_MAP)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(new ProviderFilterDTO())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", hasSize(PAGE_SIZE_MAP)));
    }

    @Test
    @Transactional
    public void getPostalCodes() throws Exception {
        restActivityMockMvc.perform(get("/public-api/activity-filter/service-providers/get-postal-codes?siloName=" + TEST_SILO_NAME))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    public void getRegions() throws Exception {
        restActivityMockMvc.perform(get("/public-api/activity-filter/service-providers/get-regions?siloName=" + TEST_SILO_NAME))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    public void getCities() throws Exception {
        restActivityMockMvc.perform(get("/public-api/activity-filter/service-providers/get-cities?siloName=" + TEST_SILO_NAME))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    public void getTaxonomies() throws Exception {
        restActivityMockMvc.perform(get("/public-api/activity-filter/service-providers/get-taxonomies?siloName=" + TEST_SILO_NAME))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    public void getProviderTaxonomies() throws Exception {
        restActivityMockMvc.perform(get("/public-api/provider-taxonomies/" + SERVICE_PROVIDER + "?siloName=" + TEST_SILO_NAME))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$", not(hasSize(0))));
    }

    @Test
    @Transactional
    public void getASpecificRecord() throws Exception {
        restActivityMockMvc.perform(post("/api/provider-organization/" + org.getId() + "?siloName=" + TEST_SILO_NAME)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(new ProviderFilterDTO())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(org.getId().toString()));
    }
}
