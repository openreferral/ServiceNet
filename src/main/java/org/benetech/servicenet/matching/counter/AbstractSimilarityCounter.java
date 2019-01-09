package org.benetech.servicenet.matching.counter;

public abstract class AbstractSimilarityCounter<V> {

    /**
     * Count the similarity ratio between two organizations
     *
     * @param obj1 the first object to be compared
     * @param obj2 the second object to be compared
     *
     * @return similarity ratio
     */
    public abstract float countSimilarityRatio(V obj1, V obj2);

    protected static final float NO_MATCH_RATIO = 0;
    protected static final float COMPLETE_MATCH_RATIO = 1;
}
