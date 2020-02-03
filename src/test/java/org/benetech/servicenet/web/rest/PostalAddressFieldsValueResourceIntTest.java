package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.PostalAddressFieldsValue;
import org.benetech.servicenet.repository.PostalAddressFieldsValueRepository;
import org.benetech.servicenet.service.PostalAddressFieldsValueService;
import org.benetech.servicenet.service.dto.PostalAddressFieldsValueDTO;
import org.benetech.servicenet.service.mapper.PostalAddressFieldsValueMapper;
import org.benetech.servicenet.web.rest.errors.ExceptionTranslator;

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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.benetech.servicenet.domain.enumeration.PostalAddressFields;
/**
 * Integration tests for the {@link PostalAddressFieldsValueResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class PostalAddressFieldsValueResourceIntTest {

    private static final PostalAddressFields DEFAULT_POSTAL_ADDRESS_FIELD = PostalAddressFields.ATTENTION;
    private static final PostalAddressFields UPDATED_POSTAL_ADDRESS_FIELD = PostalAddressFields.ADDRESS1;

    @Autowired
    private PostalAddressFieldsValueRepository postalAddressFieldsValueRepository;

    @Autowired
    private PostalAddressFieldsValueMapper postalAddressFieldsValueMapper;

    @Autowired
    private PostalAddressFieldsValueService postalAddressFieldsValueService;

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

    private MockMvc restPostalAddressFieldsValueMockMvc;

    private PostalAddressFieldsValue postalAddressFieldsValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostalAddressFieldsValueResource postalAddressFieldsValueResource = new PostalAddressFieldsValueResource(postalAddressFieldsValueService);
        this.restPostalAddressFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(postalAddressFieldsValueResource)
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
    public static PostalAddressFieldsValue createEntity(EntityManager em) {
        PostalAddressFieldsValue postalAddressFieldsValue = new PostalAddressFieldsValue()
            .postalAddressField(DEFAULT_POSTAL_ADDRESS_FIELD);
        return postalAddressFieldsValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostalAddressFieldsValue createUpdatedEntity(EntityManager em) {
        PostalAddressFieldsValue postalAddressFieldsValue = new PostalAddressFieldsValue()
            .postalAddressField(UPDATED_POSTAL_ADDRESS_FIELD);
        return postalAddressFieldsValue;
    }

    @Before
    public void initTest() {
        postalAddressFieldsValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostalAddressFieldsValue() throws Exception {
        int databaseSizeBeforeCreate = postalAddressFieldsValueRepository.findAll().size();

        // Create the PostalAddressFieldsValue
        PostalAddressFieldsValueDTO postalAddressFieldsValueDTO = postalAddressFieldsValueMapper.toDto(postalAddressFieldsValue);
        restPostalAddressFieldsValueMockMvc.perform(post("/api/postal-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressFieldsValueDTO)))
            .andExpect(status().isCreated());

        // Validate the PostalAddressFieldsValue in the database
        List<PostalAddressFieldsValue> postalAddressFieldsValueList = postalAddressFieldsValueRepository.findAll();
        assertThat(postalAddressFieldsValueList).hasSize(databaseSizeBeforeCreate + 1);
        PostalAddressFieldsValue testPostalAddressFieldsValue = postalAddressFieldsValueList.get(postalAddressFieldsValueList.size() - 1);
        assertThat(testPostalAddressFieldsValue.getPostalAddressField()).isEqualTo(DEFAULT_POSTAL_ADDRESS_FIELD);
    }

    @Test
    @Transactional
    public void createPostalAddressFieldsValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postalAddressFieldsValueRepository.findAll().size();

        // Create the PostalAddressFieldsValue with an existing ID
        postalAddressFieldsValue.setId(TestConstants.UUID_1);
        PostalAddressFieldsValueDTO postalAddressFieldsValueDTO = postalAddressFieldsValueMapper.toDto(postalAddressFieldsValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostalAddressFieldsValueMockMvc.perform(post("/api/postal-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostalAddressFieldsValue in the database
        List<PostalAddressFieldsValue> postalAddressFieldsValueList = postalAddressFieldsValueRepository.findAll();
        assertThat(postalAddressFieldsValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPostalAddressFieldsValues() throws Exception {
        // Initialize the database
        postalAddressFieldsValueRepository.saveAndFlush(postalAddressFieldsValue);

        // Get all the postalAddressFieldsValueList
        restPostalAddressFieldsValueMockMvc.perform(get("/api/postal-address-fields-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalAddressFieldsValue.getId().toString())))
            .andExpect(jsonPath("$.[*].postalAddressField").value(hasItem(DEFAULT_POSTAL_ADDRESS_FIELD.toString())));
    }
    
    @Test
    @Transactional
    public void getPostalAddressFieldsValue() throws Exception {
        // Initialize the database
        postalAddressFieldsValueRepository.saveAndFlush(postalAddressFieldsValue);

        // Get the postalAddressFieldsValue
        restPostalAddressFieldsValueMockMvc.perform(get("/api/postal-address-fields-values/{id}", postalAddressFieldsValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(postalAddressFieldsValue.getId().toString()))
            .andExpect(jsonPath("$.postalAddressField").value(DEFAULT_POSTAL_ADDRESS_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPostalAddressFieldsValue() throws Exception {
        // Get the postalAddressFieldsValue
        restPostalAddressFieldsValueMockMvc.perform(get("/api/postal-address-fields-values/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostalAddressFieldsValue() throws Exception {
        // Initialize the database
        postalAddressFieldsValueRepository.saveAndFlush(postalAddressFieldsValue);

        int databaseSizeBeforeUpdate = postalAddressFieldsValueRepository.findAll().size();

        // Update the postalAddressFieldsValue
        PostalAddressFieldsValue updatedPostalAddressFieldsValue = postalAddressFieldsValueRepository.findById(postalAddressFieldsValue.getId()).get();
        // Disconnect from session so that the updates on updatedPostalAddressFieldsValue are not directly saved in db
        em.detach(updatedPostalAddressFieldsValue);
        updatedPostalAddressFieldsValue
            .postalAddressField(UPDATED_POSTAL_ADDRESS_FIELD);
        PostalAddressFieldsValueDTO postalAddressFieldsValueDTO = postalAddressFieldsValueMapper.toDto(updatedPostalAddressFieldsValue);

        restPostalAddressFieldsValueMockMvc.perform(put("/api/postal-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressFieldsValueDTO)))
            .andExpect(status().isOk());

        // Validate the PostalAddressFieldsValue in the database
        List<PostalAddressFieldsValue> postalAddressFieldsValueList = postalAddressFieldsValueRepository.findAll();
        assertThat(postalAddressFieldsValueList).hasSize(databaseSizeBeforeUpdate);
        PostalAddressFieldsValue testPostalAddressFieldsValue = postalAddressFieldsValueList.get(postalAddressFieldsValueList.size() - 1);
        assertThat(testPostalAddressFieldsValue.getPostalAddressField()).isEqualTo(UPDATED_POSTAL_ADDRESS_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingPostalAddressFieldsValue() throws Exception {
        int databaseSizeBeforeUpdate = postalAddressFieldsValueRepository.findAll().size();

        // Create the PostalAddressFieldsValue
        PostalAddressFieldsValueDTO postalAddressFieldsValueDTO = postalAddressFieldsValueMapper.toDto(postalAddressFieldsValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostalAddressFieldsValueMockMvc.perform(put("/api/postal-address-fields-values")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalAddressFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostalAddressFieldsValue in the database
        List<PostalAddressFieldsValue> postalAddressFieldsValueList = postalAddressFieldsValueRepository.findAll();
        assertThat(postalAddressFieldsValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePostalAddressFieldsValue() throws Exception {
        // Initialize the database
        postalAddressFieldsValueRepository.saveAndFlush(postalAddressFieldsValue);

        int databaseSizeBeforeDelete = postalAddressFieldsValueRepository.findAll().size();

        // Delete the postalAddressFieldsValue
        restPostalAddressFieldsValueMockMvc.perform(delete("/api/postal-address-fields-values/{id}", postalAddressFieldsValue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostalAddressFieldsValue> postalAddressFieldsValueList = postalAddressFieldsValueRepository.findAll();
        assertThat(postalAddressFieldsValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddressFieldsValue.class);
        PostalAddressFieldsValue postalAddressFieldsValue1 = new PostalAddressFieldsValue();
        postalAddressFieldsValue1.setId(TestConstants.UUID_1);
        PostalAddressFieldsValue postalAddressFieldsValue2 = new PostalAddressFieldsValue();
        postalAddressFieldsValue2.setId(postalAddressFieldsValue1.getId());
        assertThat(postalAddressFieldsValue1).isEqualTo(postalAddressFieldsValue2);
        postalAddressFieldsValue2.setId(TestConstants.UUID_2);
        assertThat(postalAddressFieldsValue1).isNotEqualTo(postalAddressFieldsValue2);
        postalAddressFieldsValue1.setId(null);
        assertThat(postalAddressFieldsValue1).isNotEqualTo(postalAddressFieldsValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddressFieldsValueDTO.class);
        PostalAddressFieldsValueDTO postalAddressFieldsValueDTO1 = new PostalAddressFieldsValueDTO();
        postalAddressFieldsValueDTO1.setId(TestConstants.UUID_1);
        PostalAddressFieldsValueDTO postalAddressFieldsValueDTO2 = new PostalAddressFieldsValueDTO();
        assertThat(postalAddressFieldsValueDTO1).isNotEqualTo(postalAddressFieldsValueDTO2);
        postalAddressFieldsValueDTO2.setId(postalAddressFieldsValueDTO1.getId());
        assertThat(postalAddressFieldsValueDTO1).isEqualTo(postalAddressFieldsValueDTO2);
        postalAddressFieldsValueDTO2.setId(TestConstants.UUID_2);
        assertThat(postalAddressFieldsValueDTO1).isNotEqualTo(postalAddressFieldsValueDTO2);
        postalAddressFieldsValueDTO1.setId(null);
        assertThat(postalAddressFieldsValueDTO1).isNotEqualTo(postalAddressFieldsValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(postalAddressFieldsValueMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(postalAddressFieldsValueMapper.fromId(null)).isNull();
    }
}
