package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ConfidentialRecord;
import org.benetech.servicenet.repository.ConfidentialRecordRepository;
import org.benetech.servicenet.service.ConfidentialRecordService;
import org.benetech.servicenet.service.dto.ConfidentialRecordDTO;
import org.benetech.servicenet.service.mapper.ConfidentialRecordMapper;
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
import java.util.UUID;

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
 * Test class for the ConfidentialRecordResource REST controller.
 *
 * @see ConfidentialRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ConfidentialRecordResourceIntTest {

    private static final UUID DEFAULT_RESOURCE_ID = TestConstants.UUID_1;
    private static final UUID UPDATED_RESOURCE_ID = TestConstants.UUID_2;

    private static final String DEFAULT_FIELDS = "AAAAAAAAAA";
    private static final String UPDATED_FIELDS = "BBBBBBBBBB";

    @Autowired
    private ConfidentialRecordRepository confidentialRecordRepository;

    @Autowired
    private ConfidentialRecordMapper confidentialRecordMapper;

    @Autowired
    private ConfidentialRecordService confidentialRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfidentialRecordMockMvc;

    private ConfidentialRecord confidentialRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfidentialRecordResource confidentialRecordResource = new ConfidentialRecordResource(confidentialRecordService);
        this.restConfidentialRecordMockMvc = MockMvcBuilders.standaloneSetup(confidentialRecordResource)
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
    public static ConfidentialRecord createEntity(EntityManager em) {
        ConfidentialRecord confidentialRecord = ConfidentialRecord.builder()
            .resourceId(DEFAULT_RESOURCE_ID)
            .fields(DEFAULT_FIELDS)
            .build();
        return confidentialRecord;
    }

    @Before
    public void initTest() {
        confidentialRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfidentialRecord() throws Exception {
        int databaseSizeBeforeCreate = confidentialRecordRepository.findAll().size();

        // Create the ConfidentialRecord
        ConfidentialRecordDTO confidentialRecordDTO = confidentialRecordMapper.toDto(confidentialRecord);
        restConfidentialRecordMockMvc.perform(post("/api/confidential-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confidentialRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the ConfidentialRecord in the database
        List<ConfidentialRecord> confidentialRecordList = confidentialRecordRepository.findAll();
        assertThat(confidentialRecordList).hasSize(databaseSizeBeforeCreate + 1);
        ConfidentialRecord testConfidentialRecord = confidentialRecordList.get(confidentialRecordList.size() - 1);
        assertThat(testConfidentialRecord.getResourceId()).isEqualTo(DEFAULT_RESOURCE_ID);
        assertThat(testConfidentialRecord.getFields()).isEqualTo(DEFAULT_FIELDS);
    }

    @Test
    @Transactional
    public void createConfidentialRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = confidentialRecordRepository.findAll().size();

        // Create the ConfidentialRecord with an existing ID
        confidentialRecord.setId(TestConstants.UUID_1);
        ConfidentialRecordDTO confidentialRecordDTO = confidentialRecordMapper.toDto(confidentialRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfidentialRecordMockMvc.perform(post("/api/confidential-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confidentialRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfidentialRecord in the database
        List<ConfidentialRecord> confidentialRecordList = confidentialRecordRepository.findAll();
        assertThat(confidentialRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConfidentialRecords() throws Exception {
        // Initialize the database
        confidentialRecordRepository.saveAndFlush(confidentialRecord);

        // Get all the confidentialRecordList
        restConfidentialRecordMockMvc.perform(get("/api/confidential-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(confidentialRecord.getId().toString())))
            .andExpect(jsonPath("$.[*].resourceId").value(hasItem(DEFAULT_RESOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].fields").value(hasItem(DEFAULT_FIELDS.toString())));
    }
    
    @Test
    @Transactional
    public void getConfidentialRecord() throws Exception {
        // Initialize the database
        confidentialRecordRepository.saveAndFlush(confidentialRecord);

        // Get the confidentialRecord
        restConfidentialRecordMockMvc.perform(get("/api/confidential-records/{id}", confidentialRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(confidentialRecord.getId().toString()))
            .andExpect(jsonPath("$.resourceId").value(DEFAULT_RESOURCE_ID.toString()))
            .andExpect(jsonPath("$.fields").value(DEFAULT_FIELDS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfidentialRecord() throws Exception {
        // Get the confidentialRecord
        restConfidentialRecordMockMvc.perform(get("/api/confidential-records/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfidentialRecord() throws Exception {
        // Initialize the database
        confidentialRecordRepository.saveAndFlush(confidentialRecord);

        int databaseSizeBeforeUpdate = confidentialRecordRepository.findAll().size();

        // Update the confidentialRecord
        ConfidentialRecord updatedConfidentialRecord = confidentialRecordRepository.findById(confidentialRecord.getId()).get();
        // Disconnect from session so that the updates on updatedConfidentialRecord are not directly saved in db
        em.detach(updatedConfidentialRecord);
        updatedConfidentialRecord.setResourceId(UPDATED_RESOURCE_ID);
        updatedConfidentialRecord.setFields(UPDATED_FIELDS);
        ConfidentialRecordDTO confidentialRecordDTO = confidentialRecordMapper.toDto(updatedConfidentialRecord);

        restConfidentialRecordMockMvc.perform(put("/api/confidential-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confidentialRecordDTO)))
            .andExpect(status().isOk());

        // Validate the ConfidentialRecord in the database
        List<ConfidentialRecord> confidentialRecordList = confidentialRecordRepository.findAll();
        assertThat(confidentialRecordList).hasSize(databaseSizeBeforeUpdate);
        ConfidentialRecord testConfidentialRecord = confidentialRecordList.get(confidentialRecordList.size() - 1);
        assertThat(testConfidentialRecord.getResourceId()).isEqualTo(UPDATED_RESOURCE_ID);
        assertThat(testConfidentialRecord.getFields()).isEqualTo(UPDATED_FIELDS);
    }

    @Test
    @Transactional
    public void updateNonExistingConfidentialRecord() throws Exception {
        int databaseSizeBeforeUpdate = confidentialRecordRepository.findAll().size();

        // Create the ConfidentialRecord
        ConfidentialRecordDTO confidentialRecordDTO = confidentialRecordMapper.toDto(confidentialRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfidentialRecordMockMvc.perform(put("/api/confidential-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(confidentialRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfidentialRecord in the database
        List<ConfidentialRecord> confidentialRecordList = confidentialRecordRepository.findAll();
        assertThat(confidentialRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConfidentialRecord() throws Exception {
        // Initialize the database
        confidentialRecordRepository.saveAndFlush(confidentialRecord);

        int databaseSizeBeforeDelete = confidentialRecordRepository.findAll().size();

        // Get the confidentialRecord
        restConfidentialRecordMockMvc.perform(delete("/api/confidential-records/{id}", confidentialRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfidentialRecord> confidentialRecordList = confidentialRecordRepository.findAll();
        assertThat(confidentialRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfidentialRecord.class);
        ConfidentialRecord confidentialRecord1 = new ConfidentialRecord();
        confidentialRecord1.setId(TestConstants.UUID_1);
        ConfidentialRecord confidentialRecord2 = new ConfidentialRecord();
        confidentialRecord2.setId(confidentialRecord1.getId());
        assertThat(confidentialRecord1).isEqualTo(confidentialRecord2);
        confidentialRecord2.setId(TestConstants.UUID_2);
        assertThat(confidentialRecord1).isNotEqualTo(confidentialRecord2);
        confidentialRecord1.setId(null);
        assertThat(confidentialRecord1).isNotEqualTo(confidentialRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfidentialRecordDTO.class);
        ConfidentialRecordDTO confidentialRecordDTO1 = new ConfidentialRecordDTO();
        confidentialRecordDTO1.setId(TestConstants.UUID_1);
        ConfidentialRecordDTO confidentialRecordDTO2 = new ConfidentialRecordDTO();
        assertThat(confidentialRecordDTO1).isNotEqualTo(confidentialRecordDTO2);
        confidentialRecordDTO2.setId(confidentialRecordDTO1.getId());
        assertThat(confidentialRecordDTO1).isEqualTo(confidentialRecordDTO2);
        confidentialRecordDTO2.setId(TestConstants.UUID_2);
        assertThat(confidentialRecordDTO1).isNotEqualTo(confidentialRecordDTO2);
        confidentialRecordDTO1.setId(null);
        assertThat(confidentialRecordDTO1).isNotEqualTo(confidentialRecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(confidentialRecordMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(confidentialRecordMapper.fromId(null)).isNull();
    }
}
