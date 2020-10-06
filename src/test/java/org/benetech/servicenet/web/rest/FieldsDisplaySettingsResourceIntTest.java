package org.benetech.servicenet.web.rest;

import java.util.Collections;
import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.FieldsDisplaySettings;
import org.benetech.servicenet.repository.FieldsDisplaySettingsRepository;
import org.benetech.servicenet.service.FieldsDisplaySettingsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.FieldsDisplaySettingsDTO;
import org.benetech.servicenet.service.mapper.FieldsDisplaySettingsMapper;
import org.benetech.servicenet.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.benetech.servicenet.ZeroCodeSpringJUnit4Runner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link FieldsDisplaySettingsResource} REST controller.
 */
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class FieldsDisplaySettingsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final List<String> DEFAULT_LOCATION_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_LOCATION_FIELDS = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_ORGANIZATION_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_ORGANIZATION_FIELDS = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_PHYSICAL_ADDRESS_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_PHYSICAL_ADDRESS_FIELDS = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_POSTAL_ADDRESS_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_POSTAL_ADDRESS_FIELDS = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_SERVICE_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_SERVICE_FIELDS = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_SERVICE_TAXONOMIES_DETAILS_FIELDS = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_CONTACT_DETAILS_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_CONTACT_DETAILS_FIELDS = Collections.singletonList("BBBBBBBBBB");
    private static final String SYSTEM_ACCOUNT_NAME = "healthleads";

    @Autowired
    private FieldsDisplaySettingsRepository fieldsDisplaySettingsRepository;

    @Autowired
    private FieldsDisplaySettingsMapper fieldsDisplaySettingsMapper;

    @Autowired
    private FieldsDisplaySettingsService fieldsDisplaySettingsService;

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

    private MockMvc restFieldsDisplaySettingsMockMvc;

    private FieldsDisplaySettings fieldsDisplaySettings;

    @Mock
    private UserService mockUserService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final FieldsDisplaySettingsResource fieldsDisplaySettingsResource = new FieldsDisplaySettingsResource(
            fieldsDisplaySettingsService, mockUserService);
        this.restFieldsDisplaySettingsMockMvc = MockMvcBuilders.standaloneSetup(fieldsDisplaySettingsResource)
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
    public static FieldsDisplaySettings createEntity(EntityManager em) {
        FieldsDisplaySettings fieldsDisplaySettings = new FieldsDisplaySettings()
            .name(DEFAULT_NAME)
            .locationFields(DEFAULT_LOCATION_FIELDS)
            .organizationFields(DEFAULT_ORGANIZATION_FIELDS)
            .physicalAddressFields(DEFAULT_PHYSICAL_ADDRESS_FIELDS)
            .postalAddressFields(DEFAULT_POSTAL_ADDRESS_FIELDS)
            .serviceFields(DEFAULT_SERVICE_FIELDS)
            .serviceTaxonomiesDetailsFields(DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELDS)
            .contactDetailsFields(DEFAULT_CONTACT_DETAILS_FIELDS);
        return fieldsDisplaySettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FieldsDisplaySettings createUpdatedEntity(EntityManager em) {
        FieldsDisplaySettings fieldsDisplaySettings = new FieldsDisplaySettings()
            .name(UPDATED_NAME)
            .locationFields(UPDATED_LOCATION_FIELDS)
            .organizationFields(UPDATED_ORGANIZATION_FIELDS)
            .physicalAddressFields(UPDATED_PHYSICAL_ADDRESS_FIELDS)
            .postalAddressFields(UPDATED_POSTAL_ADDRESS_FIELDS)
            .serviceFields(UPDATED_SERVICE_FIELDS)
            .serviceTaxonomiesDetailsFields(UPDATED_SERVICE_TAXONOMIES_DETAILS_FIELDS)
            .contactDetailsFields(UPDATED_CONTACT_DETAILS_FIELDS);
        return fieldsDisplaySettings;
    }

    @Before
    public void initTest() {
        fieldsDisplaySettings = createEntity(em);
    }

    @Test
    @Transactional
    public void createFieldsDisplaySettings() throws Exception {
        int databaseSizeBeforeCreate = fieldsDisplaySettingsRepository.findAll().size();
        when(mockUserService.getCurrentSystemAccountName()).thenReturn(SYSTEM_ACCOUNT_NAME);
        // Create the FieldsDisplaySettings
        FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO = fieldsDisplaySettingsMapper.toDto(fieldsDisplaySettings);
        restFieldsDisplaySettingsMockMvc.perform(post("/api/fields-display-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fieldsDisplaySettingsDTO)))
            .andExpect(status().isCreated());

        // Validate the FieldsDisplaySettings in the database
        List<FieldsDisplaySettings> fieldsDisplaySettingsList = fieldsDisplaySettingsRepository.findAll();
        assertThat(fieldsDisplaySettingsList).hasSize(databaseSizeBeforeCreate + 1);
        FieldsDisplaySettings testFieldsDisplaySettings = fieldsDisplaySettingsList
            .get(fieldsDisplaySettingsList.size() - 1);
        assertThat(testFieldsDisplaySettings.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFieldsDisplaySettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fieldsDisplaySettingsRepository.findAll().size();

        // Create the FieldsDisplaySettings with an existing ID
        fieldsDisplaySettings.setId(TestConstants.UUID_1);
        FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO = fieldsDisplaySettingsMapper.toDto(fieldsDisplaySettings);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldsDisplaySettingsMockMvc.perform(post("/api/fields-display-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fieldsDisplaySettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FieldsDisplaySettings in the database
        List<FieldsDisplaySettings> fieldsDisplaySettingsList = fieldsDisplaySettingsRepository.findAll();
        assertThat(fieldsDisplaySettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFieldsDisplaySettings() throws Exception {
        // Initialize the database
        fieldsDisplaySettingsRepository.saveAndFlush(fieldsDisplaySettings);

        // Get all the fieldsDisplaySettingsList
        restFieldsDisplaySettingsMockMvc.perform(get("/api/fields-display-settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fieldsDisplaySettings.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].locationFields").value(hasItem(DEFAULT_LOCATION_FIELDS)))
            .andExpect(jsonPath("$.[*].organizationFields").value(hasItem(DEFAULT_ORGANIZATION_FIELDS)))
            .andExpect(jsonPath("$.[*].physicalAddressFields").value(hasItem(DEFAULT_PHYSICAL_ADDRESS_FIELDS)))
            .andExpect(jsonPath("$.[*].postalAddressFields").value(hasItem(DEFAULT_POSTAL_ADDRESS_FIELDS)))
            .andExpect(jsonPath("$.[*].serviceFields").value(hasItem(DEFAULT_SERVICE_FIELDS)))
            .andExpect(jsonPath("$.[*].serviceTaxonomiesDetailsFields")
                .value(hasItem(DEFAULT_SERVICE_TAXONOMIES_DETAILS_FIELDS)))
            .andExpect(jsonPath("$.[*].contactDetailsFields").value(hasItem(DEFAULT_CONTACT_DETAILS_FIELDS)));
    }

    @Test
    @Transactional
    public void getFieldsDisplaySettings() throws Exception {
        // Initialize the database
        fieldsDisplaySettingsRepository.saveAndFlush(fieldsDisplaySettings);

        // Get the fieldsDisplaySettings
        restFieldsDisplaySettingsMockMvc
            .perform(get("/api/fields-display-settings/{id}", fieldsDisplaySettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fieldsDisplaySettings.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFieldsDisplaySettings() throws Exception {
        // Get the fieldsDisplaySettings
        restFieldsDisplaySettingsMockMvc.perform(get("/api/fields-display-settings/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFieldsDisplaySettings() throws Exception {
        // Initialize the database
        fieldsDisplaySettingsRepository.saveAndFlush(fieldsDisplaySettings);

        int databaseSizeBeforeUpdate = fieldsDisplaySettingsRepository.findAll().size();

        // Update the fieldsDisplaySettings
        FieldsDisplaySettings updatedFieldsDisplaySettings = fieldsDisplaySettingsRepository
            .findById(fieldsDisplaySettings.getId()).get();
        // Disconnect from session so that the updates on updatedFieldsDisplaySettings are not directly saved in db
        em.detach(updatedFieldsDisplaySettings);
        updatedFieldsDisplaySettings
            .name(UPDATED_NAME)
            .locationFields(UPDATED_LOCATION_FIELDS)
            .organizationFields(UPDATED_ORGANIZATION_FIELDS)
            .physicalAddressFields(UPDATED_PHYSICAL_ADDRESS_FIELDS)
            .postalAddressFields(UPDATED_POSTAL_ADDRESS_FIELDS)
            .serviceFields(UPDATED_SERVICE_FIELDS)
            .serviceTaxonomiesDetailsFields(UPDATED_SERVICE_TAXONOMIES_DETAILS_FIELDS)
            .contactDetailsFields(UPDATED_CONTACT_DETAILS_FIELDS);
        FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO = fieldsDisplaySettingsMapper.toDto(updatedFieldsDisplaySettings);

        restFieldsDisplaySettingsMockMvc.perform(put("/api/fields-display-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fieldsDisplaySettingsDTO)))
            .andExpect(status().isOk());

        // Validate the FieldsDisplaySettings in the database
        List<FieldsDisplaySettings> fieldsDisplaySettingsList = fieldsDisplaySettingsRepository.findAll();
        assertThat(fieldsDisplaySettingsList).hasSize(databaseSizeBeforeUpdate);
        FieldsDisplaySettings testFieldsDisplaySettings = fieldsDisplaySettingsList
            .get(fieldsDisplaySettingsList.size() - 1);
        assertThat(testFieldsDisplaySettings.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingFieldsDisplaySettings() throws Exception {
        int databaseSizeBeforeUpdate = fieldsDisplaySettingsRepository.findAll().size();

        // Create the FieldsDisplaySettings
        FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO = fieldsDisplaySettingsMapper.toDto(fieldsDisplaySettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldsDisplaySettingsMockMvc.perform(put("/api/fields-display-settings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fieldsDisplaySettingsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FieldsDisplaySettings in the database
        List<FieldsDisplaySettings> fieldsDisplaySettingsList = fieldsDisplaySettingsRepository.findAll();
        assertThat(fieldsDisplaySettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFieldsDisplaySettings() throws Exception {
        // Initialize the database
        fieldsDisplaySettingsRepository.saveAndFlush(fieldsDisplaySettings);

        int databaseSizeBeforeDelete = fieldsDisplaySettingsRepository.findAll().size();

        // Delete the fieldsDisplaySettings
        restFieldsDisplaySettingsMockMvc
            .perform(delete("/api/fields-display-settings/{id}", fieldsDisplaySettings.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FieldsDisplaySettings> fieldsDisplaySettingsList = fieldsDisplaySettingsRepository.findAll();
        assertThat(fieldsDisplaySettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldsDisplaySettings.class);
        FieldsDisplaySettings fieldsDisplaySettings1 = new FieldsDisplaySettings();
        fieldsDisplaySettings1.setId(TestConstants.UUID_1);
        FieldsDisplaySettings fieldsDisplaySettings2 = new FieldsDisplaySettings();
        fieldsDisplaySettings2.setId(fieldsDisplaySettings1.getId());
        assertThat(fieldsDisplaySettings1).isEqualTo(fieldsDisplaySettings2);
        fieldsDisplaySettings2.setId(TestConstants.UUID_2);
        assertThat(fieldsDisplaySettings1).isNotEqualTo(fieldsDisplaySettings2);
        fieldsDisplaySettings1.setId(null);
        assertThat(fieldsDisplaySettings1).isNotEqualTo(fieldsDisplaySettings2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FieldsDisplaySettingsDTO.class);
        FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO1 = new FieldsDisplaySettingsDTO();
        fieldsDisplaySettingsDTO1.setId(TestConstants.UUID_1);
        FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO2 = new FieldsDisplaySettingsDTO();
        assertThat(fieldsDisplaySettingsDTO1).isNotEqualTo(fieldsDisplaySettingsDTO2);
        fieldsDisplaySettingsDTO2.setId(fieldsDisplaySettingsDTO1.getId());
        assertThat(fieldsDisplaySettingsDTO1).isEqualTo(fieldsDisplaySettingsDTO2);
        fieldsDisplaySettingsDTO2.setId(TestConstants.UUID_2);
        assertThat(fieldsDisplaySettingsDTO1).isNotEqualTo(fieldsDisplaySettingsDTO2);
        fieldsDisplaySettingsDTO1.setId(null);
        assertThat(fieldsDisplaySettingsDTO1).isNotEqualTo(fieldsDisplaySettingsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fieldsDisplaySettingsMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(fieldsDisplaySettingsMapper.fromId(null)).isNull();
    }
}
