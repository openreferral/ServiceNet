package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.service.dto.ActivityDTO;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;

public final class ActivityComparatorFactory {

    private static final String AGE = "age";

    public static Comparator<ActivityDTO> createComparator(Pageable pageable) {
        Comparator<ActivityDTO> result = new ActivitySizeComparator();

        if (pageable.getSort().getOrderFor(AGE) != null) {
            result = new ActivityUpdateAgeComparator();
        }

        return result;
    }

    private ActivityComparatorFactory() {
    }
}
