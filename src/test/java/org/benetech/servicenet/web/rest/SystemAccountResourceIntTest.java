package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.interceptor.HibernateInterceptor;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.service.SystemAccountService;
import org.benetech.servicenet.service.dto.SystemAccountDTO;
import org.benetech.servicenet.service.mapper.SystemAccountMapper;
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
 * Test class for the SystemAccountResource REST controller.
 *
 * @see SystemAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class SystemAccountResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private HibernateInterceptor hibernateInterceptor;

    @Autowired
    private SystemAccountRepository systemAccountRepository;

    @Autowired
    private SystemAccountMapper systemAccountMapper;

    @Autowired
    private SystemAccountService systemAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSystemAccountMockMvc;

    private SystemAccount systemAccount;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemAccount createEntity(EntityManager em) {
        SystemAccount systemAccount = new SystemAccount()
            .name(DEFAULT_NAME);
        return systemAccount;
    }

    @Before
    public void setup() {
        hibernateInterceptor.disableEventListeners();
        MockitoAnnotations.initMocks(this);
        final SystemAccountResource systemAccountResource = new SystemAccountResource(systemAccountService);
        this.restSystemAccountMockMvc = MockMvcBuilders.standaloneSetup(systemAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        systemAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemAccount() throws Exception {
        int databaseSizeBeforeCreate = systemAccountRepository.findAll().size();

        // Create the SystemAccount
        SystemAccountDTO systemAccountDTO = systemAccountMapper.toDto(systemAccount);
        restSystemAccountMockMvc.perform(post("/api/system-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the SystemAccount in the database
        List<SystemAccount> systemAccountList = systemAccountRepository.findAll();
        assertThat(systemAccountList).hasSize(databaseSizeBeforeCreate + 1);
        SystemAccount testSystemAccount = systemAccountList.get(systemAccountList.size() - 1);
        assertThat(testSystemAccount.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSystemAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemAccountRepository.findAll().size();

        // Create the SystemAccount with an existing ID
        systemAccount.setId(TestConstants.UUID_1);
        SystemAccountDTO systemAccountDTO = systemAccountMapper.toDto(systemAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemAccountMockMvc.perform(post("/api/system-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemAccount in the database
        List<SystemAccount> systemAccountList = systemAccountRepository.findAll();
        assertThat(systemAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemAccountRepository.findAll().size();
        // set the field null
        systemAccount.setName(null);

        // Create the SystemAccount, which fails.
        SystemAccountDTO systemAccountDTO = systemAccountMapper.toDto(systemAccount);

        restSystemAccountMockMvc.perform(post("/api/system-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemAccountDTO)))
            .andExpect(status().isBadRequest());

        List<SystemAccount> systemAccountList = systemAccountRepository.findAll();
        assertThat(systemAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSystemAccounts() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(systemAccount);

        // Get all the systemAccountList
        restSystemAccountMockMvc.perform(get("/api/system-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemAccount.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSystemAccount() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(systemAccount);

        // Get the systemAccount
        restSystemAccountMockMvc.perform(get("/api/system-accounts/{id}", systemAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemAccount.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSystemAccount() throws Exception {
        // Get the systemAccount
        restSystemAccountMockMvc.perform(get("/api/system-accounts/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemAccount() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(systemAccount);

        int databaseSizeBeforeUpdate = systemAccountRepository.findAll().size();

        // Update the systemAccount
        SystemAccount updatedSystemAccount = systemAccountRepository.findById(systemAccount.getId()).get();
        // Disconnect from session so that the updates on updatedSystemAccount are not directly saved in db
        em.detach(updatedSystemAccount);
        updatedSystemAccount
            .name(UPDATED_NAME);
        SystemAccountDTO systemAccountDTO = systemAccountMapper.toDto(updatedSystemAccount);

        restSystemAccountMockMvc.perform(put("/api/system-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemAccountDTO)))
            .andExpect(status().isOk());

        // Validate the SystemAccount in the database
        List<SystemAccount> systemAccountList = systemAccountRepository.findAll();
        assertThat(systemAccountList).hasSize(databaseSizeBeforeUpdate);
        SystemAccount testSystemAccount = systemAccountList.get(systemAccountList.size() - 1);
        assertThat(testSystemAccount.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemAccount() throws Exception {
        int databaseSizeBeforeUpdate = systemAccountRepository.findAll().size();

        // Create the SystemAccount
        SystemAccountDTO systemAccountDTO = systemAccountMapper.toDto(systemAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemAccountMockMvc.perform(put("/api/system-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemAccount in the database
        List<SystemAccount> systemAccountList = systemAccountRepository.findAll();
        assertThat(systemAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSystemAccount() throws Exception {
        // Initialize the database
        systemAccountRepository.saveAndFlush(systemAccount);

        int databaseSizeBeforeDelete = systemAccountRepository.findAll().size();

        // Get the systemAccount
        restSystemAccountMockMvc.perform(delete("/api/system-accounts/{id}", systemAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SystemAccount> systemAccountList = systemAccountRepository.findAll();
        assertThat(systemAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemAccount.class);
        SystemAccount systemAccount1 = new SystemAccount();
        systemAccount1.setId(TestConstants.UUID_1);
        SystemAccount systemAccount2 = new SystemAccount();
        systemAccount2.setId(systemAccount1.getId());
        assertThat(systemAccount1).isEqualTo(systemAccount2);
        systemAccount2.setId(TestConstants.UUID_2);
        assertThat(systemAccount1).isNotEqualTo(systemAccount2);
        systemAccount1.setId(null);
        assertThat(systemAccount1).isNotEqualTo(systemAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemAccountDTO.class);
        SystemAccountDTO systemAccountDTO1 = new SystemAccountDTO();
        systemAccountDTO1.setId(TestConstants.UUID_1);
        SystemAccountDTO systemAccountDTO2 = new SystemAccountDTO();
        assertThat(systemAccountDTO1).isNotEqualTo(systemAccountDTO2);
        systemAccountDTO2.setId(systemAccountDTO1.getId());
        assertThat(systemAccountDTO1).isEqualTo(systemAccountDTO2);
        systemAccountDTO2.setId(TestConstants.UUID_2);
        assertThat(systemAccountDTO1).isNotEqualTo(systemAccountDTO2);
        systemAccountDTO1.setId(null);
        assertThat(systemAccountDTO1).isNotEqualTo(systemAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(systemAccountMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(systemAccountMapper.fromId(null)).isNull();
    }
}
