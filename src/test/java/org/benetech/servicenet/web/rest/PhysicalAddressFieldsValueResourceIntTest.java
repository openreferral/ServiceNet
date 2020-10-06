package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.PhysicalAddressFieldsValue;
import org.benetech.servicenet.repository.PhysicalAddressFieldsValueRepository;
import org.benetech.servicenet.service.PhysicalAddressFieldsValueService;
import org.benetech.servicenet.service.dto.PhysicalAddressFieldsValueDTO;
import org.benetech.servicenet.service.mapper.PhysicalAddressFieldsValueMapper;
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
import org.benetech.servicenet.ZeroCodeSpringJUnit4Runner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.benetech.servicenet.domain.enumeration.PhysicalAddressFields;

/**
 * Integration tests for the {@link PhysicalAddressFieldsValueResource} REST controller.
 */
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class PhysicalAddressFieldsValueResourceIntTest {

    private static final PhysicalAddressFields DEFAULT_PHYSICAL_ADDRESS_FIELD = PhysicalAddressFields.ATTENTION;
    private static final PhysicalAddressFields UPDATED_PHYSICAL_ADDRESS_FIELD = PhysicalAddressFields.ADDRESS_1;

    @Autowired
    private PhysicalAddressFieldsValueRepository physicalAddressFieldsValueRepository;

    @Autowired
    private PhysicalAddressFieldsValueMapper physicalAddressFieldsValueMapper;

    @Autowired
    private PhysicalAddressFieldsValueService physicalAddressFieldsValueService;

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

    private MockMvc restPhysicalAddressFieldsValueMockMvc;

    private PhysicalAddressFieldsValue physicalAddressFieldsValue;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final PhysicalAddressFieldsValueResource physicalAddressFieldsValueResource =
            new PhysicalAddressFieldsValueResource(physicalAddressFieldsValueService);
        this.restPhysicalAddressFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(physicalAddressFieldsValueResource)
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
    public static PhysicalAddressFieldsValue createEntity(EntityManager em) {
        PhysicalAddressFieldsValue physicalAddressFieldsValue = new PhysicalAddressFieldsValue()
            .physicalAddressField(DEFAULT_PHYSICAL_ADDRESS_FIELD);
        return physicalAddressFieldsValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhysicalAddressFieldsValue createUpdatedEntity(EntityManager em) {
        PhysicalAddressFieldsValue physicalAddressFieldsValue = new PhysicalAddressFieldsValue()
            .physicalAddressField(UPDATED_PHYSICAL_ADDRESS_FIELD);
        return physicalAddressFieldsValue;
    }

    @Before
    public void initTest() {
        physicalAddressFieldsValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhysicalAddressFieldsValue() throws Exception {
        int databaseSizeBeforeCreate = physicalAddressFieldsValueRepository.findAll().size();

        // Create the PhysicalAddressFieldsValue
        PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO = physicalAddressFieldsValueMapper
            .toDto(physicalAddressFieldsValue);
        restPhysicalAddressFieldsValueMockMvc.perform(post("/api/physical-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressFieldsValueDTO)))
            .andExpect(status().isCreated());

        // Validate the PhysicalAddressFieldsValue in the database
        List<PhysicalAddressFieldsValue> physicalAddressFieldsValueList = physicalAddressFieldsValueRepository
            .findAll();
        assertThat(physicalAddressFieldsValueList).hasSize(databaseSizeBeforeCreate + 1);
        PhysicalAddressFieldsValue testPhysicalAddressFieldsValue = physicalAddressFieldsValueList
            .get(physicalAddressFieldsValueList.size() - 1);
        assertThat(testPhysicalAddressFieldsValue.getPhysicalAddressField()).isEqualTo(DEFAULT_PHYSICAL_ADDRESS_FIELD);
    }

    @Test
    @Transactional
    public void createPhysicalAddressFieldsValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = physicalAddressFieldsValueRepository.findAll().size();

        // Create the PhysicalAddressFieldsValue with an existing ID
        physicalAddressFieldsValue.setId(TestConstants.UUID_1);
        PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO = physicalAddressFieldsValueMapper
            .toDto(physicalAddressFieldsValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhysicalAddressFieldsValueMockMvc.perform(post("/api/physical-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PhysicalAddressFieldsValue in the database
        List<PhysicalAddressFieldsValue> physicalAddressFieldsValueList = physicalAddressFieldsValueRepository
            .findAll();
        assertThat(physicalAddressFieldsValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPhysicalAddressFieldsValues() throws Exception {
        // Initialize the database
        physicalAddressFieldsValueRepository.saveAndFlush(physicalAddressFieldsValue);

        // Get all the physicalAddressFieldsValueList
        restPhysicalAddressFieldsValueMockMvc.perform(get("/api/physical-address-fields-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(physicalAddressFieldsValue.getId().toString())))
            .andExpect(jsonPath("$.[*].physicalAddressField")
                .value(hasItem(DEFAULT_PHYSICAL_ADDRESS_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getPhysicalAddressFieldsValue() throws Exception {
        // Initialize the database
        physicalAddressFieldsValueRepository.saveAndFlush(physicalAddressFieldsValue);

        // Get the physicalAddressFieldsValue
        restPhysicalAddressFieldsValueMockMvc
            .perform(get("/api/physical-address-fields-values/{id}", physicalAddressFieldsValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(physicalAddressFieldsValue.getId().toString()))
            .andExpect(jsonPath("$.physicalAddressField").value(DEFAULT_PHYSICAL_ADDRESS_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhysicalAddressFieldsValue() throws Exception {
        // Get the physicalAddressFieldsValue
        restPhysicalAddressFieldsValueMockMvc
            .perform(get("/api/physical-address-fields-values/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhysicalAddressFieldsValue() throws Exception {
        // Initialize the database
        physicalAddressFieldsValueRepository.saveAndFlush(physicalAddressFieldsValue);

        int databaseSizeBeforeUpdate = physicalAddressFieldsValueRepository.findAll().size();

        // Update the physicalAddressFieldsValue
        PhysicalAddressFieldsValue updatedPhysicalAddressFieldsValue = physicalAddressFieldsValueRepository
            .findById(physicalAddressFieldsValue.getId()).get();
        // Disconnect from session so that the updates on updatedPhysicalAddressFieldsValue are not directly saved in db
        em.detach(updatedPhysicalAddressFieldsValue);
        updatedPhysicalAddressFieldsValue
            .physicalAddressField(UPDATED_PHYSICAL_ADDRESS_FIELD);
        PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO = physicalAddressFieldsValueMapper
            .toDto(updatedPhysicalAddressFieldsValue);

        restPhysicalAddressFieldsValueMockMvc.perform(put("/api/physical-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressFieldsValueDTO)))
            .andExpect(status().isOk());

        // Validate the PhysicalAddressFieldsValue in the database
        List<PhysicalAddressFieldsValue> physicalAddressFieldsValueList = physicalAddressFieldsValueRepository.findAll();
        assertThat(physicalAddressFieldsValueList).hasSize(databaseSizeBeforeUpdate);
        PhysicalAddressFieldsValue testPhysicalAddressFieldsValue = physicalAddressFieldsValueList
            .get(physicalAddressFieldsValueList.size() - 1);
        assertThat(testPhysicalAddressFieldsValue.getPhysicalAddressField()).isEqualTo(UPDATED_PHYSICAL_ADDRESS_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingPhysicalAddressFieldsValue() throws Exception {
        int databaseSizeBeforeUpdate = physicalAddressFieldsValueRepository.findAll().size();

        // Create the PhysicalAddressFieldsValue
        PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO = physicalAddressFieldsValueMapper
            .toDto(physicalAddressFieldsValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhysicalAddressFieldsValueMockMvc.perform(put("/api/physical-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PhysicalAddressFieldsValue in the database
        List<PhysicalAddressFieldsValue> physicalAddressFieldsValueList = physicalAddressFieldsValueRepository.findAll();
        assertThat(physicalAddressFieldsValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePhysicalAddressFieldsValue() throws Exception {
        // Initialize the database
        physicalAddressFieldsValueRepository.saveAndFlush(physicalAddressFieldsValue);

        int databaseSizeBeforeDelete = physicalAddressFieldsValueRepository.findAll().size();

        // Delete the physicalAddressFieldsValue
        restPhysicalAddressFieldsValueMockMvc
            .perform(delete("/api/physical-address-fields-values/{id}", physicalAddressFieldsValue.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhysicalAddressFieldsValue> physicalAddressFieldsValueList = physicalAddressFieldsValueRepository.findAll();
        assertThat(physicalAddressFieldsValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhysicalAddressFieldsValue.class);
        PhysicalAddressFieldsValue physicalAddressFieldsValue1 = new PhysicalAddressFieldsValue();
        physicalAddressFieldsValue1.setId(TestConstants.UUID_1);
        PhysicalAddressFieldsValue physicalAddressFieldsValue2 = new PhysicalAddressFieldsValue();
        physicalAddressFieldsValue2.setId(physicalAddressFieldsValue1.getId());
        assertThat(physicalAddressFieldsValue1).isEqualTo(physicalAddressFieldsValue2);
        physicalAddressFieldsValue2.setId(TestConstants.UUID_2);
        assertThat(physicalAddressFieldsValue1).isNotEqualTo(physicalAddressFieldsValue2);
        physicalAddressFieldsValue1.setId(null);
        assertThat(physicalAddressFieldsValue1).isNotEqualTo(physicalAddressFieldsValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhysicalAddressFieldsValueDTO.class);
        PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO1 = new PhysicalAddressFieldsValueDTO();
        physicalAddressFieldsValueDTO1.setId(TestConstants.UUID_1);
        PhysicalAddressFieldsValueDTO physicalAddressFieldsValueDTO2 = new PhysicalAddressFieldsValueDTO();
        assertThat(physicalAddressFieldsValueDTO1).isNotEqualTo(physicalAddressFieldsValueDTO2);
        physicalAddressFieldsValueDTO2.setId(physicalAddressFieldsValueDTO1.getId());
        assertThat(physicalAddressFieldsValueDTO1).isEqualTo(physicalAddressFieldsValueDTO2);
        physicalAddressFieldsValueDTO2.setId(TestConstants.UUID_2);
        assertThat(physicalAddressFieldsValueDTO1).isNotEqualTo(physicalAddressFieldsValueDTO2);
        physicalAddressFieldsValueDTO1.setId(null);
        assertThat(physicalAddressFieldsValueDTO1).isNotEqualTo(physicalAddressFieldsValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(physicalAddressFieldsValueMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(physicalAddressFieldsValueMapper.fromId(null)).isNull();
    }
}
