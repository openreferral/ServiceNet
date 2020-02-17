package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.repository.DataImportReportRepository;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.dto.DataImportReportDTO;
import org.benetech.servicenet.service.mapper.DataImportReportMapper;
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
 * Test class for the DataImportReportResource REST controller.
 *
 * @see DataImportReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class DataImportReportResourceIntTest {

    private static final Integer DEFAULT_NUMBER_OF_UPDATED_SERVICES = 1;
    private static final Integer UPDATED_NUMBER_OF_UPDATED_SERVICES = 2;

    private static final Integer DEFAULT_NUMBER_OF_CREATED_SERVICES = 1;
    private static final Integer UPDATED_NUMBER_OF_CREATED_SERVICES = 2;

    private static final Integer DEFAULT_NUMBER_OF_UPDATED_ORGS = 1;
    private static final Integer UPDATED_NUMBER_OF_UPDATED_ORGS = 2;

    private static final Integer DEFAULT_NUMBER_OF_CREATED_ORGS = 1;
    private static final Integer UPDATED_NUMBER_OF_CREATED_ORGS = 2;

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_JOB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOB_NAME = "BBBBBBBBBB";

    @Autowired
    private DataImportReportRepository dataImportReportRepository;

    @Autowired
    private DataImportReportMapper dataImportReportMapper;

    @Autowired
    private DataImportReportService dataImportReportService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDataImportReportMockMvc;

    private DataImportReport dataImportReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataImportReportResource dataImportReportResource = new DataImportReportResource(dataImportReportService);
        this.restDataImportReportMockMvc = MockMvcBuilders.standaloneSetup(dataImportReportResource)
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
    public static DataImportReport createEntity(EntityManager em) {
        DataImportReport dataImportReport = new DataImportReport()
            .numberOfUpdatedServices(DEFAULT_NUMBER_OF_UPDATED_SERVICES)
            .numberOfCreatedServices(DEFAULT_NUMBER_OF_CREATED_SERVICES)
            .numberOfUpdatedOrgs(DEFAULT_NUMBER_OF_UPDATED_ORGS)
            .numberOfCreatedOrgs(DEFAULT_NUMBER_OF_CREATED_ORGS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .jobName(DEFAULT_JOB_NAME);
        return dataImportReport;
    }

    @Before
    public void initTest() {
        dataImportReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataImportReport() throws Exception {
        int databaseSizeBeforeCreate = dataImportReportRepository.findAll().size();

        // Create the DataImportReport
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);
        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isCreated());

        // Validate the DataImportReport in the database
        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeCreate + 1);
        DataImportReport testDataImportReport = dataImportReportList.get(dataImportReportList.size() - 1);
        assertThat(testDataImportReport.getNumberOfUpdatedServices()).isEqualTo(DEFAULT_NUMBER_OF_UPDATED_SERVICES);
        assertThat(testDataImportReport.getNumberOfCreatedServices()).isEqualTo(DEFAULT_NUMBER_OF_CREATED_SERVICES);
        assertThat(testDataImportReport.getNumberOfUpdatedOrgs()).isEqualTo(DEFAULT_NUMBER_OF_UPDATED_ORGS);
        assertThat(testDataImportReport.getNumberOfCreatedOrgs()).isEqualTo(DEFAULT_NUMBER_OF_CREATED_ORGS);
        assertThat(testDataImportReport.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDataImportReport.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDataImportReport.getJobName()).isEqualTo(DEFAULT_JOB_NAME);
    }

    @Test
    @Transactional
    public void createDataImportReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataImportReportRepository.findAll().size();

        // Create the DataImportReport with an existing ID
        dataImportReport.setId(TestConstants.UUID_1);
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataImportReport in the database
        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberOfUpdatedServicesIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataImportReportRepository.findAll().size();
        // set the field null
        dataImportReport.setNumberOfUpdatedServices(null);

        // Create the DataImportReport, which fails.
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberOfCreatedServicesIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataImportReportRepository.findAll().size();
        // set the field null
        dataImportReport.setNumberOfCreatedServices(null);

        // Create the DataImportReport, which fails.
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberOfUpdatedOrgsIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataImportReportRepository.findAll().size();
        // set the field null
        dataImportReport.setNumberOfUpdatedOrgs(null);

        // Create the DataImportReport, which fails.
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberOfCreatedOrgsIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataImportReportRepository.findAll().size();
        // set the field null
        dataImportReport.setNumberOfCreatedOrgs(null);

        // Create the DataImportReport, which fails.
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataImportReportRepository.findAll().size();
        // set the field null
        dataImportReport.setStartDate(null);

        // Create the DataImportReport, which fails.
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dataImportReportRepository.findAll().size();
        // set the field null
        dataImportReport.setEndDate(null);

        // Create the DataImportReport, which fails.
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        restDataImportReportMockMvc.perform(post("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDataImportReports() throws Exception {
        // Initialize the database
        dataImportReportRepository.saveAndFlush(dataImportReport);

        // Get all the dataImportReportList
        restDataImportReportMockMvc.perform(get("/api/data-import-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataImportReport.getId().toString())))
            .andExpect(jsonPath("$.[*].numberOfUpdatedServices").value(hasItem(DEFAULT_NUMBER_OF_UPDATED_SERVICES)))
            .andExpect(jsonPath("$.[*].numberOfCreatedServices").value(hasItem(DEFAULT_NUMBER_OF_CREATED_SERVICES)))
            .andExpect(jsonPath("$.[*].numberOfUpdatedOrgs").value(hasItem(DEFAULT_NUMBER_OF_UPDATED_ORGS)))
            .andExpect(jsonPath("$.[*].numberOfCreatedOrgs").value(hasItem(DEFAULT_NUMBER_OF_CREATED_ORGS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDataImportReport() throws Exception {
        // Initialize the database
        dataImportReportRepository.saveAndFlush(dataImportReport);

        // Get the dataImportReport
        restDataImportReportMockMvc.perform(get("/api/data-import-reports/{id}", dataImportReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataImportReport.getId().toString()))
            .andExpect(jsonPath("$.numberOfUpdatedServices").value(DEFAULT_NUMBER_OF_UPDATED_SERVICES))
            .andExpect(jsonPath("$.numberOfCreatedServices").value(DEFAULT_NUMBER_OF_CREATED_SERVICES))
            .andExpect(jsonPath("$.numberOfUpdatedOrgs").value(DEFAULT_NUMBER_OF_UPDATED_ORGS))
            .andExpect(jsonPath("$.numberOfCreatedOrgs").value(DEFAULT_NUMBER_OF_CREATED_ORGS))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.jobName").value(DEFAULT_JOB_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataImportReport() throws Exception {
        // Get the dataImportReport
        restDataImportReportMockMvc.perform(get("/api/data-import-reports/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataImportReport() throws Exception {
        // Initialize the database
        dataImportReportRepository.saveAndFlush(dataImportReport);

        int databaseSizeBeforeUpdate = dataImportReportRepository.findAll().size();

        // Update the dataImportReport
        DataImportReport updatedDataImportReport = dataImportReportRepository.findById(dataImportReport.getId()).get();
        // Disconnect from session so that the updates on updatedDataImportReport are not directly saved in db
        em.detach(updatedDataImportReport);
        updatedDataImportReport
            .numberOfUpdatedServices(UPDATED_NUMBER_OF_UPDATED_SERVICES)
            .numberOfCreatedServices(UPDATED_NUMBER_OF_CREATED_SERVICES)
            .numberOfUpdatedOrgs(UPDATED_NUMBER_OF_UPDATED_ORGS)
            .numberOfCreatedOrgs(UPDATED_NUMBER_OF_CREATED_ORGS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .jobName(UPDATED_JOB_NAME);
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(updatedDataImportReport);

        restDataImportReportMockMvc.perform(put("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isOk());

        // Validate the DataImportReport in the database
        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeUpdate);
        DataImportReport testDataImportReport = dataImportReportList.get(dataImportReportList.size() - 1);
        assertThat(testDataImportReport.getNumberOfUpdatedServices()).isEqualTo(UPDATED_NUMBER_OF_UPDATED_SERVICES);
        assertThat(testDataImportReport.getNumberOfCreatedServices()).isEqualTo(UPDATED_NUMBER_OF_CREATED_SERVICES);
        assertThat(testDataImportReport.getNumberOfUpdatedOrgs()).isEqualTo(UPDATED_NUMBER_OF_UPDATED_ORGS);
        assertThat(testDataImportReport.getNumberOfCreatedOrgs()).isEqualTo(UPDATED_NUMBER_OF_CREATED_ORGS);
        assertThat(testDataImportReport.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDataImportReport.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDataImportReport.getJobName()).isEqualTo(UPDATED_JOB_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingDataImportReport() throws Exception {
        int databaseSizeBeforeUpdate = dataImportReportRepository.findAll().size();

        // Create the DataImportReport
        DataImportReportDTO dataImportReportDTO = dataImportReportMapper.toDto(dataImportReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataImportReportMockMvc.perform(put("/api/data-import-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataImportReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DataImportReport in the database
        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDataImportReport() throws Exception {
        // Initialize the database
        dataImportReportRepository.saveAndFlush(dataImportReport);

        int databaseSizeBeforeDelete = dataImportReportRepository.findAll().size();

        // Get the dataImportReport
        restDataImportReportMockMvc.perform(delete("/api/data-import-reports/{id}", dataImportReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DataImportReport> dataImportReportList = dataImportReportRepository.findAll();
        assertThat(dataImportReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataImportReport.class);
        DataImportReport dataImportReport1 = new DataImportReport();
        dataImportReport1.setId(TestConstants.UUID_1);
        DataImportReport dataImportReport2 = new DataImportReport();
        dataImportReport2.setId(dataImportReport1.getId());
        assertThat(dataImportReport1).isEqualTo(dataImportReport2);
        dataImportReport2.setId(TestConstants.UUID_2);
        assertThat(dataImportReport1).isNotEqualTo(dataImportReport2);
        dataImportReport1.setId(null);
        assertThat(dataImportReport1).isNotEqualTo(dataImportReport2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataImportReportDTO.class);
        DataImportReportDTO dataImportReportDTO1 = new DataImportReportDTO();
        dataImportReportDTO1.setId(TestConstants.UUID_1);
        DataImportReportDTO dataImportReportDTO2 = new DataImportReportDTO();
        assertThat(dataImportReportDTO1).isNotEqualTo(dataImportReportDTO2);
        dataImportReportDTO2.setId(dataImportReportDTO1.getId());
        assertThat(dataImportReportDTO1).isEqualTo(dataImportReportDTO2);
        dataImportReportDTO2.setId(TestConstants.UUID_2);
        assertThat(dataImportReportDTO1).isNotEqualTo(dataImportReportDTO2);
        dataImportReportDTO1.setId(null);
        assertThat(dataImportReportDTO1).isNotEqualTo(dataImportReportDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dataImportReportMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(dataImportReportMapper.fromId(null)).isNull();
    }
}
