package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.MockedUserTestConfiguration;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.Funding;
import org.benetech.servicenet.repository.FundingRepository;
import org.benetech.servicenet.service.FundingService;
import org.benetech.servicenet.service.dto.FundingDTO;
import org.benetech.servicenet.service.mapper.FundingMapper;
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
 * Test class for the FundingResource REST controller.
 *
 * @see FundingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ServiceNetApp.class, MockedUserTestConfiguration.class})
public class FundingResourceIntTest {

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    @Autowired
    private FundingRepository fundingRepository;

    @Autowired
    private FundingMapper fundingMapper;

    @Autowired
    private FundingService fundingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFundingMockMvc;

    private Funding funding;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Funding createEntity(EntityManager em) {
        Funding funding = new Funding()
            .source(DEFAULT_SOURCE);
        return funding;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FundingResource fundingResource = new FundingResource(fundingService);
        this.restFundingMockMvc = MockMvcBuilders.standaloneSetup(fundingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        funding = createEntity(em);
    }

    @Test
    @Transactional
    public void createFunding() throws Exception {
        int databaseSizeBeforeCreate = fundingRepository.findAll().size();

        // Create the Funding
        FundingDTO fundingDTO = fundingMapper.toDto(funding);
        restFundingMockMvc.perform(post("/api/fundings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
            .andExpect(status().isCreated());

        // Validate the Funding in the database
        List<Funding> fundingList = fundingRepository.findAll();
        assertThat(fundingList).hasSize(databaseSizeBeforeCreate + 1);
        Funding testFunding = fundingList.get(fundingList.size() - 1);
        assertThat(testFunding.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void createFundingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fundingRepository.findAll().size();

        // Create the Funding with an existing ID
        funding.setId(TestConstants.UUID_1);
        FundingDTO fundingDTO = fundingMapper.toDto(funding);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFundingMockMvc.perform(post("/api/fundings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Funding in the database
        List<Funding> fundingList = fundingRepository.findAll();
        assertThat(fundingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = fundingRepository.findAll().size();
        // set the field null
        funding.setSource(null);

        // Create the Funding, which fails.
        FundingDTO fundingDTO = fundingMapper.toDto(funding);

        restFundingMockMvc.perform(post("/api/fundings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
            .andExpect(status().isBadRequest());

        List<Funding> fundingList = fundingRepository.findAll();
        assertThat(fundingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFundings() throws Exception {
        // Initialize the database
        fundingRepository.saveAndFlush(funding);

        // Get all the fundingList
        restFundingMockMvc.perform(get("/api/fundings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funding.getId().toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    public void getFunding() throws Exception {
        // Initialize the database
        fundingRepository.saveAndFlush(funding);

        // Get the funding
        restFundingMockMvc.perform(get("/api/fundings/{id}", funding.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(funding.getId().toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFunding() throws Exception {
        // Get the funding
        restFundingMockMvc.perform(get("/api/fundings/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFunding() throws Exception {
        // Initialize the database
        fundingRepository.saveAndFlush(funding);

        int databaseSizeBeforeUpdate = fundingRepository.findAll().size();

        // Update the funding
        Funding updatedFunding = fundingRepository.findById(funding.getId()).get();
        // Disconnect from session so that the updates on updatedFunding are not directly saved in db
        em.detach(updatedFunding);
        updatedFunding
            .source(UPDATED_SOURCE);
        FundingDTO fundingDTO = fundingMapper.toDto(updatedFunding);

        restFundingMockMvc.perform(put("/api/fundings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
            .andExpect(status().isOk());

        // Validate the Funding in the database
        List<Funding> fundingList = fundingRepository.findAll();
        assertThat(fundingList).hasSize(databaseSizeBeforeUpdate);
        Funding testFunding = fundingList.get(fundingList.size() - 1);
        assertThat(testFunding.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void updateNonExistingFunding() throws Exception {
        int databaseSizeBeforeUpdate = fundingRepository.findAll().size();

        // Create the Funding
        FundingDTO fundingDTO = fundingMapper.toDto(funding);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFundingMockMvc.perform(put("/api/fundings")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Funding in the database
        List<Funding> fundingList = fundingRepository.findAll();
        assertThat(fundingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFunding() throws Exception {
        // Initialize the database
        fundingRepository.saveAndFlush(funding);

        int databaseSizeBeforeDelete = fundingRepository.findAll().size();

        // Get the funding
        restFundingMockMvc.perform(delete("/api/fundings/{id}", funding.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Funding> fundingList = fundingRepository.findAll();
        assertThat(fundingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Funding.class);
        Funding funding1 = new Funding();
        funding1.setId(TestConstants.UUID_1);
        Funding funding2 = new Funding();
        funding2.setId(funding1.getId());
        assertThat(funding1).isEqualTo(funding2);
        funding2.setId(TestConstants.UUID_2);
        assertThat(funding1).isNotEqualTo(funding2);
        funding1.setId(null);
        assertThat(funding1).isNotEqualTo(funding2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FundingDTO.class);
        FundingDTO fundingDTO1 = new FundingDTO();
        fundingDTO1.setId(TestConstants.UUID_1);
        FundingDTO fundingDTO2 = new FundingDTO();
        assertThat(fundingDTO1).isNotEqualTo(fundingDTO2);
        fundingDTO2.setId(fundingDTO1.getId());
        assertThat(fundingDTO1).isEqualTo(fundingDTO2);
        fundingDTO2.setId(TestConstants.UUID_2);
        assertThat(fundingDTO1).isNotEqualTo(fundingDTO2);
        fundingDTO1.setId(null);
        assertThat(fundingDTO1).isNotEqualTo(fundingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fundingMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(fundingMapper.fromId(null)).isNull();
    }
}
