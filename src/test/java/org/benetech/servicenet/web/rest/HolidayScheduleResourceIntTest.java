package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.HolidaySchedule;
import org.benetech.servicenet.repository.HolidayScheduleRepository;
import org.benetech.servicenet.service.HolidayScheduleService;
import org.benetech.servicenet.service.dto.HolidayScheduleDTO;
import org.benetech.servicenet.service.mapper.HolidayScheduleMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Test class for the HolidayScheduleResource REST controller.
 *
 * @see HolidayScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class HolidayScheduleResourceIntTest {

    private static final Boolean DEFAULT_CLOSED = false;
    private static final Boolean UPDATED_CLOSED = true;

    private static final String DEFAULT_OPENS_AT = "AAAAAAAAAA";
    private static final String UPDATED_OPENS_AT = "BBBBBBBBBB";

    private static final String DEFAULT_CLOSES_AT = "AAAAAAAAAA";
    private static final String UPDATED_CLOSES_AT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private HolidayScheduleRepository holidayScheduleRepository;

    @Autowired
    private HolidayScheduleMapper holidayScheduleMapper;

    @Autowired
    private HolidayScheduleService holidayScheduleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHolidayScheduleMockMvc;

    private HolidaySchedule holidaySchedule;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HolidaySchedule createEntity(EntityManager em) {
        HolidaySchedule holidaySchedule = new HolidaySchedule()
            .closed(DEFAULT_CLOSED)
            .opensAt(DEFAULT_OPENS_AT)
            .closesAt(DEFAULT_CLOSES_AT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return holidaySchedule;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HolidayScheduleResource holidayScheduleResource = new HolidayScheduleResource(holidayScheduleService);
        this.restHolidayScheduleMockMvc = MockMvcBuilders.standaloneSetup(holidayScheduleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        holidaySchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createHolidaySchedule() throws Exception {
        int databaseSizeBeforeCreate = holidayScheduleRepository.findAll().size();

        // Create the HolidaySchedule
        HolidayScheduleDTO holidayScheduleDTO = holidayScheduleMapper.toDto(holidaySchedule);
        restHolidayScheduleMockMvc.perform(post("/api/holiday-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayScheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the HolidaySchedule in the database
        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        HolidaySchedule testHolidaySchedule = holidayScheduleList.get(holidayScheduleList.size() - 1);
        assertThat(testHolidaySchedule.isClosed()).isEqualTo(DEFAULT_CLOSED);
        assertThat(testHolidaySchedule.getOpensAt()).isEqualTo(DEFAULT_OPENS_AT);
        assertThat(testHolidaySchedule.getClosesAt()).isEqualTo(DEFAULT_CLOSES_AT);
        assertThat(testHolidaySchedule.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testHolidaySchedule.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createHolidayScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = holidayScheduleRepository.findAll().size();

        // Create the HolidaySchedule with an existing ID
        holidaySchedule.setId(1L);
        HolidayScheduleDTO holidayScheduleDTO = holidayScheduleMapper.toDto(holidaySchedule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidayScheduleMockMvc.perform(post("/api/holiday-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HolidaySchedule in the database
        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClosedIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayScheduleRepository.findAll().size();
        // set the field null
        holidaySchedule.setClosed(null);

        // Create the HolidaySchedule, which fails.
        HolidayScheduleDTO holidayScheduleDTO = holidayScheduleMapper.toDto(holidaySchedule);

        restHolidayScheduleMockMvc.perform(post("/api/holiday-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayScheduleDTO)))
            .andExpect(status().isBadRequest());

        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayScheduleRepository.findAll().size();
        // set the field null
        holidaySchedule.setStartDate(null);

        // Create the HolidaySchedule, which fails.
        HolidayScheduleDTO holidayScheduleDTO = holidayScheduleMapper.toDto(holidaySchedule);

        restHolidayScheduleMockMvc.perform(post("/api/holiday-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayScheduleDTO)))
            .andExpect(status().isBadRequest());

        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayScheduleRepository.findAll().size();
        // set the field null
        holidaySchedule.setEndDate(null);

        // Create the HolidaySchedule, which fails.
        HolidayScheduleDTO holidayScheduleDTO = holidayScheduleMapper.toDto(holidaySchedule);

        restHolidayScheduleMockMvc.perform(post("/api/holiday-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayScheduleDTO)))
            .andExpect(status().isBadRequest());

        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHolidaySchedules() throws Exception {
        // Initialize the database
        holidayScheduleRepository.saveAndFlush(holidaySchedule);

        // Get all the holidayScheduleList
        restHolidayScheduleMockMvc.perform(get("/api/holiday-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holidaySchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.booleanValue())))
            .andExpect(jsonPath("$.[*].opensAt").value(hasItem(DEFAULT_OPENS_AT.toString())))
            .andExpect(jsonPath("$.[*].closesAt").value(hasItem(DEFAULT_CLOSES_AT.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getHolidaySchedule() throws Exception {
        // Initialize the database
        holidayScheduleRepository.saveAndFlush(holidaySchedule);

        // Get the holidaySchedule
        restHolidayScheduleMockMvc.perform(get("/api/holiday-schedules/{id}", holidaySchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(holidaySchedule.getId().intValue()))
            .andExpect(jsonPath("$.closed").value(DEFAULT_CLOSED.booleanValue()))
            .andExpect(jsonPath("$.opensAt").value(DEFAULT_OPENS_AT.toString()))
            .andExpect(jsonPath("$.closesAt").value(DEFAULT_CLOSES_AT.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHolidaySchedule() throws Exception {
        // Get the holidaySchedule
        restHolidayScheduleMockMvc.perform(get("/api/holiday-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHolidaySchedule() throws Exception {
        // Initialize the database
        holidayScheduleRepository.saveAndFlush(holidaySchedule);

        int databaseSizeBeforeUpdate = holidayScheduleRepository.findAll().size();

        // Update the holidaySchedule
        HolidaySchedule updatedHolidaySchedule = holidayScheduleRepository.findById(holidaySchedule.getId()).get();
        // Disconnect from session so that the updates on updatedHolidaySchedule are not directly saved in db
        em.detach(updatedHolidaySchedule);
        updatedHolidaySchedule
            .closed(UPDATED_CLOSED)
            .opensAt(UPDATED_OPENS_AT)
            .closesAt(UPDATED_CLOSES_AT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        HolidayScheduleDTO holidayScheduleDTO = holidayScheduleMapper.toDto(updatedHolidaySchedule);

        restHolidayScheduleMockMvc.perform(put("/api/holiday-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayScheduleDTO)))
            .andExpect(status().isOk());

        // Validate the HolidaySchedule in the database
        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeUpdate);
        HolidaySchedule testHolidaySchedule = holidayScheduleList.get(holidayScheduleList.size() - 1);
        assertThat(testHolidaySchedule.isClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testHolidaySchedule.getOpensAt()).isEqualTo(UPDATED_OPENS_AT);
        assertThat(testHolidaySchedule.getClosesAt()).isEqualTo(UPDATED_CLOSES_AT);
        assertThat(testHolidaySchedule.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testHolidaySchedule.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingHolidaySchedule() throws Exception {
        int databaseSizeBeforeUpdate = holidayScheduleRepository.findAll().size();

        // Create the HolidaySchedule
        HolidayScheduleDTO holidayScheduleDTO = holidayScheduleMapper.toDto(holidaySchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayScheduleMockMvc.perform(put("/api/holiday-schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(holidayScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HolidaySchedule in the database
        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHolidaySchedule() throws Exception {
        // Initialize the database
        holidayScheduleRepository.saveAndFlush(holidaySchedule);

        int databaseSizeBeforeDelete = holidayScheduleRepository.findAll().size();

        // Get the holidaySchedule
        restHolidayScheduleMockMvc.perform(delete("/api/holiday-schedules/{id}", holidaySchedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HolidaySchedule> holidayScheduleList = holidayScheduleRepository.findAll();
        assertThat(holidayScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HolidaySchedule.class);
        HolidaySchedule holidaySchedule1 = new HolidaySchedule();
        holidaySchedule1.setId(1L);
        HolidaySchedule holidaySchedule2 = new HolidaySchedule();
        holidaySchedule2.setId(holidaySchedule1.getId());
        assertThat(holidaySchedule1).isEqualTo(holidaySchedule2);
        holidaySchedule2.setId(2L);
        assertThat(holidaySchedule1).isNotEqualTo(holidaySchedule2);
        holidaySchedule1.setId(null);
        assertThat(holidaySchedule1).isNotEqualTo(holidaySchedule2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HolidayScheduleDTO.class);
        HolidayScheduleDTO holidayScheduleDTO1 = new HolidayScheduleDTO();
        holidayScheduleDTO1.setId(1L);
        HolidayScheduleDTO holidayScheduleDTO2 = new HolidayScheduleDTO();
        assertThat(holidayScheduleDTO1).isNotEqualTo(holidayScheduleDTO2);
        holidayScheduleDTO2.setId(holidayScheduleDTO1.getId());
        assertThat(holidayScheduleDTO1).isEqualTo(holidayScheduleDTO2);
        holidayScheduleDTO2.setId(2L);
        assertThat(holidayScheduleDTO1).isNotEqualTo(holidayScheduleDTO2);
        holidayScheduleDTO1.setId(null);
        assertThat(holidayScheduleDTO1).isNotEqualTo(holidayScheduleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(holidayScheduleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(holidayScheduleMapper.fromId(null)).isNull();
    }
}
