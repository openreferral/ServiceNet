package org.benetech.servicenet.matching.counter;

import java.util.Collections;
import java.util.List;
import org.benetech.servicenet.matching.model.MatchingContext;

public abstract class AbstractSimilarityCounter<V> {

    /**
     * Count the similarity ratio between two organizations
     *
     * @param obj1 the first object to be compared
     * @param obj2 the second object to be compared
     * @param context contains contex required to perform matching, so it won't have to initialized too often
     *
     * @return similarity ratio
     */
    public abstract float countSimilarityRatio(V obj1, V obj2, MatchingContext context);

    public List getMatchSimilarityDTOs(V obj1, V obj2, MatchingContext context) {
        return Collections.emptyList();
    }

    protected static final float NO_MATCH_RATIO = 0;
    protected static final float COMPLETE_MATCH_RATIO = 1;
}
