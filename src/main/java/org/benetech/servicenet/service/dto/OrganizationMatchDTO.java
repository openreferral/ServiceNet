package org.benetech.servicenet.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the OrganizationMatch entity.
 */
@Data
public class OrganizationMatchDTO implements Serializable {

    private UUID id;

    private ZonedDateTime timestamp;

    private Boolean dismissed;

    private UUID organizationRecordId;

    private String organizationRecordName;

    private UUID partnerVersionId;

    private String partnerVersionName;

    public Boolean isDismissed() {
        return dismissed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationMatchDTO organizationMatchDTO = (OrganizationMatchDTO) o;
        if (organizationMatchDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationMatchDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
