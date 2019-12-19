package org.benetech.servicenet.util;

import java.util.Collection;
import org.benetech.servicenet.domain.DeepComparable;
import org.springframework.util.CollectionUtils;

public final class CompareUtils {
    public static boolean oneSidedDeepEquals(DeepComparable e1, DeepComparable e2) {
        return (e2 == null || e1 != null && e1.deepEquals(e2));
    }

    public static boolean deepEquals(DeepComparable e1, DeepComparable e2) {
        return (e1 == null && e2 == null || e1 != null && e1.deepEquals(e2));
    }

    public static <V extends DeepComparable> boolean deepEquals(Collection<V> c1, Collection<V> c2) {
        if (c1 == c2 || CollectionUtils.isEmpty(c1) && CollectionUtils.isEmpty(c2)) {
            return true;
        }
        if (c1 == null || c2 == null || c2.size() != c1.size()) {
            return false;
        }
        return containsAll(c1, c2) && containsAll(c2, c1);
    }

    // One sided to only check if second collection is inside left collection
    public static <V extends DeepComparable> boolean oneSidedDeepEquals(Collection<V> c1, Collection<V> c2) {
        if (c2 == null || c1 == c2 || !CollectionUtils.isEmpty(c1) && CollectionUtils.isEmpty(c2)) {
            return true;
        }
        if (c1 != null && c1.size() < c2.size()) {
            return false;
        }
        return containsAll(c2, c1);
    }

    private static <V extends DeepComparable> boolean containsAll(Collection<V> c1, Collection<V> c2) {
        for (V e1 : c1) {
            if (!hasElement(e1, c2)) {
                return false;
            }
        }
        return true;
    }

    public static <V extends DeepComparable> boolean hasElement(V e1, Collection<V> col) {
        for (DeepComparable e2 : col) {
            if (e1 == null || e2 == null) {
                continue;
            }
            if (e2 == e1 || e2.deepEquals(e1)) {
                return true;
            }
        }
        return false;
    }

    private CompareUtils() { }
}
