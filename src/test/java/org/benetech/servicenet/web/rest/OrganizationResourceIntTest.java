package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.ZeroCodeSpringJUnit4Runner;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.repository.OrganizationRepository;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.service.ActivityService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.OrganizationDTO;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
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
 * Test class for the OrganizationResource REST controller.
 *
 * @see OrganizationResource
 */
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class OrganizationResourceIntTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    private Organization organizationWithAllRelations;

    private static final Boolean DEFAULT_ACTIVE = true;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization result = OrganizationMother.createDefault(DEFAULT_ACTIVE);
        result.setAccount(SystemAccountMother.createDefaultAndPersist(em));
        return result;
    }

    public static Organization createEntityWithAllRelations(EntityManager em) {
        return OrganizationMother.createDefaultWithAllRelationsAndPersist(em);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final OrganizationResource organizationResource = new OrganizationResource(
            organizationService, userService, activityService
        );
        this.restOrganizationMockMvc = MockMvcBuilders.standaloneSetup(organizationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        organization = createEntity(em);
        organizationWithAllRelations = createEntityWithAllRelations(em);
    }

    @Test
    @Transactional
    public void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(OrganizationMother.DEFAULT_NAME);
        assertThat(testOrganization.getAlternateName()).isEqualTo(OrganizationMother.DEFAULT_ALTERNATE_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(OrganizationMother.DEFAULT_DESCRIPTION);
        assertThat(testOrganization.getEmail()).isEqualTo(OrganizationMother.DEFAULT_EMAIL);
        assertThat(testOrganization.getUrl()).isEqualTo(OrganizationMother.DEFAULT_URL);
        assertThat(testOrganization.getTaxStatus()).isEqualTo(OrganizationMother.DEFAULT_TAX_STATUS);
        assertThat(testOrganization.getTaxId()).isEqualTo(OrganizationMother.DEFAULT_TAX_ID);
        assertThat(testOrganization.getYearIncorporated()).isEqualTo(OrganizationMother.DEFAULT_YEAR_INCORPORATED);
        assertThat(testOrganization.getLegalStatus()).isEqualTo(OrganizationMother.DEFAULT_LEGAL_STATUS);
        assertThat(testOrganization.isActive()).isEqualTo(OrganizationMother.DEFAULT_ACTIVE);
        assertThat(testOrganization.getUpdatedAt()).isNotNull();
    }

    @Test
    @Transactional
    public void createOrganizationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // Create the Organization with an existing ID
        organization.setId(TestConstants.UUID_1);
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setName(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setActive(null);

        // Create the Organization, which fails.
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        restOrganizationMockMvc.perform(post("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizations() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(organization.getAccount());
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc.perform(get("/api/organizations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(OrganizationMother.DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].alternateName").value(hasItem(OrganizationMother.DEFAULT_ALTERNATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(OrganizationMother.DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(OrganizationMother.DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(OrganizationMother.DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].taxStatus").value(hasItem(OrganizationMother.DEFAULT_TAX_STATUS.toString())))
            .andExpect(jsonPath("$.[*].taxId").value(hasItem(OrganizationMother.DEFAULT_TAX_ID.toString())))
            .andExpect(jsonPath("$.[*].yearIncorporated").value(hasItem(OrganizationMother.DEFAULT_YEAR_INCORPORATED
                .toString())))
            .andExpect(jsonPath("$.[*].legalStatus").value(hasItem(OrganizationMother.DEFAULT_LEGAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(OrganizationMother.DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getOrganization() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(organization.getAccount());
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().toString()))
            .andExpect(jsonPath("$.name").value(OrganizationMother.DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alternateName").value(OrganizationMother.DEFAULT_ALTERNATE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(OrganizationMother.DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.email").value(OrganizationMother.DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.url").value(OrganizationMother.DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.taxStatus").value(OrganizationMother.DEFAULT_TAX_STATUS.toString()))
            .andExpect(jsonPath("$.taxId").value(OrganizationMother.DEFAULT_TAX_ID.toString()))
            .andExpect(jsonPath("$.yearIncorporated").value(OrganizationMother.DEFAULT_YEAR_INCORPORATED.toString()))
            .andExpect(jsonPath("$.legalStatus").value(OrganizationMother.DEFAULT_LEGAL_STATUS.toString()))
            .andExpect(jsonPath("$.active").value(OrganizationMother.DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get("/api/organizations/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganization() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(organization.getAccount());
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).get();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization
            .name(OrganizationMother.UPDATED_NAME)
            .alternateName(OrganizationMother.UPDATED_ALTERNATE_NAME)
            .description(OrganizationMother.UPDATED_DESCRIPTION)
            .email(OrganizationMother.UPDATED_EMAIL)
            .url(OrganizationMother.UPDATED_URL)
            .taxStatus(OrganizationMother.UPDATED_TAX_STATUS)
            .taxId(OrganizationMother.UPDATED_TAX_ID)
            .yearIncorporated(OrganizationMother.UPDATED_YEAR_INCORPORATED)
            .legalStatus(OrganizationMother.UPDATED_LEGAL_STATUS)
            .active(OrganizationMother.UPDATED_ACTIVE);
        OrganizationDTO organizationDTO = organizationMapper.toDto(updatedOrganization);

        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(OrganizationMother.UPDATED_NAME);
        assertThat(testOrganization.getAlternateName()).isEqualTo(OrganizationMother.UPDATED_ALTERNATE_NAME);
        assertThat(testOrganization.getDescription()).isEqualTo(OrganizationMother.UPDATED_DESCRIPTION);
        assertThat(testOrganization.getEmail()).isEqualTo(OrganizationMother.UPDATED_EMAIL);
        assertThat(testOrganization.getUrl()).isEqualTo(OrganizationMother.UPDATED_URL);
        assertThat(testOrganization.getTaxStatus()).isEqualTo(OrganizationMother.UPDATED_TAX_STATUS);
        assertThat(testOrganization.getTaxId()).isEqualTo(OrganizationMother.UPDATED_TAX_ID);
        assertThat(testOrganization.getYearIncorporated()).isEqualTo(OrganizationMother.UPDATED_YEAR_INCORPORATED);
        assertThat(testOrganization.getLegalStatus()).isEqualTo(OrganizationMother.UPDATED_LEGAL_STATUS);
        assertThat(testOrganization.isActive()).isEqualTo(OrganizationMother.UPDATED_ACTIVE);
        assertThat(testOrganization.getUpdatedAt()).isNotNull();
    }

    @Test
    @Transactional
    public void updateNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc.perform(put("/api/organizations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganization() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(organization.getAccount());
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Get the organization
        restOrganizationMockMvc.perform(delete("/api/organizations/{id}", organization.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void deleteOrganizationWithAllRelations() throws Exception {
        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Get the organization
        restOrganizationMockMvc.perform(delete("/api/organizations/{id}", organizationWithAllRelations.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organization.class);
        Organization organization1 = new Organization();
        organization1.setId(TestConstants.UUID_1);
        Organization organization2 = new Organization();
        organization2.setId(organization1.getId());
        assertThat(organization1).isEqualTo(organization2);
        organization2.setId(TestConstants.UUID_2);
        assertThat(organization1).isNotEqualTo(organization2);
        organization1.setId(null);
        assertThat(organization1).isNotEqualTo(organization2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationDTO.class);
        OrganizationDTO organizationDTO1 = new OrganizationDTO();
        organizationDTO1.setId(TestConstants.UUID_1);
        OrganizationDTO organizationDTO2 = new OrganizationDTO();
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
        organizationDTO2.setId(organizationDTO1.getId());
        assertThat(organizationDTO1).isEqualTo(organizationDTO2);
        organizationDTO2.setId(TestConstants.UUID_2);
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
        organizationDTO1.setId(null);
        assertThat(organizationDTO1).isNotEqualTo(organizationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(organizationMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(organizationMapper.fromId(null)).isNull();
    }
}
