package org.benetech.servicenet.matching.counter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EmailSimilarityCounter extends AbstractSimilarityCounter<String> {

    private static final String AT = "@";
    private static final int DOMAIN_PART = 1;
    private static final int LOCAL_PART = 0;

    @Value("${similarity-ratio.weight.email.same-domain}")
    private float domainWeight;

    @Value("${similarity-ratio.weight.email.same-normalized-local-parts}")
    private float normalizedLocalPartWeight;

    @Override
    public float countSimilarityRatio(String email1, String email2) {
        if (StringUtils.isBlank(email1) || StringUtils.isBlank(email2)) {
            return NO_MATCH_RATIO;
        }
        if (areDomainsDifferent(email1, email2)) {
            return NO_MATCH_RATIO;
        }

        if (areNormalizedLocalPartDifferent(email1, email2)) {
            return domainWeight;
        }

        if (areLocalPartDifferent(email1, email2)) {
            return normalizedLocalPartWeight;
        }

        return COMPLETE_MATCH_RATIO;
    }

    private String getDomainName(String email) {
        String[] parts = email.split(AT);
        return parts[DOMAIN_PART].toUpperCase(Locale.ROOT);
    }

    private String getLocalPart(String email) {
        String[] parts = email.split(AT);
        return parts[LOCAL_PART];
    }

    private boolean areDomainsDifferent(String email1, String email2) {
        return !getDomainName(email1).equals(getDomainName(email2));
    }

    private boolean areNormalizedLocalPartDifferent(String email1, String email2) {
        return !getLocalPart(email1).toUpperCase(Locale.ROOT).equals(getLocalPart(email2).toUpperCase(Locale.ROOT));
    }

    private boolean areLocalPartDifferent(String email1, String email2) {
        return !getLocalPart(email1).equals(getLocalPart(email2));
    }
}
