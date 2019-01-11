package org.benetech.servicenet.matching.counter;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class DescriptionSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Override
    public float countSimilarityRatio(String description1, String description2) {
        if (description1 == null || description2 == null) {
            return NO_MATCH_RATIO;
        }
        String normalized1 = normalize(description1);
        String normalized2 = normalize(description2);

        int longerDescriptionSize = Math.max(normalized1.length(), normalized2.length());
        float longestCommonSubsequence = LCSUtils.getLongestCommonSubsequence(normalized1, normalized2);
        return longestCommonSubsequence / longerDescriptionSize;
    }

    //TODO: use StringMatchingUtils when it will be approved
    public static String normalize(String name) {
        return name.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9 ]", "").trim().replaceAll(" +", " ");
    }
}
