package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Funding entity.
 */
public class FundingDTO implements Serializable {

    private UUID id;

    @NotNull
    private String source;

    private UUID organizationId;

    private String organizationName;

    private UUID srvcId;

    private String srvcName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public UUID getSrvcId() {
        return srvcId;
    }

    public void setSrvcId(UUID serviceId) {
        this.srvcId = serviceId;
    }

    public String getSrvcName() {
        return srvcName;
    }

    public void setSrvcName(String serviceName) {
        this.srvcName = serviceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FundingDTO fundingDTO = (FundingDTO) o;
        if (fundingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fundingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FundingDTO{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            ", organization=" + getOrganizationId() +
            ", organization='" + getOrganizationName() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            "}";
    }
}
