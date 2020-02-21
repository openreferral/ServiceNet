package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.TaxonomyGroup;
import org.benetech.servicenet.repository.TaxonomyGroupRepository;
import org.benetech.servicenet.service.TaxonomyGroupService;
import org.benetech.servicenet.service.dto.TaxonomyGroupDTO;
import org.benetech.servicenet.service.mapper.TaxonomyGroupMapper;
import org.benetech.servicenet.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TaxonomyGroupResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class TaxonomyGroupResourceIT {

    @Autowired
    private TaxonomyGroupRepository taxonomyGroupRepository;

    @Mock
    private TaxonomyGroupRepository taxonomyGroupRepositoryMock;

    @Autowired
    private TaxonomyGroupMapper taxonomyGroupMapper;

    @Mock
    private TaxonomyGroupService taxonomyGroupServiceMock;

    @Autowired
    private TaxonomyGroupService taxonomyGroupService;

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

    private MockMvc restTaxonomyGroupMockMvc;

    private TaxonomyGroup taxonomyGroup;

    private static UUID id = UUID.randomUUID();

    private static UUID anotherId = UUID.randomUUID();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaxonomyGroupResource taxonomyGroupResource = new TaxonomyGroupResource(taxonomyGroupService);
        this.restTaxonomyGroupMockMvc = MockMvcBuilders.standaloneSetup(taxonomyGroupResource)
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
    public static TaxonomyGroup createEntity(EntityManager em) {
        TaxonomyGroup taxonomyGroup = new TaxonomyGroup();
        return taxonomyGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxonomyGroup createUpdatedEntity(EntityManager em) {
        TaxonomyGroup taxonomyGroup = new TaxonomyGroup();
        return taxonomyGroup;
    }

    @Before
    public void initTest() {
        taxonomyGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaxonomyGroup() throws Exception {
        int databaseSizeBeforeCreate = taxonomyGroupRepository.findAll().size();

        // Create the TaxonomyGroup
        TaxonomyGroupDTO taxonomyGroupDTO = taxonomyGroupMapper.toDto(taxonomyGroup);
        restTaxonomyGroupMockMvc.perform(post("/api/taxonomy-groups")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the TaxonomyGroup in the database
        List<TaxonomyGroup> taxonomyGroupList = taxonomyGroupRepository.findAll();
        assertThat(taxonomyGroupList).hasSize(databaseSizeBeforeCreate + 1);
        TaxonomyGroup testTaxonomyGroup = taxonomyGroupList.get(taxonomyGroupList.size() - 1);
    }

    @Test
    @Transactional
    public void createTaxonomyGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taxonomyGroupRepository.findAll().size();

        // Create the TaxonomyGroup with an existing ID
        taxonomyGroup.setId(id);
        TaxonomyGroupDTO taxonomyGroupDTO = taxonomyGroupMapper.toDto(taxonomyGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxonomyGroupMockMvc.perform(post("/api/taxonomy-groups")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxonomyGroup in the database
        List<TaxonomyGroup> taxonomyGroupList = taxonomyGroupRepository.findAll();
        assertThat(taxonomyGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTaxonomyGroups() throws Exception {
        // Initialize the database
        taxonomyGroupRepository.saveAndFlush(taxonomyGroup);

        // Get all the taxonomyGroupList
        restTaxonomyGroupMockMvc.perform(get("/api/taxonomy-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxonomyGroup.getId().toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTaxonomyGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        TaxonomyGroupResource taxonomyGroupResource = new TaxonomyGroupResource(taxonomyGroupServiceMock);
        when(taxonomyGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTaxonomyGroupMockMvc = MockMvcBuilders.standaloneSetup(taxonomyGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTaxonomyGroupMockMvc.perform(get("/api/taxonomy-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(taxonomyGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTaxonomyGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        TaxonomyGroupResource taxonomyGroupResource = new TaxonomyGroupResource(taxonomyGroupServiceMock);
            when(taxonomyGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTaxonomyGroupMockMvc = MockMvcBuilders.standaloneSetup(taxonomyGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTaxonomyGroupMockMvc.perform(get("/api/taxonomy-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(taxonomyGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTaxonomyGroup() throws Exception {
        // Initialize the database
        taxonomyGroupRepository.saveAndFlush(taxonomyGroup);

        // Get the taxonomyGroup
        restTaxonomyGroupMockMvc.perform(get("/api/taxonomy-groups/{id}", taxonomyGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxonomyGroup.getId().toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaxonomyGroup() throws Exception {
        // Get the taxonomyGroup
        restTaxonomyGroupMockMvc.perform(get("/api/taxonomy-groups/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaxonomyGroup() throws Exception {
        // Initialize the database
        taxonomyGroupRepository.saveAndFlush(taxonomyGroup);

        int databaseSizeBeforeUpdate = taxonomyGroupRepository.findAll().size();

        // Update the taxonomyGroup
        TaxonomyGroup updatedTaxonomyGroup = taxonomyGroupRepository.findById(taxonomyGroup.getId()).get();
        // Disconnect from session so that the updates on updatedTaxonomyGroup are not directly saved in db
        em.detach(updatedTaxonomyGroup);
        TaxonomyGroupDTO taxonomyGroupDTO = taxonomyGroupMapper.toDto(updatedTaxonomyGroup);

        restTaxonomyGroupMockMvc.perform(put("/api/taxonomy-groups")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyGroupDTO)))
            .andExpect(status().isOk());

        // Validate the TaxonomyGroup in the database
        List<TaxonomyGroup> taxonomyGroupList = taxonomyGroupRepository.findAll();
        assertThat(taxonomyGroupList).hasSize(databaseSizeBeforeUpdate);
        TaxonomyGroup testTaxonomyGroup = taxonomyGroupList.get(taxonomyGroupList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingTaxonomyGroup() throws Exception {
        int databaseSizeBeforeUpdate = taxonomyGroupRepository.findAll().size();

        // Create the TaxonomyGroup
        TaxonomyGroupDTO taxonomyGroupDTO = taxonomyGroupMapper.toDto(taxonomyGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxonomyGroupMockMvc.perform(put("/api/taxonomy-groups")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taxonomyGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxonomyGroup in the database
        List<TaxonomyGroup> taxonomyGroupList = taxonomyGroupRepository.findAll();
        assertThat(taxonomyGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTaxonomyGroup() throws Exception {
        // Initialize the database
        taxonomyGroupRepository.saveAndFlush(taxonomyGroup);

        int databaseSizeBeforeDelete = taxonomyGroupRepository.findAll().size();

        // Delete the taxonomyGroup
        restTaxonomyGroupMockMvc.perform(delete("/api/taxonomy-groups/{id}", taxonomyGroup.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxonomyGroup> taxonomyGroupList = taxonomyGroupRepository.findAll();
        assertThat(taxonomyGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxonomyGroup.class);
        TaxonomyGroup taxonomyGroup1 = new TaxonomyGroup();
        taxonomyGroup1.setId(id);
        TaxonomyGroup taxonomyGroup2 = new TaxonomyGroup();
        taxonomyGroup2.setId(taxonomyGroup1.getId());
        assertThat(taxonomyGroup1).isEqualTo(taxonomyGroup2);
        taxonomyGroup2.setId(anotherId);
        assertThat(taxonomyGroup1).isNotEqualTo(taxonomyGroup2);
        taxonomyGroup1.setId(null);
        assertThat(taxonomyGroup1).isNotEqualTo(taxonomyGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxonomyGroupDTO.class);
        TaxonomyGroupDTO taxonomyGroupDTO1 = new TaxonomyGroupDTO();
        taxonomyGroupDTO1.setId(id);
        TaxonomyGroupDTO taxonomyGroupDTO2 = new TaxonomyGroupDTO();
        assertThat(taxonomyGroupDTO1).isNotEqualTo(taxonomyGroupDTO2);
        taxonomyGroupDTO2.setId(taxonomyGroupDTO1.getId());
        assertThat(taxonomyGroupDTO1).isEqualTo(taxonomyGroupDTO2);
        taxonomyGroupDTO2.setId(anotherId);
        assertThat(taxonomyGroupDTO1).isNotEqualTo(taxonomyGroupDTO2);
        taxonomyGroupDTO1.setId(null);
        assertThat(taxonomyGroupDTO1).isNotEqualTo(taxonomyGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taxonomyGroupMapper.fromId(anotherId).getId()).isEqualTo(anotherId);
        assertThat(taxonomyGroupMapper.fromId(null)).isNull();
    }
}
