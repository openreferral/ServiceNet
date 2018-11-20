package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.repository.ServiceRepository;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.dto.ServiceDTO;
import org.benetech.servicenet.service.mapper.ServiceMapper;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.benetech.servicenet.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ServiceResource REST controller.
 *
 * @see ServiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ServiceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_INTERPRETATION_SERVICES = "AAAAAAAAAA";
    private static final String UPDATED_INTERPRETATION_SERVICES = "BBBBBBBBBB";

    private static final String DEFAULT_APPLICATION_PROCESS = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_PROCESS = "BBBBBBBBBB";

    private static final String DEFAULT_WAIT_TIME = "AAAAAAAAAA";
    private static final String UPDATED_WAIT_TIME = "BBBBBBBBBB";

    private static final String DEFAULT_FEES = "AAAAAAAAAA";
    private static final String UPDATED_FEES = "BBBBBBBBBB";

    private static final String DEFAULT_ACCREDITATIONS = "AAAAAAAAAA";
    private static final String UPDATED_ACCREDITATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSES = "AAAAAAAAAA";
    private static final String UPDATED_LICENSES = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceMockMvc;

    private Service service;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Service createEntity(EntityManager em) {
        Service service = new Service()
            .name(DEFAULT_NAME)
            .alternateName(DEFAULT_ALTERNATE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .email(DEFAULT_EMAIL)
            .status(DEFAULT_STATUS)
            .interpretationServices(DEFAULT_INTERPRETATION_SERVICES)
            .applicationProcess(DEFAULT_APPLICATION_PROCESS)
            .waitTime(DEFAULT_WAIT_TIME)
            .fees(DEFAULT_FEES)
            .accreditations(DEFAULT_ACCREDITATIONS)
            .licenses(DEFAULT_LICENSES)
            .type(DEFAULT_TYPE)
            .updatedAt(DEFAULT_UPDATED_AT);
        return service;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceResource serviceResource = new ServiceResource(serviceService);
        this.restServiceMockMvc = MockMvcBuilders.standaloneSetup(serviceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        service = createEntity(em);
    }

    @Test
    @Transactional
    public void createService() throws Exception {
        int databaseSizeBeforeCreate = serviceRepository.findAll().size();

        // Create the Service
        ServiceDTO serviceDTO = serviceMapper.toDto(service);
        restServiceMockMvc.perform(post("/api/services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceDTO)))
            .andExpect(status().isCreated());

        // Validate the Service in the database
        List<Service> serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeCreate + 1);
        Service testService = serviceList.get(serviceList.size() - 1);
        assertThat(testService.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testService.getAlternateName()).isEqualTo(DEFAULT_ALTERNATE_NAME);
        assertThat(testService.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testService.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testService.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testService.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testService.getInterpretationServices()).isEqualTo(DEFAULT_INTERPRETATION_SERVICES);
        assertThat(testService.getApplicationProcess()).isEqualTo(DEFAULT_APPLICATION_PROCESS);
        assertThat(testService.getWaitTime()).isEqualTo(DEFAULT_WAIT_TIME);
        assertThat(testService.getFees()).isEqualTo(DEFAULT_FEES);
        assertThat(testService.getAccreditations()).isEqualTo(DEFAULT_ACCREDITATIONS);
        assertThat(testService.getLicenses()).isEqualTo(DEFAULT_LICENSES);
        assertThat(testService.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testService.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createServiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceRepository.findAll().size();

        // Create the Service with an existing ID
        service.setId(TestConstants.UUID_1);
        ServiceDTO serviceDTO = serviceMapper.toDto(service);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceMockMvc.perform(post("/api/services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Service in the database
        List<Service> serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRepository.findAll().size();
        // set the field null
        service.setName(null);

        // Create the Service, which fails.
        ServiceDTO serviceDTO = serviceMapper.toDto(service);

        restServiceMockMvc.perform(post("/api/services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceDTO)))
            .andExpect(status().isBadRequest());

        List<Service> serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServices() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Get all the serviceList
        restServiceMockMvc.perform(get("/api/services?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(service.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].alternateName").value(hasItem(DEFAULT_ALTERNATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].interpretationServices").value(hasItem(DEFAULT_INTERPRETATION_SERVICES.toString())))
            .andExpect(jsonPath("$.[*].applicationProcess").value(hasItem(DEFAULT_APPLICATION_PROCESS.toString())))
            .andExpect(jsonPath("$.[*].waitTime").value(hasItem(DEFAULT_WAIT_TIME.toString())))
            .andExpect(jsonPath("$.[*].fees").value(hasItem(DEFAULT_FEES.toString())))
            .andExpect(jsonPath("$.[*].accreditations").value(hasItem(DEFAULT_ACCREDITATIONS.toString())))
            .andExpect(jsonPath("$.[*].licenses").value(hasItem(DEFAULT_LICENSES.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    public void getService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        // Get the service
        restServiceMockMvc.perform(get("/api/services/{id}", service.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(service.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alternateName").value(DEFAULT_ALTERNATE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.interpretationServices").value(DEFAULT_INTERPRETATION_SERVICES.toString()))
            .andExpect(jsonPath("$.applicationProcess").value(DEFAULT_APPLICATION_PROCESS.toString()))
            .andExpect(jsonPath("$.waitTime").value(DEFAULT_WAIT_TIME.toString()))
            .andExpect(jsonPath("$.fees").value(DEFAULT_FEES.toString()))
            .andExpect(jsonPath("$.accreditations").value(DEFAULT_ACCREDITATIONS.toString()))
            .andExpect(jsonPath("$.licenses").value(DEFAULT_LICENSES.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingService() throws Exception {
        // Get the service
        restServiceMockMvc.perform(get("/api/services/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        int databaseSizeBeforeUpdate = serviceRepository.findAll().size();

        // Update the service
        Service updatedService = serviceRepository.findById(service.getId()).get();
        // Disconnect from session so that the updates on updatedService are not directly saved in db
        em.detach(updatedService);
        updatedService
            .name(UPDATED_NAME)
            .alternateName(UPDATED_ALTERNATE_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .email(UPDATED_EMAIL)
            .status(UPDATED_STATUS)
            .interpretationServices(UPDATED_INTERPRETATION_SERVICES)
            .applicationProcess(UPDATED_APPLICATION_PROCESS)
            .waitTime(UPDATED_WAIT_TIME)
            .fees(UPDATED_FEES)
            .accreditations(UPDATED_ACCREDITATIONS)
            .licenses(UPDATED_LICENSES)
            .type(UPDATED_TYPE)
            .updatedAt(UPDATED_UPDATED_AT);
        ServiceDTO serviceDTO = serviceMapper.toDto(updatedService);

        restServiceMockMvc.perform(put("/api/services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceDTO)))
            .andExpect(status().isOk());

        // Validate the Service in the database
        List<Service> serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeUpdate);
        Service testService = serviceList.get(serviceList.size() - 1);
        assertThat(testService.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testService.getAlternateName()).isEqualTo(UPDATED_ALTERNATE_NAME);
        assertThat(testService.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testService.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testService.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testService.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testService.getInterpretationServices()).isEqualTo(UPDATED_INTERPRETATION_SERVICES);
        assertThat(testService.getApplicationProcess()).isEqualTo(UPDATED_APPLICATION_PROCESS);
        assertThat(testService.getWaitTime()).isEqualTo(UPDATED_WAIT_TIME);
        assertThat(testService.getFees()).isEqualTo(UPDATED_FEES);
        assertThat(testService.getAccreditations()).isEqualTo(UPDATED_ACCREDITATIONS);
        assertThat(testService.getLicenses()).isEqualTo(UPDATED_LICENSES);
        assertThat(testService.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testService.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingService() throws Exception {
        int databaseSizeBeforeUpdate = serviceRepository.findAll().size();

        // Create the Service
        ServiceDTO serviceDTO = serviceMapper.toDto(service);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceMockMvc.perform(put("/api/services")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Service in the database
        List<Service> serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteService() throws Exception {
        // Initialize the database
        serviceRepository.saveAndFlush(service);

        int databaseSizeBeforeDelete = serviceRepository.findAll().size();

        // Get the service
        restServiceMockMvc.perform(delete("/api/services/{id}", service.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Service> serviceList = serviceRepository.findAll();
        assertThat(serviceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Service.class);
        Service service1 = new Service();
        service1.setId(TestConstants.UUID_1);
        Service service2 = new Service();
        service2.setId(service1.getId());
        assertThat(service1).isEqualTo(service2);
        service2.setId(TestConstants.UUID_2);
        assertThat(service1).isNotEqualTo(service2);
        service1.setId(null);
        assertThat(service1).isNotEqualTo(service2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceDTO.class);
        ServiceDTO serviceDTO1 = new ServiceDTO();
        serviceDTO1.setId(TestConstants.UUID_1);
        ServiceDTO serviceDTO2 = new ServiceDTO();
        assertThat(serviceDTO1).isNotEqualTo(serviceDTO2);
        serviceDTO2.setId(serviceDTO1.getId());
        assertThat(serviceDTO1).isEqualTo(serviceDTO2);
        serviceDTO2.setId(TestConstants.UUID_2);
        assertThat(serviceDTO1).isNotEqualTo(serviceDTO2);
        serviceDTO1.setId(null);
        assertThat(serviceDTO1).isNotEqualTo(serviceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serviceMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(serviceMapper.fromId(null)).isNull();
    }
}
