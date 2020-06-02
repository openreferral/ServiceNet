package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Conflict entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConflictDTO implements Serializable {

    private UUID id;

    private String currentValue;

    private ZonedDateTime currentValueDate;

    private String offeredValue;

    private ZonedDateTime offeredValueDate;

    private String fieldName;

    private String entityPath;

    private ConflictStateEnum state;

    private ZonedDateTime stateDate;

    private ZonedDateTime createdDate;

    private UUID resourceId;

    private UUID partnerResourceId;

    private UUID ownerId;

    private String ownerName;

    private UUID partnerId;

    private String partnerName;

    private OwnerDTO owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConflictDTO conflictDTO = (ConflictDTO) o;
        if (conflictDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conflictDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
