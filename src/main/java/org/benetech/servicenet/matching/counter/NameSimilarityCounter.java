package org.benetech.servicenet.matching.counter;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.benetech.servicenet.matching.counter.StringMatchingUtils.extractInitials;
import static org.benetech.servicenet.matching.counter.StringMatchingUtils.normalize;
import static org.benetech.servicenet.matching.counter.StringMatchingUtils.sort;

@Component
public class NameSimilarityCounter extends AbstractSimilarityCounter<String> {

    @Value("${similarity-ratio.weight.name.similar-initials-sorted}")
    private float sortedInitialsWeight;

    @Value("${similarity-ratio.weight.name.similar-initials}")
    private float initialsWeight;

    @Value("${similarity-ratio.weight.name.similar-words-sorted}")
    private float similarSortedWordsWeight;

    @Value("${similarity-ratio.weight.name.fuzzy-similarity-threshold}")
    private float fuzzySimilarityThreshold;

    @Value("${similarity-ratio.weight.name.fuzzy-partial-similarity-threshold}")
    private float fuzzyPartialSimilarityThreshold;

    @Override
    public float countSimilarityRatio(String name1, String name2) {
        String normalizedName1 = normalize(name1);
        String normalizedName2 = normalize(name2);

        if (areSortedInitialsDifferent(normalizedName1, normalizedName2)) {
            return NO_MATCH_RATIO;
        }

        if (areInitialsDifferent(normalizedName1, normalizedName2)) {
            return sortedInitialsWeight;
        }

        if (areSortedWordsDifferent(normalizedName1, normalizedName2)) {
            return initialsWeight;
        }

        if (areWordsDifferent(normalizedName1, normalizedName2)) {
            return similarSortedWordsWeight;
        }

        return COMPLETE_MATCH_RATIO;
    }

    public boolean areSortedInitialsDifferent(String name1, String name2) {
        return isRatioBelowThreshold(FuzzySearch.ratio(extractInitials(sort(name1)), extractInitials(sort(name2))))
            && isRatioBelowPartialThreshold(FuzzySearch.partialRatio(sort(name1), extractInitials(sort(name2))))
            && isRatioBelowPartialThreshold(FuzzySearch.partialRatio(extractInitials(sort(name1)), sort(name2)))
            && isRatioBelowPartialThreshold(
                FuzzySearch.partialRatio(extractInitials(sort(name1)), extractInitials(sort(name2))));
    }

    public boolean areInitialsDifferent(String name1, String name2) {
        return isRatioBelowThreshold(FuzzySearch.ratio(extractInitials(name1), extractInitials(name2)))
            && isRatioBelowPartialThreshold(FuzzySearch.partialRatio(name1, extractInitials(name2)))
            && isRatioBelowPartialThreshold(FuzzySearch.partialRatio(extractInitials(name1), name2))
            && isRatioBelowPartialThreshold(FuzzySearch.partialRatio(extractInitials(name1), extractInitials(name2)));
    }

    public boolean areSortedWordsDifferent(String name1, String name2) {
        return isRatioBelowThreshold(FuzzySearch.ratio(sort(normalize(name1)), sort(normalize(name2))))
            && isRatioBelowPartialThreshold(FuzzySearch.partialRatio(sort(normalize(name1)), sort(normalize(name2))));
    }

    public boolean areWordsDifferent(String name1, String name2) {
        return isRatioBelowThreshold(FuzzySearch.ratio(normalize(name1), normalize(name2)))
            && isRatioBelowPartialThreshold(FuzzySearch.partialRatio(normalize(name1), normalize(name2)));
    }

    private boolean isRatioBelowThreshold(int fuzzyRatio) {
        return isRatioBelowThreshold(fuzzyRatio, fuzzySimilarityThreshold);
    }

    private boolean isRatioBelowPartialThreshold(int fuzzyRatio) {
        return isRatioBelowThreshold(fuzzyRatio, fuzzyPartialSimilarityThreshold);
    }

    private boolean isRatioBelowThreshold(int fuzzyRatio, float threshold) {
        return fuzzyRatio < threshold * 100;
    }
}
