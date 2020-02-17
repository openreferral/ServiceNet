package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.repository.ServiceAtLocationRepository;
import org.benetech.servicenet.service.ServiceAtLocationService;
import org.benetech.servicenet.service.dto.ServiceAtLocationDTO;
import org.benetech.servicenet.service.mapper.ServiceAtLocationMapper;
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
 * Test class for the ServiceAtLocationResource REST controller.
 *
 * @see ServiceAtLocationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class ServiceAtLocationResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ServiceAtLocationRepository serviceAtLocationRepository;

    @Autowired
    private ServiceAtLocationMapper serviceAtLocationMapper;

    @Autowired
    private ServiceAtLocationService serviceAtLocationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceAtLocationMockMvc;

    private ServiceAtLocation serviceAtLocation;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceAtLocation createEntity(EntityManager em) {
        ServiceAtLocation serviceAtLocation = new ServiceAtLocation()
            .description(DEFAULT_DESCRIPTION);
        return serviceAtLocation;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceAtLocationResource serviceAtLocationResource = new ServiceAtLocationResource(serviceAtLocationService);
        this.restServiceAtLocationMockMvc = MockMvcBuilders.standaloneSetup(serviceAtLocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceAtLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceAtLocation() throws Exception {
        int databaseSizeBeforeCreate = serviceAtLocationRepository.findAll().size();

        // Create the ServiceAtLocation
        ServiceAtLocationDTO serviceAtLocationDTO = serviceAtLocationMapper.toDto(serviceAtLocation);
        restServiceAtLocationMockMvc.perform(post("/api/service-at-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAtLocationDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceAtLocation in the database
        List<ServiceAtLocation> serviceAtLocationList = serviceAtLocationRepository.findAll();
        assertThat(serviceAtLocationList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceAtLocation testServiceAtLocation = serviceAtLocationList.get(serviceAtLocationList.size() - 1);
        assertThat(testServiceAtLocation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createServiceAtLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceAtLocationRepository.findAll().size();

        // Create the ServiceAtLocation with an existing ID
        serviceAtLocation.setId(TestConstants.UUID_1);
        ServiceAtLocationDTO serviceAtLocationDTO = serviceAtLocationMapper.toDto(serviceAtLocation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceAtLocationMockMvc.perform(post("/api/service-at-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAtLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceAtLocation in the database
        List<ServiceAtLocation> serviceAtLocationList = serviceAtLocationRepository.findAll();
        assertThat(serviceAtLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiceAtLocations() throws Exception {
        // Initialize the database
        serviceAtLocationRepository.saveAndFlush(serviceAtLocation);

        // Get all the serviceAtLocationList
        restServiceAtLocationMockMvc.perform(get("/api/service-at-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceAtLocation.getId().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getServiceAtLocation() throws Exception {
        // Initialize the database
        serviceAtLocationRepository.saveAndFlush(serviceAtLocation);

        // Get the serviceAtLocation
        restServiceAtLocationMockMvc.perform(get("/api/service-at-locations/{id}", serviceAtLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceAtLocation.getId().toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceAtLocation() throws Exception {
        // Get the serviceAtLocation
        restServiceAtLocationMockMvc.perform(get("/api/service-at-locations/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceAtLocation() throws Exception {
        // Initialize the database
        serviceAtLocationRepository.saveAndFlush(serviceAtLocation);

        int databaseSizeBeforeUpdate = serviceAtLocationRepository.findAll().size();

        // Update the serviceAtLocation
        ServiceAtLocation updatedServiceAtLocation = serviceAtLocationRepository.findById(serviceAtLocation.getId()).get();
        // Disconnect from session so that the updates on updatedServiceAtLocation are not directly saved in db
        em.detach(updatedServiceAtLocation);
        updatedServiceAtLocation
            .description(UPDATED_DESCRIPTION);
        ServiceAtLocationDTO serviceAtLocationDTO = serviceAtLocationMapper.toDto(updatedServiceAtLocation);

        restServiceAtLocationMockMvc.perform(put("/api/service-at-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAtLocationDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceAtLocation in the database
        List<ServiceAtLocation> serviceAtLocationList = serviceAtLocationRepository.findAll();
        assertThat(serviceAtLocationList).hasSize(databaseSizeBeforeUpdate);
        ServiceAtLocation testServiceAtLocation = serviceAtLocationList.get(serviceAtLocationList.size() - 1);
        assertThat(testServiceAtLocation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceAtLocation() throws Exception {
        int databaseSizeBeforeUpdate = serviceAtLocationRepository.findAll().size();

        // Create the ServiceAtLocation
        ServiceAtLocationDTO serviceAtLocationDTO = serviceAtLocationMapper.toDto(serviceAtLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceAtLocationMockMvc.perform(put("/api/service-at-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAtLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceAtLocation in the database
        List<ServiceAtLocation> serviceAtLocationList = serviceAtLocationRepository.findAll();
        assertThat(serviceAtLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceAtLocation() throws Exception {
        // Initialize the database
        serviceAtLocationRepository.saveAndFlush(serviceAtLocation);

        int databaseSizeBeforeDelete = serviceAtLocationRepository.findAll().size();

        // Get the serviceAtLocation
        restServiceAtLocationMockMvc.perform(delete("/api/service-at-locations/{id}", serviceAtLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceAtLocation> serviceAtLocationList = serviceAtLocationRepository.findAll();
        assertThat(serviceAtLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceAtLocation.class);
        ServiceAtLocation serviceAtLocation1 = new ServiceAtLocation();
        serviceAtLocation1.setId(TestConstants.UUID_1);
        ServiceAtLocation serviceAtLocation2 = new ServiceAtLocation();
        serviceAtLocation2.setId(serviceAtLocation1.getId());
        assertThat(serviceAtLocation1).isEqualTo(serviceAtLocation2);
        serviceAtLocation2.setId(TestConstants.UUID_2);
        assertThat(serviceAtLocation1).isNotEqualTo(serviceAtLocation2);
        serviceAtLocation1.setId(null);
        assertThat(serviceAtLocation1).isNotEqualTo(serviceAtLocation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceAtLocationDTO.class);
        ServiceAtLocationDTO serviceAtLocationDTO1 = new ServiceAtLocationDTO();
        serviceAtLocationDTO1.setId(TestConstants.UUID_1);
        ServiceAtLocationDTO serviceAtLocationDTO2 = new ServiceAtLocationDTO();
        assertThat(serviceAtLocationDTO1).isNotEqualTo(serviceAtLocationDTO2);
        serviceAtLocationDTO2.setId(serviceAtLocationDTO1.getId());
        assertThat(serviceAtLocationDTO1).isEqualTo(serviceAtLocationDTO2);
        serviceAtLocationDTO2.setId(TestConstants.UUID_2);
        assertThat(serviceAtLocationDTO1).isNotEqualTo(serviceAtLocationDTO2);
        serviceAtLocationDTO1.setId(null);
        assertThat(serviceAtLocationDTO1).isNotEqualTo(serviceAtLocationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceAtLocationMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(serviceAtLocationMapper.fromId(null)).isNull();
    }
}
