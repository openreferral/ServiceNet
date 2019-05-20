package org.benetech.servicenet.conflict.detector;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDetector<T extends AbstractEntity> {

    protected List<Conflict> detectConflicts(T current, T offered,
        String currentValue, String offeredValue, String fieldName) {
        List<Conflict> conflicts = new LinkedList<>();
        if (detect(currentValue, offeredValue)) {
            conflicts.add(createConflict(current, offered, currentValue, offeredValue, fieldName));
        }
        return conflicts;
    }

    protected boolean detect(String currentValue, String offeredValue) {
        return !this.areEquals(currentValue, offeredValue);
    }

    protected <Y> List<Conflict> detectConflicts(T current, T offered, Y currentValue, Y offeredValue, String fieldName) {
        List<Conflict> conflicts = new LinkedList<>();
        if (detect(currentValue, offeredValue)) {
            conflicts.add(createConflict(current, offered, currentValue, offeredValue, fieldName));
        }
        return conflicts;
    }

    protected <Y> boolean detect(Y currentValue, Y offeredValue) {
        return notEquals(currentValue, offeredValue);
    }

    private <Y> Conflict createConflict(T current, T offered, Y currentValue, Y offeredValue, String fieldName) {
        return Conflict.builder()
            .resourceId(current.getId())
            .partnerResourceId(offered.getId())
            .currentValue(getString(currentValue))
            .offeredValue(getString(offeredValue))
            .fieldName(fieldName)
            .entityPath(current.getClass().getCanonicalName())
            .state(ConflictStateEnum.PENDING)
            .stateDate(ZonedDateTime.now())
            .createdDate(ZonedDateTime.now())
            .build();
    }

    private boolean areEquals(String current, String offered) {
        return StringUtils.equalsIgnoreCase(current, offered) ||
            (StringUtils.isBlank(current) && StringUtils.isBlank(offered));
    }

    private<Y> boolean notEquals(Y obj, Y obj2) {
        return (obj != null && !obj.equals(obj2)) || (obj2 != null && !obj2.equals(obj));
    }

    private<Y> String getString(Y val) {
        if (val == null) {
            return StringUtils.EMPTY;
        } else {
            return val.toString();
        }
    }

}
