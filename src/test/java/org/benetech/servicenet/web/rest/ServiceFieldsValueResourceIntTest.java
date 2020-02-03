package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ServiceFieldsValue;
import org.benetech.servicenet.repository.ServiceFieldsValueRepository;
import org.benetech.servicenet.service.ServiceFieldsValueService;
import org.benetech.servicenet.service.dto.ServiceFieldsValueDTO;
import org.benetech.servicenet.service.mapper.ServiceFieldsValueMapper;
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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.benetech.servicenet.domain.enumeration.ServiceFields;
/**
 * Integration tests for the {@link ServiceFieldsValueResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ServiceFieldsValueResourceIntTest {

    private static final ServiceFields DEFAULT_SERVICE_FIELD = ServiceFields.NAME;
    private static final ServiceFields UPDATED_SERVICE_FIELD = ServiceFields.ALTERNATE_NAME;

    @Autowired
    private ServiceFieldsValueRepository serviceFieldsValueRepository;

    @Autowired
    private ServiceFieldsValueMapper serviceFieldsValueMapper;

    @Autowired
    private ServiceFieldsValueService serviceFieldsValueService;

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

    private MockMvc restServiceFieldsValueMockMvc;

    private ServiceFieldsValue serviceFieldsValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceFieldsValueResource serviceFieldsValueResource = new ServiceFieldsValueResource(serviceFieldsValueService);
        this.restServiceFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(serviceFieldsValueResource)
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
    public static ServiceFieldsValue createEntity(EntityManager em) {
        ServiceFieldsValue serviceFieldsValue = new ServiceFieldsValue()
            .serviceField(DEFAULT_SERVICE_FIELD);
        return serviceFieldsValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceFieldsValue createUpdatedEntity(EntityManager em) {
        ServiceFieldsValue serviceFieldsValue = new ServiceFieldsValue()
            .serviceField(UPDATED_SERVICE_FIELD);
        return serviceFieldsValue;
    }

    @Before
    public void initTest() {
        serviceFieldsValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceFieldsValue() throws Exception {
        int databaseSizeBeforeCreate = serviceFieldsValueRepository.findAll().size();

        // Create the ServiceFieldsValue
        ServiceFieldsValueDTO serviceFieldsValueDTO = serviceFieldsValueMapper.toDto(serviceFieldsValue);
        restServiceFieldsValueMockMvc.perform(post("/api/service-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFieldsValueDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceFieldsValue in the database
        List<ServiceFieldsValue> serviceFieldsValueList = serviceFieldsValueRepository.findAll();
        assertThat(serviceFieldsValueList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceFieldsValue testServiceFieldsValue = serviceFieldsValueList.get(serviceFieldsValueList.size() - 1);
        assertThat(testServiceFieldsValue.getServiceField()).isEqualTo(DEFAULT_SERVICE_FIELD);
    }

    @Test
    @Transactional
    public void createServiceFieldsValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceFieldsValueRepository.findAll().size();

        // Create the ServiceFieldsValue with an existing ID
        serviceFieldsValue.setId(TestConstants.UUID_1);
        ServiceFieldsValueDTO serviceFieldsValueDTO = serviceFieldsValueMapper.toDto(serviceFieldsValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceFieldsValueMockMvc.perform(post("/api/service-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceFieldsValue in the database
        List<ServiceFieldsValue> serviceFieldsValueList = serviceFieldsValueRepository.findAll();
        assertThat(serviceFieldsValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiceFieldsValues() throws Exception {
        // Initialize the database
        serviceFieldsValueRepository.saveAndFlush(serviceFieldsValue);

        // Get all the serviceFieldsValueList
        restServiceFieldsValueMockMvc.perform(get("/api/service-fields-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceFieldsValue.getId().toString())))
            .andExpect(jsonPath("$.[*].serviceField").value(hasItem(DEFAULT_SERVICE_FIELD.toString())));
    }
    
    @Test
    @Transactional
    public void getServiceFieldsValue() throws Exception {
        // Initialize the database
        serviceFieldsValueRepository.saveAndFlush(serviceFieldsValue);

        // Get the serviceFieldsValue
        restServiceFieldsValueMockMvc.perform(get("/api/service-fields-values/{id}", serviceFieldsValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceFieldsValue.getId().toString()))
            .andExpect(jsonPath("$.serviceField").value(DEFAULT_SERVICE_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceFieldsValue() throws Exception {
        // Get the serviceFieldsValue
        restServiceFieldsValueMockMvc.perform(get("/api/service-fields-values/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceFieldsValue() throws Exception {
        // Initialize the database
        serviceFieldsValueRepository.saveAndFlush(serviceFieldsValue);

        int databaseSizeBeforeUpdate = serviceFieldsValueRepository.findAll().size();

        // Update the serviceFieldsValue
        ServiceFieldsValue updatedServiceFieldsValue = serviceFieldsValueRepository.findById(serviceFieldsValue.getId()).get();
        // Disconnect from session so that the updates on updatedServiceFieldsValue are not directly saved in db
        em.detach(updatedServiceFieldsValue);
        updatedServiceFieldsValue
            .serviceField(UPDATED_SERVICE_FIELD);
        ServiceFieldsValueDTO serviceFieldsValueDTO = serviceFieldsValueMapper.toDto(updatedServiceFieldsValue);

        restServiceFieldsValueMockMvc.perform(put("/api/service-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFieldsValueDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceFieldsValue in the database
        List<ServiceFieldsValue> serviceFieldsValueList = serviceFieldsValueRepository.findAll();
        assertThat(serviceFieldsValueList).hasSize(databaseSizeBeforeUpdate);
        ServiceFieldsValue testServiceFieldsValue = serviceFieldsValueList.get(serviceFieldsValueList.size() - 1);
        assertThat(testServiceFieldsValue.getServiceField()).isEqualTo(UPDATED_SERVICE_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceFieldsValue() throws Exception {
        int databaseSizeBeforeUpdate = serviceFieldsValueRepository.findAll().size();

        // Create the ServiceFieldsValue
        ServiceFieldsValueDTO serviceFieldsValueDTO = serviceFieldsValueMapper.toDto(serviceFieldsValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceFieldsValueMockMvc.perform(put("/api/service-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceFieldsValue in the database
        List<ServiceFieldsValue> serviceFieldsValueList = serviceFieldsValueRepository.findAll();
        assertThat(serviceFieldsValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceFieldsValue() throws Exception {
        // Initialize the database
        serviceFieldsValueRepository.saveAndFlush(serviceFieldsValue);

        int databaseSizeBeforeDelete = serviceFieldsValueRepository.findAll().size();

        // Delete the serviceFieldsValue
        restServiceFieldsValueMockMvc.perform(delete("/api/service-fields-values/{id}", serviceFieldsValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceFieldsValue> serviceFieldsValueList = serviceFieldsValueRepository.findAll();
        assertThat(serviceFieldsValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceFieldsValue.class);
        ServiceFieldsValue serviceFieldsValue1 = new ServiceFieldsValue();
        serviceFieldsValue1.setId(TestConstants.UUID_1);
        ServiceFieldsValue serviceFieldsValue2 = new ServiceFieldsValue();
        serviceFieldsValue2.setId(serviceFieldsValue1.getId());
        assertThat(serviceFieldsValue1).isEqualTo(serviceFieldsValue2);
        serviceFieldsValue2.setId(TestConstants.UUID_2);
        assertThat(serviceFieldsValue1).isNotEqualTo(serviceFieldsValue2);
        serviceFieldsValue1.setId(null);
        assertThat(serviceFieldsValue1).isNotEqualTo(serviceFieldsValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceFieldsValueDTO.class);
        ServiceFieldsValueDTO serviceFieldsValueDTO1 = new ServiceFieldsValueDTO();
        serviceFieldsValueDTO1.setId(TestConstants.UUID_1);
        ServiceFieldsValueDTO serviceFieldsValueDTO2 = new ServiceFieldsValueDTO();
        assertThat(serviceFieldsValueDTO1).isNotEqualTo(serviceFieldsValueDTO2);
        serviceFieldsValueDTO2.setId(serviceFieldsValueDTO1.getId());
        assertThat(serviceFieldsValueDTO1).isEqualTo(serviceFieldsValueDTO2);
        serviceFieldsValueDTO2.setId(TestConstants.UUID_2);
        assertThat(serviceFieldsValueDTO1).isNotEqualTo(serviceFieldsValueDTO2);
        serviceFieldsValueDTO1.setId(null);
        assertThat(serviceFieldsValueDTO1).isNotEqualTo(serviceFieldsValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceFieldsValueMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(serviceFieldsValueMapper.fromId(null)).isNull();
    }
}
