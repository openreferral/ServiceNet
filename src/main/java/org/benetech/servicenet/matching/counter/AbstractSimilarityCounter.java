package org.benetech.servicenet.matching.counter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.benetech.servicenet.matching.model.MatchingContext;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;

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
    public abstract BigDecimal countSimilarityRatio(V obj1, V obj2, MatchingContext context);

    public List<MatchSimilarityDTO> getMatchSimilarityDTOs(V obj1, V obj2, MatchingContext context) {
        return Collections.emptyList();
    }

    protected static final BigDecimal NO_MATCH_RATIO = BigDecimal.ZERO;
    protected static final BigDecimal COMPLETE_MATCH_RATIO = BigDecimal.ONE;
}
