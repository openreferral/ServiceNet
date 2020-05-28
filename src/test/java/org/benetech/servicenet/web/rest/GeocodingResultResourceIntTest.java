package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;

import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.mother.GeocodingResultMother;
import org.benetech.servicenet.repository.GeocodingResultRepository;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.dto.GeocodingResultDTO;
import org.benetech.servicenet.service.mapper.GeocodingResultMapper;
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

import static org.benetech.servicenet.mother.GeocodingResultMother.DEFAULT_ADDRESS;
import static org.benetech.servicenet.mother.GeocodingResultMother.DEFAULT_LATITUDE;
import static org.benetech.servicenet.mother.GeocodingResultMother.DEFAULT_LONGITUDE;
import static org.benetech.servicenet.mother.GeocodingResultMother.UPDATED_ADDRESS;
import static org.benetech.servicenet.mother.GeocodingResultMother.UPDATED_LATITUDE;
import static org.benetech.servicenet.mother.GeocodingResultMother.UPDATED_LONGITUDE;
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
 * Test class for the GeocodingResultResource REST controller.
 *
 * @see GeocodingResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class GeocodingResultResourceIntTest {

    @Autowired
    private GeocodingResultRepository geocodingResultRepository;

    @Autowired
    private GeocodingResultMapper geocodingResultMapper;

    @Autowired
    private GeocodingResultService geocodingResultService;

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

    private MockMvc restGeocodingResultMockMvc;

    private GeocodingResult geocodingResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GeocodingResultResource geocodingResultResource = new GeocodingResultResource(geocodingResultService);
        this.restGeocodingResultMockMvc = MockMvcBuilders.standaloneSetup(geocodingResultResource)
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
    public static GeocodingResult createEntity() {
        return GeocodingResultMother.generateDefault();
    }

    @Before
    public void initTest() {
        geocodingResult = createEntity();
    }

    @Test
    @Transactional
    public void createGeocodingResult() throws Exception {
        int databaseSizeBeforeCreate = geocodingResultRepository.findAll().size();

        // Create the GeocodingResult
        GeocodingResultDTO geocodingResultDTO = geocodingResultMapper.toDto(geocodingResult);
        restGeocodingResultMockMvc.perform(post("/api/geocoding-results")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geocodingResultDTO)))
            .andExpect(status().isCreated());

        // Validate the GeocodingResult in the database
        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeCreate + 1);
        GeocodingResult testGeocodingResult = geocodingResultList.get(geocodingResultList.size() - 1);
        assertThat(testGeocodingResult.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testGeocodingResult.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testGeocodingResult.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    public void createGeocodingResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = geocodingResultRepository.findAll().size();

        // Create the GeocodingResult with an existing ID
        geocodingResult.setId(TestConstants.UUID_1);
        GeocodingResultDTO geocodingResultDTO = geocodingResultMapper.toDto(geocodingResult);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGeocodingResultMockMvc.perform(post("/api/geocoding-results")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geocodingResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeocodingResult in the database
        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = geocodingResultRepository.findAll().size();
        // set the field null
        geocodingResult.setAddress(null);

        // Create the GeocodingResult, which fails.
        GeocodingResultDTO geocodingResultDTO = geocodingResultMapper.toDto(geocodingResult);

        restGeocodingResultMockMvc.perform(post("/api/geocoding-results")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geocodingResultDTO)))
            .andExpect(status().isBadRequest());

        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = geocodingResultRepository.findAll().size();
        // set the field null
        geocodingResult.setLatitude(null);

        // Create the GeocodingResult, which fails.
        GeocodingResultDTO geocodingResultDTO = geocodingResultMapper.toDto(geocodingResult);

        restGeocodingResultMockMvc.perform(post("/api/geocoding-results")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geocodingResultDTO)))
            .andExpect(status().isBadRequest());

        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudeIsRequired() throws Exception {
        int databaseSizeBeforeTest = geocodingResultRepository.findAll().size();
        // set the field null
        geocodingResult.setLongitude(null);

        // Create the GeocodingResult, which fails.
        GeocodingResultDTO geocodingResultDTO = geocodingResultMapper.toDto(geocodingResult);

        restGeocodingResultMockMvc.perform(post("/api/geocoding-results")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geocodingResultDTO)))
            .andExpect(status().isBadRequest());

        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGeocodingResults() throws Exception {
        // Initialize the database
        geocodingResultRepository.saveAndFlush(geocodingResult);

        // Get all the geocodingResultList
        restGeocodingResultMockMvc.perform(get("/api/geocoding-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(geocodingResult.getId().toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())));
    }

    @Test
    @Transactional
    public void getGeocodingResult() throws Exception {
        // Initialize the database
        geocodingResultRepository.saveAndFlush(geocodingResult);

        // Get the geocodingResult
        restGeocodingResultMockMvc.perform(get("/api/geocoding-results/{id}", geocodingResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(geocodingResult.getId().toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGeocodingResult() throws Exception {
        // Get the geocodingResult
        restGeocodingResultMockMvc.perform(get("/api/geocoding-results/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGeocodingResult() throws Exception {
        // Initialize the database
        geocodingResultRepository.saveAndFlush(geocodingResult);

        int databaseSizeBeforeUpdate = geocodingResultRepository.findAll().size();

        // Update the geocodingResult
        GeocodingResult updatedGeocodingResult = geocodingResultRepository.findById(geocodingResult.getId()).get();
        // Disconnect from session so that the updates on updatedGeocodingResult are not directly saved in db
        em.detach(updatedGeocodingResult);
        updatedGeocodingResult
            .address(UPDATED_ADDRESS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        GeocodingResultDTO geocodingResultDTO = geocodingResultMapper.toDto(updatedGeocodingResult);

        restGeocodingResultMockMvc.perform(put("/api/geocoding-results")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geocodingResultDTO)))
            .andExpect(status().isOk());

        // Validate the GeocodingResult in the database
        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeUpdate);
        GeocodingResult testGeocodingResult = geocodingResultList.get(geocodingResultList.size() - 1);
        assertThat(testGeocodingResult.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testGeocodingResult.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testGeocodingResult.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void updateNonExistingGeocodingResult() throws Exception {
        int databaseSizeBeforeUpdate = geocodingResultRepository.findAll().size();

        // Create the GeocodingResult
        GeocodingResultDTO geocodingResultDTO = geocodingResultMapper.toDto(geocodingResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGeocodingResultMockMvc.perform(put("/api/geocoding-results")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(geocodingResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GeocodingResult in the database
        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGeocodingResult() throws Exception {
        // Initialize the database
        geocodingResultRepository.saveAndFlush(geocodingResult);

        int databaseSizeBeforeDelete = geocodingResultRepository.findAll().size();

        // Delete the geocodingResult
        restGeocodingResultMockMvc.perform(delete("/api/geocoding-results/{id}", geocodingResult.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GeocodingResult> geocodingResultList = geocodingResultRepository.findAll();
        assertThat(geocodingResultList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeocodingResult.class);
        GeocodingResult geocodingResult1 = new GeocodingResult();
        geocodingResult1.setId(TestConstants.UUID_1);
        GeocodingResult geocodingResult2 = new GeocodingResult();
        geocodingResult2.setId(geocodingResult1.getId());
        assertThat(geocodingResult1).isEqualTo(geocodingResult2);
        geocodingResult2.setId(TestConstants.UUID_2);
        assertThat(geocodingResult1).isNotEqualTo(geocodingResult2);
        geocodingResult1.setId(null);
        assertThat(geocodingResult1).isNotEqualTo(geocodingResult2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeocodingResultDTO.class);
        GeocodingResultDTO geocodingResultDTO1 = new GeocodingResultDTO();
        geocodingResultDTO1.setId(TestConstants.UUID_1);
        GeocodingResultDTO geocodingResultDTO2 = new GeocodingResultDTO();
        assertThat(geocodingResultDTO1).isNotEqualTo(geocodingResultDTO2);
        geocodingResultDTO2.setId(geocodingResultDTO1.getId());
        assertThat(geocodingResultDTO1).isEqualTo(geocodingResultDTO2);
        geocodingResultDTO2.setId(TestConstants.UUID_2);
        assertThat(geocodingResultDTO1).isNotEqualTo(geocodingResultDTO2);
        geocodingResultDTO1.setId(null);
        assertThat(geocodingResultDTO1).isNotEqualTo(geocodingResultDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(geocodingResultMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(geocodingResultMapper.fromId(null)).isNull();
    }
}
