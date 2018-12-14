package org.benetech.servicenet.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.benetech.servicenet.domain.enumeration.ActionType;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Activity entity.
 */
@Getter
@Setter
@ToString
public class ActivityDTO implements Serializable {

    private UUID id;

    private UUID userId;

    private String userLogin;

    private Set<UserDTO> reviewers = new HashSet<>();

    private UUID metadataId;

    private ActionType metadataActionType;

    private ZonedDateTime metadataActionDate;

    private String metadataFieldName;

    private UUID organizationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityDTO activityDTO = (ActivityDTO) o;
        if (activityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
