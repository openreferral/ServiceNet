package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;

import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.repository.FieldExclusionRepository;
import org.benetech.servicenet.service.FieldExclusionService;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.benetech.servicenet.service.mapper.FieldExclusionMapper;
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


import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FieldExclusionResource REST controller.
 *
 * @see FieldExclusionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class FieldExclusionResourceIntTest {

    private static final String DEFAULT_FIELDS = "AAAAAAAAAA";
    private static final String UPDATED_FIELDS = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY = "BBBBBBBBBB";

    @Autowired
    private FieldExclusionRepository fieldExclusionRepository;

    @Autowired
    private FieldExclusionMapper fieldExclusionMapper;

    @Autowired
    private FieldExclusionService fieldExclusionService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFieldExclusionMockMvc;

    private FieldExclusion fieldExclusion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FieldExclusionResource fieldExclusionResource = new FieldExclusionResource(fieldExclusionService);
        this.restFieldExclusionMockMvc = MockMvcBuilders.standaloneSetup(fieldExclusionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldExclusion createEntity(EntityManager em) {
        FieldExclusion fieldExclusion = new FieldExclusion()
            .fields(DEFAULT_FIELDS)
            .entity(DEFAULT_ENTITY);
        return fieldExclusion;
    }

    @Before
    public void initTest() {
        fieldExclusion = createEntity(em);
    }

    @Test
    @Transactional
    public void createFieldExclusion() throws Exception {
        int databaseSizeBeforeCreate = fieldExclusionRepository.findAll().size();

        // Create the FieldExclusion
        FieldExclusionDTO fieldExclusionDTO = fieldExclusionMapper.toDto(fieldExclusion);
        restFieldExclusionMockMvc.perform(post("/api/field-exclusions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fieldExclusionDTO)))
            .andExpect(status().isCreated());

        // Validate the FieldExclusion in the database
        List<FieldExclusion> fieldExclusionList = fieldExclusionRepository.findAll();
        assertThat(fieldExclusionList).hasSize(databaseSizeBeforeCreate + 1);
        FieldExclusion testFieldExclusion = fieldExclusionList.get(fieldExclusionList.size() - 1);
        assertThat(testFieldExclusion.getFields()).isEqualTo(DEFAULT_FIELDS);
        assertThat(testFieldExclusion.getEntity()).isEqualTo(DEFAULT_ENTITY);
    }

    @Test
    @Transactional
    public void createFieldExclusionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fieldExclusionRepository.findAll().size();

        // Create the FieldExclusion with an existing ID
        fieldExclusion.setId(TestConstants.UUID_1);
        FieldExclusionDTO fieldExclusionDTO = fieldExclusionMapper.toDto(fieldExclusion);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldExclusionMockMvc.perform(post("/api/field-exclusions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fieldExclusionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FieldExclusion in the database
        List<FieldExclusion> fieldExclusionList = fieldExclusionRepository.findAll();
        assertThat(fieldExclusionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFieldExclusions() throws Exception {
        // Initialize the database
        fieldExclusionRepository.saveAndFlush(fieldExclusion);

        // Get all the fieldExclusionList
        restFieldExclusionMockMvc.perform(get("/api/field-exclusions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fieldExclusion.getId().toString())))
            .andExpect(jsonPath("$.[*].fields").value(hasItem(DEFAULT_FIELDS.toString())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_ENTITY.toString())));
    }
    
    @Test
    @Transactional
    public void getFieldExclusion() throws Exception {
        // Initialize the database
        fieldExclusionRepository.saveAndFlush(fieldExclusion);

        // Get the fieldExclusion
        restFieldExclusionMockMvc.perform(get("/api/field-exclusions/{id}", fieldExclusion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fieldExclusion.getId().toString()))
            .andExpect(jsonPath("$.fields").value(DEFAULT_FIELDS.toString()))
            .andExpect(jsonPath("$.entity").value(DEFAULT_ENTITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFieldExclusion() throws Exception {
        // Get the fieldExclusion
        restFieldExclusionMockMvc.perform(get("/api/field-exclusions/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFieldExclusion() throws Exception {
        // Initialize the database
        fieldExclusionRepository.saveAndFlush(fieldExclusion);

        int databaseSizeBeforeUpdate = fieldExclusionRepository.findAll().size();

        // Update the fieldExclusion
        FieldExclusion updatedFieldExclusion = fieldExclusionRepository.findById(fieldExclusion.getId()).get();
        // Disconnect from session so that the updates on updatedFieldExclusion are not directly saved in db
        em.detach(updatedFieldExclusion);
        updatedFieldExclusion
            .fields(UPDATED_FIELDS)
            .entity(UPDATED_ENTITY);
        FieldExclusionDTO fieldExclusionDTO = fieldExclusionMapper.toDto(updatedFieldExclusion);

        restFieldExclusionMockMvc.perform(put("/api/field-exclusions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fieldExclusionDTO)))
            .andExpect(status().isOk());

        // Validate the FieldExclusion in the database
        List<FieldExclusion> fieldExclusionList = fieldExclusionRepository.findAll();
        assertThat(fieldExclusionList).hasSize(databaseSizeBeforeUpdate);
        FieldExclusion testFieldExclusion = fieldExclusionList.get(fieldExclusionList.size() - 1);
        assertThat(testFieldExclusion.getFields()).isEqualTo(UPDATED_FIELDS);
        assertThat(testFieldExclusion.getEntity()).isEqualTo(UPDATED_ENTITY);
    }

    @Test
    @Transactional
    public void updateNonExistingFieldExclusion() throws Exception {
        int databaseSizeBeforeUpdate = fieldExclusionRepository.findAll().size();

        // Create the FieldExclusion
        FieldExclusionDTO fieldExclusionDTO = fieldExclusionMapper.toDto(fieldExclusion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldExclusionMockMvc.perform(put("/api/field-exclusions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fieldExclusionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FieldExclusion in the database
        List<FieldExclusion> fieldExclusionList = fieldExclusionRepository.findAll();
        assertThat(fieldExclusionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFieldExclusion() throws Exception {
        // Initialize the database
        fieldExclusionRepository.saveAndFlush(fieldExclusion);

        int databaseSizeBeforeDelete = fieldExclusionRepository.findAll().size();

        // Get the fieldExclusion
        restFieldExclusionMockMvc.perform(delete("/api/field-exclusions/{id}", fieldExclusion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FieldExclusion> fieldExclusionList = fieldExclusionRepository.findAll();
        assertThat(fieldExclusionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldExclusion.class);
        FieldExclusion fieldExclusion1 = new FieldExclusion();
        fieldExclusion1.setId(TestConstants.UUID_1);
        FieldExclusion fieldExclusion2 = new FieldExclusion();
        fieldExclusion2.setId(fieldExclusion1.getId());
        assertThat(fieldExclusion1).isEqualTo(fieldExclusion2);
        fieldExclusion2.setId(TestConstants.UUID_2);
        assertThat(fieldExclusion1).isNotEqualTo(fieldExclusion2);
        fieldExclusion1.setId(null);
        assertThat(fieldExclusion1).isNotEqualTo(fieldExclusion2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldExclusionDTO.class);
        FieldExclusionDTO fieldExclusionDTO1 = new FieldExclusionDTO();
        fieldExclusionDTO1.setId(TestConstants.UUID_1);
        FieldExclusionDTO fieldExclusionDTO2 = new FieldExclusionDTO();
        assertThat(fieldExclusionDTO1).isNotEqualTo(fieldExclusionDTO2);
        fieldExclusionDTO2.setId(fieldExclusionDTO1.getId());
        assertThat(fieldExclusionDTO1).isEqualTo(fieldExclusionDTO2);
        fieldExclusionDTO2.setId(TestConstants.UUID_2);
        assertThat(fieldExclusionDTO1).isNotEqualTo(fieldExclusionDTO2);
        fieldExclusionDTO1.setId(null);
        assertThat(fieldExclusionDTO1).isNotEqualTo(fieldExclusionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fieldExclusionMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(fieldExclusionMapper.fromId(null)).isNull();
    }
}
