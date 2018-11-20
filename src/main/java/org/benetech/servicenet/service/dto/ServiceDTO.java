package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the Service entity.
 */
public class ServiceDTO implements Serializable {

    private Long id;

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

    private Long organizationId;

    private String organizationName;

    private Long programId;

    private String programName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterpretationServices() {
        return interpretationServices;
    }

    public void setInterpretationServices(String interpretationServices) {
        this.interpretationServices = interpretationServices;
    }

    public String getApplicationProcess() {
        return applicationProcess;
    }

    public void setApplicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getAccreditations() {
        return accreditations;
    }

    public void setAccreditations(String accreditations) {
        this.accreditations = accreditations;
    }

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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
