package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;

import org.benetech.servicenet.domain.Beds;
import org.benetech.servicenet.repository.BedsRepository;
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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BedsResource REST controller.
 *
 * @see BedsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class BedsResourceIntTest {

    private static final Integer DEFAULT_AVAILABLE_BEDS = 1;
    private static final Integer UPDATED_AVAILABLE_BEDS = 2;

    private static final Integer DEFAULT_WAITLIST = 1;
    private static final Integer UPDATED_WAITLIST = 2;

    @Autowired
    private BedsRepository bedsRepository;

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

    private MockMvc restBedsMockMvc;

    private Beds beds;

    static UUID id = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BedsResource bedsResource = new BedsResource(bedsRepository);
        this.restBedsMockMvc = MockMvcBuilders.standaloneSetup(bedsResource)
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
    public static Beds createEntity(EntityManager em) {
        Beds beds = new Beds()
            .availableBeds(DEFAULT_AVAILABLE_BEDS)
            .waitlist(DEFAULT_WAITLIST);
        return beds;
    }

    @Before
    public void initTest() {
        beds = createEntity(em);
    }

    @Test
    @Transactional
    public void createBeds() throws Exception {
        int databaseSizeBeforeCreate = bedsRepository.findAll().size();

        // Create the Beds
        restBedsMockMvc.perform(post("/api/beds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beds)))
            .andExpect(status().isCreated());

        // Validate the Beds in the database
        List<Beds> bedsList = bedsRepository.findAll();
        assertThat(bedsList).hasSize(databaseSizeBeforeCreate + 1);
        Beds testBeds = bedsList.get(bedsList.size() - 1);
        assertThat(testBeds.getAvailableBeds()).isEqualTo(DEFAULT_AVAILABLE_BEDS);
        assertThat(testBeds.getWaitlist()).isEqualTo(DEFAULT_WAITLIST);
        assertThat(testBeds.getUpdatedAt()).isNotNull();
    }

    @Test
    @Transactional
    public void createBedsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bedsRepository.findAll().size();

        // Create the Beds with an existing ID
        beds.setId(id);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBedsMockMvc.perform(post("/api/beds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beds)))
            .andExpect(status().isBadRequest());

        // Validate the Beds in the database
        List<Beds> bedsList = bedsRepository.findAll();
        assertThat(bedsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBeds() throws Exception {
        // Initialize the database
        bedsRepository.saveAndFlush(beds);

        // Get all the bedsList
        restBedsMockMvc.perform(get("/api/beds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(beds.getId().toString()))
            .andExpect(jsonPath("$.[*].availableBeds").value(hasItem(DEFAULT_AVAILABLE_BEDS)))
            .andExpect(jsonPath("$.[*].waitlist").value(hasItem(DEFAULT_WAITLIST)));
    }

    @Test
    @Transactional
    public void getBeds() throws Exception {
        // Initialize the database
        bedsRepository.saveAndFlush(beds);

        // Get the beds
        restBedsMockMvc.perform(get("/api/beds/{id}", beds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(beds.getId().toString()))
            .andExpect(jsonPath("$.availableBeds").value(DEFAULT_AVAILABLE_BEDS))
            .andExpect(jsonPath("$.waitlist").value(DEFAULT_WAITLIST));
    }

    @Test
    @Transactional
    public void getNonExistingBeds() throws Exception {
        // Get the beds
        restBedsMockMvc.perform(get("/api/beds/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBeds() throws Exception {
        // Initialize the database
        bedsRepository.saveAndFlush(beds);

        int databaseSizeBeforeUpdate = bedsRepository.findAll().size();

        // Update the beds
        Beds updatedBeds = bedsRepository.findById(beds.getId()).get();
        // Disconnect from session so that the updates on updatedBeds are not directly saved in db
        em.detach(updatedBeds);
        updatedBeds
            .availableBeds(UPDATED_AVAILABLE_BEDS)
            .waitlist(UPDATED_WAITLIST);

        restBedsMockMvc.perform(put("/api/beds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBeds)))
            .andExpect(status().isOk());

        // Validate the Beds in the database
        List<Beds> bedsList = bedsRepository.findAll();
        assertThat(bedsList).hasSize(databaseSizeBeforeUpdate);
        Beds testBeds = bedsList.get(bedsList.size() - 1);
        assertThat(testBeds.getAvailableBeds()).isEqualTo(UPDATED_AVAILABLE_BEDS);
        assertThat(testBeds.getWaitlist()).isEqualTo(UPDATED_WAITLIST);
    }

    @Test
    @Transactional
    public void updateNonExistingBeds() throws Exception {
        int databaseSizeBeforeUpdate = bedsRepository.findAll().size();

        // Create the Beds

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBedsMockMvc.perform(put("/api/beds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(beds)))
            .andExpect(status().isBadRequest());

        // Validate the Beds in the database
        List<Beds> bedsList = bedsRepository.findAll();
        assertThat(bedsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBeds() throws Exception {
        // Initialize the database
        bedsRepository.saveAndFlush(beds);

        int databaseSizeBeforeDelete = bedsRepository.findAll().size();

        // Delete the beds
        restBedsMockMvc.perform(delete("/api/beds/{id}", beds.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Beds> bedsList = bedsRepository.findAll();
        assertThat(bedsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Beds.class);
        Beds beds1 = new Beds();
        beds1.setId(UUID.randomUUID());
        Beds beds2 = new Beds();
        beds2.setId(beds1.getId());
        assertThat(beds1).isEqualTo(beds2);
        beds2.setId(UUID.randomUUID());
        assertThat(beds1).isNotEqualTo(beds2);
        beds1.setId(null);
        assertThat(beds1).isNotEqualTo(beds2);
    }
}
