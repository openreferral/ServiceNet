package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.OrganizationError;
import org.benetech.servicenet.repository.OrganizationErrorRepository;
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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link OrganizationErrorResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class OrganizationErrorResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_DB_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_DB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVALID_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_INVALID_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_CAUSE = "BBBBBBBBBB";

    private static final UUID id = UUID.randomUUID();

    @Autowired
    private OrganizationErrorRepository organizationErrorRepository;

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

    private MockMvc restOrganizationErrorMockMvc;

    private OrganizationError organizationError;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganizationErrorResource organizationErrorResource = new OrganizationErrorResource(organizationErrorRepository);
        this.restOrganizationErrorMockMvc = MockMvcBuilders.standaloneSetup(organizationErrorResource)
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
    public static OrganizationError createEntity(EntityManager em) {
        OrganizationError organizationError = new OrganizationError()
            .entityName(DEFAULT_ENTITY_NAME)
            .fieldName(DEFAULT_FIELD_NAME)
            .externalDbId(DEFAULT_EXTERNAL_DB_ID)
            .invalidValue(DEFAULT_INVALID_VALUE)
            .cause(DEFAULT_CAUSE);
        return organizationError;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationError createUpdatedEntity(EntityManager em) {
        OrganizationError organizationError = new OrganizationError()
            .entityName(UPDATED_ENTITY_NAME)
            .fieldName(UPDATED_FIELD_NAME)
            .externalDbId(UPDATED_EXTERNAL_DB_ID)
            .invalidValue(UPDATED_INVALID_VALUE)
            .cause(UPDATED_CAUSE);
        return organizationError;
    }

    @Before
    public void initTest() {
        organizationError = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizationError() throws Exception {
        int databaseSizeBeforeCreate = organizationErrorRepository.findAll().size();

        // Create the OrganizationError
        restOrganizationErrorMockMvc.perform(post("/api/organization-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationError)))
            .andExpect(status().isCreated());

        // Validate the OrganizationError in the database
        List<OrganizationError> organizationErrorList = organizationErrorRepository.findAll();
        assertThat(organizationErrorList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationError testOrganizationError = organizationErrorList.get(organizationErrorList.size() - 1);
        assertThat(testOrganizationError.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testOrganizationError.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testOrganizationError.getExternalDbId()).isEqualTo(DEFAULT_EXTERNAL_DB_ID);
        assertThat(testOrganizationError.getInvalidValue()).isEqualTo(DEFAULT_INVALID_VALUE);
        assertThat(testOrganizationError.getCause()).isEqualTo(DEFAULT_CAUSE);
    }

    @Test
    @Transactional
    public void createOrganizationErrorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationErrorRepository.findAll().size();

        // Create the OrganizationError with an existing ID
        organizationError.setId(id);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationErrorMockMvc.perform(post("/api/organization-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationError)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationError in the database
        List<OrganizationError> organizationErrorList = organizationErrorRepository.findAll();
        assertThat(organizationErrorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrganizationErrors() throws Exception {
        // Initialize the database
        organizationErrorRepository.saveAndFlush(organizationError);

        // Get all the organizationErrorList
        restOrganizationErrorMockMvc.perform(get("/api/organization-errors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationError.getId().toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME.toString())))
            .andExpect(jsonPath("$.[*].externalDbId").value(hasItem(DEFAULT_EXTERNAL_DB_ID.toString())))
            .andExpect(jsonPath("$.[*].invalidValue").value(hasItem(DEFAULT_INVALID_VALUE.toString())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE.toString())));
    }
    
    @Test
    @Transactional
    public void getOrganizationError() throws Exception {
        // Initialize the database
        organizationErrorRepository.saveAndFlush(organizationError);

        // Get the organizationError
        restOrganizationErrorMockMvc.perform(get("/api/organization-errors/{id}", organizationError.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizationError.getId().toString()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME.toString()))
            .andExpect(jsonPath("$.externalDbId").value(DEFAULT_EXTERNAL_DB_ID.toString()))
            .andExpect(jsonPath("$.invalidValue").value(DEFAULT_INVALID_VALUE.toString()))
            .andExpect(jsonPath("$.cause").value(DEFAULT_CAUSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationError() throws Exception {
        // Get the organizationError
        restOrganizationErrorMockMvc.perform(get("/api/organization-errors/{id}",  TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizationError() throws Exception {
        // Initialize the database
        organizationErrorRepository.saveAndFlush(organizationError);

        int databaseSizeBeforeUpdate = organizationErrorRepository.findAll().size();

        // Update the organizationError
        OrganizationError updatedOrganizationError = organizationErrorRepository.findById(organizationError.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationError are not directly saved in db
        em.detach(updatedOrganizationError);
        updatedOrganizationError
            .entityName(UPDATED_ENTITY_NAME)
            .fieldName(UPDATED_FIELD_NAME)
            .externalDbId(UPDATED_EXTERNAL_DB_ID)
            .invalidValue(UPDATED_INVALID_VALUE)
            .cause(UPDATED_CAUSE);

        restOrganizationErrorMockMvc.perform(put("/api/organization-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationError)))
            .andExpect(status().isOk());

        // Validate the OrganizationError in the database
        List<OrganizationError> organizationErrorList = organizationErrorRepository.findAll();
        assertThat(organizationErrorList).hasSize(databaseSizeBeforeUpdate);
        OrganizationError testOrganizationError = organizationErrorList.get(organizationErrorList.size() - 1);
        assertThat(testOrganizationError.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testOrganizationError.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testOrganizationError.getExternalDbId()).isEqualTo(UPDATED_EXTERNAL_DB_ID);
        assertThat(testOrganizationError.getInvalidValue()).isEqualTo(UPDATED_INVALID_VALUE);
        assertThat(testOrganizationError.getCause()).isEqualTo(UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizationError() throws Exception {
        int databaseSizeBeforeUpdate = organizationErrorRepository.findAll().size();

        // Create the OrganizationError

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationErrorMockMvc.perform(put("/api/organization-errors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationError)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationError in the database
        List<OrganizationError> organizationErrorList = organizationErrorRepository.findAll();
        assertThat(organizationErrorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganizationError() throws Exception {
        // Initialize the database
        organizationErrorRepository.saveAndFlush(organizationError);

        int databaseSizeBeforeDelete = organizationErrorRepository.findAll().size();

        // Delete the organizationError
        restOrganizationErrorMockMvc.perform(delete("/api/organization-errors/{id}", organizationError.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationError> organizationErrorList = organizationErrorRepository.findAll();
        assertThat(organizationErrorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationError.class);
        OrganizationError organizationError1 = new OrganizationError();
        organizationError1.setId(id);
        OrganizationError organizationError2 = new OrganizationError();
        organizationError2.setId(organizationError1.getId());
        assertThat(organizationError1).isEqualTo(organizationError2);
        organizationError2.setId(UUID.randomUUID());
        assertThat(organizationError1).isNotEqualTo(organizationError2);
        organizationError1.setId(null);
        assertThat(organizationError1).isNotEqualTo(organizationError2);
    }
}
