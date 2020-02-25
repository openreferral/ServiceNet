package org.benetech.servicenet.service;

import java.util.List;
import java.util.UUID;
import org.benetech.servicenet.service.dto.ServiceMatchDto;

public interface ServiceMatchService {

    ServiceMatchDto save(ServiceMatchDto serviceMatch);

    List<ServiceMatchDto> findAllForService(UUID serviceId);

    void delete(ServiceMatchDto serviceMatchDto);
}
