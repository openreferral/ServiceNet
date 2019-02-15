package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.service.dto.ActivityDTO;

import java.util.Comparator;

public class ActivityRecentlyUpdatedComparator implements Comparator<ActivityDTO> {

    static int compareUpdateTime(ActivityDTO o1, ActivityDTO o2) {
        return o2.getLastUpdated().compareTo(o1.getLastUpdated());
    }

    @Override
    public int compare(ActivityDTO o1, ActivityDTO o2) {
        int result = compareUpdateTime(o1, o2);
        if (result == 0) {
            result = ActivitySizeComparator.compareSizes(o1, o2);
        }
        return result;
    }
}
