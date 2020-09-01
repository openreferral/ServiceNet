package org.benetech.servicenet.web.rest;

import java.math.BigDecimal;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.MatchSimilarity;
import org.benetech.servicenet.repository.MatchSimilarityRepository;
import org.benetech.servicenet.service.MatchSimilarityService;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.benetech.servicenet.service.mapper.MatchSimilarityMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@Link MatchSimilarityResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class MatchSimilarityResourceIntTest {

    private static final BigDecimal DEFAULT_SIMILARITY = BigDecimal.ONE;
    private static final BigDecimal UPDATED_SIMILARITY = BigDecimal.valueOf(2);

    private static final String DEFAULT_RESOURCE_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_CLASS = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    @Autowired
    private MatchSimilarityRepository matchSimilarityRepository;

    @Autowired
    private MatchSimilarityMapper matchSimilarityMapper;

    @Autowired
    private MatchSimilarityService matchSimilarityService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMatchSimilarityMockMvc;

    private MatchSimilarity matchSimilarity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final MatchSimilarityResource matchSimilarityResource = new MatchSimilarityResource(matchSimilarityService);
        this.restMatchSimilarityMockMvc = MockMvcBuilders.standaloneSetup(matchSimilarityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchSimilarity createEntity(EntityManager em) {
        MatchSimilarity matchSimilarity = new MatchSimilarity()
            .similarity(DEFAULT_SIMILARITY)
            .resourceClass(DEFAULT_RESOURCE_CLASS)
            .fieldName(DEFAULT_FIELD_NAME);
        return matchSimilarity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MatchSimilarity createUpdatedEntity(EntityManager em) {
        MatchSimilarity matchSimilarity = new MatchSimilarity()
            .similarity(UPDATED_SIMILARITY)
            .resourceClass(UPDATED_RESOURCE_CLASS)
            .fieldName(UPDATED_FIELD_NAME);
        return matchSimilarity;
    }

    @Before
    public void initTest() {
        matchSimilarity = createEntity(em);
    }

    @Test
    @Transactional
    public void createMatchSimilarity() throws Exception {
        int databaseSizeBeforeCreate = matchSimilarityRepository.findAll().size();

        // Create the MatchSimilarity
        restMatchSimilarityMockMvc.perform(post("/api/match-similarities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matchSimilarity)))
            .andExpect(status().isCreated());

        // Validate the MatchSimilarity in the database
        List<MatchSimilarity> matchSimilarityList = matchSimilarityRepository.findAll();
        assertThat(matchSimilarityList).hasSize(databaseSizeBeforeCreate + 1);
        MatchSimilarity testMatchSimilarity = matchSimilarityList.get(matchSimilarityList.size() - 1);
        assertThat(testMatchSimilarity.getSimilarity()).isEqualTo(DEFAULT_SIMILARITY);
        assertThat(testMatchSimilarity.getResourceClass()).isEqualTo(DEFAULT_RESOURCE_CLASS);
        assertThat(testMatchSimilarity.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
    }

    @Test
    @Transactional
    public void createMatchSimilarityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = matchSimilarityRepository.findAll().size();

        // Create the MatchSimilarity with an existing ID
        matchSimilarity.setId(TestConstants.UUID_1);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchSimilarityMockMvc.perform(post("/api/match-similarities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matchSimilarity)))
            .andExpect(status().isBadRequest());

        // Validate the MatchSimilarity in the database
        List<MatchSimilarity> matchSimilarityList = matchSimilarityRepository.findAll();
        assertThat(matchSimilarityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMatchSimilarities() throws Exception {
        // Initialize the database
        matchSimilarityRepository.saveAndFlush(matchSimilarity);

        // Get all the matchSimilarityList
        restMatchSimilarityMockMvc.perform(get("/api/match-similarities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matchSimilarity.getId().toString())))
            .andExpect(jsonPath("$.[*].similarity").value(hasItem(DEFAULT_SIMILARITY.intValue())))
            .andExpect(jsonPath("$.[*].resourceClass").value(hasItem(DEFAULT_RESOURCE_CLASS.toString())));
    }

    @Test
    @Transactional
    public void getMatchSimilarity() throws Exception {
        // Initialize the database
        matchSimilarityRepository.saveAndFlush(matchSimilarity);

        // Get the matchSimilarity
        restMatchSimilarityMockMvc.perform(get("/api/match-similarities/{id}", matchSimilarity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matchSimilarity.getId().toString()))
            .andExpect(jsonPath("$.similarity").value(DEFAULT_SIMILARITY))
            .andExpect(jsonPath("$.resourceClass").value(DEFAULT_RESOURCE_CLASS))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingMatchSimilarity() throws Exception {
        // Get the matchSimilarity
        restMatchSimilarityMockMvc.perform(get("/api/match-similarities/{id}",  TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatchSimilarity() throws Exception {
        // Initialize the database
        matchSimilarityRepository.saveAndFlush(matchSimilarity);

        int databaseSizeBeforeUpdate = matchSimilarityRepository.findAll().size();

        // Update the matchSimilarity
        MatchSimilarity updatedMatchSimilarity = matchSimilarityRepository.findById(matchSimilarity.getId()).get();
        // Disconnect from session so that the updates on updatedMatchSimilarity are not directly saved in db
        em.detach(updatedMatchSimilarity);
        updatedMatchSimilarity
            .similarity(UPDATED_SIMILARITY)
            .resourceClass(UPDATED_RESOURCE_CLASS)
            .fieldName(UPDATED_FIELD_NAME);

        restMatchSimilarityMockMvc.perform(put("/api/match-similarities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matchSimilarity)))
            .andExpect(status().isOk());

        // Validate the MatchSimilarity in the database
        List<MatchSimilarity> matchSimilarityList = matchSimilarityRepository.findAll();
        assertThat(matchSimilarityList).hasSize(databaseSizeBeforeUpdate);
        MatchSimilarity testMatchSimilarity = matchSimilarityList.get(matchSimilarityList.size() - 1);
        assertThat(testMatchSimilarity.getSimilarity()).isEqualTo(UPDATED_SIMILARITY);
        assertThat(testMatchSimilarity.getResourceClass()).isEqualTo(UPDATED_RESOURCE_CLASS);
        assertThat(testMatchSimilarity.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMatchSimilarity() throws Exception {
        int databaseSizeBeforeUpdate = matchSimilarityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchSimilarityMockMvc.perform(put("/api/match-similarities")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(matchSimilarity)))
            .andExpect(status().isBadRequest());

        // Validate the MatchSimilarity in the database
        List<MatchSimilarity> matchSimilarityList = matchSimilarityRepository.findAll();
        assertThat(matchSimilarityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMatchSimilarity() throws Exception {
        // Initialize the database
        matchSimilarityRepository.saveAndFlush(matchSimilarity);

        int databaseSizeBeforeDelete = matchSimilarityRepository.findAll().size();

        // Delete the matchSimilarity
        restMatchSimilarityMockMvc.perform(delete("/api/match-similarities/{id}", matchSimilarity.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MatchSimilarity> matchSimilarityList = matchSimilarityRepository.findAll();
        assertThat(matchSimilarityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchSimilarity.class);
        MatchSimilarity matchSimilarity1 = new MatchSimilarity();
        matchSimilarity1.setId(TestConstants.UUID_1);
        MatchSimilarity matchSimilarity2 = new MatchSimilarity();
        matchSimilarity2.setId(matchSimilarity1.getId());
        assertThat(matchSimilarity1).isEqualTo(matchSimilarity2);
        matchSimilarity2.setId(TestConstants.UUID_2);
        assertThat(matchSimilarity1).isNotEqualTo(matchSimilarity2);
        matchSimilarity1.setId(null);
        assertThat(matchSimilarity1).isNotEqualTo(matchSimilarity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MatchSimilarityDTO.class);
        MatchSimilarityDTO matchSimilarityDTO1 = new MatchSimilarityDTO();
        matchSimilarityDTO1.setId(TestConstants.UUID_1);
        MatchSimilarityDTO matchSimilarityDTO2 = new MatchSimilarityDTO();
        assertThat(matchSimilarityDTO1).isNotEqualTo(matchSimilarityDTO2);
        matchSimilarityDTO2.setId(matchSimilarityDTO1.getId());
        assertThat(matchSimilarityDTO1).isEqualTo(matchSimilarityDTO2);
        matchSimilarityDTO2.setId(TestConstants.UUID_2);
        assertThat(matchSimilarityDTO1).isNotEqualTo(matchSimilarityDTO2);
        matchSimilarityDTO1.setId(null);
        assertThat(matchSimilarityDTO1).isNotEqualTo(matchSimilarityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(matchSimilarityMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(matchSimilarityMapper.fromId(null)).isNull();
    }
}
