package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.DocumentUpload;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.listener.HibernatePostCreateListener;
import org.benetech.servicenet.listener.HibernatePostDeleteListener;
import org.benetech.servicenet.listener.HibernatePostUpdateListener;
import org.benetech.servicenet.repository.DocumentUploadRepository;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.service.MetadataService;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.benetech.servicenet.service.mapper.DocumentUploadMapper;
import org.benetech.servicenet.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
 * Test class for the DocumentUploadResource REST controller.
 *
 * @see DocumentUploadResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class DocumentUploadResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE_UPLOADED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_UPLOADED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DOCUMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_ID = "BBBBBBBBBB";

    @Mock
    private MetadataService metadataService;

    @Autowired
    @InjectMocks
    private HibernatePostUpdateListener hibernatePostUpdateListener;

    @Autowired
    @InjectMocks
    private HibernatePostCreateListener hibernatePostCreateListener;

    @Autowired
    @InjectMocks
    private HibernatePostDeleteListener hibernatePostDeleteListener;

    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private DocumentUploadMapper documentUploadMapper;

    @Autowired
    private DocumentUploadService documentUploadService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentUploadMockMvc;

    private DocumentUpload documentUpload;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentUpload createEntity(EntityManager em) {
        DocumentUpload documentUpload = new DocumentUpload()
            .dateUploaded(DEFAULT_DATE_UPLOADED)
            .documentId(DEFAULT_DOCUMENT_ID);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        documentUpload.setUploader(user);
        return documentUpload;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentUploadResource documentUploadResource = new DocumentUploadResource(documentUploadService);
        this.restDocumentUploadMockMvc = MockMvcBuilders.standaloneSetup(documentUploadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        documentUpload = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocumentUpload() throws Exception {
        int databaseSizeBeforeCreate = documentUploadRepository.findAll().size();

        // Create the DocumentUpload
        DocumentUploadDTO documentUploadDTO = documentUploadMapper.toDto(documentUpload);
        restDocumentUploadMockMvc.perform(post("/api/document-uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentUploadDTO)))
            .andExpect(status().isCreated());

        // Validate the DocumentUpload in the database
        List<DocumentUpload> documentUploadList = documentUploadRepository.findAll();
        assertThat(documentUploadList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentUpload testDocumentUpload = documentUploadList.get(documentUploadList.size() - 1);
        assertThat(testDocumentUpload.getDateUploaded()).isEqualTo(DEFAULT_DATE_UPLOADED);
        assertThat(testDocumentUpload.getDocumentId()).isEqualTo(DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void createDocumentUploadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentUploadRepository.findAll().size();

        // Create the DocumentUpload with an existing ID
        documentUpload.setId(TestConstants.UUID_1);
        DocumentUploadDTO documentUploadDTO = documentUploadMapper.toDto(documentUpload);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentUploadMockMvc.perform(post("/api/document-uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentUploadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentUpload in the database
        List<DocumentUpload> documentUploadList = documentUploadRepository.findAll();
        assertThat(documentUploadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateUploadedIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentUploadRepository.findAll().size();
        // set the field null
        documentUpload.setDateUploaded(null);

        // Create the DocumentUpload, which fails.
        DocumentUploadDTO documentUploadDTO = documentUploadMapper.toDto(documentUpload);

        restDocumentUploadMockMvc.perform(post("/api/document-uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentUploadDTO)))
            .andExpect(status().isBadRequest());

        List<DocumentUpload> documentUploadList = documentUploadRepository.findAll();
        assertThat(documentUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDocumentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentUploadRepository.findAll().size();
        // set the field null
        documentUpload.setDocumentId(null);

        // Create the DocumentUpload, which fails.
        DocumentUploadDTO documentUploadDTO = documentUploadMapper.toDto(documentUpload);

        restDocumentUploadMockMvc.perform(post("/api/document-uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentUploadDTO)))
            .andExpect(status().isBadRequest());

        List<DocumentUpload> documentUploadList = documentUploadRepository.findAll();
        assertThat(documentUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocumentUploads() throws Exception {
        // Initialize the database
        documentUploadRepository.saveAndFlush(documentUpload);

        // Get all the documentUploadList
        restDocumentUploadMockMvc.perform(get("/api/document-uploads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentUpload.getId().toString())))
            .andExpect(jsonPath("$.[*].dateUploaded").value(hasItem(sameInstant(DEFAULT_DATE_UPLOADED))))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.toString())));
    }

    @Test
    @Transactional
    public void getDocumentUpload() throws Exception {
        // Initialize the database
        documentUploadRepository.saveAndFlush(documentUpload);

        // Get the documentUpload
        restDocumentUploadMockMvc.perform(get("/api/document-uploads/{id}", documentUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(documentUpload.getId().toString()))
            .andExpect(jsonPath("$.dateUploaded").value(sameInstant(DEFAULT_DATE_UPLOADED)))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocumentUpload() throws Exception {
        // Get the documentUpload
        restDocumentUploadMockMvc.perform(get("/api/document-uploads/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocumentUpload() throws Exception {
        // Initialize the database
        documentUploadRepository.saveAndFlush(documentUpload);

        int databaseSizeBeforeUpdate = documentUploadRepository.findAll().size();

        // Update the documentUpload
        DocumentUpload updatedDocumentUpload = documentUploadRepository.findById(documentUpload.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentUpload are not directly saved in db
        em.detach(updatedDocumentUpload);
        updatedDocumentUpload
            .dateUploaded(UPDATED_DATE_UPLOADED)
            .documentId(UPDATED_DOCUMENT_ID);
        DocumentUploadDTO documentUploadDTO = documentUploadMapper.toDto(updatedDocumentUpload);

        restDocumentUploadMockMvc.perform(put("/api/document-uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentUploadDTO)))
            .andExpect(status().isOk());

        // Validate the DocumentUpload in the database
        List<DocumentUpload> documentUploadList = documentUploadRepository.findAll();
        assertThat(documentUploadList).hasSize(databaseSizeBeforeUpdate);
        DocumentUpload testDocumentUpload = documentUploadList.get(documentUploadList.size() - 1);
        assertThat(testDocumentUpload.getDateUploaded()).isEqualTo(UPDATED_DATE_UPLOADED);
        assertThat(testDocumentUpload.getDocumentId()).isEqualTo(UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingDocumentUpload() throws Exception {
        int databaseSizeBeforeUpdate = documentUploadRepository.findAll().size();

        // Create the DocumentUpload
        DocumentUploadDTO documentUploadDTO = documentUploadMapper.toDto(documentUpload);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentUploadMockMvc.perform(put("/api/document-uploads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentUploadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentUpload in the database
        List<DocumentUpload> documentUploadList = documentUploadRepository.findAll();
        assertThat(documentUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocumentUpload() throws Exception {
        // Initialize the database
        documentUploadRepository.saveAndFlush(documentUpload);

        int databaseSizeBeforeDelete = documentUploadRepository.findAll().size();

        // Get the documentUpload
        restDocumentUploadMockMvc.perform(delete("/api/document-uploads/{id}", documentUpload.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DocumentUpload> documentUploadList = documentUploadRepository.findAll();
        assertThat(documentUploadList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentUpload.class);
        DocumentUpload documentUpload1 = new DocumentUpload();
        documentUpload1.setId(TestConstants.UUID_1);
        DocumentUpload documentUpload2 = new DocumentUpload();
        documentUpload2.setId(documentUpload1.getId());
        assertThat(documentUpload1).isEqualTo(documentUpload2);
        documentUpload2.setId(TestConstants.UUID_2);
        assertThat(documentUpload1).isNotEqualTo(documentUpload2);
        documentUpload1.setId(null);
        assertThat(documentUpload1).isNotEqualTo(documentUpload2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentUploadDTO.class);
        DocumentUploadDTO documentUploadDTO1 = new DocumentUploadDTO();
        documentUploadDTO1.setId(TestConstants.UUID_1);
        DocumentUploadDTO documentUploadDTO2 = new DocumentUploadDTO();
        assertThat(documentUploadDTO1).isNotEqualTo(documentUploadDTO2);
        documentUploadDTO2.setId(documentUploadDTO1.getId());
        assertThat(documentUploadDTO1).isEqualTo(documentUploadDTO2);
        documentUploadDTO2.setId(TestConstants.UUID_2);
        assertThat(documentUploadDTO1).isNotEqualTo(documentUploadDTO2);
        documentUploadDTO1.setId(null);
        assertThat(documentUploadDTO1).isNotEqualTo(documentUploadDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(documentUploadMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(documentUploadMapper.fromId(null)).isNull();
    }
}
