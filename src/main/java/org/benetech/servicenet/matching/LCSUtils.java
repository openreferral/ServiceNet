package org.benetech.servicenet.matching;

public final class LCSUtils {

    public static int getLongestCommonSubsequence(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();
        int[][] lengthOfSubsequences = new int[firstLength + 1][secondLength + 1];

        for (int i = 0; i <= firstLength; i++) {
            for (int j = 0; j <= secondLength; j++) {
                lengthOfSubsequences[i][j] = countLength(first, second, lengthOfSubsequences, i, j);
            }
        }

        return lengthOfSubsequences[firstLength][secondLength];
    }

    private static int countLength(String first, String second, int[][] lengthOfSubsequences, int firstIndex,
                                   int secondIndex) {
        if (isBeginning(firstIndex, secondIndex)) {
            return 0;
        } else if (arePreviousCharsEquals(first, second, firstIndex, secondIndex)) {
            return 1 + lengthOfSubsequences[firstIndex - 1][secondIndex - 1];
        } else {
            return Math.max(
                lengthOfSubsequences[firstIndex - 1][secondIndex], lengthOfSubsequences[firstIndex][secondIndex - 1]);
        }
    }

    private static boolean isBeginning(int firstIndex, int secondIndex) {
        return firstIndex == 0 || secondIndex == 0;
    }

    private static boolean arePreviousCharsEquals(String first, String second, int firstIndex, int secondIndex) {
        return first.charAt(firstIndex - 1) == second.charAt(secondIndex - 1);
    }

    private LCSUtils() {
    }
}
