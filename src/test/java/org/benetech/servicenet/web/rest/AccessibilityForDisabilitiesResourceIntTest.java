package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.repository.AccessibilityForDisabilitiesRepository;
import org.benetech.servicenet.service.AccessibilityForDisabilitiesService;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.benetech.servicenet.service.mapper.AccessibilityForDisabilitiesMapper;
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
 * Test class for the AccessibilityForDisabilitiesResource REST controller.
 *
 * @see AccessibilityForDisabilitiesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class AccessibilityForDisabilitiesResourceIntTest {

    private static final String DEFAULT_ACCESSIBILITY = "AAAAAAAAAA";
    private static final String UPDATED_ACCESSIBILITY = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    @Autowired
    private AccessibilityForDisabilitiesRepository accessibilityForDisabilitiesRepository;

    @Autowired
    private AccessibilityForDisabilitiesMapper accessibilityForDisabilitiesMapper;

    @Autowired
    private AccessibilityForDisabilitiesService accessibilityForDisabilitiesService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccessibilityForDisabilitiesMockMvc;

    private AccessibilityForDisabilities accessibilityForDisabilities;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccessibilityForDisabilities createEntity(EntityManager em) {
        AccessibilityForDisabilities accessibilityForDisabilities = new AccessibilityForDisabilities()
            .accessibility(DEFAULT_ACCESSIBILITY)
            .details(DEFAULT_DETAILS);
        return accessibilityForDisabilities;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AccessibilityForDisabilitiesResource accessibilityForDisabilitiesResource = new AccessibilityForDisabilitiesResource(accessibilityForDisabilitiesService);
        this.restAccessibilityForDisabilitiesMockMvc = MockMvcBuilders.standaloneSetup(accessibilityForDisabilitiesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        accessibilityForDisabilities = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccessibilityForDisabilities() throws Exception {
        int databaseSizeBeforeCreate = accessibilityForDisabilitiesRepository.findAll().size();

        // Create the AccessibilityForDisabilities
        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO = accessibilityForDisabilitiesMapper.toDto(accessibilityForDisabilities);
        restAccessibilityForDisabilitiesMockMvc.perform(post("/api/accessibility-for-disabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessibilityForDisabilitiesDTO)))
            .andExpect(status().isCreated());

        // Validate the AccessibilityForDisabilities in the database
        List<AccessibilityForDisabilities> accessibilityForDisabilitiesList = accessibilityForDisabilitiesRepository.findAll();
        assertThat(accessibilityForDisabilitiesList).hasSize(databaseSizeBeforeCreate + 1);
        AccessibilityForDisabilities testAccessibilityForDisabilities = accessibilityForDisabilitiesList.get(accessibilityForDisabilitiesList.size() - 1);
        assertThat(testAccessibilityForDisabilities.getAccessibility()).isEqualTo(DEFAULT_ACCESSIBILITY);
        assertThat(testAccessibilityForDisabilities.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    public void createAccessibilityForDisabilitiesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accessibilityForDisabilitiesRepository.findAll().size();

        // Create the AccessibilityForDisabilities with an existing ID
        accessibilityForDisabilities.setId(1L);
        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO = accessibilityForDisabilitiesMapper.toDto(accessibilityForDisabilities);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccessibilityForDisabilitiesMockMvc.perform(post("/api/accessibility-for-disabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessibilityForDisabilitiesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AccessibilityForDisabilities in the database
        List<AccessibilityForDisabilities> accessibilityForDisabilitiesList = accessibilityForDisabilitiesRepository.findAll();
        assertThat(accessibilityForDisabilitiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAccessibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = accessibilityForDisabilitiesRepository.findAll().size();
        // set the field null
        accessibilityForDisabilities.setAccessibility(null);

        // Create the AccessibilityForDisabilities, which fails.
        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO = accessibilityForDisabilitiesMapper.toDto(accessibilityForDisabilities);

        restAccessibilityForDisabilitiesMockMvc.perform(post("/api/accessibility-for-disabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessibilityForDisabilitiesDTO)))
            .andExpect(status().isBadRequest());

        List<AccessibilityForDisabilities> accessibilityForDisabilitiesList = accessibilityForDisabilitiesRepository.findAll();
        assertThat(accessibilityForDisabilitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccessibilityForDisabilities() throws Exception {
        // Initialize the database
        accessibilityForDisabilitiesRepository.saveAndFlush(accessibilityForDisabilities);

        // Get all the accessibilityForDisabilitiesList
        restAccessibilityForDisabilitiesMockMvc.perform(get("/api/accessibility-for-disabilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accessibilityForDisabilities.getId().intValue())))
            .andExpect(jsonPath("$.[*].accessibility").value(hasItem(DEFAULT_ACCESSIBILITY.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS.toString())));
    }

    @Test
    @Transactional
    public void getAccessibilityForDisabilities() throws Exception {
        // Initialize the database
        accessibilityForDisabilitiesRepository.saveAndFlush(accessibilityForDisabilities);

        // Get the accessibilityForDisabilities
        restAccessibilityForDisabilitiesMockMvc.perform(get("/api/accessibility-for-disabilities/{id}", accessibilityForDisabilities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accessibilityForDisabilities.getId().intValue()))
            .andExpect(jsonPath("$.accessibility").value(DEFAULT_ACCESSIBILITY.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccessibilityForDisabilities() throws Exception {
        // Get the accessibilityForDisabilities
        restAccessibilityForDisabilitiesMockMvc.perform(get("/api/accessibility-for-disabilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccessibilityForDisabilities() throws Exception {
        // Initialize the database
        accessibilityForDisabilitiesRepository.saveAndFlush(accessibilityForDisabilities);

        int databaseSizeBeforeUpdate = accessibilityForDisabilitiesRepository.findAll().size();

        // Update the accessibilityForDisabilities
        AccessibilityForDisabilities updatedAccessibilityForDisabilities = accessibilityForDisabilitiesRepository.findById(accessibilityForDisabilities.getId()).get();
        // Disconnect from session so that the updates on updatedAccessibilityForDisabilities are not directly saved in db
        em.detach(updatedAccessibilityForDisabilities);
        updatedAccessibilityForDisabilities
            .accessibility(UPDATED_ACCESSIBILITY)
            .details(UPDATED_DETAILS);
        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO = accessibilityForDisabilitiesMapper.toDto(updatedAccessibilityForDisabilities);

        restAccessibilityForDisabilitiesMockMvc.perform(put("/api/accessibility-for-disabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessibilityForDisabilitiesDTO)))
            .andExpect(status().isOk());

        // Validate the AccessibilityForDisabilities in the database
        List<AccessibilityForDisabilities> accessibilityForDisabilitiesList = accessibilityForDisabilitiesRepository.findAll();
        assertThat(accessibilityForDisabilitiesList).hasSize(databaseSizeBeforeUpdate);
        AccessibilityForDisabilities testAccessibilityForDisabilities = accessibilityForDisabilitiesList.get(accessibilityForDisabilitiesList.size() - 1);
        assertThat(testAccessibilityForDisabilities.getAccessibility()).isEqualTo(UPDATED_ACCESSIBILITY);
        assertThat(testAccessibilityForDisabilities.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    @Transactional
    public void updateNonExistingAccessibilityForDisabilities() throws Exception {
        int databaseSizeBeforeUpdate = accessibilityForDisabilitiesRepository.findAll().size();

        // Create the AccessibilityForDisabilities
        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO = accessibilityForDisabilitiesMapper.toDto(accessibilityForDisabilities);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccessibilityForDisabilitiesMockMvc.perform(put("/api/accessibility-for-disabilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accessibilityForDisabilitiesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AccessibilityForDisabilities in the database
        List<AccessibilityForDisabilities> accessibilityForDisabilitiesList = accessibilityForDisabilitiesRepository.findAll();
        assertThat(accessibilityForDisabilitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAccessibilityForDisabilities() throws Exception {
        // Initialize the database
        accessibilityForDisabilitiesRepository.saveAndFlush(accessibilityForDisabilities);

        int databaseSizeBeforeDelete = accessibilityForDisabilitiesRepository.findAll().size();

        // Get the accessibilityForDisabilities
        restAccessibilityForDisabilitiesMockMvc.perform(delete("/api/accessibility-for-disabilities/{id}", accessibilityForDisabilities.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AccessibilityForDisabilities> accessibilityForDisabilitiesList = accessibilityForDisabilitiesRepository.findAll();
        assertThat(accessibilityForDisabilitiesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessibilityForDisabilities.class);
        AccessibilityForDisabilities accessibilityForDisabilities1 = new AccessibilityForDisabilities();
        accessibilityForDisabilities1.setId(1L);
        AccessibilityForDisabilities accessibilityForDisabilities2 = new AccessibilityForDisabilities();
        accessibilityForDisabilities2.setId(accessibilityForDisabilities1.getId());
        assertThat(accessibilityForDisabilities1).isEqualTo(accessibilityForDisabilities2);
        accessibilityForDisabilities2.setId(2L);
        assertThat(accessibilityForDisabilities1).isNotEqualTo(accessibilityForDisabilities2);
        accessibilityForDisabilities1.setId(null);
        assertThat(accessibilityForDisabilities1).isNotEqualTo(accessibilityForDisabilities2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccessibilityForDisabilitiesDTO.class);
        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO1 = new AccessibilityForDisabilitiesDTO();
        accessibilityForDisabilitiesDTO1.setId(1L);
        AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO2 = new AccessibilityForDisabilitiesDTO();
        assertThat(accessibilityForDisabilitiesDTO1).isNotEqualTo(accessibilityForDisabilitiesDTO2);
        accessibilityForDisabilitiesDTO2.setId(accessibilityForDisabilitiesDTO1.getId());
        assertThat(accessibilityForDisabilitiesDTO1).isEqualTo(accessibilityForDisabilitiesDTO2);
        accessibilityForDisabilitiesDTO2.setId(2L);
        assertThat(accessibilityForDisabilitiesDTO1).isNotEqualTo(accessibilityForDisabilitiesDTO2);
        accessibilityForDisabilitiesDTO1.setId(null);
        assertThat(accessibilityForDisabilitiesDTO1).isNotEqualTo(accessibilityForDisabilitiesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(accessibilityForDisabilitiesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(accessibilityForDisabilitiesMapper.fromId(null)).isNull();
    }
}
