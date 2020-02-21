package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.Metadata;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.domain.enumeration.ActionType;
import org.benetech.servicenet.repository.MetadataRepository;
import org.benetech.servicenet.service.MetadataService;
import org.benetech.servicenet.service.dto.MetadataDTO;
import org.benetech.servicenet.service.mapper.MetadataMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
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
import java.util.UUID;

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
 * Test class for the MetadataResource REST controller.
 *
 * @see MetadataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class MetadataResourceIntTest {

    private static final UUID DEFAULT_RESOURCE_ID = TestConstants.UUID_1;
    private static final UUID UPDATED_RESOURCE_ID = TestConstants.UUID_2;

    private static final ZonedDateTime DEFAULT_LAST_ACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_ACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ActionType DEFAULT_LAST_ACTION_TYPE = ActionType.CREATE;
    private static final ActionType UPDATED_LAST_ACTION_TYPE = ActionType.UPDATE;

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIOUS_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_REPLACEMENT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_REPLACEMENT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCE_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_CLASS = "BBBBBBBBBB";

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MetadataMapper metadataMapper;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMetadataMockMvc;

    private Metadata metadata;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metadata createEntity(EntityManager em) {
        Metadata metadata = new Metadata()
            .resourceId(DEFAULT_RESOURCE_ID)
            .lastActionDate(DEFAULT_LAST_ACTION_DATE)
            .lastActionType(DEFAULT_LAST_ACTION_TYPE)
            .fieldName(DEFAULT_FIELD_NAME)
            .previousValue(DEFAULT_PREVIOUS_VALUE)
            .replacementValue(DEFAULT_REPLACEMENT_VALUE)
            .resourceClass(DEFAULT_RESOURCE_CLASS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        metadata.setUser(user);
        return metadata;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetadataResource metadataResource = new MetadataResource(metadataService);
        this.restMetadataMockMvc = MockMvcBuilders.standaloneSetup(metadataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
        metadata = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetadata() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);
        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isCreated());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeCreate + 1);
        Metadata testMetadata = metadataList.get(metadataList.size() - 1);
        assertThat(testMetadata.getResourceId()).isEqualTo(DEFAULT_RESOURCE_ID);
        assertThat(testMetadata.getLastActionDate()).isEqualTo(DEFAULT_LAST_ACTION_DATE);
        assertThat(testMetadata.getLastActionType()).isEqualTo(DEFAULT_LAST_ACTION_TYPE);
        assertThat(testMetadata.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testMetadata.getPreviousValue()).isEqualTo(DEFAULT_PREVIOUS_VALUE);
        assertThat(testMetadata.getReplacementValue()).isEqualTo(DEFAULT_REPLACEMENT_VALUE);
        assertThat(testMetadata.getResourceClass()).isEqualTo(DEFAULT_RESOURCE_CLASS);
    }

    @Test
    @Transactional
    public void createMetadataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metadataRepository.findAll().size();

        // Create the Metadata with an existing ID
        metadata.setId(TestConstants.UUID_1);
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkResourceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setResourceId(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastActionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setLastActionDate(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastActionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setLastActionType(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFieldNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setFieldName(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResourceClassIsRequired() throws Exception {
        int databaseSizeBeforeTest = metadataRepository.findAll().size();
        // set the field null
        metadata.setResourceClass(null);

        // Create the Metadata, which fails.
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        restMetadataMockMvc.perform(post("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void getAllMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        // Get all the metadataList
        restMetadataMockMvc.perform(get("/api/metadata?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId().toString())))
            .andExpect(jsonPath("$.[*].resourceId").value(hasItem(DEFAULT_RESOURCE_ID.toString())))
            .andExpect(jsonPath("$.[*].lastActionDate").value(hasItem(sameInstant(DEFAULT_LAST_ACTION_DATE))))
            .andExpect(jsonPath("$.[*].lastActionType").value(hasItem(DEFAULT_LAST_ACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].previousValue").value(hasItem(DEFAULT_PREVIOUS_VALUE)))
            .andExpect(jsonPath("$.[*].replacementValue").value(hasItem(DEFAULT_REPLACEMENT_VALUE)))
            .andExpect(jsonPath("$.[*].resourceClass").value(hasItem(DEFAULT_RESOURCE_CLASS)));
    }

    @Test
    @Transactional
    public void getMetadata() throws Exception {

        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadata/{id}", metadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metadata.getId().toString()))
            .andExpect(jsonPath("$.resourceId").value(DEFAULT_RESOURCE_ID.toString()))
            .andExpect(jsonPath("$.lastActionDate").value(sameInstant(DEFAULT_LAST_ACTION_DATE)))
            .andExpect(jsonPath("$.lastActionType").value(DEFAULT_LAST_ACTION_TYPE.toString()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.previousValue").value(DEFAULT_PREVIOUS_VALUE))
            .andExpect(jsonPath("$.replacementValue").value(DEFAULT_REPLACEMENT_VALUE))
            .andExpect(jsonPath("$.resourceClass").value(DEFAULT_RESOURCE_CLASS));
    }

    @Test
    @Transactional
    public void getNonExistingMetadata() throws Exception {
        // Get the metadata
        restMetadataMockMvc.perform(get("/api/metadata/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        int databaseSizeBeforeUpdate = metadataRepository.findAll().size();

        // Update the metadata
        Metadata updatedMetadata = metadataRepository.findById(metadata.getId()).get();
        // Disconnect from session so that the updates on updatedMetadata are not directly saved in db
        em.detach(updatedMetadata);
        updatedMetadata
            .resourceId(UPDATED_RESOURCE_ID)
            .lastActionDate(UPDATED_LAST_ACTION_DATE)
            .lastActionType(UPDATED_LAST_ACTION_TYPE)
            .fieldName(UPDATED_FIELD_NAME)
            .previousValue(UPDATED_PREVIOUS_VALUE)
            .replacementValue(UPDATED_REPLACEMENT_VALUE)
            .resourceClass(UPDATED_RESOURCE_CLASS);
        MetadataDTO metadataDTO = metadataMapper.toDto(updatedMetadata);

        restMetadataMockMvc.perform(put("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isOk());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeUpdate);
        Metadata testMetadata = metadataList.get(metadataList.size() - 1);
        assertThat(testMetadata.getResourceId()).isEqualTo(UPDATED_RESOURCE_ID);
        assertThat(testMetadata.getLastActionDate()).isEqualTo(UPDATED_LAST_ACTION_DATE);
        assertThat(testMetadata.getLastActionType()).isEqualTo(UPDATED_LAST_ACTION_TYPE);
        assertThat(testMetadata.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testMetadata.getPreviousValue()).isEqualTo(UPDATED_PREVIOUS_VALUE);
        assertThat(testMetadata.getReplacementValue()).isEqualTo(UPDATED_REPLACEMENT_VALUE);
        assertThat(testMetadata.getResourceClass()).isEqualTo(UPDATED_RESOURCE_CLASS);
    }

    @Test
    @Transactional
    public void updateNonExistingMetadata() throws Exception {
        int databaseSizeBeforeUpdate = metadataRepository.findAll().size();

        // Create the Metadata
        MetadataDTO metadataDTO = metadataMapper.toDto(metadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetadataMockMvc.perform(put("/api/metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(metadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMetadata() throws Exception {
        // Initialize the database
        metadataRepository.saveAndFlush(metadata);

        int databaseSizeBeforeDelete = metadataRepository.findAll().size();

        // Get the metadata
        restMetadataMockMvc.perform(delete("/api/metadata/{id}", metadata.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Metadata> metadataList = metadataRepository.findAll();
        assertThat(metadataList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metadata.class);
        Metadata metadata1 = new Metadata();
        metadata1.setId(TestConstants.UUID_1);
        Metadata metadata2 = new Metadata();
        metadata2.setId(metadata1.getId());
        assertThat(metadata1).isEqualTo(metadata2);
        metadata2.setId(TestConstants.UUID_2);
        assertThat(metadata1).isNotEqualTo(metadata2);
        metadata1.setId(null);
        assertThat(metadata1).isNotEqualTo(metadata2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetadataDTO.class);
        MetadataDTO metadataDTO1 = new MetadataDTO();
        metadataDTO1.setId(TestConstants.UUID_1);
        MetadataDTO metadataDTO2 = new MetadataDTO();
        assertThat(metadataDTO1).isNotEqualTo(metadataDTO2);
        metadataDTO2.setId(metadataDTO1.getId());
        assertThat(metadataDTO1).isEqualTo(metadataDTO2);
        metadataDTO2.setId(TestConstants.UUID_2);
        assertThat(metadataDTO1).isNotEqualTo(metadataDTO2);
        metadataDTO1.setId(null);
        assertThat(metadataDTO1).isNotEqualTo(metadataDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(metadataMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(metadataMapper.fromId(null)).isNull();
    }
}
