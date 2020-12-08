package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.DailyUpdate;

public final class DailyUpdateMother {

    public static final String DAILY_UPDATE = "Daily Update";

    public static DailyUpdate createDefault() {
        return new DailyUpdate()
            .update(DAILY_UPDATE);
    }

    private DailyUpdateMother() {
    }
}
