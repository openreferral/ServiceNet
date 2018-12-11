package org.benetech.servicenet.matching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class UrlSimilarityCounter extends AbstractSimilarityCounter<String> {

    public static final String WWW = "WWW.";
    public static final String HTTPS = "HTTPS://";
    public static final String HTTP = "HTTP://";

    @Value("${similarity-ratio.weight.url.base}")
    private float baseWeight;
    
    @Value("${similarity-ratio.weight.url.equal-upper-cased}")
    private float uppercasedWeight;

    @Override
    public float countSimilarityRatio(String url1, String ulr2) {
        return countRawSimilarityRatio(url1, ulr2) * baseWeight;
    }

    private float countRawSimilarityRatio(String url1, String url2) {
        if (areNormalizedAndUpperCasedDifferent(url1, url2)) {
            return NO_MATCH_RATIO;
        }

        if (areNormalizedDifferent(url1, url2)) {
            return uppercasedWeight;
        }

        return COMPLETE_MATCH_RATIO;
    }

    private String normalize(String url) {
        if (url.toUpperCase(Locale.ROOT).startsWith(HTTP)) {
            url = url.substring(HTTP.length());
        }
        if (url.toUpperCase(Locale.ROOT).startsWith(HTTPS)) {
            url = url.substring(HTTPS.length());
        }
        if (url.toUpperCase(Locale.ROOT).startsWith(WWW)) {
            url = url.substring(WWW.length());
        }
        return url;
    }

    private boolean areNormalizedAndUpperCasedDifferent(String url1, String url2) {
        return !normalize(url1).toUpperCase(Locale.ROOT).equals(normalize(url2).toUpperCase(Locale.ROOT));
    }

    private boolean areNormalizedDifferent(String url1, String url2) {
        return !normalize(url1).equals(normalize(url2));
    }
}
