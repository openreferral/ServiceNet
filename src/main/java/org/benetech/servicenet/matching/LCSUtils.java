package org.benetech.servicenet.matching;

import org.apache.commons.text.similarity.LongestCommonSubsequence;

public final class LCSUtils {

    public static int getLongestCommonSubsequence(String first, String second) {
        LongestCommonSubsequence counter = new LongestCommonSubsequence();
        return counter.longestCommonSubsequence(first, second).length();
    }

    private LCSUtils() {
    }
}
