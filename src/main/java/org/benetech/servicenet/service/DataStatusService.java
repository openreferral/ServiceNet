package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.DataStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataStatusService {

    /**
     *  Get page of data statuses.
     * @param pageable Current Page
     * @return Dtos of data statuses.
     */
    Page<DataStatusDto> getDataStatuses(Pageable pageable);
}
