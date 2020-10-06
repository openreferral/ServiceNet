package org.benetech.servicenet.service.dto;

import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.NoArgsConstructor;

/**
 * A DTO for the Service entity.
 */
@Data
@NoArgsConstructor
public class ServiceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private String name;

    private String alternateName;

    @Lob
    private String description;

    private String url;

    private String email;

    private String status;

    @Lob
    private String interpretationServices;

    @Lob
    private String applicationProcess;

    @Lob
    private String waitTime;

    @Lob
    private String fees;

    @Lob
    private String accreditations;

    @Lob
    private String licenses;

    private String type;

    private ZonedDateTime updatedAt;

    private ZonedDateTime lastVerifiedOn;

    private UUID organizationId;

    private String organizationName;

    private UUID programId;

    private String programName;

    private String externalDbId;

    private String providerName;

    public ServiceDTO(UUID id, String name, UUID organizationId) {
        this.id = id;
        this.name = name;
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceDTO serviceDTO = (ServiceDTO) o;
        if (serviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @SuppressWarnings("CPD-START")
    @Override
    public String toString() {
        return "ServiceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", email='" + getEmail() + "'" +
            ", status='" + getStatus() + "'" +
            ", interpretationServices='" + getInterpretationServices() + "'" +
            ", applicationProcess='" + getApplicationProcess() + "'" +
            ", waitTime='" + getWaitTime() + "'" +
            ", fees='" + getFees() + "'" +
            ", accreditations='" + getAccreditations() + "'" +
            ", licenses='" + getLicenses() + "'" +
            ", type='" + getType() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", organization=" + getOrganizationId() +
            ", organization='" + getOrganizationName() + "'" +
            ", program=" + getProgramId() +
            ", program='" + getProgramName() + "'" +
            "}";
    }
}
