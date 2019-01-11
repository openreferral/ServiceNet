package org.benetech.servicenet.matching.counter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UrlSimilarityCounter extends AbstractSimilarityCounter<String> {

    public static final String WWW = "WWW.";
    public static final String HTTPS = "HTTPS://";
    public static final String HTTP = "HTTP://";

    @Value("${similarity-ratio.weight.url.equal-upper-cased}")
    private float uppercasedWeight;

    @Override
    public float countSimilarityRatio(String url1, String url2) {
        if (url1 == null || url2 == null) {
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

    private String normalize(String url) {
        String result = url;
        if (result.toUpperCase(Locale.ROOT).startsWith(HTTP)) {
            result = result.substring(HTTP.length());
        }
        if (result.toUpperCase(Locale.ROOT).startsWith(HTTPS)) {
            result = result.substring(HTTPS.length());
        }
        if (result.toUpperCase(Locale.ROOT).startsWith(WWW)) {
            result = result.substring(WWW.length());
        }
        return result;
    }

    private boolean areNormalizedAndUpperCasedDifferent(String url1, String url2) {
        return !normalize(url1).toUpperCase(Locale.ROOT).equals(normalize(url2).toUpperCase(Locale.ROOT));
    }

    private boolean areNormalizedDifferent(String url1, String url2) {
        return !normalize(url1).equals(normalize(url2));
    }
}
