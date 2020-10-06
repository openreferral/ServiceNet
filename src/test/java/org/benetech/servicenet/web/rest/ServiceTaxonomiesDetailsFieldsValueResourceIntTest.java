package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ServiceTaxonomiesDetailsFieldsValue;
import org.benetech.servicenet.repository.ServiceTaxonomiesDetailsFieldsValueRepository;
import org.benetech.servicenet.service.ServiceTaxonomiesDetailsFieldsValueService;
import org.benetech.servicenet.service.dto.ServiceTaxonomiesDetailsFieldsValueDTO;
import org.benetech.servicenet.service.mapper.ServiceTaxonomiesDetailsFieldsValueMapper;
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
import org.benetech.servicenet.ZeroCodeSpringJUnit4Runner;
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

import org.benetech.servicenet.domain.enumeration.ServiceTaxonomiesDetailsFields;

/**
 * Integration tests for the {@link ServiceTaxonomiesDetailsFieldsValueResource} REST controller.
 */
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ServiceTaxonomiesDetailsFieldsValueResourceIntTest {

    private static final ServiceTaxonomiesDetailsFields DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELD =
        ServiceTaxonomiesDetailsFields.TAXONOMY_NAME;
    private static final ServiceTaxonomiesDetailsFields UPDATED_SERVICE_TAXONOMIES_DETAILS_FIELD =
        ServiceTaxonomiesDetailsFields.TAXONOMY_DETAILS;

    @Autowired
    private ServiceTaxonomiesDetailsFieldsValueRepository serviceTaxonomiesDetailsFieldsValueRepository;

    @Autowired
    private ServiceTaxonomiesDetailsFieldsValueMapper serviceTaxonomiesDetailsFieldsValueMapper;

    @Autowired
    private ServiceTaxonomiesDetailsFieldsValueService serviceTaxonomiesDetailsFieldsValueService;

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

    private MockMvc restServiceTaxonomiesDetailsFieldsValueMockMvc;

    private ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsFieldsValue;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ServiceTaxonomiesDetailsFieldsValueResource serviceTaxonomiesDetailsFieldsValueResource =
            new ServiceTaxonomiesDetailsFieldsValueResource(serviceTaxonomiesDetailsFieldsValueService);
        this.restServiceTaxonomiesDetailsFieldsValueMockMvc = MockMvcBuilders
            .standaloneSetup(serviceTaxonomiesDetailsFieldsValueResource)
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
    public static ServiceTaxonomiesDetailsFieldsValue createEntity(EntityManager em) {
        ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsFieldsValue = new ServiceTaxonomiesDetailsFieldsValue()
            .serviceTaxonomiesDetailsField(DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELD);
        return serviceTaxonomiesDetailsFieldsValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceTaxonomiesDetailsFieldsValue createUpdatedEntity(EntityManager em) {
        ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsFieldsValue = new ServiceTaxonomiesDetailsFieldsValue()
            .serviceTaxonomiesDetailsField(UPDATED_SERVICE_TAXONOMIES_DETAILS_FIELD);
        return serviceTaxonomiesDetailsFieldsValue;
    }

    @Before
    public void initTest() {
        serviceTaxonomiesDetailsFieldsValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceTaxonomiesDetailsFieldsValue() throws Exception {
        int databaseSizeBeforeCreate = serviceTaxonomiesDetailsFieldsValueRepository.findAll().size();

        // Create the ServiceTaxonomiesDetailsFieldsValue
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO =
            serviceTaxonomiesDetailsFieldsValueMapper.toDto(serviceTaxonomiesDetailsFieldsValue);
        restServiceTaxonomiesDetailsFieldsValueMockMvc
            .perform(post("/api/service-taxonomies-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomiesDetailsFieldsValueDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceTaxonomiesDetailsFieldsValue in the database
        List<ServiceTaxonomiesDetailsFieldsValue> serviceTaxonomiesDetailsFieldsValueList =
            serviceTaxonomiesDetailsFieldsValueRepository.findAll();
        assertThat(serviceTaxonomiesDetailsFieldsValueList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceTaxonomiesDetailsFieldsValue testServiceTaxonomiesDetailsFieldsValue =
            serviceTaxonomiesDetailsFieldsValueList.get(serviceTaxonomiesDetailsFieldsValueList.size() - 1);
        assertThat(testServiceTaxonomiesDetailsFieldsValue
            .getServiceTaxonomiesDetailsField()).isEqualTo(DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELD);
    }

    @Test
    @Transactional
    public void createServiceTaxonomiesDetailsFieldsValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceTaxonomiesDetailsFieldsValueRepository.findAll().size();

        // Create the ServiceTaxonomiesDetailsFieldsValue with an existing ID
        serviceTaxonomiesDetailsFieldsValue.setId(TestConstants.UUID_1);
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO =
            serviceTaxonomiesDetailsFieldsValueMapper.toDto(serviceTaxonomiesDetailsFieldsValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceTaxonomiesDetailsFieldsValueMockMvc
            .perform(post("/api/service-taxonomies-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomiesDetailsFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceTaxonomiesDetailsFieldsValue in the database
        List<ServiceTaxonomiesDetailsFieldsValue> serviceTaxonomiesDetailsFieldsValueList =
            serviceTaxonomiesDetailsFieldsValueRepository.findAll();
        assertThat(serviceTaxonomiesDetailsFieldsValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiceTaxonomiesDetailsFieldsValues() throws Exception {
        // Initialize the database
        serviceTaxonomiesDetailsFieldsValueRepository.saveAndFlush(serviceTaxonomiesDetailsFieldsValue);

        // Get all the serviceTaxonomiesDetailsFieldsValueList
        restServiceTaxonomiesDetailsFieldsValueMockMvc
            .perform(get("/api/service-taxonomies-details-fields-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id")
                .value(hasItem(serviceTaxonomiesDetailsFieldsValue.getId().toString())))
            .andExpect(jsonPath("$.[*].serviceTaxonomiesDetailsField")
                .value(hasItem(DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getServiceTaxonomiesDetailsFieldsValue() throws Exception {
        // Initialize the database
        serviceTaxonomiesDetailsFieldsValueRepository.saveAndFlush(serviceTaxonomiesDetailsFieldsValue);

        // Get the serviceTaxonomiesDetailsFieldsValue
        restServiceTaxonomiesDetailsFieldsValueMockMvc
            .perform(get("/api/service-taxonomies-details-fields-values/{id}",
                serviceTaxonomiesDetailsFieldsValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id")
                .value(serviceTaxonomiesDetailsFieldsValue.getId().toString()))
            .andExpect(jsonPath("$.serviceTaxonomiesDetailsField")
                .value(DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceTaxonomiesDetailsFieldsValue() throws Exception {
        // Get the serviceTaxonomiesDetailsFieldsValue
        restServiceTaxonomiesDetailsFieldsValueMockMvc
            .perform(get("/api/service-taxonomies-details-fields-values/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceTaxonomiesDetailsFieldsValue() throws Exception {
        // Initialize the database
        serviceTaxonomiesDetailsFieldsValueRepository.saveAndFlush(serviceTaxonomiesDetailsFieldsValue);

        int databaseSizeBeforeUpdate = serviceTaxonomiesDetailsFieldsValueRepository.findAll().size();

        // Update the serviceTaxonomiesDetailsFieldsValue
        ServiceTaxonomiesDetailsFieldsValue updatedServiceTaxonomiesDetailsFieldsValue =
            serviceTaxonomiesDetailsFieldsValueRepository.findById(serviceTaxonomiesDetailsFieldsValue.getId()).get();
        // Disconnect from session so that the updates on updatedServiceTaxonomiesDetailsFieldsValue are not directly
        // saved in db
        em.detach(updatedServiceTaxonomiesDetailsFieldsValue);
        updatedServiceTaxonomiesDetailsFieldsValue
            .serviceTaxonomiesDetailsField(UPDATED_SERVICE_TAXONOMIES_DETAILS_FIELD);
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO =
            serviceTaxonomiesDetailsFieldsValueMapper.toDto(updatedServiceTaxonomiesDetailsFieldsValue);

        restServiceTaxonomiesDetailsFieldsValueMockMvc.perform(put("/api/service-taxonomies-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomiesDetailsFieldsValueDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceTaxonomiesDetailsFieldsValue in the database
        List<ServiceTaxonomiesDetailsFieldsValue> serviceTaxonomiesDetailsFieldsValueList =
            serviceTaxonomiesDetailsFieldsValueRepository.findAll();
        assertThat(serviceTaxonomiesDetailsFieldsValueList).hasSize(databaseSizeBeforeUpdate);
        ServiceTaxonomiesDetailsFieldsValue testServiceTaxonomiesDetailsFieldsValue =
            serviceTaxonomiesDetailsFieldsValueList.get(serviceTaxonomiesDetailsFieldsValueList.size() - 1);
        assertThat(testServiceTaxonomiesDetailsFieldsValue.getServiceTaxonomiesDetailsField())
            .isEqualTo(UPDATED_SERVICE_TAXONOMIES_DETAILS_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceTaxonomiesDetailsFieldsValue() throws Exception {
        int databaseSizeBeforeUpdate = serviceTaxonomiesDetailsFieldsValueRepository.findAll().size();

        // Create the ServiceTaxonomiesDetailsFieldsValue
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO =
            serviceTaxonomiesDetailsFieldsValueMapper.toDto(serviceTaxonomiesDetailsFieldsValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceTaxonomiesDetailsFieldsValueMockMvc.perform(put("/api/service-taxonomies-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(serviceTaxonomiesDetailsFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceTaxonomiesDetailsFieldsValue in the database
        List<ServiceTaxonomiesDetailsFieldsValue> serviceTaxonomiesDetailsFieldsValueList =
            serviceTaxonomiesDetailsFieldsValueRepository.findAll();
        assertThat(serviceTaxonomiesDetailsFieldsValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceTaxonomiesDetailsFieldsValue() throws Exception {
        // Initialize the database
        serviceTaxonomiesDetailsFieldsValueRepository.saveAndFlush(serviceTaxonomiesDetailsFieldsValue);

        int databaseSizeBeforeDelete = serviceTaxonomiesDetailsFieldsValueRepository.findAll().size();

        // Delete the serviceTaxonomiesDetailsFieldsValue
        restServiceTaxonomiesDetailsFieldsValueMockMvc
            .perform(delete("/api/service-taxonomies-details-fields-values/{id}",
                serviceTaxonomiesDetailsFieldsValue.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceTaxonomiesDetailsFieldsValue> serviceTaxonomiesDetailsFieldsValueList =
            serviceTaxonomiesDetailsFieldsValueRepository.findAll();
        assertThat(serviceTaxonomiesDetailsFieldsValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceTaxonomiesDetailsFieldsValue.class);
        ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsFieldsValue1 =
            new ServiceTaxonomiesDetailsFieldsValue();
        serviceTaxonomiesDetailsFieldsValue1.setId(TestConstants.UUID_1);
        ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsFieldsValue2 =
            new ServiceTaxonomiesDetailsFieldsValue();
        serviceTaxonomiesDetailsFieldsValue2.setId(serviceTaxonomiesDetailsFieldsValue1.getId());
        assertThat(serviceTaxonomiesDetailsFieldsValue1).isEqualTo(serviceTaxonomiesDetailsFieldsValue2);
        serviceTaxonomiesDetailsFieldsValue2.setId(TestConstants.UUID_2);
        assertThat(serviceTaxonomiesDetailsFieldsValue1).isNotEqualTo(serviceTaxonomiesDetailsFieldsValue2);
        serviceTaxonomiesDetailsFieldsValue1.setId(null);
        assertThat(serviceTaxonomiesDetailsFieldsValue1).isNotEqualTo(serviceTaxonomiesDetailsFieldsValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceTaxonomiesDetailsFieldsValueDTO.class);
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO1 =
            new ServiceTaxonomiesDetailsFieldsValueDTO();
        serviceTaxonomiesDetailsFieldsValueDTO1.setId(TestConstants.UUID_1);
        ServiceTaxonomiesDetailsFieldsValueDTO serviceTaxonomiesDetailsFieldsValueDTO2 =
            new ServiceTaxonomiesDetailsFieldsValueDTO();
        assertThat(serviceTaxonomiesDetailsFieldsValueDTO1).isNotEqualTo(serviceTaxonomiesDetailsFieldsValueDTO2);
        serviceTaxonomiesDetailsFieldsValueDTO2.setId(serviceTaxonomiesDetailsFieldsValueDTO1.getId());
        assertThat(serviceTaxonomiesDetailsFieldsValueDTO1).isEqualTo(serviceTaxonomiesDetailsFieldsValueDTO2);
        serviceTaxonomiesDetailsFieldsValueDTO2.setId(TestConstants.UUID_2);
        assertThat(serviceTaxonomiesDetailsFieldsValueDTO1).isNotEqualTo(serviceTaxonomiesDetailsFieldsValueDTO2);
        serviceTaxonomiesDetailsFieldsValueDTO1.setId(null);
        assertThat(serviceTaxonomiesDetailsFieldsValueDTO1).isNotEqualTo(serviceTaxonomiesDetailsFieldsValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceTaxonomiesDetailsFieldsValueMapper.fromId(TestConstants.UUID_42).getId())
            .isEqualTo(TestConstants.UUID_42);
        assertThat(serviceTaxonomiesDetailsFieldsValueMapper.fromId(null)).isNull();
    }
}
