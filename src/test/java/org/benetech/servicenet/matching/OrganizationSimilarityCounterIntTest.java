package org.benetech.servicenet.matching;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.counter.DescriptionSimilarityCounter;
import org.benetech.servicenet.matching.counter.EmailSimilarityCounter;
import org.benetech.servicenet.matching.counter.LocationSimilarityCounter;
import org.benetech.servicenet.matching.counter.NameSimilarityCounter;
import org.benetech.servicenet.matching.counter.OrganizationSimilarityCounter;
import org.benetech.servicenet.matching.counter.UrlSimilarityCounter;
import org.benetech.servicenet.matching.counter.WeightProvider;
import org.benetech.servicenet.matching.counter.YearIncorporatedSimilarityCounter;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class OrganizationSimilarityCounterIntTest {

    private static final BigDecimal BASE_WEIGHT = BigDecimal.valueOf(1);
    private static final BigDecimal NAME_RATIO = BigDecimal.valueOf(0.1f);
    private static final BigDecimal DESCRIPTION_RATIO = BigDecimal.valueOf(0.3f);
    private static final BigDecimal EMAIL_RATIO = BigDecimal.valueOf(0.4f);
    private static final BigDecimal LOCATION_RATIO = BigDecimal.valueOf(0.5f);
    private static final BigDecimal URL_RATIO = BigDecimal.valueOf(0.6f);
    private static final BigDecimal YEARS_INCORPORATED_RATIO = BigDecimal.valueOf(0.7f);
    private static final BigDecimal TWO_POINT_TWO = BigDecimal.valueOf(2.2);

    @Mock
    private NameSimilarityCounter nameSimilarityCounter;

    @Mock
    private DescriptionSimilarityCounter descriptionSimilarityCounter;

    @Mock
    private EmailSimilarityCounter emailSimilarityCounter;

    @Mock
    private LocationSimilarityCounter locationSimilarityCounter;

    @Mock
    private UrlSimilarityCounter urlSimilarityCounter;

    @Mock
    private YearIncorporatedSimilarityCounter yearIncorporatedSimilarityCounter;

    @Mock
    private WeightProvider weightProvider;

    @Spy
    @InjectMocks
    private OrganizationSimilarityCounter organizationSimilarityCounter;

    @Before
    public void setUp() {
        when(weightProvider.getNameWeight()).thenReturn(BASE_WEIGHT);
        when(weightProvider.getAlternateNameWeight()).thenReturn(BASE_WEIGHT);
        when(weightProvider.getDescriptionWeight()).thenReturn(BASE_WEIGHT);
        when(weightProvider.getEmailWeight()).thenReturn(BASE_WEIGHT);
        when(weightProvider.getLocationWeight()).thenReturn(BASE_WEIGHT);
        when(weightProvider.getUrlWeight()).thenReturn(BASE_WEIGHT);
        when(weightProvider.getYearsIncorporatedWeight()).thenReturn(BASE_WEIGHT);

        MatchSimilarityDTO nameSimilarity = new MatchSimilarityDTO();
        nameSimilarity.setSimilarity(NAME_RATIO);
        nameSimilarity.setWeight(BASE_WEIGHT);
        when(nameSimilarityCounter.getFieldMatchSimilarityDTO(
            null, null, "Name", "Organization", BASE_WEIGHT))
            .thenReturn(nameSimilarity);
        MatchSimilarityDTO alternateNameSimilarity = new MatchSimilarityDTO();
        alternateNameSimilarity.setSimilarity(NAME_RATIO);
        alternateNameSimilarity.setWeight(BASE_WEIGHT);
        when(nameSimilarityCounter.getFieldMatchSimilarityDTO(
            null, null, "AlternateName", "Organization", BASE_WEIGHT))
            .thenReturn(alternateNameSimilarity);
        MatchSimilarityDTO descriptionSimilarity = new MatchSimilarityDTO();
        descriptionSimilarity.setSimilarity(DESCRIPTION_RATIO);
        descriptionSimilarity.setWeight(BASE_WEIGHT);
        when(descriptionSimilarityCounter.getFieldMatchSimilarityDTO(
            null, null, "Description", "Organization", BASE_WEIGHT))
            .thenReturn(descriptionSimilarity);
        MatchSimilarityDTO emailSimilarity = new MatchSimilarityDTO();
        emailSimilarity.setSimilarity(EMAIL_RATIO);
        emailSimilarity.setWeight(BASE_WEIGHT);
        when(emailSimilarityCounter.getFieldMatchSimilarityDTO(
            null, null, "Email", "Organization", BASE_WEIGHT))
            .thenReturn(emailSimilarity);
        MatchSimilarityDTO locationSimilarity = new MatchSimilarityDTO();
        locationSimilarity.setSimilarity(LOCATION_RATIO);
        locationSimilarity.setWeight(BASE_WEIGHT);
        when(locationSimilarityCounter.getFieldMatchSimilarityDTO(
            null, null, "Location", "Organization", BASE_WEIGHT))
            .thenReturn(locationSimilarity);
        MatchSimilarityDTO urlSimilarity = new MatchSimilarityDTO();
        urlSimilarity.setSimilarity(URL_RATIO);
        urlSimilarity.setWeight(BASE_WEIGHT);
        when(urlSimilarityCounter.getFieldMatchSimilarityDTO(
            null, null, "Url", "Organization", BASE_WEIGHT))
            .thenReturn(urlSimilarity);
        MatchSimilarityDTO yearIncorporatedSimilarity = new MatchSimilarityDTO();
        yearIncorporatedSimilarity.setSimilarity(YEARS_INCORPORATED_RATIO);
        yearIncorporatedSimilarity.setWeight(BASE_WEIGHT);
        when(yearIncorporatedSimilarityCounter.getFieldMatchSimilarityDTO(
            null, null, "YearIncorporated", "Organization", BASE_WEIGHT))
            .thenReturn(yearIncorporatedSimilarity);
    }

    @Test
    public void shouldReturnSumOfAllFieldsSimilarityRatio() {
        BigDecimal result = organizationSimilarityCounter.countSimilarityRatio(
            new Organization().locations(new HashSet<>()),
            new Organization().locations(new HashSet<>()));
        assertEquals(0, TWO_POINT_TWO.compareTo(result.setScale(2, RoundingMode.HALF_UP)));
    }
}
