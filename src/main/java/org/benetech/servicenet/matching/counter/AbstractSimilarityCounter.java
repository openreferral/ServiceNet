package org.benetech.servicenet.matching.counter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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

    public MatchSimilarityDTO getFieldMatchSimilarityDTO(V obj1, V obj2, MatchingContext context,
        String fieldName, String resourceClass, BigDecimal weight) {
        MatchSimilarityDTO similarityMatchDto = new MatchSimilarityDTO();
        similarityMatchDto.setSimilarity(countSimilarityRatio(obj1, obj2, context).multiply(weight));
        similarityMatchDto.setResourceClass(resourceClass);
        similarityMatchDto.setFieldName(fieldName);
        similarityMatchDto.setWeight(hasValue(obj1, obj2) ? weight : BigDecimal.ZERO);
        return similarityMatchDto;
    }

    public Boolean hasValue(V obj1, V obj2) {
        if (obj1 instanceof String) {
            return StringUtils.isNotBlank((String) obj1) || StringUtils.isNotBlank((String) obj2);
        }
        return obj1 != null || obj2 != null;
    }

    protected static final BigDecimal NO_MATCH_RATIO = BigDecimal.ZERO;
    protected static final BigDecimal COMPLETE_MATCH_RATIO = BigDecimal.ONE;
}
