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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.ActivityFilter;
import org.benetech.servicenet.domain.enumeration.DateFilter;
import org.benetech.servicenet.repository.ActivityFilterRepository;
import org.benetech.servicenet.service.ActivityFilterService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.benetech.servicenet.service.mapper.ActivityFilterMapper;
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

/**
 * Integration tests for the {@Link ActivityFilterResource} REST controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ActivityFilterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final List<String> DEFAULT_CITIES_FILTER_LIST = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_CITIES_FILTER_LIST = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_REGION_FILTER_LIST = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_REGION_FILTER_LIST = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_POSTAL_CODES_FILTER_LIST = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_POSTAL_CODES_FILTER_LIST = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_TAXONOMIES_FILTER_LIST = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_TAXONOMIES_FILTER_LIST = Collections.singletonList("BBBBBBBBBB");

    private static final List<String> DEFAULT_SEARCH_FIELDS = Collections.singletonList("AAAAAAAAAA");
    private static final List<String> UPDATED_SEARCH_FIELDS = Collections.singletonList("BBBBBBBBBB");

    private static final List<UUID> DEFAULT_PARTNER_FILTER_LIST = Collections.singletonList(UUID.randomUUID());
    private static final List<UUID> UPDATED_PARTNER_FILTER_LIST = Collections.singletonList(UUID.randomUUID());

    private static final DateFilter DEFAULT_DATE_FILTER = DateFilter.LAST_7_DAYS;
    private static final DateFilter UPDATED_DATE_FILTER = DateFilter.LAST_30_DAYS;

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_HIDDEN_FILTER = false;
    private static final Boolean UPDATED_HIDDEN_FILTER = true;

    private static final Boolean DEFAULT_SHOW_HIGHLY_MATCHED_FILTER = true;
    private static final Boolean UPDATED_SHOW_HIGHLY_MATCHED_FILTER = false;

    private static final UUID ID = UUID.randomUUID();

    @Autowired
    private ActivityFilterRepository activityFilterRepository;

    @Autowired
    private ActivityFilterMapper activityFilterMapper;

    @Autowired
    private ActivityFilterService activityFilterService;

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

    @Autowired
    private UserService userService;

    private MockMvc restActivityFilterMockMvc;

    private ActivityFilter activityFilter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityFilterResource activityFilterResource = new ActivityFilterResource(activityFilterService, userService);
        this.restActivityFilterMockMvc = MockMvcBuilders.standaloneSetup(activityFilterResource)
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
    public static ActivityFilter createEntity(EntityManager em) {
        ActivityFilter activityFilter = new ActivityFilter()
            .name(DEFAULT_NAME)
            .citiesFilterList(DEFAULT_CITIES_FILTER_LIST)
            .regionFilterList(DEFAULT_REGION_FILTER_LIST)
            .postalCodesFilterList(DEFAULT_POSTAL_CODES_FILTER_LIST)
            .taxonomiesFilterList(DEFAULT_TAXONOMIES_FILTER_LIST)
            .searchFields(DEFAULT_SEARCH_FIELDS)
            .partnerFilterList(DEFAULT_PARTNER_FILTER_LIST)
            .dateFilter(DEFAULT_DATE_FILTER)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .hiddenFilter(DEFAULT_HIDDEN_FILTER)
            .showOnlyHighlyMatched(DEFAULT_SHOW_HIGHLY_MATCHED_FILTER);
        return activityFilter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityFilter createUpdatedEntity(EntityManager em) {
        ActivityFilter activityFilter = new ActivityFilter()
            .name(UPDATED_NAME)
            .citiesFilterList(UPDATED_CITIES_FILTER_LIST)
            .regionFilterList(UPDATED_REGION_FILTER_LIST)
            .postalCodesFilterList(UPDATED_POSTAL_CODES_FILTER_LIST)
            .taxonomiesFilterList(UPDATED_TAXONOMIES_FILTER_LIST)
            .searchFields(UPDATED_SEARCH_FIELDS)
            .partnerFilterList(UPDATED_PARTNER_FILTER_LIST)
            .dateFilter(UPDATED_DATE_FILTER)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .hiddenFilter(UPDATED_HIDDEN_FILTER).
            showOnlyHighlyMatched(UPDATED_SHOW_HIGHLY_MATCHED_FILTER);
        return activityFilter;
    }

    @Before
    public void initTest() {
        activityFilter = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityFilter() throws Exception {
        int databaseSizeBeforeCreate = activityFilterRepository.findAll().size();

        // Create the ActivityFilter
        ActivityFilterDTO activityFilterDTO = activityFilterMapper.toDto(activityFilter);
        restActivityFilterMockMvc.perform(post("/api/activity-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityFilterDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityFilter in the database
        List<ActivityFilter> activityFilterList = activityFilterRepository.findAll();
        assertThat(activityFilterList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityFilter testActivityFilter = activityFilterList.get(activityFilterList.size() - 1);
        assertThat(testActivityFilter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActivityFilter.getDateFilter()).isEqualTo(DEFAULT_DATE_FILTER);
        assertThat(testActivityFilter.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testActivityFilter.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testActivityFilter.isHiddenFilter()).isEqualTo(DEFAULT_HIDDEN_FILTER);
        assertThat(testActivityFilter.isShowOnlyHighlyMatched()).isEqualTo(DEFAULT_SHOW_HIGHLY_MATCHED_FILTER);
    }

    @Test
    @Transactional
    public void createActivityFilterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityFilterRepository.findAll().size();

        // Create the ActivityFilter with an existing ID
        activityFilter.setId(ID);
        ActivityFilterDTO activityFilterDTO = activityFilterMapper.toDto(activityFilter);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityFilterMockMvc.perform(post("/api/activity-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityFilterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityFilter in the database
        List<ActivityFilter> activityFilterList = activityFilterRepository.findAll();
        assertThat(activityFilterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActivityFilters() throws Exception {
        // Initialize the database
        activityFilterRepository.saveAndFlush(activityFilter);

        // Get all the activityFilterList
        restActivityFilterMockMvc.perform(get("/api/activity-filters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityFilter.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].citiesFilterList").value(hasItem(DEFAULT_CITIES_FILTER_LIST)))
            .andExpect(jsonPath("$.[*].regionFilterList").value(hasItem(DEFAULT_REGION_FILTER_LIST)))
            .andExpect(jsonPath("$.[*].postalCodesFilterList").value(hasItem(DEFAULT_POSTAL_CODES_FILTER_LIST)))
            .andExpect(jsonPath("$.[*].taxonomiesFilterList").value(hasItem(DEFAULT_TAXONOMIES_FILTER_LIST)))
            .andExpect(jsonPath("$.[*].searchFields").value(hasItem(DEFAULT_SEARCH_FIELDS)))
            .andExpect(jsonPath("$.[*].partnerFilterList")
                .value(hasItem(DEFAULT_PARTNER_FILTER_LIST.stream().map(UUID::toString).collect(Collectors.toList()))))
            .andExpect(jsonPath("$.[*].dateFilter").value(hasItem(DEFAULT_DATE_FILTER.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].hiddenFilter").value(hasItem(DEFAULT_HIDDEN_FILTER.booleanValue())))
            .andExpect(jsonPath("$.[*].showOnlyHighlyMatched").value(hasItem(DEFAULT_SHOW_HIGHLY_MATCHED_FILTER)));
    }

    @Test
    @Transactional
    public void getActivityFilter() throws Exception {
        // Initialize the database
        activityFilterRepository.saveAndFlush(activityFilter);

        // Get the activityFilter
        restActivityFilterMockMvc.perform(get("/api/activity-filters/{id}", activityFilter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityFilter.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dateFilter").value(DEFAULT_DATE_FILTER.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.hiddenFilter").value(DEFAULT_HIDDEN_FILTER.booleanValue()))
            .andExpect(jsonPath("$.showOnlyHighlyMatched").value(DEFAULT_SHOW_HIGHLY_MATCHED_FILTER));
    }

    @Test
    @Transactional
    public void getNonExistingActivityFilter() throws Exception {
        // Get the activityFilter
        restActivityFilterMockMvc.perform(get("/api/activity-filters/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityFilter() throws Exception {
        // Initialize the database
        activityFilterRepository.saveAndFlush(activityFilter);

        int databaseSizeBeforeUpdate = activityFilterRepository.findAll().size();

        // Update the activityFilter
        ActivityFilter updatedActivityFilter = activityFilterRepository.findById(activityFilter.getId()).get();
        // Disconnect from session so that the updates on updatedActivityFilter are not directly saved in db
        em.detach(updatedActivityFilter);
        updatedActivityFilter
            .name(UPDATED_NAME)
            .citiesFilterList(UPDATED_CITIES_FILTER_LIST)
            .regionFilterList(UPDATED_REGION_FILTER_LIST)
            .postalCodesFilterList(UPDATED_POSTAL_CODES_FILTER_LIST)
            .taxonomiesFilterList(UPDATED_TAXONOMIES_FILTER_LIST)
            .searchFields(UPDATED_SEARCH_FIELDS)
            .partnerFilterList(UPDATED_PARTNER_FILTER_LIST)
            .dateFilter(UPDATED_DATE_FILTER)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .hiddenFilter(UPDATED_HIDDEN_FILTER)
            .showOnlyHighlyMatched(UPDATED_SHOW_HIGHLY_MATCHED_FILTER);

        ActivityFilterDTO activityFilterDTO = activityFilterMapper.toDto(updatedActivityFilter);

        restActivityFilterMockMvc.perform(put("/api/activity-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityFilterDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityFilter in the database
        List<ActivityFilter> activityFilterList = activityFilterRepository.findAll();
        assertThat(activityFilterList).hasSize(databaseSizeBeforeUpdate);
        ActivityFilter testActivityFilter = activityFilterList.get(activityFilterList.size() - 1);
        assertThat(testActivityFilter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActivityFilter.getDateFilter()).isEqualTo(UPDATED_DATE_FILTER);
        assertThat(testActivityFilter.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testActivityFilter.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testActivityFilter.isHiddenFilter()).isEqualTo(UPDATED_HIDDEN_FILTER);
        assertThat(testActivityFilter.isShowOnlyHighlyMatched()).isEqualTo(UPDATED_SHOW_HIGHLY_MATCHED_FILTER);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityFilter() throws Exception {
        int databaseSizeBeforeUpdate = activityFilterRepository.findAll().size();

        // Create the ActivityFilter
        ActivityFilterDTO activityFilterDTO = activityFilterMapper.toDto(activityFilter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityFilterMockMvc.perform(put("/api/activity-filters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityFilterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityFilter in the database
        List<ActivityFilter> activityFilterList = activityFilterRepository.findAll();
        assertThat(activityFilterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivityFilter() throws Exception {
        // Initialize the database
        activityFilterRepository.saveAndFlush(activityFilter);

        int databaseSizeBeforeDelete = activityFilterRepository.findAll().size();

        // Delete the activityFilter
        restActivityFilterMockMvc.perform(delete("/api/activity-filters/{id}", activityFilter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActivityFilter> activityFilterList = activityFilterRepository.findAll();
        assertThat(activityFilterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityFilter.class);
        ActivityFilter activityFilter1 = new ActivityFilter();
        activityFilter1.setId(UUID.randomUUID());
        ActivityFilter activityFilter2 = new ActivityFilter();
        activityFilter2.setId(activityFilter1.getId());
        assertThat(activityFilter1).isEqualTo(activityFilter2);
        activityFilter2.setId(UUID.randomUUID());
        assertThat(activityFilter1).isNotEqualTo(activityFilter2);
        activityFilter1.setId(null);
        assertThat(activityFilter1).isNotEqualTo(activityFilter2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityFilterDTO.class);
        ActivityFilterDTO activityFilterDTO1 = new ActivityFilterDTO();
        activityFilterDTO1.setId(UUID.randomUUID());
        ActivityFilterDTO activityFilterDTO2 = new ActivityFilterDTO();
        assertThat(activityFilterDTO1).isNotEqualTo(activityFilterDTO2);
        activityFilterDTO2.setId(activityFilterDTO1.getId());
        assertThat(activityFilterDTO1).isEqualTo(activityFilterDTO2);
        activityFilterDTO2.setId(UUID.randomUUID());
        assertThat(activityFilterDTO1).isNotEqualTo(activityFilterDTO2);
        activityFilterDTO1.setId(null);
        assertThat(activityFilterDTO1).isNotEqualTo(activityFilterDTO2);
    }
}
