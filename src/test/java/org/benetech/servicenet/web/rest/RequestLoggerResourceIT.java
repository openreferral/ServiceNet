package org.benetech.servicenet.web.rest;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.TestConstants;
import org.benetech.servicenet.domain.RequestLogger;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.repository.RequestLoggerRepository;
import org.benetech.servicenet.service.RequestLoggerService;
import org.benetech.servicenet.service.dto.RequestLoggerDTO;
import org.benetech.servicenet.service.mapper.RequestLoggerMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

/**
 * Integration tests for the {@link RequestLoggerResource} REST controller.
 */
@SpringBootTest(classes = ServiceNetApp.class)
public class RequestLoggerResourceIT {

    private static final String DEFAULT_REQUEST_URI = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_URI = "BBBBBBBBBB";

    private static final String DEFAULT_REMOTE_ADDR = "AAAAAAAAAA";
    private static final String UPDATED_Remote_Addr = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_PARAMETERS = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_PARAMETERS = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_BODY = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_Request_Method = "AAAAAAAAAA";
    private static final String UPDATED_Request_Method = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    @Autowired
    private RequestLoggerRepository requestLoggerRepository;

    @Autowired
    private RequestLoggerMapper requestLoggerMapper;

    @Autowired
    private RequestLoggerService requestLoggerService;

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

    private MockMvc restRequestLoggerMockMvc;

