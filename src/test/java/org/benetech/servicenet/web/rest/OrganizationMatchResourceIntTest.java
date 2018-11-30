package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.benetech.servicenet.interceptor.HibernateInterceptor;
import org.benetech.servicenet.repository.OrganizationMatchRepository;
import org.benetech.servicenet.service.OrganizationMatchService;
import org.benetech.servicenet.service.dto.OrganizationMatchDTO;
import org.benetech.servicenet.service.mapper.OrganizationMatchMapper;
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
 * Test class for the OrganizationMatchResource REST controller.
 *
 * @see OrganizationMatchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class OrganizationMatchResourceIntTest {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String DEFAULT_FIELD_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_MATCHED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_MATCHED_VALUE = "BBBBBBBBBB";

    @Autowired
    private HibernateInterceptor hibernateInterceptor;

    @Autowired
    private OrganizationMatchRepository organizationMatchRepository;

    @Autowired
    private OrganizationMatchMapper organizationMatchMapper;

    @Autowired
    private OrganizationMatchService organizationMatchService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganizationMatchMockMvc;

    private OrganizationMatch organizationMatch;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationMatch createEntity(EntityManager em) {
        OrganizationMatch organizationMatch = new OrganizationMatch()
            .fieldName(DEFAULT_FIELD_NAME)
            .timestamp(DEFAULT_TIMESTAMP)
            .deleted(DEFAULT_DELETED)
            .fieldPath(DEFAULT_FIELD_PATH)
            .matchedValue(DEFAULT_MATCHED_VALUE);
        return organizationMatch;
    }

    @Before
    public void setup() {
        hibernateInterceptor.disableEventListeners();
        MockitoAnnotations.initMocks(this);
        final OrganizationMatchResource organizationMatchResource = new OrganizationMatchResource(organizationMatchService);
        this.restOrganizationMatchMockMvc = MockMvcBuilders.standaloneSetup(organizationMatchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        organizationMatch = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizationMatch() throws Exception {
        int databaseSizeBeforeCreate = organizationMatchRepository.findAll().size();

        // Create the OrganizationMatch
        OrganizationMatchDTO organizationMatchDTO = organizationMatchMapper.toDto(organizationMatch);
        restOrganizationMatchMockMvc.perform(post("/api/organization-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMatchDTO)))
            .andExpect(status().isCreated());

        // Validate the OrganizationMatch in the database
        List<OrganizationMatch> organizationMatchList = organizationMatchRepository.findAll();
        assertThat(organizationMatchList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationMatch testOrganizationMatch = organizationMatchList.get(organizationMatchList.size() - 1);
        assertThat(testOrganizationMatch.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testOrganizationMatch.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testOrganizationMatch.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testOrganizationMatch.getFieldPath()).isEqualTo(DEFAULT_FIELD_PATH);
        assertThat(testOrganizationMatch.getMatchedValue()).isEqualTo(DEFAULT_MATCHED_VALUE);
    }

    @Test
    @Transactional
    public void createOrganizationMatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationMatchRepository.findAll().size();

        // Create the OrganizationMatch with an existing ID
        organizationMatch.setId(TestConstants.UUID_1);
        OrganizationMatchDTO organizationMatchDTO = organizationMatchMapper.toDto(organizationMatch);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMatchMockMvc.perform(post("/api/organization-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationMatch in the database
        List<OrganizationMatch> organizationMatchList = organizationMatchRepository.findAll();
        assertThat(organizationMatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrganizationMatches() throws Exception {
        // Initialize the database
        organizationMatchRepository.saveAndFlush(organizationMatch);

        // Get all the organizationMatchList
        restOrganizationMatchMockMvc.perform(get("/api/organization-matches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationMatch.getId().toString())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].fieldPath").value(hasItem(DEFAULT_FIELD_PATH.toString())))
            .andExpect(jsonPath("$.[*].matchedValue").value(hasItem(DEFAULT_MATCHED_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getOrganizationMatch() throws Exception {
        // Initialize the database
        organizationMatchRepository.saveAndFlush(organizationMatch);

        // Get the organizationMatch
        restOrganizationMatchMockMvc.perform(get("/api/organization-matches/{id}", organizationMatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizationMatch.getId().toString()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME.toString()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.fieldPath").value(DEFAULT_FIELD_PATH.toString()))
            .andExpect(jsonPath("$.matchedValue").value(DEFAULT_MATCHED_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationMatch() throws Exception {
        // Get the organizationMatch
        restOrganizationMatchMockMvc.perform(get("/api/organization-matches/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizationMatch() throws Exception {
        // Initialize the database
        organizationMatchRepository.saveAndFlush(organizationMatch);

        int databaseSizeBeforeUpdate = organizationMatchRepository.findAll().size();

        // Update the organizationMatch
        OrganizationMatch updatedOrganizationMatch = organizationMatchRepository.findById(organizationMatch.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationMatch are not directly saved in db
        em.detach(updatedOrganizationMatch);
        updatedOrganizationMatch
            .fieldName(UPDATED_FIELD_NAME)
            .timestamp(UPDATED_TIMESTAMP)
            .deleted(UPDATED_DELETED)
            .fieldPath(UPDATED_FIELD_PATH)
            .matchedValue(UPDATED_MATCHED_VALUE);
        OrganizationMatchDTO organizationMatchDTO = organizationMatchMapper.toDto(updatedOrganizationMatch);

        restOrganizationMatchMockMvc.perform(put("/api/organization-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMatchDTO)))
            .andExpect(status().isOk());

        // Validate the OrganizationMatch in the database
        List<OrganizationMatch> organizationMatchList = organizationMatchRepository.findAll();
        assertThat(organizationMatchList).hasSize(databaseSizeBeforeUpdate);
        OrganizationMatch testOrganizationMatch = organizationMatchList.get(organizationMatchList.size() - 1);
        assertThat(testOrganizationMatch.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testOrganizationMatch.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testOrganizationMatch.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testOrganizationMatch.getFieldPath()).isEqualTo(UPDATED_FIELD_PATH);
        assertThat(testOrganizationMatch.getMatchedValue()).isEqualTo(UPDATED_MATCHED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizationMatch() throws Exception {
        int databaseSizeBeforeUpdate = organizationMatchRepository.findAll().size();

        // Create the OrganizationMatch
        OrganizationMatchDTO organizationMatchDTO = organizationMatchMapper.toDto(organizationMatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMatchMockMvc.perform(put("/api/organization-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationMatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationMatch in the database
        List<OrganizationMatch> organizationMatchList = organizationMatchRepository.findAll();
        assertThat(organizationMatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganizationMatch() throws Exception {
        // Initialize the database
        organizationMatchRepository.saveAndFlush(organizationMatch);

        int databaseSizeBeforeDelete = organizationMatchRepository.findAll().size();

        // Get the organizationMatch
        restOrganizationMatchMockMvc.perform(delete("/api/organization-matches/{id}", organizationMatch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrganizationMatch> organizationMatchList = organizationMatchRepository.findAll();
        assertThat(organizationMatchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMatch.class);
        OrganizationMatch organizationMatch1 = new OrganizationMatch();
        organizationMatch1.setId(TestConstants.UUID_1);
        OrganizationMatch organizationMatch2 = new OrganizationMatch();
        organizationMatch2.setId(organizationMatch1.getId());
        assertThat(organizationMatch1).isEqualTo(organizationMatch2);
        organizationMatch2.setId(TestConstants.UUID_2);
        assertThat(organizationMatch1).isNotEqualTo(organizationMatch2);
        organizationMatch1.setId(null);
        assertThat(organizationMatch1).isNotEqualTo(organizationMatch2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMatchDTO.class);
        OrganizationMatchDTO organizationMatchDTO1 = new OrganizationMatchDTO();
        organizationMatchDTO1.setId(TestConstants.UUID_1);
        OrganizationMatchDTO organizationMatchDTO2 = new OrganizationMatchDTO();
        assertThat(organizationMatchDTO1).isNotEqualTo(organizationMatchDTO2);
        organizationMatchDTO2.setId(organizationMatchDTO1.getId());
        assertThat(organizationMatchDTO1).isEqualTo(organizationMatchDTO2);
        organizationMatchDTO2.setId(TestConstants.UUID_2);
        assertThat(organizationMatchDTO1).isNotEqualTo(organizationMatchDTO2);
        organizationMatchDTO1.setId(null);
        assertThat(organizationMatchDTO1).isNotEqualTo(organizationMatchDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(organizationMatchMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(organizationMatchMapper.fromId(null)).isNull();
    }
}
