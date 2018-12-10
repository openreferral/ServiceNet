package org.benetech.servicenet.matching;

public abstract class AbstractSimilarityCounter<V> {

    /**
     * Count the similarity ratio between two organizations
     *
     * @param obj1 the first object to be compared
     * @param obj2 the second object to be compared
     *
     * @return similarity ratio, multiplied by the proper weight
     */
    public abstract float countSimilarityRatio(V obj1, V obj2);
}
