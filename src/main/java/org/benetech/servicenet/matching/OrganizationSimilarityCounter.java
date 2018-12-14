package org.benetech.servicenet.matching;

import org.benetech.servicenet.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationSimilarityCounter extends AbstractSimilarityCounter<Organization> {

    @Autowired
    private NameSimilarityCounter nameSimilarityCounter;

    @Autowired
    private AlternateNameSimilarityCounter alternateNameSimilarityCounter;

    @Autowired
    private LocationSimilarityCounter locationSimilarityCounter;

    @Autowired
    private DescriptionSimilarityCounter descriptionSimilarityCounter;

    @Autowired
    private EmailSimilarityCounter emailSimilarityCounter;

    @Autowired
    private UrlSimilarityCounter urlSimilarityCounter;

    @Autowired
    private YearIncorporatedSimilarityCounter yearIncorporatedSimilarityCounter;

    @Autowired
    private WeightProvider weightProvider;

    @Override
    public float countSimilarityRatio(Organization org1, Organization org2) {
        float result = 0;
        result += nameSimilarityCounter.countSimilarityRatio(org1.getName(), org2.getName())
            * weightProvider.getNameWeight();
        result += alternateNameSimilarityCounter.countSimilarityRatio(org1.getAlternateName(), org2.getAlternateName())
            * weightProvider.getAlternateNameWeight();
        result += locationSimilarityCounter.countSimilarityRatio(org1.getLocation(), org2.getLocation())
            * weightProvider.getLocationWeight();
        result += descriptionSimilarityCounter.countSimilarityRatio(org1.getDescription(), org2.getDescription())
            * weightProvider.getDescriptionWeight();
        result += emailSimilarityCounter.countSimilarityRatio(org1.getEmail(), org2.getEmail())
            * weightProvider.getEmailWeight();
        result += urlSimilarityCounter.countSimilarityRatio(org1.getUrl(), org2.getUrl())
            * weightProvider.getUrlWeight();
        result += yearIncorporatedSimilarityCounter.countSimilarityRatio(org1.getYearIncorporated(),
            org2.getYearIncorporated())
            * weightProvider.getYearsIncorporatedWeight();
        return result;
    }
}
