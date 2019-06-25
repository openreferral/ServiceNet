package org.benetech.servicenet.matching.counter;

import org.benetech.servicenet.util.UrlNormalizationUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UrlSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Value("${similarity-ratio.weight.url.equal-upper-cased}")
    private float uppercasedWeight;

    @Override
    public float countSimilarityRatio(String url1, String url2, MatchingContext context) {
        if (StringUtils.isBlank(url1) || StringUtils.isBlank(url2)) {
            return NO_MATCH_RATIO;
        }
        if (areNormalizedAndUpperCasedDifferent(url1, url2)) {
            return NO_MATCH_RATIO;
        }
        if (areNormalizedDifferent(url1, url2)) {
            return uppercasedWeight;
        }

        return COMPLETE_MATCH_RATIO;
    }

    private boolean areNormalizedAndUpperCasedDifferent(String url1, String url2) {
        return !UrlNormalizationUtils.normalize(url1).toUpperCase(Locale.ROOT)
            .equals(UrlNormalizationUtils.normalize(url2).toUpperCase(Locale.ROOT));
    }

    private boolean areNormalizedDifferent(String url1, String url2) {
        return !UrlNormalizationUtils.normalize(url1).equals(UrlNormalizationUtils.normalize(url2));
    }
}
