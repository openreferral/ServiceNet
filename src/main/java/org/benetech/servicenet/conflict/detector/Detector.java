package org.benetech.servicenet.conflict.detector;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Conflict;

import java.util.LinkedList;
import java.util.List;

public abstract class Detector<T> {

    protected boolean equals(String current, String offered) {
        return StringUtils.equalsIgnoreCase(current, offered) ||
            (StringUtils.isBlank(current) && StringUtils.isBlank(offered));
    }

    protected List<Conflict> detectConflict(T current, String name, String name2) {
        List<Conflict> conflicts = new LinkedList<>();
        if (!this.equals(name, name2)) {
            conflicts.add(createConflict(current, name, name2));
        }
        return conflicts;
    }

    protected <Y> List<Conflict> detectConflict(T current, Y val, Y val2) {
        List<Conflict> conflicts = new LinkedList<>();
        if (notEqual(val, val2)) {
            conflicts.add(createConflict(current, val, val2));
        }
        return conflicts;
    }

    protected <Z> Conflict createConflict(T obj, Z currentValue, Z offeredValue) {
        return Conflict.builder()
            .currentValue(getString(currentValue))
            .offeredValue(getString(offeredValue))
            .entityPath(obj.getClass().getCanonicalName())
            .build();
    }

    private <Y> boolean notEqual(Y obj, Y obj2) {
        return (obj != null && !obj.equals(obj2)) || (obj2 != null && !obj2.equals(obj));
    }

    private <W> String getString(W val) {
        if (val == null) {
            return StringUtils.EMPTY;
        } else  {
            return val.toString();
        }
    }

}
