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

    private UUID userId;

    private String userLogin;

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
