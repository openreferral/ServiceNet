package org.benetech.servicenet.service.comparator;

import org.benetech.servicenet.service.dto.ActivityDTO;

import java.util.Comparator;

public class ActivityUpdateAgeComparator implements Comparator<ActivityDTO> {

    @Override
    public int compare(ActivityDTO o1, ActivityDTO o2) {
        return o2.getLastUpdated().compareTo(o1.getLastUpdated());
    }
}
