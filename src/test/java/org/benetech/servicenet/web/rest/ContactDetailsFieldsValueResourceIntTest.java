package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ContactDetailsFieldsValue;
import org.benetech.servicenet.repository.ContactDetailsFieldsValueRepository;
import org.benetech.servicenet.service.ContactDetailsFieldsValueService;
import org.benetech.servicenet.service.dto.ContactDetailsFieldsValueDTO;
import org.benetech.servicenet.service.mapper.ContactDetailsFieldsValueMapper;
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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.benetech.servicenet.domain.enumeration.ContactDetailsFields;
/**
 * Integration tests for the {@link ContactDetailsFieldsValueResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ContactDetailsFieldsValueResourceIntTest {

    private static final ContactDetailsFields DEFAULT_CONTACT_DETAILS_FIELD = ContactDetailsFields.NAME;
    private static final ContactDetailsFields UPDATED_CONTACT_DETAILS_FIELD = ContactDetailsFields.TITLE;

    @Autowired
    private ContactDetailsFieldsValueRepository contactDetailsFieldsValueRepository;

    @Autowired
    private ContactDetailsFieldsValueMapper contactDetailsFieldsValueMapper;

    @Autowired
    private ContactDetailsFieldsValueService contactDetailsFieldsValueService;

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

    private MockMvc restContactDetailsFieldsValueMockMvc;

    private ContactDetailsFieldsValue contactDetailsFieldsValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactDetailsFieldsValueResource contactDetailsFieldsValueResource = new ContactDetailsFieldsValueResource(contactDetailsFieldsValueService);
        this.restContactDetailsFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(contactDetailsFieldsValueResource)
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
    public static ContactDetailsFieldsValue createEntity(EntityManager em) {
        ContactDetailsFieldsValue contactDetailsFieldsValue = new ContactDetailsFieldsValue()
            .contactDetailsField(DEFAULT_CONTACT_DETAILS_FIELD);
        return contactDetailsFieldsValue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactDetailsFieldsValue createUpdatedEntity(EntityManager em) {
        ContactDetailsFieldsValue contactDetailsFieldsValue = new ContactDetailsFieldsValue()
            .contactDetailsField(UPDATED_CONTACT_DETAILS_FIELD);
        return contactDetailsFieldsValue;
    }

    @Before
    public void initTest() {
        contactDetailsFieldsValue = createEntity(em);
    }

    @Test
    @Transactional
    public void createContactDetailsFieldsValue() throws Exception {
        int databaseSizeBeforeCreate = contactDetailsFieldsValueRepository.findAll().size();

        // Create the ContactDetailsFieldsValue
        ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO = contactDetailsFieldsValueMapper.toDto(contactDetailsFieldsValue);
        restContactDetailsFieldsValueMockMvc.perform(post("/api/contact-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDetailsFieldsValueDTO)))
            .andExpect(status().isCreated());

        // Validate the ContactDetailsFieldsValue in the database
        List<ContactDetailsFieldsValue> contactDetailsFieldsValueList = contactDetailsFieldsValueRepository.findAll();
        assertThat(contactDetailsFieldsValueList).hasSize(databaseSizeBeforeCreate + 1);
        ContactDetailsFieldsValue testContactDetailsFieldsValue = contactDetailsFieldsValueList.get(contactDetailsFieldsValueList.size() - 1);
        assertThat(testContactDetailsFieldsValue.getContactDetailsField()).isEqualTo(DEFAULT_CONTACT_DETAILS_FIELD);
    }

    @Test
    @Transactional
    public void createContactDetailsFieldsValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactDetailsFieldsValueRepository.findAll().size();

        // Create the ContactDetailsFieldsValue with an existing ID
        contactDetailsFieldsValue.setId(TestConstants.UUID_1);
        ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO = contactDetailsFieldsValueMapper.toDto(contactDetailsFieldsValue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactDetailsFieldsValueMockMvc.perform(post("/api/contact-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDetailsFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactDetailsFieldsValue in the database
        List<ContactDetailsFieldsValue> contactDetailsFieldsValueList = contactDetailsFieldsValueRepository.findAll();
        assertThat(contactDetailsFieldsValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContactDetailsFieldsValues() throws Exception {
        // Initialize the database
        contactDetailsFieldsValueRepository.saveAndFlush(contactDetailsFieldsValue);

        // Get all the contactDetailsFieldsValueList
        restContactDetailsFieldsValueMockMvc.perform(get("/api/contact-details-fields-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactDetailsFieldsValue.getId().toString())))
            .andExpect(jsonPath("$.[*].contactDetailsField").value(hasItem(DEFAULT_CONTACT_DETAILS_FIELD.toString())));
    }

    @Test
    @Transactional
    public void getContactDetailsFieldsValue() throws Exception {
        // Initialize the database
        contactDetailsFieldsValueRepository.saveAndFlush(contactDetailsFieldsValue);

        // Get the contactDetailsFieldsValue
        restContactDetailsFieldsValueMockMvc.perform(get("/api/contact-details-fields-values/{id}", contactDetailsFieldsValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactDetailsFieldsValue.getId().toString()))
            .andExpect(jsonPath("$.contactDetailsField").value(DEFAULT_CONTACT_DETAILS_FIELD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContactDetailsFieldsValue() throws Exception {
        // Get the contactDetailsFieldsValue
        restContactDetailsFieldsValueMockMvc.perform(get("/api/contact-details-fields-values/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContactDetailsFieldsValue() throws Exception {
        // Initialize the database
        contactDetailsFieldsValueRepository.saveAndFlush(contactDetailsFieldsValue);

        int databaseSizeBeforeUpdate = contactDetailsFieldsValueRepository.findAll().size();

        // Update the contactDetailsFieldsValue
        ContactDetailsFieldsValue updatedContactDetailsFieldsValue = contactDetailsFieldsValueRepository.findById(contactDetailsFieldsValue.getId()).get();
        // Disconnect from session so that the updates on updatedContactDetailsFieldsValue are not directly saved in db
        em.detach(updatedContactDetailsFieldsValue);
        updatedContactDetailsFieldsValue
            .contactDetailsField(UPDATED_CONTACT_DETAILS_FIELD);
        ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO = contactDetailsFieldsValueMapper.toDto(updatedContactDetailsFieldsValue);

        restContactDetailsFieldsValueMockMvc.perform(put("/api/contact-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDetailsFieldsValueDTO)))
            .andExpect(status().isOk());

        // Validate the ContactDetailsFieldsValue in the database
        List<ContactDetailsFieldsValue> contactDetailsFieldsValueList = contactDetailsFieldsValueRepository.findAll();
        assertThat(contactDetailsFieldsValueList).hasSize(databaseSizeBeforeUpdate);
        ContactDetailsFieldsValue testContactDetailsFieldsValue = contactDetailsFieldsValueList.get(contactDetailsFieldsValueList.size() - 1);
        assertThat(testContactDetailsFieldsValue.getContactDetailsField()).isEqualTo(UPDATED_CONTACT_DETAILS_FIELD);
    }

    @Test
    @Transactional
    public void updateNonExistingContactDetailsFieldsValue() throws Exception {
        int databaseSizeBeforeUpdate = contactDetailsFieldsValueRepository.findAll().size();

        // Create the ContactDetailsFieldsValue
        ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO = contactDetailsFieldsValueMapper.toDto(contactDetailsFieldsValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactDetailsFieldsValueMockMvc.perform(put("/api/contact-details-fields-values")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contactDetailsFieldsValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContactDetailsFieldsValue in the database
        List<ContactDetailsFieldsValue> contactDetailsFieldsValueList = contactDetailsFieldsValueRepository.findAll();
        assertThat(contactDetailsFieldsValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContactDetailsFieldsValue() throws Exception {
        // Initialize the database
        contactDetailsFieldsValueRepository.saveAndFlush(contactDetailsFieldsValue);

        int databaseSizeBeforeDelete = contactDetailsFieldsValueRepository.findAll().size();

        // Delete the contactDetailsFieldsValue
        restContactDetailsFieldsValueMockMvc.perform(delete("/api/contact-details-fields-values/{id}", contactDetailsFieldsValue.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactDetailsFieldsValue> contactDetailsFieldsValueList = contactDetailsFieldsValueRepository.findAll();
        assertThat(contactDetailsFieldsValueList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactDetailsFieldsValue.class);
        ContactDetailsFieldsValue contactDetailsFieldsValue1 = new ContactDetailsFieldsValue();
        contactDetailsFieldsValue1.setId(TestConstants.UUID_1);
        ContactDetailsFieldsValue contactDetailsFieldsValue2 = new ContactDetailsFieldsValue();
        contactDetailsFieldsValue2.setId(contactDetailsFieldsValue1.getId());
        assertThat(contactDetailsFieldsValue1).isEqualTo(contactDetailsFieldsValue2);
        contactDetailsFieldsValue2.setId(TestConstants.UUID_2);
        assertThat(contactDetailsFieldsValue1).isNotEqualTo(contactDetailsFieldsValue2);
        contactDetailsFieldsValue1.setId(null);
        assertThat(contactDetailsFieldsValue1).isNotEqualTo(contactDetailsFieldsValue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactDetailsFieldsValueDTO.class);
        ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO1 = new ContactDetailsFieldsValueDTO();
        contactDetailsFieldsValueDTO1.setId(TestConstants.UUID_1);
        ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO2 = new ContactDetailsFieldsValueDTO();
        assertThat(contactDetailsFieldsValueDTO1).isNotEqualTo(contactDetailsFieldsValueDTO2);
        contactDetailsFieldsValueDTO2.setId(contactDetailsFieldsValueDTO1.getId());
        assertThat(contactDetailsFieldsValueDTO1).isEqualTo(contactDetailsFieldsValueDTO2);
        contactDetailsFieldsValueDTO2.setId(TestConstants.UUID_2);
        assertThat(contactDetailsFieldsValueDTO1).isNotEqualTo(contactDetailsFieldsValueDTO2);
        contactDetailsFieldsValueDTO1.setId(null);
        assertThat(contactDetailsFieldsValueDTO1).isNotEqualTo(contactDetailsFieldsValueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contactDetailsFieldsValueMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(contactDetailsFieldsValueMapper.fromId(null)).isNull();
    }
}
