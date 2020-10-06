package org.benetech.servicenet.web.rest;

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

import java.util.List;
import javax.persistence.EntityManager;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.RegularSchedule;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.repository.RegularScheduleRepository;
import org.benetech.servicenet.service.RegularScheduleService;
import org.benetech.servicenet.service.dto.RegularScheduleDTO;
import org.benetech.servicenet.service.mapper.RegularScheduleMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

/**
 * Test class for the RegularScheduleResource REST controller.
 *
 * @see RegularScheduleResource
 */
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class RegularScheduleResourceIntTest {

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Autowired
    private RegularScheduleMapper regularScheduleMapper;

    @Autowired
    private RegularScheduleService regularScheduleService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRegularScheduleMockMvc;

    private RegularSchedule regularSchedule;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegularSchedule createEntity(EntityManager em) {
        return new RegularSchedule();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final RegularScheduleResource regularScheduleResource = new RegularScheduleResource(regularScheduleService);
        this.restRegularScheduleMockMvc = MockMvcBuilders.standaloneSetup(regularScheduleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        regularSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegularSchedule() throws Exception {
        int databaseSizeBeforeCreate = regularScheduleRepository.findAll().size();

        // Create the RegularSchedule
        RegularScheduleDTO regularScheduleDTO = regularScheduleMapper.toDto(regularSchedule);
        restRegularScheduleMockMvc.perform(post("/api/regular-schedules")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regularScheduleDTO)))
            .andExpect(status().isCreated());

        // Validate the RegularSchedule in the database
        List<RegularSchedule> regularScheduleList = regularScheduleRepository.findAll();
        assertThat(regularScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        RegularSchedule testRegularSchedule = regularScheduleList.get(regularScheduleList.size() - 1);
    }

    @Test
    @Transactional
    public void createRegularScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regularScheduleRepository.findAll().size();

        // Create the RegularSchedule with an existing ID
        regularSchedule.setId(TestConstants.UUID_1);
        RegularScheduleDTO regularScheduleDTO = regularScheduleMapper.toDto(regularSchedule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegularScheduleMockMvc.perform(post("/api/regular-schedules")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regularScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RegularSchedule in the database
        List<RegularSchedule> regularScheduleList = regularScheduleRepository.findAll();
        assertThat(regularScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegularSchedules() throws Exception {
        // Initialize the database
        regularScheduleRepository.saveAndFlush(regularSchedule);

        // Get all the regularScheduleList
        restRegularScheduleMockMvc.perform(get("/api/regular-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regularSchedule.getId().toString())));
    }

    @Test
    @Transactional
    public void getRegularSchedule() throws Exception {
        // Initialize the database
        regularScheduleRepository.saveAndFlush(regularSchedule);

        // Get the regularSchedule
        restRegularScheduleMockMvc.perform(get("/api/regular-schedules/{id}", regularSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regularSchedule.getId().toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRegularSchedule() throws Exception {
        // Get the regularSchedule
        restRegularScheduleMockMvc.perform(get("/api/regular-schedules/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegularSchedule() throws Exception {
        // Initialize the database
        regularScheduleRepository.saveAndFlush(regularSchedule);

        int databaseSizeBeforeUpdate = regularScheduleRepository.findAll().size();

        // Update the regularSchedule
        RegularSchedule updatedRegularSchedule = regularScheduleRepository.findById(regularSchedule.getId()).get();
        // Disconnect from session so that the updates on updatedRegularSchedule are not directly saved in db
        em.detach(updatedRegularSchedule);
        RegularScheduleDTO regularScheduleDTO = regularScheduleMapper.toDto(updatedRegularSchedule);

        restRegularScheduleMockMvc.perform(put("/api/regular-schedules")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regularScheduleDTO)))
            .andExpect(status().isOk());

        // Validate the RegularSchedule in the database
        List<RegularSchedule> regularScheduleList = regularScheduleRepository.findAll();
        assertThat(regularScheduleList).hasSize(databaseSizeBeforeUpdate);
        RegularSchedule testRegularSchedule = regularScheduleList.get(regularScheduleList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingRegularSchedule() throws Exception {
        int databaseSizeBeforeUpdate = regularScheduleRepository.findAll().size();

        // Create the RegularSchedule
        RegularScheduleDTO regularScheduleDTO = regularScheduleMapper.toDto(regularSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegularScheduleMockMvc.perform(put("/api/regular-schedules")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regularScheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RegularSchedule in the database
        List<RegularSchedule> regularScheduleList = regularScheduleRepository.findAll();
        assertThat(regularScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegularSchedule() throws Exception {
        // Initialize the database
        regularScheduleRepository.saveAndFlush(regularSchedule);

        int databaseSizeBeforeDelete = regularScheduleRepository.findAll().size();

        // Get the regularSchedule
        restRegularScheduleMockMvc.perform(delete("/api/regular-schedules/{id}", regularSchedule.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RegularSchedule> regularScheduleList = regularScheduleRepository.findAll();
        assertThat(regularScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegularSchedule.class);
        RegularSchedule regularSchedule1 = new RegularSchedule();
        regularSchedule1.setId(TestConstants.UUID_1);
        RegularSchedule regularSchedule2 = new RegularSchedule();
        regularSchedule2.setId(regularSchedule1.getId());
        assertThat(regularSchedule1).isEqualTo(regularSchedule2);
        regularSchedule2.setId(TestConstants.UUID_2);
        assertThat(regularSchedule1).isNotEqualTo(regularSchedule2);
        regularSchedule1.setId(null);
        assertThat(regularSchedule1).isNotEqualTo(regularSchedule2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegularScheduleDTO.class);
        RegularScheduleDTO regularScheduleDTO1 = new RegularScheduleDTO();
        regularScheduleDTO1.setId(TestConstants.UUID_1);
        RegularScheduleDTO regularScheduleDTO2 = new RegularScheduleDTO();
        assertThat(regularScheduleDTO1).isNotEqualTo(regularScheduleDTO2);
        regularScheduleDTO2.setId(regularScheduleDTO1.getId());
        assertThat(regularScheduleDTO1).isEqualTo(regularScheduleDTO2);
        regularScheduleDTO2.setId(TestConstants.UUID_2);
        assertThat(regularScheduleDTO1).isNotEqualTo(regularScheduleDTO2);
        regularScheduleDTO1.setId(null);
        assertThat(regularScheduleDTO1).isNotEqualTo(regularScheduleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(regularScheduleMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(regularScheduleMapper.fromId(null)).isNull();
    }
}
