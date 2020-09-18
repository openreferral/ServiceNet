package org.benetech.servicenet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.benetech.servicenet.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit4Runner;
import org.benetech.servicenet.config.SecurityBeanOverrideConfiguration;
import org.benetech.servicenet.domain.DailyUpdate;
import org.benetech.servicenet.repository.DailyUpdateRepository;
import org.benetech.servicenet.service.dto.DailyUpdateDTO;
import org.benetech.servicenet.service.mapper.DailyUpdateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DailyUpdateResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ServiceNetApp.class })

@AutoConfigureMockMvc
@WithMockUser
@RunWith(ZeroCodeSpringJUnit4Runner.class)
public class DailyUpdateResourceIT {

    private static final String DEFAULT_UPDATE = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime
        .ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final UUID ID = UUID.randomUUID();

    @Autowired
    private DailyUpdateRepository dailyUpdateRepository;

    @Autowired
    private DailyUpdateMapper dailyUpdateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDailyUpdateMockMvc;

    private DailyUpdate dailyUpdate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyUpdate createEntity(EntityManager em) {
        DailyUpdate dailyUpdate = new DailyUpdate()
            .update(DEFAULT_UPDATE)
            .expiry(DEFAULT_EXPIRY)
            .createdAt(DEFAULT_CREATED_AT);
        return dailyUpdate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyUpdate createUpdatedEntity(EntityManager em) {
        DailyUpdate dailyUpdate = new DailyUpdate()
            .update(UPDATED_UPDATE)
            .expiry(UPDATED_EXPIRY)
            .createdAt(UPDATED_CREATED_AT);
        return dailyUpdate;
    }

    @BeforeEach
    public void initTest() {
        dailyUpdate = createEntity(em);
    }

    @Test
    @Transactional
    public void createDailyUpdate() throws Exception {
        int databaseSizeBeforeCreate = dailyUpdateRepository.findAll().size();

        // Create the DailyUpdate
        DailyUpdateDTO dailyUpdateDTO = dailyUpdateMapper.toDto(dailyUpdate);
        restDailyUpdateMockMvc.perform(post("/api/daily-updates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dailyUpdateDTO)))
            .andExpect(status().isCreated());

        // Validate the DailyUpdate in the database
        List<DailyUpdate> dailyUpdateList = dailyUpdateRepository.findAll();
        assertThat(dailyUpdateList).hasSize(databaseSizeBeforeCreate + 1);
        DailyUpdate testDailyUpdate = dailyUpdateList.get(dailyUpdateList.size() - 1);
        assertThat(testDailyUpdate.getUpdate()).isEqualTo(DEFAULT_UPDATE);
        assertThat(testDailyUpdate.getExpiry()).isEqualTo(DEFAULT_EXPIRY);
        assertThat(testDailyUpdate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createDailyUpdateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dailyUpdateRepository.findAll().size();

        // Create the DailyUpdate with an existing ID
        dailyUpdate.setId(ID);
        DailyUpdateDTO dailyUpdateDTO = dailyUpdateMapper.toDto(dailyUpdate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyUpdateMockMvc.perform(post("/api/daily-updates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dailyUpdateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DailyUpdate in the database
        List<DailyUpdate> dailyUpdateList = dailyUpdateRepository.findAll();
        assertThat(dailyUpdateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDailyUpdates() throws Exception {
        // Initialize the database
        dailyUpdateRepository.saveAndFlush(dailyUpdate);

        // Get all the dailyUpdateList
        restDailyUpdateMockMvc.perform(get("/api/daily-updates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyUpdate.getId().toString())))
            .andExpect(jsonPath("$.[*].update").value(hasItem(DEFAULT_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].expiry").value(hasItem(sameInstant(DEFAULT_EXPIRY))))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    public void getDailyUpdate() throws Exception {
        // Initialize the database
        dailyUpdateRepository.saveAndFlush(dailyUpdate);

        // Get the dailyUpdate
        restDailyUpdateMockMvc.perform(get("/api/daily-updates/{id}", dailyUpdate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dailyUpdate.getId().toString()))
            .andExpect(jsonPath("$.update").value(DEFAULT_UPDATE.toString()))
            .andExpect(jsonPath("$.expiry").value(sameInstant(DEFAULT_EXPIRY)))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingDailyUpdate() throws Exception {
        // Get the dailyUpdate
        restDailyUpdateMockMvc.perform(get("/api/daily-updates/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDailyUpdate() throws Exception {
        // Initialize the database
        dailyUpdateRepository.saveAndFlush(dailyUpdate);

        int databaseSizeBeforeUpdate = dailyUpdateRepository.findAll().size();

        // Update the dailyUpdate
        DailyUpdate updatedDailyUpdate = dailyUpdateRepository.findById(dailyUpdate.getId()).get();
        // Disconnect from session so that the updates on updatedDailyUpdate are not directly saved in db
        em.detach(updatedDailyUpdate);
        updatedDailyUpdate
            .update(UPDATED_UPDATE)
            .expiry(UPDATED_EXPIRY)
            .createdAt(UPDATED_CREATED_AT);
        DailyUpdateDTO dailyUpdateDTO = dailyUpdateMapper.toDto(updatedDailyUpdate);

        restDailyUpdateMockMvc.perform(put("/api/daily-updates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dailyUpdateDTO)))
            .andExpect(status().isOk());

        // Validate the DailyUpdate in the database
        List<DailyUpdate> dailyUpdateList = dailyUpdateRepository.findAll();
        assertThat(dailyUpdateList).hasSize(databaseSizeBeforeUpdate);
        DailyUpdate testDailyUpdate = dailyUpdateList.get(dailyUpdateList.size() - 1);
        assertThat(testDailyUpdate.getUpdate()).isEqualTo(UPDATED_UPDATE);
        assertThat(testDailyUpdate.getExpiry()).isEqualTo(UPDATED_EXPIRY);
        assertThat(testDailyUpdate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingDailyUpdate() throws Exception {
        int databaseSizeBeforeUpdate = dailyUpdateRepository.findAll().size();

        // Create the DailyUpdate
        DailyUpdateDTO dailyUpdateDTO = dailyUpdateMapper.toDto(dailyUpdate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyUpdateMockMvc.perform(put("/api/daily-updates").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dailyUpdateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DailyUpdate in the database
        List<DailyUpdate> dailyUpdateList = dailyUpdateRepository.findAll();
        assertThat(dailyUpdateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDailyUpdate() throws Exception {
        // Initialize the database
        dailyUpdateRepository.saveAndFlush(dailyUpdate);

        int databaseSizeBeforeDelete = dailyUpdateRepository.findAll().size();

        // Delete the dailyUpdate
        restDailyUpdateMockMvc.perform(delete("/api/daily-updates/{id}", dailyUpdate.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DailyUpdate> dailyUpdateList = dailyUpdateRepository.findAll();
        assertThat(dailyUpdateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
