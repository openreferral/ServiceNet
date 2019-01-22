package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.ConfidentialRecord;
import org.benetech.servicenet.service.dto.ConfidentialRecordDTO;
import org.mapstruct.Mapper;

import java.util.UUID;

/**
 * Mapper for the entity ConfidentialRecord and its DTO ConfidentialRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConfidentialRecordMapper extends EntityMapper<ConfidentialRecordDTO, ConfidentialRecord> {

    default ConfidentialRecord fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ConfidentialRecord confidentialRecord = new ConfidentialRecord();
        confidentialRecord.setId(id);
        return confidentialRecord;
    }
}
