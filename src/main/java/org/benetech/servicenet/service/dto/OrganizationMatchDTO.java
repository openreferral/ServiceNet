package org.benetech.servicenet.service.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    private String dismissComment;

    private UUID dismissedById;

    private String dismissedByName;

    private ZonedDateTime dismissDate;

    private Boolean hidden;

    private UUID hiddenById;

    private String hiddenByName;

    private ZonedDateTime hiddenDate;

    private UUID organizationRecordId;

    private String organizationRecordName;

    private UUID partnerVersionId;

    private String partnerVersionName;

    private Map<UUID, List<LocationMatchDto>> locationMatches;

    private Map<UUID, List<ServiceMatchDto>> serviceMatches;

    private Integer numberOfLocations;

    private String providerName;

    private BigDecimal similarity;

    private Long freshness;

    public Boolean isDismissed() {
        return dismissed;
    }

    public Boolean isHidden() {
        return hidden;
    }

    private OwnerDTO owner;

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
