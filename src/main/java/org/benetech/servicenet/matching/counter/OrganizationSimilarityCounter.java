package org.benetech.servicenet.matching.counter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.LocationMatch;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.service.LocationMatchService;
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

    @Autowired
    private LocationMatchService locationMatchService;

    @Value("${similarity-ratio.config.location.always-compare}")
    private Boolean alwaysCompareLocations;

    @Value("${similarity-ratio.config.location.match-threshold}")
    private BigDecimal locationMatchThreshold;

    @Override
    public BigDecimal countSimilarityRatio(Organization org1, Organization org2, MatchingContext context) {
        return getMatchSimilarityDTOs(org1, org2, context).stream()
            .map(MatchSimilarityDTO::getSimilarity)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
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
        BigDecimal currentResult = similarityDtos.stream()
            .map(MatchSimilarityDTO::getSimilarity)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(getTotalWeight(), 2, RoundingMode.CEILING);
        if (BooleanUtils.isTrue(alwaysCompareLocations) || currentResult.compareTo(BigDecimal.ZERO) > 0) {
            if (!org1.getLocations().isEmpty() && !org2.getLocations().isEmpty()) {
                similarityDtos.add(getFieldMatchSimilarityDTO(
                    getLocationSimilarity(org1, org2, context),
                    "Location"
                ));
            }
        }
        return similarityDtos.stream().filter((ms) -> ms.getSimilarity().compareTo(BigDecimal.ZERO) > 0)
            .collect(Collectors.toList());
    }

    private BigDecimal getNameSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return nameSimilarityCounter.countSimilarityRatio(org1.getName(), org2.getName(), context)
            .multiply(weightProvider.getNameWeight());
    }

    private BigDecimal getAlternateNameSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return nameSimilarityCounter.countSimilarityRatio(org1.getAlternateName(), org2.getAlternateName(), context)
            .multiply(weightProvider.getAlternateNameWeight());
    }

    private BigDecimal getDescriptionSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return descriptionSimilarityCounter.countSimilarityRatio(org1.getDescription(), org2.getDescription(), context)
            .multiply(weightProvider.getDescriptionWeight());
    }

    private BigDecimal getEmailSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return emailSimilarityCounter.countSimilarityRatio(org1.getEmail(), org2.getEmail(), context)
            .multiply(weightProvider.getEmailWeight());
    }

    private BigDecimal getUrlSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return urlSimilarityCounter.countSimilarityRatio(org1.getUrl(), org2.getUrl(), context)
            .multiply(weightProvider.getUrlWeight());
    }

    private BigDecimal getYearIncorporatedSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return yearIncorporatedSimilarityCounter.countSimilarityRatio(org1.getYearIncorporated(),
            org2.getYearIncorporated(), context)
            .multiply(weightProvider.getYearsIncorporatedWeight());
    }

    private BigDecimal getTotalWeight() {
        return weightProvider.getNameWeight().add(weightProvider.getAlternateNameWeight())
            .add(weightProvider.getDescriptionWeight()).add(weightProvider.getEmailWeight())
            .add(weightProvider.getUrlWeight()).add(weightProvider.getYearsIncorporatedWeight())
            .add(weightProvider.getLocationWeight());
    }

    private BigDecimal getLocationSimilarity(Organization org1, Organization org2, MatchingContext context) {
        BigDecimal max = NO_MATCH_RATIO;

        for (Location location1 : org1.getLocations()) {
            for (Location location2 : org2.getLocations()) {
                BigDecimal similarity = locationSimilarityCounter.countSimilarityRatio(location1, location2, context)
                    .multiply(weightProvider.getLocationWeight());
                if (similarity.compareTo(max) > 0) {
                    max = similarity;
                }
                if (similarity.compareTo(locationMatchThreshold) >= 0) {
                    LocationMatch locationMatch = new LocationMatch();
                    locationMatch.setLocation(location1);
                    locationMatch.setMatchingLocation(location2);
                    locationMatchService.saveOrUpdate(locationMatch);
                    LocationMatch mirrorMatch = new LocationMatch();
                    mirrorMatch.setLocation(location2);
                    mirrorMatch.setMatchingLocation(location1);
                    locationMatchService.saveOrUpdate(mirrorMatch);
                } else if (location1 != null && location2 != null) {
                    locationMatchService.delete(location1.getId(), location2.getId());
                    locationMatchService.delete(location2.getId(), location1.getId());
                }
            }
        }
        return max;
    }

    private MatchSimilarityDTO getFieldMatchSimilarityDTO(BigDecimal similarityValue, String fieldName) {
        MatchSimilarityDTO similarityMatchDto = new MatchSimilarityDTO();
        similarityMatchDto.setSimilarity(similarityValue);
        similarityMatchDto.setResourceClass("Organization");
        similarityMatchDto.setFieldName(fieldName);
        return similarityMatchDto;
    }
}
