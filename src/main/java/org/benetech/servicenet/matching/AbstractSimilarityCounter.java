package org.benetech.servicenet.matching;

public abstract class AbstractSimilarityCounter<V> {

    /**
     * Count the similarity ratio between two organizations
     *
     * @param obj1 the first object to be compared
     * @param obj2 the second object to be compared
     *
     * @return similarity ratio <0, 1>, multiplied by the proper weight
     */
    public abstract float countSimilarityRatio(V obj1, V obj2);

    protected static final float NO_MATCH_RATIO = 0;
    protected static final float COMPLETE_MATCH_RATIO = 1;
}