    private RequestLogger requestLogger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequestLoggerResource requestLoggerResource = new RequestLoggerResource(requestLoggerService);
        this.restRequestLoggerMockMvc = MockMvcBuilders.standaloneSetup(requestLoggerResource)
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
    public static RequestLogger createEntity(EntityManager em) {
        RequestLogger requestLogger = new RequestLogger()
            .requestUri(DEFAULT_REQUEST_URI)
            .remoteAddr(DEFAULT_REMOTE_ADDR)
            .requestParameters(DEFAULT_REQUEST_PARAMETERS)
            .requestBody(DEFAULT_REQUEST_BODY)
            .responseStatus(DEFAULT_RESPONSE_STATUS)
            .requestMethod(DEFAULT_Request_Method)
            .responseBody(DEFAULT_RESPONSE_BODY);
        return requestLogger;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestLogger createUpdatedEntity(EntityManager em) {
        RequestLogger requestLogger = new RequestLogger()
            .requestUri(UPDATED_REQUEST_URI)
            .remoteAddr(UPDATED_Remote_Addr)
            .requestParameters(UPDATED_REQUEST_PARAMETERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .requestMethod(UPDATED_Request_Method)
            .responseBody(UPDATED_RESPONSE_BODY);
        return requestLogger;
    }

    @BeforeEach
    public void initTest() {
        requestLogger = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequestLogger() throws Exception {
        int databaseSizeBeforeCreate = requestLoggerRepository.findAll().size();

        // Create the RequestLogger
        RequestLoggerDTO requestLoggerDTO = requestLoggerMapper.toDto(requestLogger);
        restRequestLoggerMockMvc.perform(post("/api/request-loggers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestLoggerDTO)))
            .andExpect(status().isCreated());

        // Validate the RequestLogger in the database
        List<RequestLogger> requestLoggerList = requestLoggerRepository.findAll();
        assertThat(requestLoggerList).hasSize(databaseSizeBeforeCreate + 1);
        RequestLogger testRequestLogger = requestLoggerList.get(requestLoggerList.size() - 1);
        assertThat(testRequestLogger.getRequestUri()).isEqualTo(DEFAULT_REQUEST_URI);
        assertThat(testRequestLogger.getRemoteAddr()).isEqualTo(DEFAULT_REMOTE_ADDR);
        assertThat(testRequestLogger.getRequestParameters()).isEqualTo(DEFAULT_REQUEST_PARAMETERS);
        assertThat(testRequestLogger.getRequestBody()).isEqualTo(DEFAULT_REQUEST_BODY);
        assertThat(testRequestLogger.getResponseStatus()).isEqualTo(DEFAULT_RESPONSE_STATUS);
        assertThat(testRequestLogger.getRequestMethod()).isEqualTo(DEFAULT_Request_Method);
        assertThat(testRequestLogger.getResponseBody()).isEqualTo(DEFAULT_RESPONSE_BODY);
    }

    @Test
    @Transactional
    public void createRequestLoggerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requestLoggerRepository.findAll().size();

        // Create the RequestLogger with an existing ID
        requestLogger.setId(TestConstants.UUID_1);
        RequestLoggerDTO requestLoggerDTO = requestLoggerMapper.toDto(requestLogger);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestLoggerMockMvc.perform(post("/api/request-loggers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestLoggerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequestLogger in the database
        List<RequestLogger> requestLoggerList = requestLoggerRepository.findAll();
        assertThat(requestLoggerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequestLoggers() throws Exception {
        // Initialize the database
        requestLoggerRepository.saveAndFlush(requestLogger);

        // Get all the requestLoggerList
        restRequestLoggerMockMvc.perform(get("/api/request-loggers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestLogger.getId().toString())))
            .andExpect(jsonPath("$.[*].requestUri").value(hasItem(DEFAULT_REQUEST_URI.toString())))
            .andExpect(jsonPath("$.[*].remoteAddr").value(hasItem(DEFAULT_REMOTE_ADDR.toString())))
            .andExpect(jsonPath("$.[*].requestParameters").value(hasItem(DEFAULT_REQUEST_PARAMETERS.toString())))
            .andExpect(jsonPath("$.[*].requestBody").value(hasItem(DEFAULT_REQUEST_BODY.toString())))
            .andExpect(jsonPath("$.[*].responseStatus").value(hasItem(DEFAULT_RESPONSE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestMethod").value(hasItem(DEFAULT_Request_Method.toString())))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY.toString())));
    }
    
    @Test
    @Transactional
    public void getRequestLogger() throws Exception {
        // Initialize the database
        requestLoggerRepository.saveAndFlush(requestLogger);

        // Get the requestLogger
        restRequestLoggerMockMvc.perform(get("/api/request-loggers/{id}", requestLogger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requestLogger.getId().toString()))
            .andExpect(jsonPath("$.requestUri").value(DEFAULT_REQUEST_URI.toString()))
            .andExpect(jsonPath("$.remoteAddr").value(DEFAULT_REMOTE_ADDR.toString()))
            .andExpect(jsonPath("$.requestParameters").value(DEFAULT_REQUEST_PARAMETERS.toString()))
            .andExpect(jsonPath("$.requestBody").value(DEFAULT_REQUEST_BODY.toString()))
            .andExpect(jsonPath("$.responseStatus").value(DEFAULT_RESPONSE_STATUS.toString()))
            .andExpect(jsonPath("$.requestMethod").value(DEFAULT_Request_Method.toString()))
            .andExpect(jsonPath("$.responseBody").value(DEFAULT_RESPONSE_BODY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequestLogger() throws Exception {
        // Get the requestLogger
        restRequestLoggerMockMvc.perform(get("/api/request-loggers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequestLogger() throws Exception {
        // Initialize the database
        requestLoggerRepository.saveAndFlush(requestLogger);

        int databaseSizeBeforeUpdate = requestLoggerRepository.findAll().size();

        // Update the requestLogger
        RequestLogger updatedRequestLogger = requestLoggerRepository.findById(requestLogger.getId()).get();
        // Disconnect from session so that the updates on updatedRequestLogger are not directly saved in db
        em.detach(updatedRequestLogger);
        updatedRequestLogger
            .requestUri(UPDATED_REQUEST_URI)
            .remoteAddr(UPDATED_Remote_Addr)
            .requestParameters(UPDATED_REQUEST_PARAMETERS)
            .requestBody(UPDATED_REQUEST_BODY)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .requestMethod(UPDATED_Request_Method)
            .responseBody(UPDATED_RESPONSE_BODY);
        RequestLoggerDTO requestLoggerDTO = requestLoggerMapper.toDto(updatedRequestLogger);

        restRequestLoggerMockMvc.perform(put("/api/request-loggers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestLoggerDTO)))
            .andExpect(status().isOk());

        // Validate the RequestLogger in the database
        List<RequestLogger> requestLoggerList = requestLoggerRepository.findAll();
        assertThat(requestLoggerList).hasSize(databaseSizeBeforeUpdate);
        RequestLogger testRequestLogger = requestLoggerList.get(requestLoggerList.size() - 1);
        assertThat(testRequestLogger.getRequestUri()).isEqualTo(UPDATED_REQUEST_URI);
        assertThat(testRequestLogger.getRemoteAddr()).isEqualTo(UPDATED_Remote_Addr);
        assertThat(testRequestLogger.getRequestParameters()).isEqualTo(UPDATED_REQUEST_PARAMETERS);
        assertThat(testRequestLogger.getRequestBody()).isEqualTo(UPDATED_REQUEST_BODY);
        assertThat(testRequestLogger.getResponseStatus()).isEqualTo(UPDATED_RESPONSE_STATUS);
        assertThat(testRequestLogger.getRequestMethod()).isEqualTo(UPDATED_Request_Method);
        assertThat(testRequestLogger.getResponseBody()).isEqualTo(UPDATED_RESPONSE_BODY);
    }

    @Test
    @Transactional
    public void updateNonExistingRequestLogger() throws Exception {
        int databaseSizeBeforeUpdate = requestLoggerRepository.findAll().size();

        // Create the RequestLogger
        RequestLoggerDTO requestLoggerDTO = requestLoggerMapper.toDto(requestLogger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestLoggerMockMvc.perform(put("/api/request-loggers")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestLoggerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequestLogger in the database
        List<RequestLogger> requestLoggerList = requestLoggerRepository.findAll();
        assertThat(requestLoggerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequestLogger() throws Exception {
        // Initialize the database
        requestLoggerRepository.saveAndFlush(requestLogger);

        int databaseSizeBeforeDelete = requestLoggerRepository.findAll().size();

        // Delete the requestLogger
        restRequestLoggerMockMvc.perform(delete("/api/request-loggers/{id}", requestLogger.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequestLogger> requestLoggerList = requestLoggerRepository.findAll();
        assertThat(requestLoggerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestLogger.class);
        RequestLogger requestLogger1 = new RequestLogger();
        requestLogger1.setId(TestConstants.UUID_1);
        RequestLogger requestLogger2 = new RequestLogger();
        requestLogger2.setId(requestLogger1.getId());
        assertThat(requestLogger1).isEqualTo(requestLogger2);
        requestLogger2.setId(TestConstants.UUID_2);
        assertThat(requestLogger1).isNotEqualTo(requestLogger2);
        requestLogger1.setId(null);
        assertThat(requestLogger1).isNotEqualTo(requestLogger2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestLoggerDTO.class);
        RequestLoggerDTO requestLoggerDTO1 = new RequestLoggerDTO();
        requestLoggerDTO1.setId(TestConstants.UUID_1);
        RequestLoggerDTO requestLoggerDTO2 = new RequestLoggerDTO();
        assertThat(requestLoggerDTO1).isNotEqualTo(requestLoggerDTO2);
        requestLoggerDTO2.setId(requestLoggerDTO1.getId());
        assertThat(requestLoggerDTO1).isEqualTo(requestLoggerDTO2);
        requestLoggerDTO2.setId(TestConstants.UUID_2);
        assertThat(requestLoggerDTO1).isNotEqualTo(requestLoggerDTO2);
        requestLoggerDTO1.setId(null);
        assertThat(requestLoggerDTO1).isNotEqualTo(requestLoggerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(requestLoggerMapper.fromId(TestConstants.UUID_42).getId()).isEqualTo(TestConstants.UUID_42);
        assertThat(requestLoggerMapper.fromId(null)).isNull();
    }
}
