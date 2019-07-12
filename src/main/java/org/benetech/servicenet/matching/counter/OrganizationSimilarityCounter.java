package org.benetech.servicenet.matching.counter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${similarity-ratio.config.location.always-compare}")
    private Boolean alwaysCompareLocations;

    @Override
    public float countSimilarityRatio(Organization org1, Organization org2, MatchingContext context) {
        return getMatchSimilarityDTOs(org1, org2, context).stream()
            .map(MatchSimilarityDTO::getSimilarity)
            .reduce(0f, Float::sum);
    }

    @Override
    public List<MatchSimilarityDTO> getMatchSimilarityDTOs(Organization org1, Organization org2, MatchingContext context) {
        List<MatchSimilarityDTO> similarityDtos = new ArrayList<>();
        similarityDtos.add(getFieldMatchSimilarityDTO(
            getNameSimilarity(org1, org2, context),
            "Name"
        ));
        similarityDtos.add(getFieldMatchSimilarityDTO(
            getAlternateNameSimilarity(org1, org2, context),
            "AlternateName"
        ));
        similarityDtos.add(getFieldMatchSimilarityDTO(
            getDescriptionSimilarity(org1, org2, context),
            "Description"
        ));
        similarityDtos.add(getFieldMatchSimilarityDTO(
            getEmailSimilarity(org1, org2, context),
            "Email"
        ));
        similarityDtos.add(getFieldMatchSimilarityDTO(
            getUrlSimilarity(org1, org2, context),
            "Url"
        ));
        similarityDtos.add(getFieldMatchSimilarityDTO(
            getYearIncorporatedSimilarity(org1, org2, context),
            "YearIncorporated"
        ));
        float currentResult = similarityDtos.stream()
            .map(MatchSimilarityDTO::getSimilarity)
            .reduce(0f, Float::sum);
        if (BooleanUtils.isTrue(alwaysCompareLocations) || currentResult > 0) {
            //TODO Compare multiple locations
            if (!org1.getLocations().isEmpty() && !org2.getLocations().isEmpty()) {
                similarityDtos.add(getFieldMatchSimilarityDTO(
                    getLocationSimilarity(org1, org2, context),
                    "Location"
                ));
            }
        }
        return similarityDtos.stream().filter((ms) -> ms.getSimilarity() > 0).collect(Collectors.toList());
    }

    private float getNameSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return nameSimilarityCounter.countSimilarityRatio(org1.getName(), org2.getName(), context)
            * weightProvider.getNameWeight();
    }

    private float getAlternateNameSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return nameSimilarityCounter.countSimilarityRatio(org1.getAlternateName(), org2.getAlternateName(), context)
            * weightProvider.getAlternateNameWeight();
    }

    private float getDescriptionSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return descriptionSimilarityCounter.countSimilarityRatio(org1.getDescription(), org2.getDescription(), context)
            * weightProvider.getDescriptionWeight();
    }

    private float getEmailSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return emailSimilarityCounter.countSimilarityRatio(org1.getEmail(), org2.getEmail(), context)
            * weightProvider.getEmailWeight();
    }

    private float getUrlSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return urlSimilarityCounter.countSimilarityRatio(org1.getUrl(), org2.getUrl(), context)
            * weightProvider.getUrlWeight();
    }

    private float getYearIncorporatedSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return yearIncorporatedSimilarityCounter.countSimilarityRatio(org1.getYearIncorporated(),
            org2.getYearIncorporated(), context)
            * weightProvider.getYearsIncorporatedWeight();
    }

    private float getLocationSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return locationSimilarityCounter.countSimilarityRatio(
            org1.getLocations().iterator().next(),
            org2.getLocations().iterator().next(), context)
            * weightProvider.getLocationWeight();
    }

    private MatchSimilarityDTO getFieldMatchSimilarityDTO(float similarityValue, String fieldName) {
        MatchSimilarityDTO similarityMatchDto = new MatchSimilarityDTO();
        similarityMatchDto.setSimilarity(similarityValue);
        similarityMatchDto.setResourceClass("Organization");
        similarityMatchDto.setFieldName(fieldName);
        return similarityMatchDto;
    }
}
