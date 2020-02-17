package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.LocationFieldsValue;
import org.benetech.servicenet.repository.LocationFieldsValueRepository;
import org.benetech.servicenet.service.LocationFieldsValueService;
import org.benetech.servicenet.service.dto.LocationFieldsValueDTO;
import org.benetech.servicenet.service.mapper.LocationFieldsValueMapper;
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

import org.benetech.servicenet.domain.enumeration.LocationFields;
/**
 * Integration tests for the {@link LocationFieldsValueResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class LocationFieldsValueResourceIntTest {

    private static final LocationFields DEFAULT_LOCATION_FIELD = LocationFields.NAME;
    private static final LocationFields UPDATED_LOCATION_FIELD = LocationFields.ALTERNATE_NAME;

    @Autowired
    private LocationFieldsValueRepository locationFieldsValueRepository;

    @Autowired
    private LocationFieldsValueMapper locationFieldsValueMapper;

    @Autowired
    private LocationFieldsValueService locationFieldsValueService;

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

    private MockMvc restLocationFieldsValueMockMvc;

    private LocationFieldsValue locationFieldsValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocationFieldsValueResource locationFieldsValueResource = new LocationFieldsValueResource(locationFieldsValueService);
        this.restLocationFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(locationFieldsValueResource)
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
    public static LocationFieldsValue createEntity(EntityManager em) {
        LocationFieldsValue locationFieldsValue = new LocationFieldsValue()
            .locationField(DEFAULT_LOCATION_FIELD);
        return locationFieldsValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationFieldsValue createUpdatedEntity(EntityManager em) {
        LocationFieldsValue locationFieldsValue = new LocationFieldsValue()
            .locationField(UPDATED_LOCATION_FIELD);
        return locationFieldsValue;
    }

    @Before
    public void initTest() {
        locationFieldsValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocationFieldsValue() throws Exception {
        int databaseSizeBeforeCreate = locationFieldsValueRepository.findAll().size();

        // Create the LocationFieldsValue
        LocationFieldsValueDTO locationFieldsValueDTO = locationFieldsValueMapper.toDto(locationFieldsValue);
        restLocationFieldsValueMockMvc.perform(post("/api/location-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationFieldsValueDTO)))
            .andExpect(status().isCreated());

        // Validate the LocationFieldsValue in the database
        List<LocationFieldsValue> locationFieldsValueList = locationFieldsValueRepository.findAll();
        assertThat(locationFieldsValueList).hasSize(databaseSizeBeforeCreate + 1);
        LocationFieldsValue testLocationFieldsValue = locationFieldsValueList.get(locationFieldsValueList.size() - 1);
        assertThat(testLocationFieldsValue.getLocationField()).isEqualTo(DEFAULT_LOCATION_FIELD);
    }

    @Test
    @Transactional
    public void createLocationFieldsValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = locationFieldsValueRepository.findAll().size();

        // Create the LocationFieldsValue with an existing ID
        locationFieldsValue.setId(TestConstants.UUID_1);
        LocationFieldsValueDTO locationFieldsValueDTO = locationFieldsValueMapper.toDto(locationFieldsValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationFieldsValueMockMvc.perform(post("/api/location-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LocationFieldsValue in the database
        List<LocationFieldsValue> locationFieldsValueList = locationFieldsValueRepository.findAll();
        assertThat(locationFieldsValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLocationFieldsValues() throws Exception {
        // Initialize the database
        locationFieldsValueRepository.saveAndFlush(locationFieldsValue);

        // Get all the locationFieldsValueList
        restLocationFieldsValueMockMvc.perform(get("/api/location-fields-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationFieldsValue.getId().toString())))
            .andExpect(jsonPath("$.[*].locationField").value(hasItem(DEFAULT_LOCATION_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getLocationFieldsValue() throws Exception {
        // Initialize the database
        locationFieldsValueRepository.saveAndFlush(locationFieldsValue);

        // Get the locationFieldsValue
        restLocationFieldsValueMockMvc.perform(get("/api/location-fields-values/{id}", locationFieldsValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(locationFieldsValue.getId().toString()))
            .andExpect(jsonPath("$.locationField").value(DEFAULT_LOCATION_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocationFieldsValue() throws Exception {
        // Get the locationFieldsValue
        restLocationFieldsValueMockMvc.perform(get("/api/location-fields-values/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocationFieldsValue() throws Exception {
        // Initialize the database
        locationFieldsValueRepository.saveAndFlush(locationFieldsValue);

        int databaseSizeBeforeUpdate = locationFieldsValueRepository.findAll().size();

        // Update the locationFieldsValue
        LocationFieldsValue updatedLocationFieldsValue = locationFieldsValueRepository.findById(locationFieldsValue.getId()).get();
        // Disconnect from session so that the updates on updatedLocationFieldsValue are not directly saved in db
        em.detach(updatedLocationFieldsValue);
        updatedLocationFieldsValue
            .locationField(UPDATED_LOCATION_FIELD);
        LocationFieldsValueDTO locationFieldsValueDTO = locationFieldsValueMapper.toDto(updatedLocationFieldsValue);

        restLocationFieldsValueMockMvc.perform(put("/api/location-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationFieldsValueDTO)))
            .andExpect(status().isOk());

        // Validate the LocationFieldsValue in the database
        List<LocationFieldsValue> locationFieldsValueList = locationFieldsValueRepository.findAll();
        assertThat(locationFieldsValueList).hasSize(databaseSizeBeforeUpdate);
        LocationFieldsValue testLocationFieldsValue = locationFieldsValueList.get(locationFieldsValueList.size() - 1);
        assertThat(testLocationFieldsValue.getLocationField()).isEqualTo(UPDATED_LOCATION_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingLocationFieldsValue() throws Exception {
        int databaseSizeBeforeUpdate = locationFieldsValueRepository.findAll().size();

        // Create the LocationFieldsValue
        LocationFieldsValueDTO locationFieldsValueDTO = locationFieldsValueMapper.toDto(locationFieldsValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationFieldsValueMockMvc.perform(put("/api/location-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(locationFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LocationFieldsValue in the database
        List<LocationFieldsValue> locationFieldsValueList = locationFieldsValueRepository.findAll();
        assertThat(locationFieldsValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLocationFieldsValue() throws Exception {
        // Initialize the database
        locationFieldsValueRepository.saveAndFlush(locationFieldsValue);

        int databaseSizeBeforeDelete = locationFieldsValueRepository.findAll().size();

        // Delete the locationFieldsValue
        restLocationFieldsValueMockMvc.perform(delete("/api/location-fields-values/{id}", locationFieldsValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationFieldsValue> locationFieldsValueList = locationFieldsValueRepository.findAll();
        assertThat(locationFieldsValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationFieldsValue.class);
        LocationFieldsValue locationFieldsValue1 = new LocationFieldsValue();
        locationFieldsValue1.setId(TestConstants.UUID_1);
        LocationFieldsValue locationFieldsValue2 = new LocationFieldsValue();
        locationFieldsValue2.setId(locationFieldsValue1.getId());
        assertThat(locationFieldsValue1).isEqualTo(locationFieldsValue2);
        locationFieldsValue2.setId(TestConstants.UUID_2);
        assertThat(locationFieldsValue1).isNotEqualTo(locationFieldsValue2);
        locationFieldsValue1.setId(null);
        assertThat(locationFieldsValue1).isNotEqualTo(locationFieldsValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationFieldsValueDTO.class);
        LocationFieldsValueDTO locationFieldsValueDTO1 = new LocationFieldsValueDTO();
        locationFieldsValueDTO1.setId(TestConstants.UUID_1);
        LocationFieldsValueDTO locationFieldsValueDTO2 = new LocationFieldsValueDTO();
        assertThat(locationFieldsValueDTO1).isNotEqualTo(locationFieldsValueDTO2);
        locationFieldsValueDTO2.setId(locationFieldsValueDTO1.getId());
        assertThat(locationFieldsValueDTO1).isEqualTo(locationFieldsValueDTO2);
        locationFieldsValueDTO2.setId(TestConstants.UUID_2);
        assertThat(locationFieldsValueDTO1).isNotEqualTo(locationFieldsValueDTO2);
        locationFieldsValueDTO1.setId(null);
        assertThat(locationFieldsValueDTO1).isNotEqualTo(locationFieldsValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(locationFieldsValueMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(locationFieldsValueMapper.fromId(null)).isNull();
    }
}
