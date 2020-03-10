package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.*;
import org.benetech.servicenet.service.dto.RequestLoggerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestLogger} and its DTO {@link RequestLoggerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RequestLoggerMapper extends EntityMapper<RequestLoggerDTO, RequestLogger> {



    default RequestLogger fromId(UUID id) {
        if (id == null) {
            return null;
        }
        RequestLogger requestLogger = new RequestLogger();
        requestLogger.setId(id);
        return requestLogger;
    }
}
