package org.benetech.servicenet.matching.counter;

import java.math.BigDecimal;
import java.math.MathContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DescriptionSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Override
    public BigDecimal countSimilarityRatio(String description1, String description2) {
        if (StringUtils.isBlank(description1) || StringUtils.isBlank(description2)) {
            return NO_MATCH_RATIO;
        }
        String normalized1 = normalize(description1);
        String normalized2 = normalize(description2);

        int longerDescriptionSize = Math.max(normalized1.length(), normalized2.length());
        int longestCommonSubsequence = LCSUtils.getLongestCommonSubsequence(normalized1, normalized2);
        return BigDecimal.valueOf(longestCommonSubsequence).divide(BigDecimal.valueOf(longerDescriptionSize),
            MathContext.DECIMAL128);
    }

    //TODO: use StringMatchingUtils when it will be approved
    public static String normalize(String name) {
        return name.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9 ]", "").trim().replaceAll(" +", " ");
    }
}
