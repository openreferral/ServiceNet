package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.service.dto.ActivityDTO;

import java.util.Comparator;

public class ActivitySizeComparator implements Comparator<ActivityDTO> {

    static int compareSizes(ActivityDTO o1, ActivityDTO o2) {
        return Integer.compare(o2.getRecord().getConflicts().size(), o1.getRecord().getConflicts().size());
    }

    @Override
    public int compare(ActivityDTO o1, ActivityDTO o2) {
        int result = compareSizes(o1, o2);
        if (result == 0) {
            result = ActivityRecentlyUpdatedComparator.compareUpdateTime(o1, o2);
        }
        return result;
    }
}
