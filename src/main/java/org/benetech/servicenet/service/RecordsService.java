package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.RecordDTO;

import java.util.Optional;
import java.util.UUID;

public interface RecordsService {

    Optional<RecordDTO> getRecordFromOrganization(UUID organizationId, UUID resourceId) throws IllegalAccessException;
}
