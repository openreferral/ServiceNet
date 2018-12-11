package org.benetech.servicenet.adapter.shared.util;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.shared.model.Hours;

import java.util.Optional;

public final class OpeningHoursUtils {

    public static Optional<Hours> getHoursFromString(String data, String delimiter) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(delimiter)) {
            return Optional.empty();
        }
        Hours result = new Hours();
        String[] parts = data.split(delimiter);
        if (parts.length != 2) {
            result.setOpen(data);
        } else {
            result.setOpen(parts[0]);
            result.setClose(parts[1]);
        }
        return Optional.of(result);
    }

    private OpeningHoursUtils() {
    }
}
