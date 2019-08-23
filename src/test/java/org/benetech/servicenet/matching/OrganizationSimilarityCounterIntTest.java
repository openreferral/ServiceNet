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
    private static BigDecimal NAME_RATIO = BigDecimal.valueOf(0.1f);
    private static BigDecimal DESCRIPTION_RATION = BigDecimal.valueOf(0.3f);
    private static BigDecimal EMAIL_RATIO = BigDecimal.valueOf(0.4f);
    private static BigDecimal LOCATION_RATIO = BigDecimal.valueOf(0.5f);
    private static BigDecimal URL_RATIO = BigDecimal.valueOf(0.6f);
    private static BigDecimal YEARS_INCORPORATED_RATIO = BigDecimal.valueOf(0.7f);

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

        when(nameSimilarityCounter.countSimilarityRatio(null, null, null)).thenReturn(NAME_RATIO);
        when(descriptionSimilarityCounter.countSimilarityRatio(null, null, null)).thenReturn(DESCRIPTION_RATION);
        when(emailSimilarityCounter.countSimilarityRatio(null, null, null)).thenReturn(EMAIL_RATIO);
        when(locationSimilarityCounter.countSimilarityRatio(null, null, null)).thenReturn(LOCATION_RATIO);
        when(urlSimilarityCounter.countSimilarityRatio(null, null, null)).thenReturn(URL_RATIO);
        when(yearIncorporatedSimilarityCounter.countSimilarityRatio(null, null, null)).thenReturn(YEARS_INCORPORATED_RATIO);
    }

    @Test
    public void shouldReturnSumOfAllFieldsSimilarityRatio() {
        BigDecimal result = organizationSimilarityCounter.countSimilarityRatio(
            new Organization().locations(new HashSet<>()),
            new Organization().locations(new HashSet<>()), null);
        assertEquals(0, BigDecimal.valueOf(2.2).compareTo(result.setScale(2, RoundingMode.HALF_UP)));
    }
}
