package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit5Extension;
import org.benetech.servicenet.domain.Referral;
import org.benetech.servicenet.mother.ReferralMother;
import org.benetech.servicenet.repository.ReferralRepository;
import org.benetech.servicenet.service.dto.ReferralDTO;
import org.benetech.servicenet.service.mapper.ReferralMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReferralResource} REST controller.
 */
@ExtendWith({ SpringExtension.class, ZeroCodeSpringJUnit5Extension.class })
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
@WithMockUser(username = "admin", roles = {"ADMIN"})
@AutoConfigureMockMvc
public class ReferralResourceIT {

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private ReferralMapper referralMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferralMockMvc;

    private Referral referral;

    private Referral referralWithAllRelations;

    UUID id = UUID.randomUUID();

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referral createEntity(EntityManager em) {
        return ReferralMother.createDefault();
    }

    public static Referral createEntityWithAllRelations(EntityManager em) {
        return ReferralMother.createDefaultWithAllRelationsAndPersist(em);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referral createUpdatedEntity(EntityManager em) {
        return ReferralMother.createDifferent();
    }

    @BeforeEach
    public void initTest() {
        referral = createEntity(em);
        referralWithAllRelations = createEntityWithAllRelations(em);
    }

    @Test
    @Transactional
    public void createReferral() throws Exception {
        int databaseSizeBeforeCreate = referralRepository.findAll().size();

        // Create the Referral
        ReferralDTO referralDTO = referralMapper.toDto(referral);
        restReferralMockMvc.perform(post("/api/referrals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(referralDTO)))
            .andExpect(status().isCreated());

        // Validate the Referral in the database
        List<Referral> referralList = referralRepository.findAll();
        assertThat(referralList).hasSize(databaseSizeBeforeCreate + 1);
        Referral testReferral = referralList.get(referralList.size() - 1);
        assertThat(testReferral.getShortcode()).isEqualTo(ReferralMother.DEFAULT_SHORTCODE);
        assertThat(testReferral.getSentAt()).isEqualTo(ReferralMother.DEFAULT_SENT_AT);
        assertThat(testReferral.getFulfilledAt()).isEqualTo(ReferralMother.DEFAULT_FULFILLED_AT);
    }

    @Test
    @Transactional
    public void createReferralWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = referralRepository.findAll().size();

        // Create the Referral with an existing ID
        referral.setId(id);
        ReferralDTO referralDTO = referralMapper.toDto(referral);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferralMockMvc.perform(post("/api/referrals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(referralDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Referral in the database
        List<Referral> referralList = referralRepository.findAll();
        assertThat(referralList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReferrals() throws Exception {
        // Initialize the database
        referralRepository.saveAndFlush(referral);

        // Get all the referralList
        restReferralMockMvc.perform(get("/api/referrals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referral.getId().toString())))
            .andExpect(jsonPath("$.[*].shortcode").value(hasItem(ReferralMother.DEFAULT_SHORTCODE)))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(sameInstant(ReferralMother.DEFAULT_SENT_AT))))
            .andExpect(jsonPath("$.[*].fulfilledAt").value(hasItem(sameInstant(ReferralMother.DEFAULT_FULFILLED_AT))));
    }

    @Test
    @Transactional
    public void getReferral() throws Exception {
        // Initialize the database
        referralRepository.saveAndFlush(referral);

        // Get the referral
        restReferralMockMvc.perform(get("/api/referrals/{id}", referral.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referral.getId().toString()))
            .andExpect(jsonPath("$.shortcode").value(ReferralMother.DEFAULT_SHORTCODE))
            .andExpect(jsonPath("$.sentAt").value(sameInstant(ReferralMother.DEFAULT_SENT_AT)))
            .andExpect(jsonPath("$.fulfilledAt").value(sameInstant(ReferralMother.DEFAULT_FULFILLED_AT)));
    }

    @Test
    @Transactional
    public void getNonExistingReferral() throws Exception {
        // Get the referral
        restReferralMockMvc.perform(get("/api/referrals/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReferral() throws Exception {
        // Initialize the database
        referralRepository.saveAndFlush(referral);

        int databaseSizeBeforeUpdate = referralRepository.findAll().size();

        // Update the referral
        Referral updatedReferral = referralRepository.findById(referral.getId()).get();
        // Disconnect from session so that the updates on updatedReferral are not directly saved in db
        em.detach(updatedReferral);
        updatedReferral
            .shortcode(ReferralMother.UPDATED_SHORTCODE)
            .sentAt(ReferralMother.UPDATED_SENT_AT)
            .fulfilledAt(ReferralMother.UPDATED_FULFILLED_AT);
        ReferralDTO referralDTO = referralMapper.toDto(updatedReferral);

        restReferralMockMvc.perform(put("/api/referrals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(referralDTO)))
            .andExpect(status().isOk());

        // Validate the Referral in the database
        List<Referral> referralList = referralRepository.findAll();
        assertThat(referralList).hasSize(databaseSizeBeforeUpdate);
        Referral testReferral = referralList.get(referralList.size() - 1);
        assertThat(testReferral.getShortcode()).isEqualTo(ReferralMother.UPDATED_SHORTCODE);
        assertThat(testReferral.getSentAt()).isEqualTo(ReferralMother.UPDATED_SENT_AT);
        assertThat(testReferral.getFulfilledAt()).isEqualTo(ReferralMother.UPDATED_FULFILLED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingReferral() throws Exception {
        int databaseSizeBeforeUpdate = referralRepository.findAll().size();

        // Create the Referral
        ReferralDTO referralDTO = referralMapper.toDto(referral);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferralMockMvc.perform(put("/api/referrals").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(referralDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Referral in the database
        List<Referral> referralList = referralRepository.findAll();
        assertThat(referralList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReferral() throws Exception {
        // Initialize the database
        referralRepository.saveAndFlush(referral);

        int databaseSizeBeforeDelete = referralRepository.findAll().size();

        // Delete the referral
        restReferralMockMvc.perform(delete("/api/referrals/{id}", referral.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Referral> referralList = referralRepository.findAll();
        assertThat(referralList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void deleteReferralWithAllRelations() throws Exception {
        int databaseSizeBeforeDelete = referralRepository.findAll().size();

        // Delete the referral
        restReferralMockMvc.perform(delete("/api/referrals/{id}", referralWithAllRelations.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Referral> referralList = referralRepository.findAll();
        assertThat(referralList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
