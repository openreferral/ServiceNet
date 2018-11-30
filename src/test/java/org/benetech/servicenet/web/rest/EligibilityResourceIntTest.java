package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.interceptor.HibernateInterceptor;
import org.benetech.servicenet.repository.EligibilityRepository;
import org.benetech.servicenet.service.EligibilityService;
import org.benetech.servicenet.service.dto.EligibilityDTO;
import org.benetech.servicenet.service.mapper.EligibilityMapper;
import org.benetech.servicenet.web.rest.errors.ExceptionTranslator;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the EligibilityResource REST controller.
 *
 * @see EligibilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class EligibilityResourceIntTest {

    private static final String DEFAULT_ELIGIBILITY = "AAAAAAAAAA";
    private static final String UPDATED_ELIGIBILITY = "BBBBBBBBBB";

    @Autowired
    private HibernateInterceptor hibernateInterceptor;

    @Autowired
    private EligibilityRepository eligibilityRepository;

    @Autowired
    private EligibilityMapper eligibilityMapper;

    @Autowired
    private EligibilityService eligibilityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEligibilityMockMvc;

    private Eligibility eligibility;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eligibility createEntity(EntityManager em) {
        Eligibility eligibility = new Eligibility()
            .eligibility(DEFAULT_ELIGIBILITY);
        return eligibility;
    }

    @Before
    public void setup() {
        hibernateInterceptor.disableEventListeners();
        MockitoAnnotations.initMocks(this);
        final EligibilityResource eligibilityResource = new EligibilityResource(eligibilityService);
        this.restEligibilityMockMvc = MockMvcBuilders.standaloneSetup(eligibilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        eligibility = createEntity(em);
    }

    @Test
    @Transactional
    public void createEligibility() throws Exception {
        int databaseSizeBeforeCreate = eligibilityRepository.findAll().size();

        // Create the Eligibility
        EligibilityDTO eligibilityDTO = eligibilityMapper.toDto(eligibility);
        restEligibilityMockMvc.perform(post("/api/eligibilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eligibilityDTO)))
            .andExpect(status().isCreated());

        // Validate the Eligibility in the database
        List<Eligibility> eligibilityList = eligibilityRepository.findAll();
        assertThat(eligibilityList).hasSize(databaseSizeBeforeCreate + 1);
        Eligibility testEligibility = eligibilityList.get(eligibilityList.size() - 1);
        assertThat(testEligibility.getEligibility()).isEqualTo(DEFAULT_ELIGIBILITY);
    }

    @Test
    @Transactional
    public void createEligibilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eligibilityRepository.findAll().size();

        // Create the Eligibility with an existing ID
        eligibility.setId(TestConstants.UUID_1);
        EligibilityDTO eligibilityDTO = eligibilityMapper.toDto(eligibility);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEligibilityMockMvc.perform(post("/api/eligibilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eligibilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Eligibility in the database
        List<Eligibility> eligibilityList = eligibilityRepository.findAll();
        assertThat(eligibilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEligibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = eligibilityRepository.findAll().size();
        // set the field null
        eligibility.setEligibility(null);

        // Create the Eligibility, which fails.
        EligibilityDTO eligibilityDTO = eligibilityMapper.toDto(eligibility);

        restEligibilityMockMvc.perform(post("/api/eligibilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eligibilityDTO)))
            .andExpect(status().isBadRequest());

        List<Eligibility> eligibilityList = eligibilityRepository.findAll();
        assertThat(eligibilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEligibilities() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get all the eligibilityList
        restEligibilityMockMvc.perform(get("/api/eligibilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eligibility.getId().toString())))
            .andExpect(jsonPath("$.[*].eligibility").value(hasItem(DEFAULT_ELIGIBILITY.toString())));
    }

    @Test
    @Transactional
    public void getEligibility() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        // Get the eligibility
        restEligibilityMockMvc.perform(get("/api/eligibilities/{id}", eligibility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eligibility.getId().toString()))
            .andExpect(jsonPath("$.eligibility").value(DEFAULT_ELIGIBILITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEligibility() throws Exception {
        // Get the eligibility
        restEligibilityMockMvc.perform(get("/api/eligibilities/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEligibility() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        int databaseSizeBeforeUpdate = eligibilityRepository.findAll().size();

        // Update the eligibility
        Eligibility updatedEligibility = eligibilityRepository.findById(eligibility.getId()).get();
        // Disconnect from session so that the updates on updatedEligibility are not directly saved in db
        em.detach(updatedEligibility);
        updatedEligibility
            .eligibility(UPDATED_ELIGIBILITY);
        EligibilityDTO eligibilityDTO = eligibilityMapper.toDto(updatedEligibility);

        restEligibilityMockMvc.perform(put("/api/eligibilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eligibilityDTO)))
            .andExpect(status().isOk());

        // Validate the Eligibility in the database
        List<Eligibility> eligibilityList = eligibilityRepository.findAll();
        assertThat(eligibilityList).hasSize(databaseSizeBeforeUpdate);
        Eligibility testEligibility = eligibilityList.get(eligibilityList.size() - 1);
        assertThat(testEligibility.getEligibility()).isEqualTo(UPDATED_ELIGIBILITY);
    }

    @Test
    @Transactional
    public void updateNonExistingEligibility() throws Exception {
        int databaseSizeBeforeUpdate = eligibilityRepository.findAll().size();

        // Create the Eligibility
        EligibilityDTO eligibilityDTO = eligibilityMapper.toDto(eligibility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEligibilityMockMvc.perform(put("/api/eligibilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eligibilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Eligibility in the database
        List<Eligibility> eligibilityList = eligibilityRepository.findAll();
        assertThat(eligibilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEligibility() throws Exception {
        // Initialize the database
        eligibilityRepository.saveAndFlush(eligibility);

        int databaseSizeBeforeDelete = eligibilityRepository.findAll().size();

        // Get the eligibility
        restEligibilityMockMvc.perform(delete("/api/eligibilities/{id}", eligibility.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Eligibility> eligibilityList = eligibilityRepository.findAll();
        assertThat(eligibilityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eligibility.class);
        Eligibility eligibility1 = new Eligibility();
        eligibility1.setId(TestConstants.UUID_1);
        Eligibility eligibility2 = new Eligibility();
        eligibility2.setId(eligibility1.getId());
        assertThat(eligibility1).isEqualTo(eligibility2);
        eligibility2.setId(TestConstants.UUID_2);
        assertThat(eligibility1).isNotEqualTo(eligibility2);
        eligibility1.setId(null);
        assertThat(eligibility1).isNotEqualTo(eligibility2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EligibilityDTO.class);
        EligibilityDTO eligibilityDTO1 = new EligibilityDTO();
        eligibilityDTO1.setId(TestConstants.UUID_1);
        EligibilityDTO eligibilityDTO2 = new EligibilityDTO();
        assertThat(eligibilityDTO1).isNotEqualTo(eligibilityDTO2);
        eligibilityDTO2.setId(eligibilityDTO1.getId());
        assertThat(eligibilityDTO1).isEqualTo(eligibilityDTO2);
        eligibilityDTO2.setId(TestConstants.UUID_2);
        assertThat(eligibilityDTO1).isNotEqualTo(eligibilityDTO2);
        eligibilityDTO1.setId(null);
        assertThat(eligibilityDTO1).isNotEqualTo(eligibilityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eligibilityMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(eligibilityMapper.fromId(null)).isNull();
    }
}
