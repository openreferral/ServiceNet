package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.mother.ConflictMother;
import org.benetech.servicenet.mother.SystemAccountMother;
import org.benetech.servicenet.repository.ConflictRepository;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.mapper.ConflictMapper;
import org.benetech.servicenet.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import static org.benetech.servicenet.TestConstants.NON_EXISTING_UUID;
import static org.benetech.servicenet.TestConstants.UUID_1;
import static org.benetech.servicenet.TestConstants.UUID_2;
import static org.benetech.servicenet.TestConstants.UUID_42;
import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.benetech.servicenet.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ConflictResource REST controller.
 *
 * @see ConflictResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ConflictResourceIntTest {

    @Autowired
    private ConflictRepository conflictRepository;

    @Mock
    private ConflictRepository conflictRepositoryMock;

    @Autowired
    private ConflictMapper conflictMapper;

    @Mock
    private ConflictService conflictServiceMock;

    @Autowired
    private ConflictService conflictService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConflictMockMvc;

    private Conflict conflict;

    private SystemAccount owner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConflictResource conflictResource = new ConflictResource(conflictService);
        this.restConflictMockMvc = MockMvcBuilders.standaloneSetup(conflictResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        owner = SystemAccountMother.createDefaultAndPersist(em);
        conflict = ConflictMother.createDefault();
        conflict.setOwner(owner);
        em.persist(conflict);
        em.flush();
    }

    @Test
    @Transactional
    public void createConflict() throws Exception {
        int databaseSizeBeforeCreate = conflictRepository.findAll().size();

        // Create the Conflict
        Conflict conflict = ConflictMother.createDefault();
        em.persist(conflict.getOwner());
        em.flush();
        ConflictDTO conflictDTO = conflictMapper.toDto(conflict);
        restConflictMockMvc.perform(post("/api/conflicts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conflictDTO)))
            .andExpect(status().isCreated());

        // Validate the Conflict in the database
        List<Conflict> conflictList = conflictRepository.findAll();
        assertThat(conflictList).hasSize(databaseSizeBeforeCreate + 1);
        Conflict testConflict = conflictList.get(conflictList.size() - 1);
        assertThat(testConflict.getCurrentValue()).isEqualTo(ConflictMother.DEFAULT_CURRENT_VALUE);
        assertThat(testConflict.getCurrentValueDate()).isEqualTo(ConflictMother.DEFAULT_CURRENT_VALUE_DATE);
        assertThat(testConflict.getOfferedValue()).isEqualTo(ConflictMother.DEFAULT_OFFERED_VALUE);
        assertThat(testConflict.getOfferedValueDate()).isEqualTo(ConflictMother.DEFAULT_OFFERED_VALUE_DATE);
        assertThat(testConflict.getFieldName()).isEqualTo(ConflictMother.DEFAULT_FIELD_NAME);
        assertThat(testConflict.getEntityPath()).isEqualTo(ConflictMother.DEFAULT_ENTITY_PATH);
        assertThat(testConflict.getState()).isEqualTo(ConflictMother.DEFAULT_STATE);
        assertThat(testConflict.getStateDate()).isEqualTo(ConflictMother.DEFAULT_STATE_DATE);
        assertThat(testConflict.getCreatedDate()).isEqualTo(ConflictMother.DEFAULT_CREATED_DATE);
        assertThat(testConflict.getResourceId()).isEqualTo(UUID_1);
    }

    @Test
    @Transactional
    public void createConflictWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conflictRepository.findAll().size();
        Conflict conflict = ConflictMother.createDefault();
        // Create the Conflict with an existing ID
        conflict.setId(UUID_1);
        ConflictDTO conflictDTO = conflictMapper.toDto(conflict);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConflictMockMvc.perform(post("/api/conflicts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conflictDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Conflict in the database
        List<Conflict> conflictList = conflictRepository.findAll();
        assertThat(conflictList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllConflicts() throws Exception {

        // Get all the conflictList
        restConflictMockMvc.perform(get("/api/conflicts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conflict.getId().toString())))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(ConflictMother.DEFAULT_CURRENT_VALUE)))
            .andExpect(jsonPath("$.[*].currentValueDate").value(
                hasItem(sameInstant(ConflictMother.DEFAULT_CURRENT_VALUE_DATE))))
            .andExpect(jsonPath("$.[*].offeredValue").value(hasItem(ConflictMother.DEFAULT_OFFERED_VALUE)))
            .andExpect(jsonPath("$.[*].offeredValueDate").value(
                hasItem(sameInstant(ConflictMother.DEFAULT_OFFERED_VALUE_DATE))))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(ConflictMother.DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].entityPath").value(hasItem(ConflictMother.DEFAULT_ENTITY_PATH)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(ConflictMother.DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].stateDate").value(hasItem(sameInstant(ConflictMother.DEFAULT_STATE_DATE))))
            .andExpect(jsonPath("$.[*].createdDate").value(
                hasItem(sameInstant(ConflictMother.DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].resourceId").value(hasItem(UUID_1.toString())));
    }

    @Test
    @Transactional
    public void getConflict() throws Exception {
        // Initialize the database
        conflictRepository.saveAndFlush(conflict);

        // Get the conflict
        restConflictMockMvc.perform(get("/api/conflicts/{id}", conflict.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conflict.getId().toString()))
            .andExpect(jsonPath("$.currentValue").value(ConflictMother.DEFAULT_CURRENT_VALUE))
            .andExpect(jsonPath("$.currentValueDate").value(sameInstant(ConflictMother.DEFAULT_CURRENT_VALUE_DATE)))
            .andExpect(jsonPath("$.offeredValue").value(ConflictMother.DEFAULT_OFFERED_VALUE))
            .andExpect(jsonPath("$.offeredValueDate").value(sameInstant(ConflictMother.DEFAULT_OFFERED_VALUE_DATE)))
            .andExpect(jsonPath("$.fieldName").value(ConflictMother.DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.entityPath").value(ConflictMother.DEFAULT_ENTITY_PATH))
            .andExpect(jsonPath("$.state").value(ConflictMother.DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.stateDate").value(sameInstant(ConflictMother.DEFAULT_STATE_DATE)))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(ConflictMother.DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.resourceId").value(UUID_1.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConflict() throws Exception {
        // Get the conflict
        restConflictMockMvc.perform(get("/api/conflicts/{id}", NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConflict() throws Exception {
        // Initialize the database
        conflictRepository.saveAndFlush(conflict);

        int databaseSizeBeforeUpdate = conflictRepository.findAll().size();

        // Update the conflict
        Conflict fetchedConflict = conflictRepository.findById(conflict.getId()).get();
        // Disconnect from session so that the updates on updatedConflict are not directly saved in db
        em.detach(fetchedConflict);
        Conflict updatedConflict = Conflict.builder()
            .owner(fetchedConflict.getOwner())
            .currentValue(ConflictMother.UPDATED_CURRENT_VALUE)
            .currentValueDate(ConflictMother.UPDATED_CURRENT_VALUE_DATE)
            .offeredValue(ConflictMother.UPDATED_OFFERED_VALUE)
            .offeredValueDate(ConflictMother.UPDATED_OFFERED_VALUE_DATE)
            .fieldName(ConflictMother.UPDATED_FIELD_NAME)
            .entityPath(ConflictMother.UPDATED_ENTITY_PATH)
            .state(ConflictMother.UPDATED_STATE)
            .stateDate(ConflictMother.UPDATED_STATE_DATE)
            .createdDate(ConflictMother.UPDATED_CREATED_DATE)
            .resourceId(UUID_2)
            .build()
            .id(fetchedConflict.getId());
        ConflictDTO conflictDTO = conflictMapper.toDto(updatedConflict);

        restConflictMockMvc.perform(put("/api/conflicts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conflictDTO)))
            .andExpect(status().isOk());

        // Validate the Conflict in the database
        List<Conflict> conflictList = conflictRepository.findAll();
        assertThat(conflictList).hasSize(databaseSizeBeforeUpdate);
        Conflict testConflict = conflictList.get(conflictList.size() - 1);
        assertThat(testConflict.getCurrentValue()).isEqualTo(ConflictMother.UPDATED_CURRENT_VALUE);
        assertThat(testConflict.getCurrentValueDate()).isEqualTo(ConflictMother.UPDATED_CURRENT_VALUE_DATE);
        assertThat(testConflict.getOfferedValue()).isEqualTo(ConflictMother.UPDATED_OFFERED_VALUE);
        assertThat(testConflict.getOfferedValueDate()).isEqualTo(ConflictMother.UPDATED_OFFERED_VALUE_DATE);
        assertThat(testConflict.getFieldName()).isEqualTo(ConflictMother.UPDATED_FIELD_NAME);
        assertThat(testConflict.getEntityPath()).isEqualTo(ConflictMother.UPDATED_ENTITY_PATH);
        assertThat(testConflict.getState()).isEqualTo(ConflictMother.UPDATED_STATE);
        assertThat(testConflict.getStateDate()).isEqualTo(ConflictMother.UPDATED_STATE_DATE);
        assertThat(testConflict.getCreatedDate()).isEqualTo(ConflictMother.UPDATED_CREATED_DATE);
        assertThat(testConflict.getResourceId()).isEqualTo(UUID_2);
    }

    @Test
    @Transactional
    public void updateNonExistingConflict() throws Exception {
        int databaseSizeBeforeUpdate = conflictRepository.findAll().size();

        // Create the Conflict
        SystemAccount owner = SystemAccountMother.createDefaultAndPersist(em);
        Conflict conflict = ConflictMother.createDefault();
        conflict.setOwner(owner);
        ConflictDTO conflictDTO = conflictMapper.toDto(conflict);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConflictMockMvc.perform(put("/api/conflicts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conflictDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Conflict in the database
        List<Conflict> conflictList = conflictRepository.findAll();
        assertThat(conflictList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConflict() throws Exception {
        // Initialize the database
        conflictRepository.saveAndFlush(conflict);

        int databaseSizeBeforeDelete = conflictRepository.findAll().size();

        // Get the conflict
        restConflictMockMvc.perform(delete("/api/conflicts/{id}", conflict.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Conflict> conflictList = conflictRepository.findAll();
        assertThat(conflictList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conflict.class);
        Conflict conflict1 = new Conflict();
        conflict1.setId(UUID_1);
        Conflict conflict2 = new Conflict();
        conflict2.setId(conflict1.getId());
        assertThat(conflict1).isEqualTo(conflict2);
        conflict2.setId(UUID_2);
        assertThat(conflict1).isNotEqualTo(conflict2);
        conflict1.setId(null);
        assertThat(conflict1).isNotEqualTo(conflict2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConflictDTO.class);
        ConflictDTO conflictDTO1 = new ConflictDTO();
        conflictDTO1.setId(UUID_1);
        ConflictDTO conflictDTO2 = new ConflictDTO();
        assertThat(conflictDTO1).isNotEqualTo(conflictDTO2);
        conflictDTO2.setId(conflictDTO1.getId());
        assertThat(conflictDTO1).isEqualTo(conflictDTO2);
        conflictDTO2.setId(UUID_2);
        assertThat(conflictDTO1).isNotEqualTo(conflictDTO2);
        conflictDTO1.setId(null);
        assertThat(conflictDTO1).isNotEqualTo(conflictDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(conflictMapper.fromId(UUID_42).getId()).isEqualTo(UUID_42);
        assertThat(conflictMapper.fromId(null)).isNull();
    }
}
