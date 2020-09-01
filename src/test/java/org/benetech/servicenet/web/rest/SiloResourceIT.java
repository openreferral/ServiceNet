package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.config.SecurityBeanOverrideConfiguration;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.repository.SiloRepository;
import org.benetech.servicenet.service.SiloService;
import org.benetech.servicenet.service.dto.SiloDTO;
import org.benetech.servicenet.service.mapper.SiloMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link SiloResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, ServiceNetApp.class})
public class SiloResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SiloRepository siloRepository;

    @Autowired
    private SiloMapper siloMapper;

    @Autowired
    private SiloService siloService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSiloMockMvc;

    private Silo silo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final SiloResource siloResource = new SiloResource(siloService);
        this.restSiloMockMvc = MockMvcBuilders.standaloneSetup(siloResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
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
    public static Silo createEntity(EntityManager em) {
        Silo silo = new Silo()
            .name(DEFAULT_NAME);
        return silo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Silo createUpdatedEntity(EntityManager em) {
        Silo silo = new Silo()
            .name(UPDATED_NAME);
        return silo;
    }

    @BeforeEach
    public void initTest() {
        silo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSilo() throws Exception {
        int databaseSizeBeforeCreate = siloRepository.findAll().size();

        // Create the Silo
        SiloDTO siloDTO = siloMapper.toDto(silo);
        restSiloMockMvc.perform(post("/api/silos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(siloDTO)))
            .andExpect(status().isCreated());

        // Validate the Silo in the database
        List<Silo> siloList = siloRepository.findAll();
        assertThat(siloList).hasSize(databaseSizeBeforeCreate + 1);
        Silo testSilo = siloList.get(siloList.size() - 1);
        assertThat(testSilo.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSiloWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = siloRepository.findAll().size();

        // Create the Silo with an existing ID
        silo.setId(UUID.randomUUID());
        SiloDTO siloDTO = siloMapper.toDto(silo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiloMockMvc.perform(post("/api/silos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(siloDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Silo in the database
        List<Silo> siloList = siloRepository.findAll();
        assertThat(siloList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSilos() throws Exception {
        // Initialize the database
        siloRepository.saveAndFlush(silo);

        // Get all the siloList
        restSiloMockMvc.perform(get("/api/silos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(silo.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getSilo() throws Exception {
        // Initialize the database
        siloRepository.saveAndFlush(silo);

        // Get the silo
        restSiloMockMvc.perform(get("/api/silos/{id}", silo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(silo.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSilo() throws Exception {
        // Get the silo
        restSiloMockMvc.perform(get("/api/silos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSilo() throws Exception {
        // Initialize the database
        siloRepository.saveAndFlush(silo);

        int databaseSizeBeforeUpdate = siloRepository.findAll().size();

        // Update the silo
        Silo updatedSilo = siloRepository.findById(silo.getId()).get();
        // Disconnect from session so that the updates on updatedSilo are not directly saved in db
        em.detach(updatedSilo);
        updatedSilo
            .name(UPDATED_NAME);
        SiloDTO siloDTO = siloMapper.toDto(updatedSilo);

        restSiloMockMvc.perform(put("/api/silos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(siloDTO)))
            .andExpect(status().isOk());

        // Validate the Silo in the database
        List<Silo> siloList = siloRepository.findAll();
        assertThat(siloList).hasSize(databaseSizeBeforeUpdate);
        Silo testSilo = siloList.get(siloList.size() - 1);
        assertThat(testSilo.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSilo() throws Exception {
        int databaseSizeBeforeUpdate = siloRepository.findAll().size();

        // Create the Silo
        SiloDTO siloDTO = siloMapper.toDto(silo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiloMockMvc.perform(put("/api/silos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(siloDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Silo in the database
        List<Silo> siloList = siloRepository.findAll();
        assertThat(siloList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSilo() throws Exception {
        // Initialize the database
        siloRepository.saveAndFlush(silo);

        int databaseSizeBeforeDelete = siloRepository.findAll().size();

        // Delete the silo
        restSiloMockMvc.perform(delete("/api/silos/{id}", silo.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Silo> siloList = siloRepository.findAll();
        assertThat(siloList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
