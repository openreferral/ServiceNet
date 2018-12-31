package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the DataImportReport entity.
 */
public class DataImportReportDTO implements Serializable {

    private UUID id;

    @NotNull
    private Integer numberOfUpdatedServices;

    @NotNull
    private Integer numberOfCreatedServices;

    @NotNull
    private Integer numberOfUpdatedOrgs;

    @NotNull
    private Integer numberOfCreatedOrgs;

    @NotNull
    private ZonedDateTime startDate;

    @NotNull
    private ZonedDateTime endDate;

    private String jobName;

    private UUID documentUploadId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getNumberOfUpdatedServices() {
        return numberOfUpdatedServices;
    }

    public void setNumberOfUpdatedServices(Integer numberOfUpdatedServices) {
        this.numberOfUpdatedServices = numberOfUpdatedServices;
    }

    public Integer getNumberOfCreatedServices() {
        return numberOfCreatedServices;
    }

    public void setNumberOfCreatedServices(Integer numberOfCreatedServices) {
        this.numberOfCreatedServices = numberOfCreatedServices;
    }

    public Integer getNumberOfUpdatedOrgs() {
        return numberOfUpdatedOrgs;
    }

    public void setNumberOfUpdatedOrgs(Integer numberOfUpdatedOrgs) {
        this.numberOfUpdatedOrgs = numberOfUpdatedOrgs;
    }

    public Integer getNumberOfCreatedOrgs() {
        return numberOfCreatedOrgs;
    }

    public void setNumberOfCreatedOrgs(Integer numberOfCreatedOrgs) {
        this.numberOfCreatedOrgs = numberOfCreatedOrgs;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public UUID getDocumentUploadId() {
        return documentUploadId;
    }

    public void setDocumentUploadId(UUID documentUploadId) {
        this.documentUploadId = documentUploadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataImportReportDTO dataImportReportDTO = (DataImportReportDTO) o;
        if (dataImportReportDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataImportReportDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataImportReportDTO{" +
            "id=" + getId() +
            ", numberOfUpdatedServices=" + getNumberOfUpdatedServices() +
            ", numberOfCreatedServices=" + getNumberOfCreatedServices() +
            ", numberOfUpdatedOrgs=" + getNumberOfUpdatedOrgs() +
            ", numberOfCreatedOrgs=" + getNumberOfCreatedOrgs() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", jobName='" + getJobName() + "'" +
            ", documentUpload=" + getDocumentUploadId() +
            "}";
    }
}
