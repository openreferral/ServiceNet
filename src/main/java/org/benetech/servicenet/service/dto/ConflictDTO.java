package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;

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

    private UUID ownerId;

    private String ownerName;

    private UUID firstAcceptedId;

    private String firstAcceptedName;

    @Builder.Default
    private Set<SystemAccountDTO> acceptedThisChange = new HashSet<>();

    public ConflictDTO addAcceptedThisChange(SystemAccountDTO systemAccount) {
        this.acceptedThisChange.add(systemAccount);
        return this;
    }

    public ConflictDTO removeAcceptedThisChange(SystemAccountDTO systemAccount) {
        this.acceptedThisChange.remove(systemAccount);
        return this;
    }

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
