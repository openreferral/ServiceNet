package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.repository.ServiceTaxonomyRepository;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;
import org.benetech.servicenet.service.mapper.ServiceTaxonomyMapper;
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
 * Test class for the ServiceTaxonomyResource REST controller.
 *
 * @see ServiceTaxonomyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ServiceTaxonomyResourceIntTest {

    private static final String DEFAULT_TAXONOMY_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_TAXONOMY_DETAILS = "BBBBBBBBBB";

    @Autowired
    private ServiceTaxonomyRepository serviceTaxonomyRepository;

    @Autowired
    private ServiceTaxonomyMapper serviceTaxonomyMapper;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceTaxonomyMockMvc;

    private ServiceTaxonomy serviceTaxonomy;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceTaxonomy createEntity(EntityManager em) {
        ServiceTaxonomy serviceTaxonomy = new ServiceTaxonomy()
            .taxonomyDetails(DEFAULT_TAXONOMY_DETAILS);
        return serviceTaxonomy;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceTaxonomyResource serviceTaxonomyResource = new ServiceTaxonomyResource(serviceTaxonomyService);
        this.restServiceTaxonomyMockMvc = MockMvcBuilders.standaloneSetup(serviceTaxonomyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceTaxonomy = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceTaxonomy() throws Exception {
        int databaseSizeBeforeCreate = serviceTaxonomyRepository.findAll().size();

        // Create the ServiceTaxonomy
        ServiceTaxonomyDTO serviceTaxonomyDTO = serviceTaxonomyMapper.toDto(serviceTaxonomy);
        restServiceTaxonomyMockMvc.perform(post("/api/service-taxonomies")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomyDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceTaxonomy in the database
        List<ServiceTaxonomy> serviceTaxonomyList = serviceTaxonomyRepository.findAll();
        assertThat(serviceTaxonomyList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceTaxonomy testServiceTaxonomy = serviceTaxonomyList.get(serviceTaxonomyList.size() - 1);
        assertThat(testServiceTaxonomy.getTaxonomyDetails()).isEqualTo(DEFAULT_TAXONOMY_DETAILS);
    }

    @Test
    @Transactional
    public void createServiceTaxonomyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceTaxonomyRepository.findAll().size();

        // Create the ServiceTaxonomy with an existing ID
        serviceTaxonomy.setId(TestConstants.UUID_1);
        ServiceTaxonomyDTO serviceTaxonomyDTO = serviceTaxonomyMapper.toDto(serviceTaxonomy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceTaxonomyMockMvc.perform(post("/api/service-taxonomies")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceTaxonomy in the database
        List<ServiceTaxonomy> serviceTaxonomyList = serviceTaxonomyRepository.findAll();
        assertThat(serviceTaxonomyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiceTaxonomies() throws Exception {
        // Initialize the database
        serviceTaxonomyRepository.saveAndFlush(serviceTaxonomy);

        // Get all the serviceTaxonomyList
        restServiceTaxonomyMockMvc.perform(get("/api/service-taxonomies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceTaxonomy.getId().toString())))
            .andExpect(jsonPath("$.[*].taxonomyDetails").value(hasItem(DEFAULT_TAXONOMY_DETAILS.toString())));
    }

    @Test
    @Transactional
    public void getServiceTaxonomy() throws Exception {
        // Initialize the database
        serviceTaxonomyRepository.saveAndFlush(serviceTaxonomy);

        // Get the serviceTaxonomy
        restServiceTaxonomyMockMvc.perform(get("/api/service-taxonomies/{id}", serviceTaxonomy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceTaxonomy.getId().toString()))
            .andExpect(jsonPath("$.taxonomyDetails").value(DEFAULT_TAXONOMY_DETAILS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceTaxonomy() throws Exception {
        // Get the serviceTaxonomy
        restServiceTaxonomyMockMvc.perform(get("/api/service-taxonomies/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceTaxonomy() throws Exception {
        // Initialize the database
        serviceTaxonomyRepository.saveAndFlush(serviceTaxonomy);

        int databaseSizeBeforeUpdate = serviceTaxonomyRepository.findAll().size();

        // Update the serviceTaxonomy
        ServiceTaxonomy updatedServiceTaxonomy = serviceTaxonomyRepository.findById(serviceTaxonomy.getId()).get();
        // Disconnect from session so that the updates on updatedServiceTaxonomy are not directly saved in db
        em.detach(updatedServiceTaxonomy);
        updatedServiceTaxonomy
            .taxonomyDetails(UPDATED_TAXONOMY_DETAILS);
        ServiceTaxonomyDTO serviceTaxonomyDTO = serviceTaxonomyMapper.toDto(updatedServiceTaxonomy);

        restServiceTaxonomyMockMvc.perform(put("/api/service-taxonomies")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomyDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceTaxonomy in the database
        List<ServiceTaxonomy> serviceTaxonomyList = serviceTaxonomyRepository.findAll();
        assertThat(serviceTaxonomyList).hasSize(databaseSizeBeforeUpdate);
        ServiceTaxonomy testServiceTaxonomy = serviceTaxonomyList.get(serviceTaxonomyList.size() - 1);
        assertThat(testServiceTaxonomy.getTaxonomyDetails()).isEqualTo(UPDATED_TAXONOMY_DETAILS);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceTaxonomy() throws Exception {
        int databaseSizeBeforeUpdate = serviceTaxonomyRepository.findAll().size();

        // Create the ServiceTaxonomy
        ServiceTaxonomyDTO serviceTaxonomyDTO = serviceTaxonomyMapper.toDto(serviceTaxonomy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceTaxonomyMockMvc.perform(put("/api/service-taxonomies")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceTaxonomy in the database
        List<ServiceTaxonomy> serviceTaxonomyList = serviceTaxonomyRepository.findAll();
        assertThat(serviceTaxonomyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceTaxonomy() throws Exception {
        // Initialize the database
        serviceTaxonomyRepository.saveAndFlush(serviceTaxonomy);

        int databaseSizeBeforeDelete = serviceTaxonomyRepository.findAll().size();

        // Get the serviceTaxonomy
        restServiceTaxonomyMockMvc.perform(delete("/api/service-taxonomies/{id}", serviceTaxonomy.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceTaxonomy> serviceTaxonomyList = serviceTaxonomyRepository.findAll();
        assertThat(serviceTaxonomyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceTaxonomy.class);
        ServiceTaxonomy serviceTaxonomy1 = new ServiceTaxonomy();
        serviceTaxonomy1.setId(TestConstants.UUID_1);
        ServiceTaxonomy serviceTaxonomy2 = new ServiceTaxonomy();
        serviceTaxonomy2.setId(serviceTaxonomy1.getId());
        assertThat(serviceTaxonomy1).isEqualTo(serviceTaxonomy2);
        serviceTaxonomy2.setId(TestConstants.UUID_2);
        assertThat(serviceTaxonomy1).isNotEqualTo(serviceTaxonomy2);
        serviceTaxonomy1.setId(null);
        assertThat(serviceTaxonomy1).isNotEqualTo(serviceTaxonomy2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceTaxonomyDTO.class);
        ServiceTaxonomyDTO serviceTaxonomyDTO1 = new ServiceTaxonomyDTO();
        serviceTaxonomyDTO1.setId(TestConstants.UUID_1);
        ServiceTaxonomyDTO serviceTaxonomyDTO2 = new ServiceTaxonomyDTO();
        assertThat(serviceTaxonomyDTO1).isNotEqualTo(serviceTaxonomyDTO2);
        serviceTaxonomyDTO2.setId(serviceTaxonomyDTO1.getId());
        assertThat(serviceTaxonomyDTO1).isEqualTo(serviceTaxonomyDTO2);
        serviceTaxonomyDTO2.setId(TestConstants.UUID_2);
        assertThat(serviceTaxonomyDTO1).isNotEqualTo(serviceTaxonomyDTO2);
        serviceTaxonomyDTO1.setId(null);
        assertThat(serviceTaxonomyDTO1).isNotEqualTo(serviceTaxonomyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceTaxonomyMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(serviceTaxonomyMapper.fromId(null)).isNull();
    }
}
