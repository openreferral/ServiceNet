package org.benetech.servicenet.repository;

import java.util.List;
import org.benetech.servicenet.service.dto.LocationMatchDto;

public interface LocationMatchRepositoryCustom {
    void deleteInBatchByLocationAndMatchingLocationIds(List<LocationMatchDto> dtos);
}
