package org.benetech.servicenet.service.mapper;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

@Mapper
public interface IntegerMapper {

    default Integer toInteger(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }

        return Integer.valueOf(number);
    }
}
