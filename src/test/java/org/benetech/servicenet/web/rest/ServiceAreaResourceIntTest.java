package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ServiceArea;
import org.benetech.servicenet.interceptor.HibernateInterceptor;
import org.benetech.servicenet.repository.ServiceAreaRepository;
import org.benetech.servicenet.service.ServiceAreaService;
import org.benetech.servicenet.service.dto.ServiceAreaDTO;
import org.benetech.servicenet.service.mapper.ServiceAreaMapper;
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
 * Test class for the ServiceAreaResource REST controller.
 *
 * @see ServiceAreaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ServiceAreaResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private HibernateInterceptor hibernateInterceptor;

    @Autowired
    private ServiceAreaRepository serviceAreaRepository;

    @Autowired
    private ServiceAreaMapper serviceAreaMapper;

    @Autowired
    private ServiceAreaService serviceAreaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceAreaMockMvc;

    private ServiceArea serviceArea;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceArea createEntity(EntityManager em) {
        ServiceArea serviceArea = new ServiceArea()
            .description(DEFAULT_DESCRIPTION);
        return serviceArea;
    }

    @Before
    public void setup() {
        hibernateInterceptor.disableEventListeners();
        MockitoAnnotations.initMocks(this);
        final ServiceAreaResource serviceAreaResource = new ServiceAreaResource(serviceAreaService);
        this.restServiceAreaMockMvc = MockMvcBuilders.standaloneSetup(serviceAreaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceArea = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceArea() throws Exception {
        int databaseSizeBeforeCreate = serviceAreaRepository.findAll().size();

        // Create the ServiceArea
        ServiceAreaDTO serviceAreaDTO = serviceAreaMapper.toDto(serviceArea);
        restServiceAreaMockMvc.perform(post("/api/service-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAreaDTO)))
            .andExpect(status().isCreated());

        // Validate the ServiceArea in the database
        List<ServiceArea> serviceAreaList = serviceAreaRepository.findAll();
        assertThat(serviceAreaList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceArea testServiceArea = serviceAreaList.get(serviceAreaList.size() - 1);
        assertThat(testServiceArea.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createServiceAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceAreaRepository.findAll().size();

        // Create the ServiceArea with an existing ID
        serviceArea.setId(TestConstants.UUID_1);
        ServiceAreaDTO serviceAreaDTO = serviceAreaMapper.toDto(serviceArea);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceAreaMockMvc.perform(post("/api/service-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceArea in the database
        List<ServiceArea> serviceAreaList = serviceAreaRepository.findAll();
        assertThat(serviceAreaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllServiceAreas() throws Exception {
        // Initialize the database
        serviceAreaRepository.saveAndFlush(serviceArea);

        // Get all the serviceAreaList
        restServiceAreaMockMvc.perform(get("/api/service-areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceArea.getId().toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getServiceArea() throws Exception {
        // Initialize the database
        serviceAreaRepository.saveAndFlush(serviceArea);

        // Get the serviceArea
        restServiceAreaMockMvc.perform(get("/api/service-areas/{id}", serviceArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceArea.getId().toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceArea() throws Exception {
        // Get the serviceArea
        restServiceAreaMockMvc.perform(get("/api/service-areas/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceArea() throws Exception {
        // Initialize the database
        serviceAreaRepository.saveAndFlush(serviceArea);

        int databaseSizeBeforeUpdate = serviceAreaRepository.findAll().size();

        // Update the serviceArea
        ServiceArea updatedServiceArea = serviceAreaRepository.findById(serviceArea.getId()).get();
        // Disconnect from session so that the updates on updatedServiceArea are not directly saved in db
        em.detach(updatedServiceArea);
        updatedServiceArea
            .description(UPDATED_DESCRIPTION);
        ServiceAreaDTO serviceAreaDTO = serviceAreaMapper.toDto(updatedServiceArea);

        restServiceAreaMockMvc.perform(put("/api/service-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAreaDTO)))
            .andExpect(status().isOk());

        // Validate the ServiceArea in the database
        List<ServiceArea> serviceAreaList = serviceAreaRepository.findAll();
        assertThat(serviceAreaList).hasSize(databaseSizeBeforeUpdate);
        ServiceArea testServiceArea = serviceAreaList.get(serviceAreaList.size() - 1);
        assertThat(testServiceArea.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceArea() throws Exception {
        int databaseSizeBeforeUpdate = serviceAreaRepository.findAll().size();

        // Create the ServiceArea
        ServiceAreaDTO serviceAreaDTO = serviceAreaMapper.toDto(serviceArea);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceAreaMockMvc.perform(put("/api/service-areas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceAreaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceArea in the database
        List<ServiceArea> serviceAreaList = serviceAreaRepository.findAll();
        assertThat(serviceAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServiceArea() throws Exception {
        // Initialize the database
        serviceAreaRepository.saveAndFlush(serviceArea);

        int databaseSizeBeforeDelete = serviceAreaRepository.findAll().size();

        // Get the serviceArea
        restServiceAreaMockMvc.perform(delete("/api/service-areas/{id}", serviceArea.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceArea> serviceAreaList = serviceAreaRepository.findAll();
        assertThat(serviceAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceArea.class);
        ServiceArea serviceArea1 = new ServiceArea();
        serviceArea1.setId(TestConstants.UUID_1);
        ServiceArea serviceArea2 = new ServiceArea();
        serviceArea2.setId(serviceArea1.getId());
        assertThat(serviceArea1).isEqualTo(serviceArea2);
        serviceArea2.setId(TestConstants.UUID_2);
        assertThat(serviceArea1).isNotEqualTo(serviceArea2);
        serviceArea1.setId(null);
        assertThat(serviceArea1).isNotEqualTo(serviceArea2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceAreaDTO.class);
        ServiceAreaDTO serviceAreaDTO1 = new ServiceAreaDTO();
        serviceAreaDTO1.setId(TestConstants.UUID_1);
        ServiceAreaDTO serviceAreaDTO2 = new ServiceAreaDTO();
        assertThat(serviceAreaDTO1).isNotEqualTo(serviceAreaDTO2);
        serviceAreaDTO2.setId(serviceAreaDTO1.getId());
        assertThat(serviceAreaDTO1).isEqualTo(serviceAreaDTO2);
        serviceAreaDTO2.setId(TestConstants.UUID_2);
        assertThat(serviceAreaDTO1).isNotEqualTo(serviceAreaDTO2);
        serviceAreaDTO1.setId(null);
        assertThat(serviceAreaDTO1).isNotEqualTo(serviceAreaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceAreaMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(serviceAreaMapper.fromId(null)).isNull();
    }
}
