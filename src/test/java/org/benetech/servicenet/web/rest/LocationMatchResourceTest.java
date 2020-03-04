package org.benetech.servicenet.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.benetech.servicenet.web.rest.TestUtil.createFormattingConversionService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.errors.ExceptionTranslator;
import org.benetech.servicenet.service.LocationMatchService;
import org.benetech.servicenet.service.dto.LocationMatchDto;
import org.benetech.servicenet.service.mapper.LocationMatchMapper;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class LocationMatchResourceTest {

    private final UUID LOCATION_UUID = UUID.randomUUID();

    private final UUID MATCHING_LOCATION_UUID = UUID.randomUUID();

    @Autowired
    private LocationMatchService locationMatchService;

    @Autowired
    private LocationMatchMapper locationMatchMapper;

    private MockMvc restLocationFieldsValueMockMvc;

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
        final LocationMatchResource locationMatchResource = new LocationMatchResource(locationMatchService);
        this.restLocationFieldsValueMockMvc = MockMvcBuilders.standaloneSetup(locationMatchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    @Test
    @Transactional
    public void shouldSaveLocationMatchProperly() throws Exception {
        LocationMatchDto locationMatchDto = locationMatchMapper.toDto(this.createLocationMatch());

        int databaseSizeBeforeSave = locationMatchService.findAll().size();

        restLocationFieldsValueMockMvc.perform(post("/api/location-matches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locationMatchDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.location").value(LOCATION_UUID.toString()))
            .andExpect(jsonPath("$.matchingLocation").value(MATCHING_LOCATION_UUID.toString()));

        int databaseSizeAfterSave = locationMatchService.findAll().size();

        assertThat(databaseSizeAfterSave).isEqualTo(databaseSizeBeforeSave + 1);
    }

    @Test
    @Transactional
    public void shouldNotSaveLocationMatchWhenLocationMatchWithSameIdsExists() throws Exception {
        LocationMatchDto locationMatchDto = locationMatchMapper.toDto(this.createLocationMatch());

        int databaseSizeBeforeSave = locationMatchService.findAll().size();

        restLocationFieldsValueMockMvc.perform(post("/api/location-matches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locationMatchDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.location").value(LOCATION_UUID.toString()))
            .andExpect(jsonPath("$.matchingLocation").value(MATCHING_LOCATION_UUID.toString()));

        int databaseSizeAfterSave = locationMatchService.findAll().size();

        restLocationFieldsValueMockMvc.perform(post("/api/location-matches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locationMatchDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.location").value(LOCATION_UUID.toString()))
            .andExpect(jsonPath("$.matchingLocation").value(MATCHING_LOCATION_UUID.toString()));

        int databaseSizeAfterSecondSave = locationMatchService.findAll().size();

        assertThat(databaseSizeAfterSave).isEqualTo(databaseSizeBeforeSave + 1);
        assertThat(databaseSizeAfterSecondSave).isEqualTo(databaseSizeAfterSave);
    }

    @Test
    @Transactional
    public void shouldDeleteLocationMatch() throws Exception {
        LocationMatch locationMatch = this.createLocationMatch();
        LocationMatchDto locationMatchDto = locationMatchMapper.toDto(locationMatch);

        locationMatchService.save(locationMatchDto);
        int databaseSizeBeforeDelete = locationMatchService.findAll().size();

        restLocationFieldsValueMockMvc.perform(delete("/api/location-matches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locationMatchDto))
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isOk());

        int databaseSizeAfterDelete = locationMatchService.findAll().size();

        assertThat(databaseSizeAfterDelete).isEqualTo(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void shouldNotDeleteNotExistingLocationMatch() throws Exception {
        LocationMatch locationMatch = this.createLocationMatch();
        LocationMatchDto locationMatchDto = locationMatchMapper.toDto(locationMatch);
        locationMatchService.save(locationMatchDto);
        int databaseSizeBeforeDelete = locationMatchService.findAll().size();

        Location location = new Location();
        location.setId(LOCATION_UUID);
        Location matchingLocation = new Location();
        matchingLocation.setId(MATCHING_LOCATION_UUID);
        LocationMatch locationMatch1 = new LocationMatch();
        locationMatch.setLocation(location);
        locationMatch.setMatchingLocation(matchingLocation);
        LocationMatchDto locationMatchDto1 = locationMatchMapper.toDto(locationMatch1);

        restLocationFieldsValueMockMvc.perform(delete("/api/location-matches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(locationMatchDto1))
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());

        int databaseSizeAfterDelete = locationMatchService.findAll().size();

        assertThat(databaseSizeBeforeDelete).isEqualTo(databaseSizeAfterDelete);
    }

    private LocationMatch createLocationMatch() {
        Location location = new Location();
        location.setId(LOCATION_UUID);
        Location matchingLocation = new Location();
        matchingLocation.setId(MATCHING_LOCATION_UUID);
        LocationMatch locationMatch = new LocationMatch();
        locationMatch.setLocation(location);
        locationMatch.setMatchingLocation(matchingLocation);
        return locationMatch;
    }

}
