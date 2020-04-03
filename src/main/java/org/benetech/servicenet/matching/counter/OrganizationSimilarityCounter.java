package org.benetech.servicenet.matching.counter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
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
        similarityDtos.add(getNameSimilarity(org1, org2, context));
        similarityDtos.add(getAlternateNameSimilarity(org1, org2, context));
        similarityDtos.add(getDescriptionSimilarity(org1, org2, context));
        similarityDtos.add(getEmailSimilarity(org1, org2, context));
        similarityDtos.add(getUrlSimilarity(org1, org2, context));
        similarityDtos.add(getYearIncorporatedSimilarity(org1, org2, context));

        BigDecimal totalWeight = getTotalWeight(similarityDtos);
        BigDecimal currentResult = (totalWeight.compareTo(BigDecimal.ZERO) > 0) ?
            similarityDtos.stream()
            .map(MatchSimilarityDTO::getSimilarity)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(totalWeight, 2, RoundingMode.FLOOR)
            : BigDecimal.ZERO;

        if (BooleanUtils.isTrue(alwaysCompareLocations) || currentResult.compareTo(BigDecimal.ZERO) > 0) {
            if (!org1.getLocations().isEmpty() && !org2.getLocations().isEmpty()) {
                similarityDtos.add(getLocationSimilarity(org1, org2, context));
            }
        }
        return similarityDtos;
    }

    private MatchSimilarityDTO getNameSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return nameSimilarityCounter.getFieldMatchSimilarityDTO(
            org1.getName(),
            org2.getName(),
            context,
            "Name",
            "Organization",
            weightProvider.getNameWeight()
        );
    }

    private MatchSimilarityDTO getAlternateNameSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return nameSimilarityCounter.getFieldMatchSimilarityDTO(
            org1.getAlternateName(),
            org2.getAlternateName(),
            context,
            "AlternateName",
            "Organization",
            weightProvider.getAlternateNameWeight()
        );
    }

    private MatchSimilarityDTO getDescriptionSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return descriptionSimilarityCounter.getFieldMatchSimilarityDTO(
            org1.getDescription(),
            org2.getDescription(),
            context,
            "Description",
            "Organization",
            weightProvider.getDescriptionWeight()
        );
    }

    private MatchSimilarityDTO getEmailSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return emailSimilarityCounter.getFieldMatchSimilarityDTO(
            org1.getEmail(),
            org2.getEmail(),
            context,
            "Email",
            "Organization",
            weightProvider.getEmailWeight()
        );
    }

    private MatchSimilarityDTO getUrlSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return urlSimilarityCounter.getFieldMatchSimilarityDTO(
            org1.getUrl(),
            org2.getUrl(),
            context,
            "Url",
            "Organization",
            weightProvider.getUrlWeight()
        );
    }

    private MatchSimilarityDTO getYearIncorporatedSimilarity(Organization org1, Organization org2, MatchingContext context) {
        return yearIncorporatedSimilarityCounter.getFieldMatchSimilarityDTO(
            org1.getYearIncorporated(),
            org2.getYearIncorporated(),
            context,
            "YearIncorporated",
            "Organization",
            weightProvider.getYearsIncorporatedWeight()
        );
    }

    public BigDecimal getTotalWeight(List<MatchSimilarityDTO> similarities) {
        return similarities.stream()
            .map(MatchSimilarityDTO::getWeight)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private MatchSimilarityDTO getLocationSimilarity(Organization org1, Organization org2, MatchingContext context) {
        BigDecimal max = NO_MATCH_RATIO;
        MatchSimilarityDTO similarityDto = null;

        for (Location location1 : org1.getLocations()) {
            for (Location location2 : org2.getLocations()) {
                similarityDto = locationSimilarityCounter.getFieldMatchSimilarityDTO(
                    location1,
                    location2,
                    context,
                    "Location",
                    "Organization",
                    weightProvider.getLocationWeight()
                );
                BigDecimal similarity = similarityDto.getSimilarity();
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
        return similarityDto;
    }
}
