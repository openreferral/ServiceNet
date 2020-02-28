package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.OrganizationFieldsValue;
import org.benetech.servicenet.repository.OrganizationFieldsValueRepository;
import org.benetech.servicenet.service.OrganizationFieldsValueService;
import org.benetech.servicenet.service.dto.OrganizationFieldsValueDTO;
import org.benetech.servicenet.service.mapper.OrganizationFieldsValueMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.benetech.servicenet.domain.enumeration.OrganizationFields;
/**
 * Integration tests for the {@link OrganizationFieldsValueResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class OrganizationFieldsValueResourceIntTest {

    private static final OrganizationFields DEFAULT_ORGANIZATION_FIELD = OrganizationFields.NAME;
    private static final OrganizationFields UPDATED_ORGANIZATION_FIELD = OrganizationFields.ALTERNATE_NAME;

    @Autowired
    private OrganizationFieldsValueRepository organizationFieldsValueRepository;

    @Autowired
    private OrganizationFieldsValueMapper organizationFieldsValueMapper;

    @Autowired
    private OrganizationFieldsValueService organizationFieldsValueService;

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

    private MockMvc restOrganizationFieldsValueMockMvc;

    private OrganizationFieldsValue organizationFieldsValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganizationFieldsValueResource organizationFieldsValueResource = new OrganizationFieldsValueResource(organizationFieldsValueService);
        this.restOrganizationFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(organizationFieldsValueResource)
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
    public static OrganizationFieldsValue createEntity(EntityManager em) {
        OrganizationFieldsValue organizationFieldsValue = new OrganizationFieldsValue()
            .organizationField(DEFAULT_ORGANIZATION_FIELD);
        return organizationFieldsValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationFieldsValue createUpdatedEntity(EntityManager em) {
        OrganizationFieldsValue organizationFieldsValue = new OrganizationFieldsValue()
            .organizationField(UPDATED_ORGANIZATION_FIELD);
        return organizationFieldsValue;
    }

    @Before
    public void initTest() {
        organizationFieldsValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizationFieldsValue() throws Exception {
        int databaseSizeBeforeCreate = organizationFieldsValueRepository.findAll().size();

        // Create the OrganizationFieldsValue
        OrganizationFieldsValueDTO organizationFieldsValueDTO = organizationFieldsValueMapper.toDto(organizationFieldsValue);
        restOrganizationFieldsValueMockMvc.perform(post("/api/organization-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationFieldsValueDTO)))
            .andExpect(status().isCreated());

        // Validate the OrganizationFieldsValue in the database
        List<OrganizationFieldsValue> organizationFieldsValueList = organizationFieldsValueRepository.findAll();
        assertThat(organizationFieldsValueList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationFieldsValue testOrganizationFieldsValue = organizationFieldsValueList.get(organizationFieldsValueList.size() - 1);
        assertThat(testOrganizationFieldsValue.getOrganizationField()).isEqualTo(DEFAULT_ORGANIZATION_FIELD);
    }

    @Test
    @Transactional
    public void createOrganizationFieldsValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationFieldsValueRepository.findAll().size();

        // Create the OrganizationFieldsValue with an existing ID
        organizationFieldsValue.setId(TestConstants.UUID_1);
        OrganizationFieldsValueDTO organizationFieldsValueDTO = organizationFieldsValueMapper.toDto(organizationFieldsValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationFieldsValueMockMvc.perform(post("/api/organization-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationFieldsValue in the database
        List<OrganizationFieldsValue> organizationFieldsValueList = organizationFieldsValueRepository.findAll();
        assertThat(organizationFieldsValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrganizationFieldsValues() throws Exception {
        // Initialize the database
        organizationFieldsValueRepository.saveAndFlush(organizationFieldsValue);

        // Get all the organizationFieldsValueList
        restOrganizationFieldsValueMockMvc.perform(get("/api/organization-fields-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationFieldsValue.getId().toString())))
            .andExpect(jsonPath("$.[*].organizationField").value(hasItem(DEFAULT_ORGANIZATION_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getOrganizationFieldsValue() throws Exception {
        // Initialize the database
        organizationFieldsValueRepository.saveAndFlush(organizationFieldsValue);

        // Get the organizationFieldsValue
        restOrganizationFieldsValueMockMvc.perform(get("/api/organization-fields-values/{id}", organizationFieldsValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationFieldsValue.getId().toString()))
            .andExpect(jsonPath("$.organizationField").value(DEFAULT_ORGANIZATION_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationFieldsValue() throws Exception {
        // Get the organizationFieldsValue
        restOrganizationFieldsValueMockMvc.perform(get("/api/organization-fields-values/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizationFieldsValue() throws Exception {
        // Initialize the database
        organizationFieldsValueRepository.saveAndFlush(organizationFieldsValue);

        int databaseSizeBeforeUpdate = organizationFieldsValueRepository.findAll().size();

        // Update the organizationFieldsValue
        OrganizationFieldsValue updatedOrganizationFieldsValue = organizationFieldsValueRepository.findById(organizationFieldsValue.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationFieldsValue are not directly saved in db
        em.detach(updatedOrganizationFieldsValue);
        updatedOrganizationFieldsValue
            .organizationField(UPDATED_ORGANIZATION_FIELD);
        OrganizationFieldsValueDTO organizationFieldsValueDTO = organizationFieldsValueMapper.toDto(updatedOrganizationFieldsValue);

        restOrganizationFieldsValueMockMvc.perform(put("/api/organization-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationFieldsValueDTO)))
            .andExpect(status().isOk());

        // Validate the OrganizationFieldsValue in the database
        List<OrganizationFieldsValue> organizationFieldsValueList = organizationFieldsValueRepository.findAll();
        assertThat(organizationFieldsValueList).hasSize(databaseSizeBeforeUpdate);
        OrganizationFieldsValue testOrganizationFieldsValue = organizationFieldsValueList.get(organizationFieldsValueList.size() - 1);
        assertThat(testOrganizationFieldsValue.getOrganizationField()).isEqualTo(UPDATED_ORGANIZATION_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizationFieldsValue() throws Exception {
        int databaseSizeBeforeUpdate = organizationFieldsValueRepository.findAll().size();

        // Create the OrganizationFieldsValue
        OrganizationFieldsValueDTO organizationFieldsValueDTO = organizationFieldsValueMapper.toDto(organizationFieldsValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationFieldsValueMockMvc.perform(put("/api/organization-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationFieldsValue in the database
        List<OrganizationFieldsValue> organizationFieldsValueList = organizationFieldsValueRepository.findAll();
        assertThat(organizationFieldsValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganizationFieldsValue() throws Exception {
        // Initialize the database
        organizationFieldsValueRepository.saveAndFlush(organizationFieldsValue);

        int databaseSizeBeforeDelete = organizationFieldsValueRepository.findAll().size();

        // Delete the organizationFieldsValue
        restOrganizationFieldsValueMockMvc.perform(delete("/api/organization-fields-values/{id}", organizationFieldsValue.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationFieldsValue> organizationFieldsValueList = organizationFieldsValueRepository.findAll();
        assertThat(organizationFieldsValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationFieldsValue.class);
        OrganizationFieldsValue organizationFieldsValue1 = new OrganizationFieldsValue();
        organizationFieldsValue1.setId(TestConstants.UUID_1);
        OrganizationFieldsValue organizationFieldsValue2 = new OrganizationFieldsValue();
        organizationFieldsValue2.setId(organizationFieldsValue1.getId());
        assertThat(organizationFieldsValue1).isEqualTo(organizationFieldsValue2);
        organizationFieldsValue2.setId(TestConstants.UUID_2);
        assertThat(organizationFieldsValue1).isNotEqualTo(organizationFieldsValue2);
        organizationFieldsValue1.setId(null);
        assertThat(organizationFieldsValue1).isNotEqualTo(organizationFieldsValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationFieldsValueDTO.class);
        OrganizationFieldsValueDTO organizationFieldsValueDTO1 = new OrganizationFieldsValueDTO();
        organizationFieldsValueDTO1.setId(TestConstants.UUID_1);
        OrganizationFieldsValueDTO organizationFieldsValueDTO2 = new OrganizationFieldsValueDTO();
        assertThat(organizationFieldsValueDTO1).isNotEqualTo(organizationFieldsValueDTO2);
        organizationFieldsValueDTO2.setId(organizationFieldsValueDTO1.getId());
        assertThat(organizationFieldsValueDTO1).isEqualTo(organizationFieldsValueDTO2);
        organizationFieldsValueDTO2.setId(TestConstants.UUID_2);
        assertThat(organizationFieldsValueDTO1).isNotEqualTo(organizationFieldsValueDTO2);
        organizationFieldsValueDTO1.setId(null);
        assertThat(organizationFieldsValueDTO1).isNotEqualTo(organizationFieldsValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(organizationFieldsValueMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(organizationFieldsValueMapper.fromId(null)).isNull();
    }
}
