package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.service.dto.ActivityDTO;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;

public final class ActivityComparatorFactory {

    private static final String AGE = "recent";

    private static final String RECOMMENDED = "recommended";

    public static Comparator<ActivityDTO> createComparator(Pageable pageable) {
        Comparator<ActivityDTO> result = new ActivityRecentlyUpdatedComparator();

        if (pageable.getSort().getOrderFor(RECOMMENDED) != null) {
            result = new ActivitySizeComparator();
        }

        return result;
    }

    private ActivityComparatorFactory() {
    }
}
