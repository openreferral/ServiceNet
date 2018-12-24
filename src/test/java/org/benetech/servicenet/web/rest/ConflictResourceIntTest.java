package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.SystemAccount;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.benetech.servicenet.TestConstants.NON_EXISTING_UUID;
import static org.benetech.servicenet.TestConstants.UUID_1;
import static org.benetech.servicenet.TestConstants.UUID_2;
import static org.benetech.servicenet.TestConstants.UUID_42;
import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.benetech.servicenet.web.rest.TestUtil.sameInstant;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;
/**
 * Test class for the ConflictResource REST controller.
 *
 * @see ConflictResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ConflictResourceIntTest {

    private static final String DEFAULT_CURRENT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENT_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CURRENT_VALUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CURRENT_VALUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OFFERED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OFFERED_VALUE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_OFFERED_VALUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_OFFERED_VALUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_PATH = "BBBBBBBBBB";

    private static final ConflictStateEnum DEFAULT_STATE = ConflictStateEnum.PENDING;
    private static final ConflictStateEnum UPDATED_STATE = ConflictStateEnum.ACCEPTED;

    private static final ZonedDateTime DEFAULT_STATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
        ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

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

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conflict createEntity(EntityManager em) {
        Conflict conflict = Conflict.builder()
            .currentValue(DEFAULT_CURRENT_VALUE)
            .currentValueDate(DEFAULT_CURRENT_VALUE_DATE)
            .offeredValue(DEFAULT_OFFERED_VALUE)
            .offeredValueDate(DEFAULT_OFFERED_VALUE_DATE)
            .fieldName(DEFAULT_FIELD_NAME)
            .entityPath(DEFAULT_ENTITY_PATH)
            .state(DEFAULT_STATE)
            .stateDate(DEFAULT_STATE_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .resourceId(UUID_1)
            .build();
        // Add required entity
        SystemAccount systemAccount = SystemAccountResourceIntTest.createEntity(em);
        em.persist(systemAccount);
        em.flush();
        conflict.setOwner(systemAccount);
        return conflict;
    }

    @Before
    public void initTest() {
        conflict = createEntity(em);
    }

    @Test
    @Transactional
    public void createConflict() throws Exception {
        int databaseSizeBeforeCreate = conflictRepository.findAll().size();

        // Create the Conflict
        ConflictDTO conflictDTO = conflictMapper.toDto(conflict);
        restConflictMockMvc.perform(post("/api/conflicts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conflictDTO)))
            .andExpect(status().isCreated());

        // Validate the Conflict in the database
        List<Conflict> conflictList = conflictRepository.findAll();
        assertThat(conflictList).hasSize(databaseSizeBeforeCreate + 1);
        Conflict testConflict = conflictList.get(conflictList.size() - 1);
        assertThat(testConflict.getCurrentValue()).isEqualTo(DEFAULT_CURRENT_VALUE);
        assertThat(testConflict.getCurrentValueDate()).isEqualTo(DEFAULT_CURRENT_VALUE_DATE);
        assertThat(testConflict.getOfferedValue()).isEqualTo(DEFAULT_OFFERED_VALUE);
        assertThat(testConflict.getOfferedValueDate()).isEqualTo(DEFAULT_OFFERED_VALUE_DATE);
        assertThat(testConflict.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testConflict.getEntityPath()).isEqualTo(DEFAULT_ENTITY_PATH);
        assertThat(testConflict.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testConflict.getStateDate()).isEqualTo(DEFAULT_STATE_DATE);
        assertThat(testConflict.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testConflict.getResourceId()).isEqualTo(UUID_1);
    }

    @Test
    @Transactional
    public void createConflictWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = conflictRepository.findAll().size();

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
        // Initialize the database
        conflictRepository.saveAndFlush(conflict);

        // Get all the conflictList
        restConflictMockMvc.perform(get("/api/conflicts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conflict.getId().toString())))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].currentValueDate").value(hasItem(sameInstant(DEFAULT_CURRENT_VALUE_DATE))))
            .andExpect(jsonPath("$.[*].offeredValue").value(hasItem(DEFAULT_OFFERED_VALUE.toString())))
            .andExpect(jsonPath("$.[*].offeredValueDate").value(hasItem(sameInstant(DEFAULT_OFFERED_VALUE_DATE))))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityPath").value(hasItem(DEFAULT_ENTITY_PATH.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].stateDate").value(hasItem(sameInstant(DEFAULT_STATE_DATE))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].resourceId").value(hasItem(UUID_1.toString())));
    }

    @SuppressWarnings({"unchecked"})
    public void getAllConflictsWithEagerRelationshipsIsEnabled() throws Exception {
        ConflictResource conflictResource = new ConflictResource(conflictServiceMock);
        when(conflictServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restConflictMockMvc = MockMvcBuilders.standaloneSetup(conflictResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConflictMockMvc.perform(get("/api/conflicts?eagerload=true"))
        .andExpect(status().isOk());

        verify(conflictServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllConflictsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ConflictResource conflictResource = new ConflictResource(conflictServiceMock);
            when(conflictServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restConflictMockMvc = MockMvcBuilders.standaloneSetup(conflictResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConflictMockMvc.perform(get("/api/conflicts?eagerload=true"))
        .andExpect(status().isOk());

            verify(conflictServiceMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.currentValue").value(DEFAULT_CURRENT_VALUE.toString()))
            .andExpect(jsonPath("$.currentValueDate").value(sameInstant(DEFAULT_CURRENT_VALUE_DATE)))
            .andExpect(jsonPath("$.offeredValue").value(DEFAULT_OFFERED_VALUE.toString()))
            .andExpect(jsonPath("$.offeredValueDate").value(sameInstant(DEFAULT_OFFERED_VALUE_DATE)))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME.toString()))
            .andExpect(jsonPath("$.entityPath").value(DEFAULT_ENTITY_PATH.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.stateDate").value(sameInstant(DEFAULT_STATE_DATE)))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
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
            .id(fetchedConflict.getId())
            .owner(fetchedConflict.getOwner())
            .currentValue(UPDATED_CURRENT_VALUE)
            .currentValueDate(UPDATED_CURRENT_VALUE_DATE)
            .offeredValue(UPDATED_OFFERED_VALUE)
            .offeredValueDate(UPDATED_OFFERED_VALUE_DATE)
            .fieldName(UPDATED_FIELD_NAME)
            .entityPath(UPDATED_ENTITY_PATH)
            .state(UPDATED_STATE)
            .stateDate(UPDATED_STATE_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .resourceId(UUID_2)
            .build();
        ConflictDTO conflictDTO = conflictMapper.toDto(updatedConflict);

        restConflictMockMvc.perform(put("/api/conflicts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(conflictDTO)))
            .andExpect(status().isOk());

        // Validate the Conflict in the database
        List<Conflict> conflictList = conflictRepository.findAll();
        assertThat(conflictList).hasSize(databaseSizeBeforeUpdate);
        Conflict testConflict = conflictList.get(conflictList.size() - 1);
        assertThat(testConflict.getCurrentValue()).isEqualTo(UPDATED_CURRENT_VALUE);
        assertThat(testConflict.getCurrentValueDate()).isEqualTo(UPDATED_CURRENT_VALUE_DATE);
        assertThat(testConflict.getOfferedValue()).isEqualTo(UPDATED_OFFERED_VALUE);
        assertThat(testConflict.getOfferedValueDate()).isEqualTo(UPDATED_OFFERED_VALUE_DATE);
        assertThat(testConflict.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testConflict.getEntityPath()).isEqualTo(UPDATED_ENTITY_PATH);
        assertThat(testConflict.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testConflict.getStateDate()).isEqualTo(UPDATED_STATE_DATE);
        assertThat(testConflict.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testConflict.getResourceId()).isEqualTo(UUID_2);
    }

    @Test
    @Transactional
    public void updateNonExistingConflict() throws Exception {
        int databaseSizeBeforeUpdate = conflictRepository.findAll().size();

        // Create the Conflict
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
