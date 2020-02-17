package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.ExclusionsConfigRepository;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.dto.ExclusionsConfigDTO;
import org.benetech.servicenet.service.mapper.ExclusionsConfigMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ExclusionsConfigResource REST controller.
 *
 * @see ExclusionsConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ExclusionsConfigResourceIntTest {

    @Autowired
    private ExclusionsConfigRepository exclusionsConfigRepository;

    @Autowired
    private ExclusionsConfigMapper exclusionsConfigMapper;

    @Autowired
    private ExclusionsConfigService exclusionsConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExclusionsConfigMockMvc;

    private ExclusionsConfig exclusionsConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExclusionsConfigResource exclusionsConfigResource = new ExclusionsConfigResource(exclusionsConfigService);
        this.restExclusionsConfigMockMvc = MockMvcBuilders.standaloneSetup(exclusionsConfigResource)
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
    public static ExclusionsConfig createEntity(EntityManager em) {
        ExclusionsConfig exclusionsConfig = new ExclusionsConfig();
        // Add required entity
        SystemAccount systemAccount = SystemAccountResourceIntTest.createEntity(em);
        em.persist(systemAccount);
        em.flush();
        exclusionsConfig.setAccount(systemAccount);
        return exclusionsConfig;
    }

    @Before
    public void initTest() {
        exclusionsConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createExclusionsConfig() throws Exception {
        int databaseSizeBeforeCreate = exclusionsConfigRepository.findAll().size();

        // Create the ExclusionsConfig
        ExclusionsConfigDTO exclusionsConfigDTO = exclusionsConfigMapper.toDto(exclusionsConfig);
        restExclusionsConfigMockMvc.perform(post("/api/exclusions-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exclusionsConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the ExclusionsConfig in the database
        List<ExclusionsConfig> exclusionsConfigList = exclusionsConfigRepository.findAll();
        assertThat(exclusionsConfigList).hasSize(databaseSizeBeforeCreate + 1);
        ExclusionsConfig testExclusionsConfig = exclusionsConfigList.get(exclusionsConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void createExclusionsConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exclusionsConfigRepository.findAll().size();

        // Create the ExclusionsConfig with an existing ID
        exclusionsConfig.setId(TestConstants.UUID_1);
        ExclusionsConfigDTO exclusionsConfigDTO = exclusionsConfigMapper.toDto(exclusionsConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExclusionsConfigMockMvc.perform(post("/api/exclusions-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exclusionsConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExclusionsConfig in the database
        List<ExclusionsConfig> exclusionsConfigList = exclusionsConfigRepository.findAll();
        assertThat(exclusionsConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExclusionsConfigs() throws Exception {
        // Initialize the database
        exclusionsConfigRepository.saveAndFlush(exclusionsConfig);

        // Get all the exclusionsConfigList
        restExclusionsConfigMockMvc.perform(get("/api/exclusions-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(exclusionsConfig.getId().toString()));
    }

    @Test
    @Transactional
    public void getExclusionsConfig() throws Exception {
        // Initialize the database
        exclusionsConfigRepository.saveAndFlush(exclusionsConfig);

        // Get the exclusionsConfig
        restExclusionsConfigMockMvc.perform(get("/api/exclusions-configs/{id}", exclusionsConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exclusionsConfig.getId().toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExclusionsConfig() throws Exception {
        // Get the exclusionsConfig
        restExclusionsConfigMockMvc.perform(get("/api/exclusions-configs/{id}", TestConstants.NON_EXISTING_UUID))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExclusionsConfig() throws Exception {
        // Initialize the database
        exclusionsConfigRepository.saveAndFlush(exclusionsConfig);

        int databaseSizeBeforeUpdate = exclusionsConfigRepository.findAll().size();

        // Update the exclusionsConfig
        ExclusionsConfig updatedExclusionsConfig = exclusionsConfigRepository.findById(exclusionsConfig.getId()).get();
        // Disconnect from session so that the updates on updatedExclusionsConfig are not directly saved in db
        em.detach(updatedExclusionsConfig);
        ExclusionsConfigDTO exclusionsConfigDTO = exclusionsConfigMapper.toDto(updatedExclusionsConfig);

        restExclusionsConfigMockMvc.perform(put("/api/exclusions-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exclusionsConfigDTO)))
            .andExpect(status().isOk());

        // Validate the ExclusionsConfig in the database
        List<ExclusionsConfig> exclusionsConfigList = exclusionsConfigRepository.findAll();
        assertThat(exclusionsConfigList).hasSize(databaseSizeBeforeUpdate);
        ExclusionsConfig testExclusionsConfig = exclusionsConfigList.get(exclusionsConfigList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingExclusionsConfig() throws Exception {
        int databaseSizeBeforeUpdate = exclusionsConfigRepository.findAll().size();

        // Create the ExclusionsConfig
        ExclusionsConfigDTO exclusionsConfigDTO = exclusionsConfigMapper.toDto(exclusionsConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExclusionsConfigMockMvc.perform(put("/api/exclusions-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exclusionsConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExclusionsConfig in the database
        List<ExclusionsConfig> exclusionsConfigList = exclusionsConfigRepository.findAll();
        assertThat(exclusionsConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExclusionsConfig() throws Exception {
        // Initialize the database
        exclusionsConfigRepository.saveAndFlush(exclusionsConfig);

        int databaseSizeBeforeDelete = exclusionsConfigRepository.findAll().size();

        // Get the exclusionsConfig
        restExclusionsConfigMockMvc.perform(delete("/api/exclusions-configs/{id}", exclusionsConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExclusionsConfig> exclusionsConfigList = exclusionsConfigRepository.findAll();
        assertThat(exclusionsConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExclusionsConfig.class);
        ExclusionsConfig exclusionsConfig1 = new ExclusionsConfig();
        exclusionsConfig1.setId(TestConstants.UUID_1);
        ExclusionsConfig exclusionsConfig2 = new ExclusionsConfig();
        exclusionsConfig2.setId(exclusionsConfig1.getId());
        assertThat(exclusionsConfig1).isEqualTo(exclusionsConfig2);
        exclusionsConfig2.setId(TestConstants.UUID_2);
        assertThat(exclusionsConfig1).isNotEqualTo(exclusionsConfig2);
        exclusionsConfig1.setId(null);
        assertThat(exclusionsConfig1).isNotEqualTo(exclusionsConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExclusionsConfigDTO.class);
        ExclusionsConfigDTO exclusionsConfigDTO1 = new ExclusionsConfigDTO();
        exclusionsConfigDTO1.setId(TestConstants.UUID_1);
        ExclusionsConfigDTO exclusionsConfigDTO2 = new ExclusionsConfigDTO();
        assertThat(exclusionsConfigDTO1).isNotEqualTo(exclusionsConfigDTO2);
        exclusionsConfigDTO2.setId(exclusionsConfigDTO1.getId());
        assertThat(exclusionsConfigDTO1).isEqualTo(exclusionsConfigDTO2);
        exclusionsConfigDTO2.setId(TestConstants.UUID_2);
        assertThat(exclusionsConfigDTO1).isNotEqualTo(exclusionsConfigDTO2);
        exclusionsConfigDTO1.setId(null);
        assertThat(exclusionsConfigDTO1).isNotEqualTo(exclusionsConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(exclusionsConfigMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(exclusionsConfigMapper.fromId(null)).isNull();
    }
}
