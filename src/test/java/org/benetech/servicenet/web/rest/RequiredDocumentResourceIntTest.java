package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.repository.RequiredDocumentRepository;
import org.benetech.servicenet.service.RequiredDocumentService;
import org.benetech.servicenet.service.dto.RequiredDocumentDTO;
import org.benetech.servicenet.service.mapper.RequiredDocumentMapper;
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
 * Test class for the RequiredDocumentResource REST controller.
 *
 * @see RequiredDocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class RequiredDocumentResourceIntTest {

    private static final String DEFAULT_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT = "BBBBBBBBBB";

    @Autowired
    private RequiredDocumentRepository requiredDocumentRepository;

    @Autowired
    private RequiredDocumentMapper requiredDocumentMapper;

    @Autowired
    private RequiredDocumentService requiredDocumentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRequiredDocumentMockMvc;

    private RequiredDocument requiredDocument;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequiredDocument createEntity(EntityManager em) {
        RequiredDocument requiredDocument = new RequiredDocument()
            .document(DEFAULT_DOCUMENT);
        return requiredDocument;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequiredDocumentResource requiredDocumentResource = new RequiredDocumentResource(requiredDocumentService);
        this.restRequiredDocumentMockMvc = MockMvcBuilders.standaloneSetup(requiredDocumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        requiredDocument = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequiredDocument() throws Exception {
        int databaseSizeBeforeCreate = requiredDocumentRepository.findAll().size();

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);
        restRequiredDocumentMockMvc.perform(post("/api/required-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredDocumentDTO)))
            .andExpect(status().isCreated());

        // Validate the RequiredDocument in the database
        List<RequiredDocument> requiredDocumentList = requiredDocumentRepository.findAll();
        assertThat(requiredDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        RequiredDocument testRequiredDocument = requiredDocumentList.get(requiredDocumentList.size() - 1);
        assertThat(testRequiredDocument.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
    }

    @Test
    @Transactional
    public void createRequiredDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requiredDocumentRepository.findAll().size();

        // Create the RequiredDocument with an existing ID
        requiredDocument.setId(TestConstants.UUID_1);
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequiredDocumentMockMvc.perform(post("/api/required-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequiredDocument in the database
        List<RequiredDocument> requiredDocumentList = requiredDocumentRepository.findAll();
        assertThat(requiredDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocumentIsRequired() throws Exception {
        int databaseSizeBeforeTest = requiredDocumentRepository.findAll().size();
        // set the field null
        requiredDocument.setDocument(null);

        // Create the RequiredDocument, which fails.
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        restRequiredDocumentMockMvc.perform(post("/api/required-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<RequiredDocument> requiredDocumentList = requiredDocumentRepository.findAll();
        assertThat(requiredDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequiredDocuments() throws Exception {
        // Initialize the database
        requiredDocumentRepository.saveAndFlush(requiredDocument);

        // Get all the requiredDocumentList
        restRequiredDocumentMockMvc.perform(get("/api/required-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requiredDocument.getId().toString())))
            .andExpect(jsonPath("$.[*].document").value(hasItem(DEFAULT_DOCUMENT.toString())));
    }

    @Test
    @Transactional
    public void getRequiredDocument() throws Exception {
        // Initialize the database
        requiredDocumentRepository.saveAndFlush(requiredDocument);

        // Get the requiredDocument
        restRequiredDocumentMockMvc.perform(get("/api/required-documents/{id}", requiredDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requiredDocument.getId().toString()))
            .andExpect(jsonPath("$.document").value(DEFAULT_DOCUMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequiredDocument() throws Exception {
        // Get the requiredDocument
        restRequiredDocumentMockMvc.perform(get("/api/required-documents/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequiredDocument() throws Exception {
        // Initialize the database
        requiredDocumentRepository.saveAndFlush(requiredDocument);

        int databaseSizeBeforeUpdate = requiredDocumentRepository.findAll().size();

        // Update the requiredDocument
        RequiredDocument updatedRequiredDocument = requiredDocumentRepository.findById(requiredDocument.getId()).get();
        // Disconnect from session so that the updates on updatedRequiredDocument are not directly saved in db
        em.detach(updatedRequiredDocument);
        updatedRequiredDocument
            .document(UPDATED_DOCUMENT);
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(updatedRequiredDocument);

        restRequiredDocumentMockMvc.perform(put("/api/required-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredDocumentDTO)))
            .andExpect(status().isOk());

        // Validate the RequiredDocument in the database
        List<RequiredDocument> requiredDocumentList = requiredDocumentRepository.findAll();
        assertThat(requiredDocumentList).hasSize(databaseSizeBeforeUpdate);
        RequiredDocument testRequiredDocument = requiredDocumentList.get(requiredDocumentList.size() - 1);
        assertThat(testRequiredDocument.getDocument()).isEqualTo(UPDATED_DOCUMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingRequiredDocument() throws Exception {
        int databaseSizeBeforeUpdate = requiredDocumentRepository.findAll().size();

        // Create the RequiredDocument
        RequiredDocumentDTO requiredDocumentDTO = requiredDocumentMapper.toDto(requiredDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequiredDocumentMockMvc.perform(put("/api/required-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequiredDocument in the database
        List<RequiredDocument> requiredDocumentList = requiredDocumentRepository.findAll();
        assertThat(requiredDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequiredDocument() throws Exception {
        // Initialize the database
        requiredDocumentRepository.saveAndFlush(requiredDocument);

        int databaseSizeBeforeDelete = requiredDocumentRepository.findAll().size();

        // Get the requiredDocument
        restRequiredDocumentMockMvc.perform(delete("/api/required-documents/{id}", requiredDocument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RequiredDocument> requiredDocumentList = requiredDocumentRepository.findAll();
        assertThat(requiredDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequiredDocument.class);
        RequiredDocument requiredDocument1 = new RequiredDocument();
        requiredDocument1.setId(TestConstants.UUID_1);
        RequiredDocument requiredDocument2 = new RequiredDocument();
        requiredDocument2.setId(requiredDocument1.getId());
        assertThat(requiredDocument1).isEqualTo(requiredDocument2);
        requiredDocument2.setId(TestConstants.UUID_2);
        assertThat(requiredDocument1).isNotEqualTo(requiredDocument2);
        requiredDocument1.setId(null);
        assertThat(requiredDocument1).isNotEqualTo(requiredDocument2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequiredDocumentDTO.class);
        RequiredDocumentDTO requiredDocumentDTO1 = new RequiredDocumentDTO();
        requiredDocumentDTO1.setId(TestConstants.UUID_1);
        RequiredDocumentDTO requiredDocumentDTO2 = new RequiredDocumentDTO();
        assertThat(requiredDocumentDTO1).isNotEqualTo(requiredDocumentDTO2);
        requiredDocumentDTO2.setId(requiredDocumentDTO1.getId());
        assertThat(requiredDocumentDTO1).isEqualTo(requiredDocumentDTO2);
        requiredDocumentDTO2.setId(TestConstants.UUID_2);
        assertThat(requiredDocumentDTO1).isNotEqualTo(requiredDocumentDTO2);
        requiredDocumentDTO1.setId(null);
        assertThat(requiredDocumentDTO1).isNotEqualTo(requiredDocumentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(requiredDocumentMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(requiredDocumentMapper.fromId(null)).isNull();
    }
}
