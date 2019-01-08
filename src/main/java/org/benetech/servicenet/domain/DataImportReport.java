package org.benetech.servicenet.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DataImportReport.
 */
@Entity
@Data
@Table(name = "data_import_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataImportReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Column(name = "number_of_updated_services", nullable = false)
    private Integer numberOfUpdatedServices = 0;

    @NotNull
    @Column(name = "number_of_created_services", nullable = false)
    private Integer numberOfCreatedServices = 0;

    @NotNull
    @Column(name = "number_of_updated_orgs", nullable = false)
    private Integer numberOfUpdatedOrgs = 0;

    @NotNull
    @Column(name = "number_of_created_orgs", nullable = false)
    private Integer numberOfCreatedOrgs = 0;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "job_name")
    private String jobName;

    @OneToOne
    @JoinColumn(unique = true)
    private DocumentUpload documentUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public DataImportReport numberOfUpdatedServices(Integer numberOfUpdatedServices) {
        this.numberOfUpdatedServices = numberOfUpdatedServices;
        return this;
    }

    public DataImportReport numberOfCreatedServices(Integer numberOfCreatedServices) {
        this.numberOfCreatedServices = numberOfCreatedServices;
        return this;
    }

    public DataImportReport numberOfUpdatedOrgs(Integer numberOfUpdatedOrgs) {
        this.numberOfUpdatedOrgs = numberOfUpdatedOrgs;
        return this;
    }

    public DataImportReport numberOfCreatedOrgs(Integer numberOfCreatedOrgs) {
        this.numberOfCreatedOrgs = numberOfCreatedOrgs;
        return this;
    }

    public DataImportReport startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public DataImportReport endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public DataImportReport jobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public DataImportReport documentUpload(DocumentUpload documentUpload) {
        this.documentUpload = documentUpload;
        return this;
    }

    public void incrementNumberOfCreatedOrgs() {
        numberOfCreatedOrgs++;
    }

    public void incrementNumberOfUpdatedOrgs() {
        numberOfUpdatedOrgs++;
    }

    public void incrementNumberOfCreatedServices() {
        numberOfCreatedServices++;
    }

    public void incrementNumberOfUpdatedServices() {
        numberOfUpdatedServices++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataImportReport dataImportReport = (DataImportReport) o;
        if (dataImportReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataImportReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
