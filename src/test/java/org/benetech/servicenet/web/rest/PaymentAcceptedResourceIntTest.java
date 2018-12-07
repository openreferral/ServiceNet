package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.PaymentAccepted;
import org.benetech.servicenet.repository.PaymentAcceptedRepository;
import org.benetech.servicenet.service.PaymentAcceptedService;
import org.benetech.servicenet.service.dto.PaymentAcceptedDTO;
import org.benetech.servicenet.service.mapper.PaymentAcceptedMapper;
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
 * Test class for the PaymentAcceptedResource REST controller.
 *
 * @see PaymentAcceptedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class PaymentAcceptedResourceIntTest {

    private static final String DEFAULT_PAYMENT = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT = "BBBBBBBBBB";

    @Autowired
    private PaymentAcceptedRepository paymentAcceptedRepository;

    @Autowired
    private PaymentAcceptedMapper paymentAcceptedMapper;

    @Autowired
    private PaymentAcceptedService paymentAcceptedService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentAcceptedMockMvc;

    private PaymentAccepted paymentAccepted;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentAccepted createEntity(EntityManager em) {
        PaymentAccepted paymentAccepted = new PaymentAccepted()
            .payment(DEFAULT_PAYMENT);
        return paymentAccepted;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentAcceptedResource paymentAcceptedResource = new PaymentAcceptedResource(paymentAcceptedService);
        this.restPaymentAcceptedMockMvc = MockMvcBuilders.standaloneSetup(paymentAcceptedResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        paymentAccepted = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentAccepted() throws Exception {
        int databaseSizeBeforeCreate = paymentAcceptedRepository.findAll().size();

        // Create the PaymentAccepted
        PaymentAcceptedDTO paymentAcceptedDTO = paymentAcceptedMapper.toDto(paymentAccepted);
        restPaymentAcceptedMockMvc.perform(post("/api/payment-accepteds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAcceptedDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentAccepted in the database
        List<PaymentAccepted> paymentAcceptedList = paymentAcceptedRepository.findAll();
        assertThat(paymentAcceptedList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentAccepted testPaymentAccepted = paymentAcceptedList.get(paymentAcceptedList.size() - 1);
        assertThat(testPaymentAccepted.getPayment()).isEqualTo(DEFAULT_PAYMENT);
    }

    @Test
    @Transactional
    public void createPaymentAcceptedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentAcceptedRepository.findAll().size();

        // Create the PaymentAccepted with an existing ID
        paymentAccepted.setId(TestConstants.UUID_1);
        PaymentAcceptedDTO paymentAcceptedDTO = paymentAcceptedMapper.toDto(paymentAccepted);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentAcceptedMockMvc.perform(post("/api/payment-accepteds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAcceptedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentAccepted in the database
        List<PaymentAccepted> paymentAcceptedList = paymentAcceptedRepository.findAll();
        assertThat(paymentAcceptedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPaymentAccepteds() throws Exception {
        // Initialize the database
        paymentAcceptedRepository.saveAndFlush(paymentAccepted);

        // Get all the paymentAcceptedList
        restPaymentAcceptedMockMvc.perform(get("/api/payment-accepteds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentAccepted.getId().toString())))
            .andExpect(jsonPath("$.[*].payment").value(hasItem(DEFAULT_PAYMENT.toString())));
    }

    @Test
    @Transactional
    public void getPaymentAccepted() throws Exception {
        // Initialize the database
        paymentAcceptedRepository.saveAndFlush(paymentAccepted);

        // Get the paymentAccepted
        restPaymentAcceptedMockMvc.perform(get("/api/payment-accepteds/{id}", paymentAccepted.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentAccepted.getId().toString()))
            .andExpect(jsonPath("$.payment").value(DEFAULT_PAYMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentAccepted() throws Exception {
        // Get the paymentAccepted
        restPaymentAcceptedMockMvc.perform(get("/api/payment-accepteds/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentAccepted() throws Exception {
        // Initialize the database
        paymentAcceptedRepository.saveAndFlush(paymentAccepted);

        int databaseSizeBeforeUpdate = paymentAcceptedRepository.findAll().size();

        // Update the paymentAccepted
        PaymentAccepted updatedPaymentAccepted = paymentAcceptedRepository.findById(paymentAccepted.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentAccepted are not directly saved in db
        em.detach(updatedPaymentAccepted);
        updatedPaymentAccepted
            .payment(UPDATED_PAYMENT);
        PaymentAcceptedDTO paymentAcceptedDTO = paymentAcceptedMapper.toDto(updatedPaymentAccepted);

        restPaymentAcceptedMockMvc.perform(put("/api/payment-accepteds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAcceptedDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentAccepted in the database
        List<PaymentAccepted> paymentAcceptedList = paymentAcceptedRepository.findAll();
        assertThat(paymentAcceptedList).hasSize(databaseSizeBeforeUpdate);
        PaymentAccepted testPaymentAccepted = paymentAcceptedList.get(paymentAcceptedList.size() - 1);
        assertThat(testPaymentAccepted.getPayment()).isEqualTo(UPDATED_PAYMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentAccepted() throws Exception {
        int databaseSizeBeforeUpdate = paymentAcceptedRepository.findAll().size();

        // Create the PaymentAccepted
        PaymentAcceptedDTO paymentAcceptedDTO = paymentAcceptedMapper.toDto(paymentAccepted);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentAcceptedMockMvc.perform(put("/api/payment-accepteds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAcceptedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentAccepted in the database
        List<PaymentAccepted> paymentAcceptedList = paymentAcceptedRepository.findAll();
        assertThat(paymentAcceptedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentAccepted() throws Exception {
        // Initialize the database
        paymentAcceptedRepository.saveAndFlush(paymentAccepted);

        int databaseSizeBeforeDelete = paymentAcceptedRepository.findAll().size();

        // Get the paymentAccepted
        restPaymentAcceptedMockMvc.perform(delete("/api/payment-accepteds/{id}", paymentAccepted.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PaymentAccepted> paymentAcceptedList = paymentAcceptedRepository.findAll();
        assertThat(paymentAcceptedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentAccepted.class);
        PaymentAccepted paymentAccepted1 = new PaymentAccepted();
        paymentAccepted1.setId(TestConstants.UUID_1);
        PaymentAccepted paymentAccepted2 = new PaymentAccepted();
        paymentAccepted2.setId(paymentAccepted1.getId());
        assertThat(paymentAccepted1).isEqualTo(paymentAccepted2);
        paymentAccepted2.setId(TestConstants.UUID_2);
        assertThat(paymentAccepted1).isNotEqualTo(paymentAccepted2);
        paymentAccepted1.setId(null);
        assertThat(paymentAccepted1).isNotEqualTo(paymentAccepted2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentAcceptedDTO.class);
        PaymentAcceptedDTO paymentAcceptedDTO1 = new PaymentAcceptedDTO();
        paymentAcceptedDTO1.setId(TestConstants.UUID_1);
        PaymentAcceptedDTO paymentAcceptedDTO2 = new PaymentAcceptedDTO();
        assertThat(paymentAcceptedDTO1).isNotEqualTo(paymentAcceptedDTO2);
        paymentAcceptedDTO2.setId(paymentAcceptedDTO1.getId());
        assertThat(paymentAcceptedDTO1).isEqualTo(paymentAcceptedDTO2);
        paymentAcceptedDTO2.setId(TestConstants.UUID_2);
        assertThat(paymentAcceptedDTO1).isNotEqualTo(paymentAcceptedDTO2);
        paymentAcceptedDTO1.setId(null);
        assertThat(paymentAcceptedDTO1).isNotEqualTo(paymentAcceptedDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentAcceptedMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(paymentAcceptedMapper.fromId(null)).isNull();
    }
}
