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
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.dto.AddressDTO;
import org.benetech.servicenet.service.mapper.PhysicalAddressMapper;
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
 * Test class for the PhysicalAddressResource REST controller.
 *
 * @see PhysicalAddressResource
 */
@SuppressWarnings("CPD-START")
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class PhysicalAddressResourceIntTest {

    private static final String DEFAULT_ATTENTION = "AAAAAAAAAA";
    private static final String UPDATED_ATTENTION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    @Autowired
    private PhysicalAddressRepository physicalAddressRepository;

    @Autowired
    private PhysicalAddressMapper physicalAddressMapper;

    @Autowired
    private PhysicalAddressService physicalAddressService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPhysicalAddressMockMvc;

    private PhysicalAddress physicalAddress;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhysicalAddress createEntity(EntityManager em) {
        PhysicalAddress physicalAddress = new PhysicalAddress()
            .attention(DEFAULT_ATTENTION)
            .address1(DEFAULT_ADDRESS_1)
            .city(DEFAULT_CITY)
            .region(DEFAULT_REGION)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .postalCode(DEFAULT_POSTAL_CODE)
            .country(DEFAULT_COUNTRY);
        return physicalAddress;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final PhysicalAddressResource physicalAddressResource = new PhysicalAddressResource(physicalAddressService);
        this.restPhysicalAddressMockMvc = MockMvcBuilders.standaloneSetup(physicalAddressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        physicalAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhysicalAddress() throws Exception {
        int databaseSizeBeforeCreate = physicalAddressRepository.findAll().size();

        // Create the PhysicalAddress
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);
        restPhysicalAddressMockMvc.perform(post("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the PhysicalAddress in the database
        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PhysicalAddress testPhysicalAddress = physicalAddressList.get(physicalAddressList.size() - 1);
        assertThat(testPhysicalAddress.getAttention()).isEqualTo(DEFAULT_ATTENTION);
        assertThat(testPhysicalAddress.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testPhysicalAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPhysicalAddress.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testPhysicalAddress.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testPhysicalAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testPhysicalAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void createPhysicalAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = physicalAddressRepository.findAll().size();

        // Create the PhysicalAddress with an existing ID
        physicalAddress.setId(TestConstants.UUID_1);
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhysicalAddressMockMvc.perform(post("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PhysicalAddress in the database
        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = physicalAddressRepository.findAll().size();
        // set the field null
        physicalAddress.setAddress1(null);

        // Create the PhysicalAddress, which fails.
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);

        restPhysicalAddressMockMvc.perform(post("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = physicalAddressRepository.findAll().size();
        // set the field null
        physicalAddress.setCity(null);

        // Create the PhysicalAddress, which fails.
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);

        restPhysicalAddressMockMvc.perform(post("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = physicalAddressRepository.findAll().size();
        // set the field null
        physicalAddress.setStateProvince(null);

        // Create the PhysicalAddress, which fails.
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);

        restPhysicalAddressMockMvc.perform(post("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = physicalAddressRepository.findAll().size();
        // set the field null
        physicalAddress.setPostalCode(null);

        // Create the PhysicalAddress, which fails.
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);

        restPhysicalAddressMockMvc.perform(post("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = physicalAddressRepository.findAll().size();
        // set the field null
        physicalAddress.setCountry(null);

        // Create the PhysicalAddress, which fails.
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);

        restPhysicalAddressMockMvc.perform(post("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhysicalAddresses() throws Exception {
        // Initialize the database
        physicalAddressRepository.saveAndFlush(physicalAddress);

        // Get all the physicalAddressList
        restPhysicalAddressMockMvc.perform(get("/api/physical-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(physicalAddress.getId().toString())))
            .andExpect(jsonPath("$.[*].attention").value(hasItem(DEFAULT_ATTENTION.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void getPhysicalAddress() throws Exception {
        // Initialize the database
        physicalAddressRepository.saveAndFlush(physicalAddress);

        // Get the physicalAddress
        restPhysicalAddressMockMvc.perform(get("/api/physical-addresses/{id}", physicalAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(physicalAddress.getId().toString()))
            .andExpect(jsonPath("$.attention").value(DEFAULT_ATTENTION.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhysicalAddress() throws Exception {
        // Get the physicalAddress
        restPhysicalAddressMockMvc.perform(get("/api/physical-addresses/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhysicalAddress() throws Exception {
        // Initialize the database
        physicalAddressRepository.saveAndFlush(physicalAddress);

        int databaseSizeBeforeUpdate = physicalAddressRepository.findAll().size();

        // Update the physicalAddress
        PhysicalAddress updatedPhysicalAddress = physicalAddressRepository.findById(physicalAddress.getId()).get();
        // Disconnect from session so that the updates on updatedPhysicalAddress are not directly saved in db
        em.detach(updatedPhysicalAddress);
        updatedPhysicalAddress
            .attention(UPDATED_ATTENTION)
            .address1(UPDATED_ADDRESS_1)
            .city(UPDATED_CITY)
            .region(UPDATED_REGION)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY);
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(updatedPhysicalAddress);

        restPhysicalAddressMockMvc.perform(put("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isOk());

        // Validate the PhysicalAddress in the database
        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeUpdate);
        PhysicalAddress testPhysicalAddress = physicalAddressList.get(physicalAddressList.size() - 1);
        assertThat(testPhysicalAddress.getAttention()).isEqualTo(UPDATED_ATTENTION);
        assertThat(testPhysicalAddress.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testPhysicalAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPhysicalAddress.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testPhysicalAddress.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testPhysicalAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testPhysicalAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void updateNonExistingPhysicalAddress() throws Exception {
        int databaseSizeBeforeUpdate = physicalAddressRepository.findAll().size();

        // Create the PhysicalAddress
        AddressDTO physicalAddressDTO = physicalAddressMapper.toDto(physicalAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhysicalAddressMockMvc.perform(put("/api/physical-addresses")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(physicalAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PhysicalAddress in the database
        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePhysicalAddress() throws Exception {
        // Initialize the database
        physicalAddressRepository.saveAndFlush(physicalAddress);

        int databaseSizeBeforeDelete = physicalAddressRepository.findAll().size();

        // Get the physicalAddress
        restPhysicalAddressMockMvc.perform(delete("/api/physical-addresses/{id}", physicalAddress.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PhysicalAddress> physicalAddressList = physicalAddressRepository.findAll();
        assertThat(physicalAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhysicalAddress.class);
        PhysicalAddress physicalAddress1 = new PhysicalAddress();
        physicalAddress1.setId(TestConstants.UUID_1);
        PhysicalAddress physicalAddress2 = new PhysicalAddress();
        physicalAddress2.setId(physicalAddress1.getId());
        assertThat(physicalAddress1).isEqualTo(physicalAddress2);
        physicalAddress2.setId(TestConstants.UUID_2);
        assertThat(physicalAddress1).isNotEqualTo(physicalAddress2);
        physicalAddress1.setId(null);
        assertThat(physicalAddress1).isNotEqualTo(physicalAddress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressDTO.class);
        AddressDTO physicalAddressDTO1 = new AddressDTO();
        physicalAddressDTO1.setId(TestConstants.UUID_1);
        AddressDTO physicalAddressDTO2 = new AddressDTO();
        assertThat(physicalAddressDTO1).isNotEqualTo(physicalAddressDTO2);
        physicalAddressDTO2.setId(physicalAddressDTO1.getId());
        assertThat(physicalAddressDTO1).isEqualTo(physicalAddressDTO2);
        physicalAddressDTO2.setId(TestConstants.UUID_2);
        assertThat(physicalAddressDTO1).isNotEqualTo(physicalAddressDTO2);
        physicalAddressDTO1.setId(null);
        assertThat(physicalAddressDTO1).isNotEqualTo(physicalAddressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(physicalAddressMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(physicalAddressMapper.fromId(null)).isNull();
    }
}
