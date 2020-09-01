package org.benetech.servicenet.service.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.benetech.servicenet.domain.OrganizationError;

/**
 * A DTO for the DataImportReport entity.
 */
@Data
public class DataImportReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Lob
    private String errorMessage;

    private UUID documentUploadId;

    private String systemAccount;

    @Getter
    @Setter
    private Set<OrganizationError> organizationErrors = new HashSet<>();

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
}
