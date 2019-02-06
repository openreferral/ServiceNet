package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.service.dto.ActivityDTO;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;

public final class ActivityComparatorFactory {

    private static final String SIZE = "size";

    private static final String AGE = "age";

    public static Comparator<ActivityDTO> createComparator(Pageable pageable) {
        if (pageable.getSort().getOrderFor(AGE) != null) {
            return new ActivityUpdateAgeComparator();
        } else {
            return new ActivitySizeComparator();
        }
    }

    private ActivityComparatorFactory() {
    }
}
