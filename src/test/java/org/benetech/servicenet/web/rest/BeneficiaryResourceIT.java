package org.benetech.servicenet.web.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.MockedSmsConfiguration;
import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.ZeroCodeSpringJUnit5Extension;
import org.benetech.servicenet.domain.Beneficiary;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Referral;
import org.benetech.servicenet.mother.OrganizationMother;
import org.benetech.servicenet.repository.BeneficiaryRepository;
import org.benetech.servicenet.repository.ReferralRepository;
import org.benetech.servicenet.service.dto.BeneficiaryDTO;
import org.benetech.servicenet.service.dto.CheckInDTO;
import org.benetech.servicenet.service.mapper.BeneficiaryMapper;

import org.benetech.servicenet.util.IdentifierUtils;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BeneficiaryResource} REST controller.
 */
@ExtendWith({ SpringExtension.class, ZeroCodeSpringJUnit5Extension.class })
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class,
    MockedSmsConfiguration.class})
@WithMockUser(username = "admin", roles = {"ADMIN"})
@AutoConfigureMockMvc
public class BeneficiaryResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private BeneficiaryMapper beneficiaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeneficiaryMockMvc;

    private Beneficiary beneficiary;

    UUID id = UUID.randomUUID();

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiary createEntity(EntityManager em) {
        Beneficiary beneficiary = new Beneficiary()
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return beneficiary;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiary createUpdatedEntity(EntityManager em) {
        Beneficiary beneficiary = new Beneficiary()
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return beneficiary;
    }

    @BeforeEach
    public void initTest() {
        beneficiary = createEntity(em);
    }

    @Test
    @Transactional
    public void createBeneficiary() throws Exception {
        int databaseSizeBeforeCreate = beneficiaryRepository.findAll().size();

        // Create the Beneficiary
        BeneficiaryDTO beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);
        restBeneficiaryMockMvc.perform(post("/api/beneficiaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO)))
            .andExpect(status().isCreated());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeCreate + 1);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createBeneficiaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = beneficiaryRepository.findAll().size();

        // Create the Beneficiary with an existing ID
        beneficiary.setId(id);
        BeneficiaryDTO beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeneficiaryMockMvc.perform(post("/api/beneficiaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBeneficiaries() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        // Get all the beneficiaryList
        restBeneficiaryMockMvc.perform(get("/api/beneficiaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiary.getId().toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    public void getBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        // Get the beneficiary
        restBeneficiaryMockMvc.perform(get("/api/beneficiaries/{id}", beneficiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beneficiary.getId().toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingBeneficiary() throws Exception {
        // Get the beneficiary
        restBeneficiaryMockMvc.perform(get("/api/beneficiaries/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Update the beneficiary
        Beneficiary updatedBeneficiary = beneficiaryRepository.findById(beneficiary.getId()).get();
        // Disconnect from session so that the updates on updatedBeneficiary are not directly saved in db
        em.detach(updatedBeneficiary);
        updatedBeneficiary
            .phoneNumber(UPDATED_PHONE_NUMBER);
        BeneficiaryDTO beneficiaryDTO = beneficiaryMapper.toDto(updatedBeneficiary);

        restBeneficiaryMockMvc.perform(put("/api/beneficiaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO)))
            .andExpect(status().isOk());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Create the Beneficiary
        BeneficiaryDTO beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc.perform(put("/api/beneficiaries").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeDelete = beneficiaryRepository.findAll().size();

        // Delete the beneficiary
        restBeneficiaryMockMvc.perform(delete("/api/beneficiaries/{id}", beneficiary.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void checkIn() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);
        Organization org = OrganizationMother.createDefaultWithAllRelationsAndPersist(em);
        Location loc = org.getLocations().stream().findFirst().get();
        assertThat(referralRepository.findAll()).hasSize(0);

        // Check in the beneficiary
        CheckInDTO checkInDTO = new CheckInDTO();
        checkInDTO.setPhoneNumber(beneficiary.getPhoneNumber());
        checkInDTO.setCboId(org.getId());
        checkInDTO.setLocationId(loc.getId());

        restBeneficiaryMockMvc.perform(post("/api/beneficiaries/check-in").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(checkInDTO)))
            .andExpect(status().isOk());

        // Validate the Check in in the database
        List<Referral> referrals = referralRepository.findAll();
        assertThat(referrals).hasSize(1);
        Referral referral = referrals.get(0);
        assertThat(referral.getBeneficiary()).isEqualTo(beneficiary);
        assertThat(referral.getFrom()).isEqualTo(org);
        assertThat(referral.getFromLocation()).isEqualTo(loc);
        assertThat(referral.getTo()).isEqualTo(org);
        assertThat(referral.getToLocation()).isNull();
        assertThat(referral.getSentAt()).isNotNull();
        assertThat(referral.getFulfilledAt()).isNotNull();
        assertThat(referral.getShortcode()).isNull();
    }

    @Test
    @Transactional
    public void refer() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);
        Organization org = OrganizationMother.createDefaultWithAllRelationsAndPersist(em);
        Organization anotherOrg = OrganizationMother.createForServiceProviderAndPersist(em);
        Organization yetAnotherOrg = OrganizationMother.createAnotherForServiceProviderAndPersist(em);
        Location loc = org.getLocations().stream().findFirst().get();
        Location anotherLoc = anotherOrg.getLocations().stream().findFirst().get();
        Location yetAnotherLoc = yetAnotherOrg.getLocations().stream().findFirst().get();
        assertThat(referralRepository.findAll()).hasSize(0);

        // Refer the beneficiary
        Map<UUID, UUID> organizationLocs = new HashMap<>();
        String referringOrganizationId = org.getId().toString();
        String referringLocationId = loc.getId().toString();
        String beneficiaryId = IdentifierUtils.toBase36(beneficiary.getIdentifier());
        organizationLocs.put(anotherOrg.getId(), anotherLoc.getId());
        organizationLocs.put(yetAnotherOrg.getId(), yetAnotherLoc.getId());

        restBeneficiaryMockMvc.perform(post("/api/beneficiaries/refer"
            + "?referringOrganizationId=" + referringOrganizationId
            + "&referringLocationId=" + referringLocationId
            + "&beneficiaryId=" + beneficiaryId
        ).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(organizationLocs)))
            .andExpect(status().isOk());

        // Validate Referrals in the database
        List<Referral> referrals = referralRepository.findAll();
        assertThat(referrals).hasSize(2);

        Optional<Referral> referralOptional = referrals.stream().filter(ref -> ref.getTo().equals(anotherOrg)).findFirst();
        assertThat(referralOptional.isPresent()).isTrue();
        Referral referral = referralOptional.get();
        assertThat(referral.getBeneficiary()).isEqualTo(beneficiary);
        assertThat(referral.getFrom()).isEqualTo(org);
        assertThat(referral.getFromLocation()).isEqualTo(loc);
        assertThat(referral.getTo()).isEqualTo(anotherOrg);
        assertThat(referral.getToLocation()).isEqualTo(anotherLoc);
        assertThat(referral.getSentAt()).isNotNull();
        assertThat(referral.getFulfilledAt()).isNull();
        assertThat(referral.getShortcode()).isNull();

        Optional<Referral> anotherReferralOptional = referrals.stream().filter(ref -> ref.getTo().equals(yetAnotherOrg)).findFirst();
        assertThat(anotherReferralOptional.isPresent()).isTrue();
        Referral anotherReferral = anotherReferralOptional.get();
        assertThat(anotherReferral.getBeneficiary()).isEqualTo(beneficiary);
        assertThat(anotherReferral.getFrom()).isEqualTo(org);
        assertThat(anotherReferral.getFromLocation()).isEqualTo(loc);
        assertThat(anotherReferral.getTo()).isEqualTo(yetAnotherOrg);
        assertThat(anotherReferral.getToLocation()).isEqualTo(yetAnotherLoc);
        assertThat(anotherReferral.getSentAt()).isNotNull();
        assertThat(anotherReferral.getFulfilledAt()).isNull();
        assertThat(anotherReferral.getShortcode()).isNull();
    }
}
