package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;

import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.repository.OpeningHoursRepository;
import org.benetech.servicenet.service.OpeningHoursService;
import org.benetech.servicenet.service.dto.OpeningHoursDTO;
import org.benetech.servicenet.service.mapper.OpeningHoursMapper;
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
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the OpeningHoursResource REST controller.
 *
 * @see OpeningHoursResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class OpeningHoursResourceIntTest {

    private static final Integer DEFAULT_WEEKDAY = 1;
    private static final Integer UPDATED_WEEKDAY = 2;

    private static final String DEFAULT_OPENS_AT = "AAAAAAAAAA";
    private static final String UPDATED_OPENS_AT = "BBBBBBBBBB";

    private static final String DEFAULT_CLOSES_AT = "AAAAAAAAAA";
    private static final String UPDATED_CLOSES_AT = "BBBBBBBBBB";

    @Autowired
    private OpeningHoursRepository openingHoursRepository;

    @Autowired
    private OpeningHoursMapper openingHoursMapper;

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOpeningHoursMockMvc;

    private OpeningHours openingHours;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OpeningHoursResource openingHoursResource = new OpeningHoursResource(openingHoursService);
        this.restOpeningHoursMockMvc = MockMvcBuilders.standaloneSetup(openingHoursResource)
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
    public static OpeningHours createEntity(EntityManager em) {
        OpeningHours openingHours = new OpeningHours()
            .weekday(DEFAULT_WEEKDAY)
            .opensAt(DEFAULT_OPENS_AT)
            .closesAt(DEFAULT_CLOSES_AT);
        return openingHours;
    }

    @Before
    public void initTest() {
        openingHours = createEntity(em);
    }

    @Test
    @Transactional
    public void createOpeningHours() throws Exception {
        int databaseSizeBeforeCreate = openingHoursRepository.findAll().size();

        // Create the OpeningHours
        OpeningHoursDTO openingHoursDTO = openingHoursMapper.toDto(openingHours);
        restOpeningHoursMockMvc.perform(post("/api/opening-hours")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(openingHoursDTO)))
            .andExpect(status().isCreated());

        // Validate the OpeningHours in the database
        List<OpeningHours> openingHoursList = openingHoursRepository.findAll();
        assertThat(openingHoursList).hasSize(databaseSizeBeforeCreate + 1);
        OpeningHours testOpeningHours = openingHoursList.get(openingHoursList.size() - 1);
        assertThat(testOpeningHours.getWeekday()).isEqualTo(DEFAULT_WEEKDAY);
        assertThat(testOpeningHours.getOpensAt()).isEqualTo(DEFAULT_OPENS_AT);
        assertThat(testOpeningHours.getClosesAt()).isEqualTo(DEFAULT_CLOSES_AT);
    }

    @Test
    @Transactional
    public void createOpeningHoursWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = openingHoursRepository.findAll().size();

        // Create the OpeningHours with an existing ID
        openingHours.setId(TestConstants.UUID_1);
        OpeningHoursDTO openingHoursDTO = openingHoursMapper.toDto(openingHours);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpeningHoursMockMvc.perform(post("/api/opening-hours")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(openingHoursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OpeningHours in the database
        List<OpeningHours> openingHoursList = openingHoursRepository.findAll();
        assertThat(openingHoursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWeekdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingHoursRepository.findAll().size();
        // set the field null
        openingHours.setWeekday(null);

        // Create the OpeningHours, which fails.
        OpeningHoursDTO openingHoursDTO = openingHoursMapper.toDto(openingHours);

        restOpeningHoursMockMvc.perform(post("/api/opening-hours")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(openingHoursDTO)))
            .andExpect(status().isBadRequest());

        List<OpeningHours> openingHoursList = openingHoursRepository.findAll();
        assertThat(openingHoursList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpeningHours() throws Exception {
        // Initialize the database
        openingHoursRepository.saveAndFlush(openingHours);

        // Get all the openingHoursList
        restOpeningHoursMockMvc.perform(get("/api/opening-hours?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(openingHours.getId().toString())))
            .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY)))
            .andExpect(jsonPath("$.[*].opensAt").value(hasItem(DEFAULT_OPENS_AT)))
            .andExpect(jsonPath("$.[*].closesAt").value(hasItem(DEFAULT_CLOSES_AT)));
    }

    @Test
    @Transactional
    public void getOpeningHours() throws Exception {
        // Initialize the database
        openingHoursRepository.saveAndFlush(openingHours);

        // Get the openingHours
        restOpeningHoursMockMvc.perform(get("/api/opening-hours/{id}", openingHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(openingHours.getId().toString()))
            .andExpect(jsonPath("$.weekday").value(DEFAULT_WEEKDAY))
            .andExpect(jsonPath("$.opensAt").value(DEFAULT_OPENS_AT))
            .andExpect(jsonPath("$.closesAt").value(DEFAULT_CLOSES_AT));
    }

    @Test
    @Transactional
    public void getNonExistingOpeningHours() throws Exception {
        // Get the openingHours
        restOpeningHoursMockMvc.perform(get("/api/opening-hours/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpeningHours() throws Exception {
        // Initialize the database
        openingHoursRepository.saveAndFlush(openingHours);

        int databaseSizeBeforeUpdate = openingHoursRepository.findAll().size();

        // Update the openingHours
        OpeningHours updatedOpeningHours = openingHoursRepository.findById(openingHours.getId()).get();
        // Disconnect from session so that the updates on updatedOpeningHours are not directly saved in db
        em.detach(updatedOpeningHours);
        updatedOpeningHours
            .weekday(UPDATED_WEEKDAY)
            .opensAt(UPDATED_OPENS_AT)
            .closesAt(UPDATED_CLOSES_AT);
        OpeningHoursDTO openingHoursDTO = openingHoursMapper.toDto(updatedOpeningHours);

        restOpeningHoursMockMvc.perform(put("/api/opening-hours")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(openingHoursDTO)))
            .andExpect(status().isOk());

        // Validate the OpeningHours in the database
        List<OpeningHours> openingHoursList = openingHoursRepository.findAll();
        assertThat(openingHoursList).hasSize(databaseSizeBeforeUpdate);
        OpeningHours testOpeningHours = openingHoursList.get(openingHoursList.size() - 1);
        assertThat(testOpeningHours.getWeekday()).isEqualTo(UPDATED_WEEKDAY);
        assertThat(testOpeningHours.getOpensAt()).isEqualTo(UPDATED_OPENS_AT);
        assertThat(testOpeningHours.getClosesAt()).isEqualTo(UPDATED_CLOSES_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingOpeningHours() throws Exception {
        int databaseSizeBeforeUpdate = openingHoursRepository.findAll().size();

        // Create the OpeningHours
        OpeningHoursDTO openingHoursDTO = openingHoursMapper.toDto(openingHours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpeningHoursMockMvc.perform(put("/api/opening-hours")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(openingHoursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OpeningHours in the database
        List<OpeningHours> openingHoursList = openingHoursRepository.findAll();
        assertThat(openingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOpeningHours() throws Exception {
        // Initialize the database
        openingHoursRepository.saveAndFlush(openingHours);

        int databaseSizeBeforeDelete = openingHoursRepository.findAll().size();

        // Get the openingHours
        restOpeningHoursMockMvc.perform(delete("/api/opening-hours/{id}", openingHours.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OpeningHours> openingHoursList = openingHoursRepository.findAll();
        assertThat(openingHoursList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpeningHours.class);
        OpeningHours openingHours1 = new OpeningHours();
        openingHours1.setId(TestConstants.UUID_1);
        OpeningHours openingHours2 = new OpeningHours();
        openingHours2.setId(openingHours1.getId());
        assertThat(openingHours1).isEqualTo(openingHours2);
        openingHours2.setId(TestConstants.UUID_2);
        assertThat(openingHours1).isNotEqualTo(openingHours2);
        openingHours1.setId(null);
        assertThat(openingHours1).isNotEqualTo(openingHours2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpeningHoursDTO.class);
        OpeningHoursDTO openingHoursDTO1 = new OpeningHoursDTO();
        openingHoursDTO1.setId(TestConstants.UUID_1);
        OpeningHoursDTO openingHoursDTO2 = new OpeningHoursDTO();
        assertThat(openingHoursDTO1).isNotEqualTo(openingHoursDTO2);
        openingHoursDTO2.setId(openingHoursDTO1.getId());
        assertThat(openingHoursDTO1).isEqualTo(openingHoursDTO2);
        openingHoursDTO2.setId(TestConstants.UUID_2);
        assertThat(openingHoursDTO1).isNotEqualTo(openingHoursDTO2);
        openingHoursDTO1.setId(null);
        assertThat(openingHoursDTO1).isNotEqualTo(openingHoursDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(openingHoursMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(openingHoursMapper.fromId(null)).isNull();
    }
}
