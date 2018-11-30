package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.interceptor.HibernateInterceptor;
import org.benetech.servicenet.repository.TaxonomyRepository;
import org.benetech.servicenet.service.TaxonomyService;
import org.benetech.servicenet.service.dto.TaxonomyDTO;
import org.benetech.servicenet.service.mapper.TaxonomyMapper;
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
 * Test class for the TaxonomyResource REST controller.
 *
 * @see TaxonomyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class TaxonomyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VOCABULARY = "AAAAAAAAAA";
    private static final String UPDATED_VOCABULARY = "BBBBBBBBBB";

    @Autowired
    private HibernateInterceptor hibernateInterceptor;

    @Autowired
    private TaxonomyRepository taxonomyRepository;

    @Autowired
    private TaxonomyMapper taxonomyMapper;

    @Autowired
    private TaxonomyService taxonomyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaxonomyMockMvc;

    private Taxonomy taxonomy;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Taxonomy createEntity(EntityManager em) {
        Taxonomy taxonomy = new Taxonomy()
            .name(DEFAULT_NAME)
            .vocabulary(DEFAULT_VOCABULARY);
        return taxonomy;
    }

    @Before
    public void setup() {
        hibernateInterceptor.disableEventListeners();
        MockitoAnnotations.initMocks(this);
        final TaxonomyResource taxonomyResource = new TaxonomyResource(taxonomyService);
        this.restTaxonomyMockMvc = MockMvcBuilders.standaloneSetup(taxonomyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taxonomy = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaxonomy() throws Exception {
        int databaseSizeBeforeCreate = taxonomyRepository.findAll().size();

        // Create the Taxonomy
        TaxonomyDTO taxonomyDTO = taxonomyMapper.toDto(taxonomy);
        restTaxonomyMockMvc.perform(post("/api/taxonomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyDTO)))
            .andExpect(status().isCreated());

        // Validate the Taxonomy in the database
        List<Taxonomy> taxonomyList = taxonomyRepository.findAll();
        assertThat(taxonomyList).hasSize(databaseSizeBeforeCreate + 1);
        Taxonomy testTaxonomy = taxonomyList.get(taxonomyList.size() - 1);
        assertThat(testTaxonomy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxonomy.getVocabulary()).isEqualTo(DEFAULT_VOCABULARY);
    }

    @Test
    @Transactional
    public void createTaxonomyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taxonomyRepository.findAll().size();

        // Create the Taxonomy with an existing ID
        taxonomy.setId(TestConstants.UUID_1);
        TaxonomyDTO taxonomyDTO = taxonomyMapper.toDto(taxonomy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxonomyMockMvc.perform(post("/api/taxonomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Taxonomy in the database
        List<Taxonomy> taxonomyList = taxonomyRepository.findAll();
        assertThat(taxonomyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTaxonomies() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);

        // Get all the taxonomyList
        restTaxonomyMockMvc.perform(get("/api/taxonomies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxonomy.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].vocabulary").value(hasItem(DEFAULT_VOCABULARY.toString())));
    }

    @Test
    @Transactional
    public void getTaxonomy() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);

        // Get the taxonomy
        restTaxonomyMockMvc.perform(get("/api/taxonomies/{id}", taxonomy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taxonomy.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.vocabulary").value(DEFAULT_VOCABULARY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaxonomy() throws Exception {
        // Get the taxonomy
        restTaxonomyMockMvc.perform(get("/api/taxonomies/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaxonomy() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);

        int databaseSizeBeforeUpdate = taxonomyRepository.findAll().size();

        // Update the taxonomy
        Taxonomy updatedTaxonomy = taxonomyRepository.findById(taxonomy.getId()).get();
        // Disconnect from session so that the updates on updatedTaxonomy are not directly saved in db
        em.detach(updatedTaxonomy);
        updatedTaxonomy
            .name(UPDATED_NAME)
            .vocabulary(UPDATED_VOCABULARY);
        TaxonomyDTO taxonomyDTO = taxonomyMapper.toDto(updatedTaxonomy);

        restTaxonomyMockMvc.perform(put("/api/taxonomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyDTO)))
            .andExpect(status().isOk());

        // Validate the Taxonomy in the database
        List<Taxonomy> taxonomyList = taxonomyRepository.findAll();
        assertThat(taxonomyList).hasSize(databaseSizeBeforeUpdate);
        Taxonomy testTaxonomy = taxonomyList.get(taxonomyList.size() - 1);
        assertThat(testTaxonomy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxonomy.getVocabulary()).isEqualTo(UPDATED_VOCABULARY);
    }

    @Test
    @Transactional
    public void updateNonExistingTaxonomy() throws Exception {
        int databaseSizeBeforeUpdate = taxonomyRepository.findAll().size();

        // Create the Taxonomy
        TaxonomyDTO taxonomyDTO = taxonomyMapper.toDto(taxonomy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxonomyMockMvc.perform(put("/api/taxonomies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Taxonomy in the database
        List<Taxonomy> taxonomyList = taxonomyRepository.findAll();
        assertThat(taxonomyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTaxonomy() throws Exception {
        // Initialize the database
        taxonomyRepository.saveAndFlush(taxonomy);

        int databaseSizeBeforeDelete = taxonomyRepository.findAll().size();

        // Get the taxonomy
        restTaxonomyMockMvc.perform(delete("/api/taxonomies/{id}", taxonomy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Taxonomy> taxonomyList = taxonomyRepository.findAll();
        assertThat(taxonomyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Taxonomy.class);
        Taxonomy taxonomy1 = new Taxonomy();
        taxonomy1.setId(TestConstants.UUID_1);
        Taxonomy taxonomy2 = new Taxonomy();
        taxonomy2.setId(taxonomy1.getId());
        assertThat(taxonomy1).isEqualTo(taxonomy2);
        taxonomy2.setId(TestConstants.UUID_2);
        assertThat(taxonomy1).isNotEqualTo(taxonomy2);
        taxonomy1.setId(null);
        assertThat(taxonomy1).isNotEqualTo(taxonomy2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxonomyDTO.class);
        TaxonomyDTO taxonomyDTO1 = new TaxonomyDTO();
        taxonomyDTO1.setId(TestConstants.UUID_1);
        TaxonomyDTO taxonomyDTO2 = new TaxonomyDTO();
        assertThat(taxonomyDTO1).isNotEqualTo(taxonomyDTO2);
        taxonomyDTO2.setId(taxonomyDTO1.getId());
        assertThat(taxonomyDTO1).isEqualTo(taxonomyDTO2);
        taxonomyDTO2.setId(TestConstants.UUID_2);
        assertThat(taxonomyDTO1).isNotEqualTo(taxonomyDTO2);
        taxonomyDTO1.setId(null);
        assertThat(taxonomyDTO1).isNotEqualTo(taxonomyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taxonomyMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(taxonomyMapper.fromId(null)).isNull();
    }
}
