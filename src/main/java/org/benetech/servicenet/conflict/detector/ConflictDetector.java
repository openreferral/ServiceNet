package org.benetech.servicenet.conflict.detector;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Conflict;

import java.util.List;

public interface ConflictDetector <T> {

    List<Conflict> detect(T current, T offered);

    default boolean equals(String current, String offered) {
        return StringUtils.equalsIgnoreCase(current, offered) ||
            (StringUtils.isBlank(current) && StringUtils.isBlank(offered));
    }
}
