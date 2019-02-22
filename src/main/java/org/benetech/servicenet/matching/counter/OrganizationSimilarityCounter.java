package org.benetech.servicenet.matching.counter;

import org.benetech.servicenet.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationSimilarityCounter extends AbstractSimilarityCounter<Organization> {

    @Autowired
    private NameSimilarityCounter nameSimilarityCounter;

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
        result += nameSimilarityCounter.countSimilarityRatio(org1.getAlternateName(), org2.getAlternateName())
            * weightProvider.getAlternateNameWeight();
        //TODO: Restore location matching
//        //TODO Compare multiple locations
//        if (!org1.getLocations().isEmpty() && !org2.getLocations().isEmpty()) {
//            result += locationSimilarityCounter.countSimilarityRatio(
//                org1.getLocations().iterator().next(),
//                org2.getLocations().iterator().next())
//                * weightProvider.getLocationWeight();
//        }
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
