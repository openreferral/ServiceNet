package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.repository.PostalAddressRepository;
import org.benetech.servicenet.service.PostalAddressService;
import org.benetech.servicenet.service.dto.PostalAddressDTO;
import org.benetech.servicenet.service.mapper.PostalAddressMapper;
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

import javax.persistence.EntityManager;
import java.util.List;

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

/**
 * Test class for the PostalAddressResource REST controller.
 *
 * @see PostalAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class PostalAddressResourceIntTest {

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
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private PostalAddressMapper postalAddressMapper;

    @Autowired
    private PostalAddressService postalAddressService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPostalAddressMockMvc;

    private PostalAddress postalAddress;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostalAddress createEntity(EntityManager em) {
        PostalAddress postalAddress = new PostalAddress()
            .attention(DEFAULT_ATTENTION)
            .address1(DEFAULT_ADDRESS_1)
            .city(DEFAULT_CITY)
            .region(DEFAULT_REGION)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .postalCode(DEFAULT_POSTAL_CODE)
            .country(DEFAULT_COUNTRY);
        return postalAddress;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostalAddressResource postalAddressResource = new PostalAddressResource(postalAddressService);
        this.restPostalAddressMockMvc = MockMvcBuilders.standaloneSetup(postalAddressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        postalAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostalAddress() throws Exception {
        int databaseSizeBeforeCreate = postalAddressRepository.findAll().size();

        // Create the PostalAddress
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);
        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isCreated());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PostalAddress testPostalAddress = postalAddressList.get(postalAddressList.size() - 1);
        assertThat(testPostalAddress.getAttention()).isEqualTo(DEFAULT_ATTENTION);
        assertThat(testPostalAddress.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testPostalAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPostalAddress.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testPostalAddress.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testPostalAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testPostalAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    public void createPostalAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postalAddressRepository.findAll().size();

        // Create the PostalAddress with an existing ID
        postalAddress.setId(TestConstants.UUID_1);
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = postalAddressRepository.findAll().size();
        // set the field null
        postalAddress.setAddress1(null);

        // Create the PostalAddress, which fails.
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = postalAddressRepository.findAll().size();
        // set the field null
        postalAddress.setCity(null);

        // Create the PostalAddress, which fails.
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = postalAddressRepository.findAll().size();
        // set the field null
        postalAddress.setStateProvince(null);

        // Create the PostalAddress, which fails.
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = postalAddressRepository.findAll().size();
        // set the field null
        postalAddress.setPostalCode(null);

        // Create the PostalAddress, which fails.
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = postalAddressRepository.findAll().size();
        // set the field null
        postalAddress.setCountry(null);

        // Create the PostalAddress, which fails.
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPostalAddresses() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList
        restPostalAddressMockMvc.perform(get("/api/postal-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalAddress.getId().toString())))
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
    public void getPostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        // Get the postalAddress
        restPostalAddressMockMvc.perform(get("/api/postal-addresses/{id}", postalAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(postalAddress.getId().toString()))
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
    public void getNonExistingPostalAddress() throws Exception {
        // Get the postalAddress
        restPostalAddressMockMvc.perform(get("/api/postal-addresses/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        int databaseSizeBeforeUpdate = postalAddressRepository.findAll().size();

        // Update the postalAddress
        PostalAddress updatedPostalAddress = postalAddressRepository.findById(postalAddress.getId()).get();
        // Disconnect from session so that the updates on updatedPostalAddress are not directly saved in db
        em.detach(updatedPostalAddress);
        updatedPostalAddress
            .attention(UPDATED_ATTENTION)
            .address1(UPDATED_ADDRESS_1)
            .city(UPDATED_CITY)
            .region(UPDATED_REGION)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY);
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(updatedPostalAddress);

        restPostalAddressMockMvc.perform(put("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isOk());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeUpdate);
        PostalAddress testPostalAddress = postalAddressList.get(postalAddressList.size() - 1);
        assertThat(testPostalAddress.getAttention()).isEqualTo(UPDATED_ATTENTION);
        assertThat(testPostalAddress.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testPostalAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPostalAddress.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testPostalAddress.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testPostalAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testPostalAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void updateNonExistingPostalAddress() throws Exception {
        int databaseSizeBeforeUpdate = postalAddressRepository.findAll().size();

        // Create the PostalAddress
        PostalAddressDTO postalAddressDTO = postalAddressMapper.toDto(postalAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc.perform(put("/api/postal-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        int databaseSizeBeforeDelete = postalAddressRepository.findAll().size();

        // Get the postalAddress
        restPostalAddressMockMvc.perform(delete("/api/postal-addresses/{id}", postalAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddress.class);
        PostalAddress postalAddress1 = new PostalAddress();
        postalAddress1.setId(TestConstants.UUID_1);
        PostalAddress postalAddress2 = new PostalAddress();
        postalAddress2.setId(postalAddress1.getId());
        assertThat(postalAddress1).isEqualTo(postalAddress2);
        postalAddress2.setId(TestConstants.UUID_2);
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);
        postalAddress1.setId(null);
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddressDTO.class);
        PostalAddressDTO postalAddressDTO1 = new PostalAddressDTO();
        postalAddressDTO1.setId(TestConstants.UUID_1);
        PostalAddressDTO postalAddressDTO2 = new PostalAddressDTO();
        assertThat(postalAddressDTO1).isNotEqualTo(postalAddressDTO2);
        postalAddressDTO2.setId(postalAddressDTO1.getId());
        assertThat(postalAddressDTO1).isEqualTo(postalAddressDTO2);
        postalAddressDTO2.setId(TestConstants.UUID_2);
        assertThat(postalAddressDTO1).isNotEqualTo(postalAddressDTO2);
        postalAddressDTO1.setId(null);
        assertThat(postalAddressDTO1).isNotEqualTo(postalAddressDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(postalAddressMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(postalAddressMapper.fromId(null)).isNull();
    }
}
