package org.benetech.servicenet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.repository.ShelterRepository;
import org.benetech.servicenet.service.ShelterService;
import org.benetech.servicenet.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.benetech.servicenet.ZeroCodeSpringJUnit4Runner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

/**
 * Test class for the ShelterResource REST controller.
 *
 * @see ShelterResource
 */
@RunWith(ZeroCodeSpringJUnit4Runner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class ShelterResourceIntTest {

    private static final String DEFAULT_AGENCY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AGENCY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_ELIGIBILITY_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ELIGIBILITY_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENTS_REQUIRED = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENTS_REQUIRED = "BBBBBBBBBB";

    private static final String DEFAULT_APPLICATION_PROCESS = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_PROCESS = "BBBBBBBBBB";

    private static final String DEFAULT_FEES = "AAAAAAAAAA";
    private static final String UPDATED_FEES = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAM_HOURS = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_HOURS = "BBBBBBBBBB";

    private static final String DEFAULT_HOLIDAY_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_HOLIDAY_SCHEDULE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BUS_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_BUS_SERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSPORTATION = "AAAAAAAAAA";
    private static final String UPDATED_TRANSPORTATION = "BBBBBBBBBB";

    private static final String DEFAULT_DISABILITY_ACCESS = "AAAAAAAAAA";
    private static final String UPDATED_DISABILITY_ACCESS = "BBBBBBBBBB";

    private static UUID id = UUID.randomUUID();

    @Autowired
    private ShelterRepository shelterRepository;

    @Mock
    private ShelterRepository shelterRepositoryMock;

    @Mock
    private ShelterService shelterServiceMock;

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private UserService userService;

    @Mock
    private UserService userServiceMock;

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

    private MockMvc restShelterMockMvc;

    private Shelter shelter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ShelterResource shelterResource = new ShelterResource(shelterService, userService);
        this.restShelterMockMvc = MockMvcBuilders.standaloneSetup(shelterResource)
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
    public static Shelter createEntity(EntityManager em) {
        Shelter shelter = new Shelter()
            .agencyName(DEFAULT_AGENCY_NAME)
            .programName(DEFAULT_PROGRAM_NAME)
            .alternateName(DEFAULT_ALTERNATE_NAME)
            .website(DEFAULT_WEBSITE)
            .eligibilityDetails(DEFAULT_ELIGIBILITY_DETAILS)
            .documentsRequired(DEFAULT_DOCUMENTS_REQUIRED)
            .applicationProcess(DEFAULT_APPLICATION_PROCESS)
            .fees(DEFAULT_FEES)
            .programHours(DEFAULT_PROGRAM_HOURS)
            .holidaySchedule(DEFAULT_HOLIDAY_SCHEDULE)
            .emails(Collections.singletonList(DEFAULT_EMAIL))
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .city(DEFAULT_CITY)
            .zipcode(DEFAULT_ZIPCODE)
            .locationDescription(DEFAULT_LOCATION_DESCRIPTION)
            .busService(DEFAULT_BUS_SERVICE)
            .transportation(DEFAULT_TRANSPORTATION)
            .disabilityAccess(DEFAULT_DISABILITY_ACCESS);
        shelter.setUserProfiles(new HashSet<>());
        return shelter;
    }

    @Before
    public void initTest() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
        SecurityContextHolder.setContext(securityContext);

        shelter = createEntity(em);
    }

    @Test
    @Transactional
    public void createShelter() throws Exception {
        int databaseSizeBeforeCreate = shelterRepository.findAll().size();

        // Create the Shelter
        restShelterMockMvc.perform(post("/api/shelters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shelter)))
            .andExpect(status().isCreated());

        // Validate the Shelter in the database
        List<Shelter> shelterList = shelterRepository.findAll();
        assertThat(shelterList).hasSize(databaseSizeBeforeCreate + 1);
        Shelter testShelter = shelterList.get(shelterList.size() - 1);
        assertThat(testShelter.getAgencyName()).isEqualTo(DEFAULT_AGENCY_NAME);
        assertThat(testShelter.getProgramName()).isEqualTo(DEFAULT_PROGRAM_NAME);
        assertThat(testShelter.getAlternateName()).isEqualTo(DEFAULT_ALTERNATE_NAME);
        assertThat(testShelter.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testShelter.getEligibilityDetails()).isEqualTo(DEFAULT_ELIGIBILITY_DETAILS);
        assertThat(testShelter.getDocumentsRequired()).isEqualTo(DEFAULT_DOCUMENTS_REQUIRED);
        assertThat(testShelter.getApplicationProcess()).isEqualTo(DEFAULT_APPLICATION_PROCESS);
        assertThat(testShelter.getFees()).isEqualTo(DEFAULT_FEES);
        assertThat(testShelter.getProgramHours()).isEqualTo(DEFAULT_PROGRAM_HOURS);
        assertThat(testShelter.getHolidaySchedule()).isEqualTo(DEFAULT_HOLIDAY_SCHEDULE);
        assertThat(testShelter.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testShelter.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testShelter.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testShelter.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testShelter.getLocationDescription()).isEqualTo(DEFAULT_LOCATION_DESCRIPTION);
        assertThat(testShelter.getBusService()).isEqualTo(DEFAULT_BUS_SERVICE);
        assertThat(testShelter.getTransportation()).isEqualTo(DEFAULT_TRANSPORTATION);
        assertThat(testShelter.getDisabilityAccess()).isEqualTo(DEFAULT_DISABILITY_ACCESS);
    }

    @Test
    @Transactional
    public void createShelterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shelterRepository.findAll().size();

        // Create the Shelter with an existing ID
        shelter.setId(id);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShelterMockMvc.perform(post("/api/shelters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shelter)))
            .andExpect(status().isBadRequest());

        // Validate the Shelter in the database
        List<Shelter> shelterList = shelterRepository.findAll();
        assertThat(shelterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllShelters() throws Exception {
        // Initialize the database
        shelterRepository.saveAndFlush(shelter);

        // Get all the shelterList
        restShelterMockMvc.perform(get("/api/shelters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shelter.getId().toString())))
            .andExpect(jsonPath("$.[*].agencyName").value(hasItem(DEFAULT_AGENCY_NAME.toString())))
            .andExpect(jsonPath("$.[*].programName").value(hasItem(DEFAULT_PROGRAM_NAME.toString())))
            .andExpect(jsonPath("$.[*].alternateName").value(hasItem(DEFAULT_ALTERNATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].eligibilityDetails").value(hasItem(DEFAULT_ELIGIBILITY_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].documentsRequired").value(hasItem(DEFAULT_DOCUMENTS_REQUIRED.toString())))
            .andExpect(jsonPath("$.[*].applicationProcess").value(hasItem(DEFAULT_APPLICATION_PROCESS.toString())))
            .andExpect(jsonPath("$.[*].fees").value(hasItem(DEFAULT_FEES.toString())))
            .andExpect(jsonPath("$.[*].programHours").value(hasItem(DEFAULT_PROGRAM_HOURS.toString())))
            .andExpect(jsonPath("$.[*].holidaySchedule").value(hasItem(DEFAULT_HOLIDAY_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].emails").value(hasItem(Collections.singletonList(DEFAULT_EMAIL.toString()))))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
            .andExpect(jsonPath("$.[*].locationDescription").value(hasItem(DEFAULT_LOCATION_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].busService").value(hasItem(DEFAULT_BUS_SERVICE.toString())))
            .andExpect(jsonPath("$.[*].transportation").value(hasItem(DEFAULT_TRANSPORTATION.toString())))
            .andExpect(jsonPath("$.[*].disabilityAccess").value(hasItem(DEFAULT_DISABILITY_ACCESS.toString())));
    }

    @SuppressWarnings({"unchecked", "CPD-START"})
    public void getAllSheltersWithEagerRelationshipsIsEnabled() throws Exception {
        ShelterResource shelterResource = new ShelterResource(shelterServiceMock, userServiceMock);
        when(shelterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restSTMockMvc = MockMvcBuilders.standaloneSetup(shelterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSTMockMvc.perform(get("/api/shelters?eagerload=true"))
        .andExpect(status().isOk());

        verify(shelterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllSheltersWithEagerRelationshipsIsNotEnabled() throws Exception {
        ShelterResource shelterResource = new ShelterResource(shelterServiceMock, userServiceMock);
        when(shelterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restSTMockMvc = MockMvcBuilders.standaloneSetup(shelterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSTMockMvc.perform(get("/api/shelters?eagerload=true"))
        .andExpect(status().isOk());

        verify(shelterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings("CPD-END")
    @Test
    @Transactional
    public void getShelter() throws Exception {
        // Initialize the database
        shelterRepository.saveAndFlush(shelter);

        // Get the shelter
        restShelterMockMvc.perform(get("/api/shelters/{id}", shelter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shelter.getId().toString()))
            .andExpect(jsonPath("$.agencyName").value(DEFAULT_AGENCY_NAME.toString()))
            .andExpect(jsonPath("$.programName").value(DEFAULT_PROGRAM_NAME.toString()))
            .andExpect(jsonPath("$.alternateName").value(DEFAULT_ALTERNATE_NAME.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.eligibilityDetails").value(DEFAULT_ELIGIBILITY_DETAILS.toString()))
            .andExpect(jsonPath("$.documentsRequired").value(DEFAULT_DOCUMENTS_REQUIRED.toString()))
            .andExpect(jsonPath("$.applicationProcess").value(DEFAULT_APPLICATION_PROCESS.toString()))
            .andExpect(jsonPath("$.fees").value(DEFAULT_FEES.toString()))
            .andExpect(jsonPath("$.programHours").value(DEFAULT_PROGRAM_HOURS.toString()))
            .andExpect(jsonPath("$.holidaySchedule").value(DEFAULT_HOLIDAY_SCHEDULE.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE.toString()))
            .andExpect(jsonPath("$.locationDescription").value(DEFAULT_LOCATION_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.busService").value(DEFAULT_BUS_SERVICE.toString()))
            .andExpect(jsonPath("$.transportation").value(DEFAULT_TRANSPORTATION.toString()))
            .andExpect(jsonPath("$.disabilityAccess").value(DEFAULT_DISABILITY_ACCESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShelter() throws Exception {
        // Get the shelter
        restShelterMockMvc.perform(get("/api/shelters/{id}", UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShelter() throws Exception {
        // Initialize the database
        shelterRepository.save(shelter);

        int databaseSizeBeforeUpdate = shelterRepository.findAll().size();

        // Update the shelter
        Shelter updatedShelter = shelterRepository.findById(shelter.getId()).get();

        // Add shelter to the current user so he can update it
        UserProfile currentUser = userService.getCurrentUserProfile();
        Set<Shelter> shelters = new HashSet<>();
        shelters.add(updatedShelter);
        currentUser.setShelters(shelters);
        userService.saveProfile(currentUser);

        // Disconnect from session so that the updates on updatedShelter are not directly saved in db
        em.detach(updatedShelter);
        updatedShelter
            .agencyName(UPDATED_AGENCY_NAME)
            .programName(UPDATED_PROGRAM_NAME)
            .alternateName(UPDATED_ALTERNATE_NAME)
            .website(UPDATED_WEBSITE)
            .eligibilityDetails(UPDATED_ELIGIBILITY_DETAILS)
            .documentsRequired(UPDATED_DOCUMENTS_REQUIRED)
            .applicationProcess(UPDATED_APPLICATION_PROCESS)
            .fees(UPDATED_FEES)
            .programHours(UPDATED_PROGRAM_HOURS)
            .holidaySchedule(UPDATED_HOLIDAY_SCHEDULE)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .city(UPDATED_CITY)
            .zipcode(UPDATED_ZIPCODE)
            .locationDescription(UPDATED_LOCATION_DESCRIPTION)
            .busService(UPDATED_BUS_SERVICE)
            .transportation(UPDATED_TRANSPORTATION)
            .disabilityAccess(UPDATED_DISABILITY_ACCESS);

        restShelterMockMvc.perform(put("/api/shelters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedShelter)))
            .andExpect(status().isOk());

        // Validate the Shelter in the database
        List<Shelter> shelterList = shelterRepository.findAll();
        assertThat(shelterList).hasSize(databaseSizeBeforeUpdate);
        Shelter testShelter = shelterList.get(shelterList.size() - 1);
        assertThat(testShelter.getAgencyName()).isEqualTo(UPDATED_AGENCY_NAME);
        assertThat(testShelter.getProgramName()).isEqualTo(UPDATED_PROGRAM_NAME);
        assertThat(testShelter.getAlternateName()).isEqualTo(UPDATED_ALTERNATE_NAME);
        assertThat(testShelter.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testShelter.getEligibilityDetails()).isEqualTo(UPDATED_ELIGIBILITY_DETAILS);
        assertThat(testShelter.getDocumentsRequired()).isEqualTo(UPDATED_DOCUMENTS_REQUIRED);
        assertThat(testShelter.getApplicationProcess()).isEqualTo(UPDATED_APPLICATION_PROCESS);
        assertThat(testShelter.getFees()).isEqualTo(UPDATED_FEES);
        assertThat(testShelter.getProgramHours()).isEqualTo(UPDATED_PROGRAM_HOURS);
        assertThat(testShelter.getHolidaySchedule()).isEqualTo(UPDATED_HOLIDAY_SCHEDULE);
        assertThat(testShelter.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testShelter.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testShelter.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testShelter.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testShelter.getLocationDescription()).isEqualTo(UPDATED_LOCATION_DESCRIPTION);
        assertThat(testShelter.getBusService()).isEqualTo(UPDATED_BUS_SERVICE);
        assertThat(testShelter.getTransportation()).isEqualTo(UPDATED_TRANSPORTATION);
        assertThat(testShelter.getDisabilityAccess()).isEqualTo(UPDATED_DISABILITY_ACCESS);
    }

    @Test
    @Transactional
    public void updateNonExistingShelter() throws Exception {
        int databaseSizeBeforeUpdate = shelterRepository.findAll().size();

        // Create the Shelter

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShelterMockMvc.perform(put("/api/shelters")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shelter)))
            .andExpect(status().isBadRequest());

        // Validate the Shelter in the database
        List<Shelter> shelterList = shelterRepository.findAll();
        assertThat(shelterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShelter() throws Exception {
        // Initialize the database
        shelterRepository.save(shelter);

        int databaseSizeBeforeDelete = shelterRepository.findAll().size();

        // Delete the shelter
        restShelterMockMvc.perform(delete("/api/shelters/{id}", shelter.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Shelter> shelterList = shelterRepository.findAll();
        assertThat(shelterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shelter.class);
        Shelter shelter1 = new Shelter();
        shelter1.setId(UUID.randomUUID());
        Shelter shelter2 = new Shelter();
        shelter2.setId(shelter1.getId());
        assertThat(shelter1).isEqualTo(shelter2);
        shelter2.setId(UUID.randomUUID());
        assertThat(shelter1).isNotEqualTo(shelter2);
        shelter1.setId(null);
        assertThat(shelter1).isNotEqualTo(shelter2);
    }
}
