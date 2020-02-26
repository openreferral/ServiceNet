package org.benetech.servicenet.web.rest;

import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceMatch;
import org.benetech.servicenet.repository.ServiceMatchRepository;
import org.benetech.servicenet.service.ServiceMatchService;
import org.benetech.servicenet.service.dto.ServiceMatchDto;
import org.benetech.servicenet.service.mapper.ServiceMatchMapper;
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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ServiceMatchResourceTest {

    private final UUID SERVICE_UUID = UUID.randomUUID();

    private final UUID MATCHING_SERVICE_UUID = UUID.randomUUID();

    @Autowired
    private ServiceMatchService serviceMatchService;

    @Autowired
    private ServiceMatchMapper serviceMatchMapper;

    private MockMvc restServiceFieldsValueMockMvc;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceMatchResource serviceMatchResource = new ServiceMatchResource(serviceMatchService);
        this.restServiceFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(serviceMatchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @Test
    @Transactional
    public void shouldSaveServiceMatchProperly() throws Exception {
        ServiceMatchDto serviceMatchDto = serviceMatchMapper.toDto(this.createServiceMatch());

        int databaseSizeBeforeSave = serviceMatchService.findAll().size();

        restServiceFieldsValueMockMvc.perform(post("/api/service-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMatchDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.service").value(SERVICE_UUID.toString()))
            .andExpect(jsonPath("$.matchingService").value(MATCHING_SERVICE_UUID.toString()));

        int databaseSizeAfterSave = serviceMatchService.findAll().size();

        assertThat(databaseSizeAfterSave).isEqualTo(databaseSizeBeforeSave + 1);
    }

    @Test
    @Transactional
    public void shouldNotSaveServiceMatchWhenServiceMatchWithSameIdsExists() throws Exception {
        ServiceMatchDto serviceMatchDto = serviceMatchMapper.toDto(this.createServiceMatch());

        int databaseSizeBeforeSave = serviceMatchService.findAll().size();

        restServiceFieldsValueMockMvc.perform(post("/api/service-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMatchDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.service").value(SERVICE_UUID.toString()))
            .andExpect(jsonPath("$.matchingService").value(MATCHING_SERVICE_UUID.toString()));

        int databaseSizeAfterSave = serviceMatchService.findAll().size();

        restServiceFieldsValueMockMvc.perform(post("/api/service-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMatchDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.service").value(SERVICE_UUID.toString()))
            .andExpect(jsonPath("$.matchingService").value(MATCHING_SERVICE_UUID.toString()));

        int databaseSizeAfterSecondSave = serviceMatchService.findAll().size();

        assertThat(databaseSizeAfterSave).isEqualTo(databaseSizeBeforeSave + 1);
        assertThat(databaseSizeAfterSecondSave).isEqualTo(databaseSizeAfterSave);
    }

    @Test
    @Transactional
    public void shouldDeleteServiceMatch() throws Exception {
        ServiceMatch serviceMatch = this.createServiceMatch();
        ServiceMatchDto serviceMatchDto = serviceMatchMapper.toDto(serviceMatch);

        serviceMatchService.save(serviceMatchDto);
        int databaseSizeBeforeDelete = serviceMatchService.findAll().size();

        restServiceFieldsValueMockMvc.perform(delete("/api/service-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMatchDto))
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        int databaseSizeAfterDelete = serviceMatchService.findAll().size();

        assertThat(databaseSizeAfterDelete).isEqualTo(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void shouldNotDeleteNotExistingServiceMatch() throws Exception {
        ServiceMatch serviceMatch = this.createServiceMatch();
        ServiceMatchDto serviceMatchDto = serviceMatchMapper.toDto(serviceMatch);
        serviceMatchService.save(serviceMatchDto);
        int databaseSizeBeforeDelete = serviceMatchService.findAll().size();

        Service service = new Service();
        service.setId(SERVICE_UUID);
        Service matchingService = new Service();
        matchingService.setId(MATCHING_SERVICE_UUID);
        ServiceMatch serviceMatch1 = new ServiceMatch();
        serviceMatch.setService(service);
        serviceMatch.setMatchingService(matchingService);
        ServiceMatchDto serviceMatchDto1 = serviceMatchMapper.toDto(serviceMatch1);

        restServiceFieldsValueMockMvc.perform(delete("/api/service-matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceMatchDto1))
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isInternalServerError());

        int databaseSizeAfterDelete = serviceMatchService.findAll().size();

        assertThat(databaseSizeBeforeDelete).isEqualTo(databaseSizeAfterDelete);
    }

    private ServiceMatch createServiceMatch() {
        Service service = new Service();
        service.setId(SERVICE_UUID);
        Service matchingService = new Service();
        matchingService.setId(MATCHING_SERVICE_UUID);
        ServiceMatch serviceMatch = new ServiceMatch();
        serviceMatch.setService(service);
        serviceMatch.setMatchingService(matchingService);
        return serviceMatch;
    }
}
