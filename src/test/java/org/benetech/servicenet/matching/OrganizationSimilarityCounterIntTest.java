package org.benetech.servicenet.matching;

import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.Organization;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class OrganizationSimilarityCounterIntTest {

    private static final float PRECISION = 0.01f;
    private static float NAME_RATIO = 0.1f;
    private static float ALTERNATE_NAME_RATIO = 0.2f;
    private static float DESCRIPTION_RATION = 0.3f;
    private static float EMAIL_RATIO = 0.4f;
    private static float LOCATION_RATIO = 0.5f;
    private static float URL_RATIO = 0.6f;
    private static float YEARS_INCORPORATED_RATIO = 0.7f;

    @Mock
    private NameSimilarityCounter nameSimilarityCounter;

    @Mock
    private AlternateNameSimilarityCounter alternateNameSimilarityCounter;

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

    @Spy
    @InjectMocks
    private OrganizationSimilarityCounter organizationSimilarityCounter;

    @Before
    public void setUp() {
        when(nameSimilarityCounter.countSimilarityRatio(null, null)).thenReturn(NAME_RATIO);
        when(alternateNameSimilarityCounter.countSimilarityRatio(null, null)).thenReturn(ALTERNATE_NAME_RATIO);
        when(descriptionSimilarityCounter.countSimilarityRatio(null, null)).thenReturn(DESCRIPTION_RATION);
        when(emailSimilarityCounter.countSimilarityRatio(null, null)).thenReturn(EMAIL_RATIO);
        when(locationSimilarityCounter.countSimilarityRatio(null, null)).thenReturn(LOCATION_RATIO);
        when(urlSimilarityCounter.countSimilarityRatio(null, null)).thenReturn(URL_RATIO);
        when(yearIncorporatedSimilarityCounter.countSimilarityRatio(null, null)).thenReturn(YEARS_INCORPORATED_RATIO);
    }

    @Test
    public void shouldReturnSumOfAllFieldsSimilarityRatio() {
        float result = organizationSimilarityCounter.countSimilarityRatio(new Organization(), new Organization());
        assertEquals(2.8f, result, PRECISION);
    }
}
