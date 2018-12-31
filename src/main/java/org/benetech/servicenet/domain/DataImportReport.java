package org.benetech.servicenet.domain;

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
    private Integer numberOfUpdatedServices;

    @NotNull
    @Column(name = "number_of_created_services", nullable = false)
    private Integer numberOfCreatedServices;

    @NotNull
    @Column(name = "number_of_updated_orgs", nullable = false)
    private Integer numberOfUpdatedOrgs;

    @NotNull
    @Column(name = "number_of_created_orgs", nullable = false)
    private Integer numberOfCreatedOrgs;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @OneToOne
    @JoinColumn(unique = true)
    private DocumentUpload documentUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getNumberOfUpdatedServices() {
        return numberOfUpdatedServices;
    }

    public DataImportReport numberOfUpdatedServices(Integer numberOfUpdatedServices) {
        this.numberOfUpdatedServices = numberOfUpdatedServices;
        return this;
    }

    public void setNumberOfUpdatedServices(Integer numberOfUpdatedServices) {
        this.numberOfUpdatedServices = numberOfUpdatedServices;
    }

    public Integer getNumberOfCreatedServices() {
        return numberOfCreatedServices;
    }

    public DataImportReport numberOfCreatedServices(Integer numberOfCreatedServices) {
        this.numberOfCreatedServices = numberOfCreatedServices;
        return this;
    }

    public void setNumberOfCreatedServices(Integer numberOfCreatedServices) {
        this.numberOfCreatedServices = numberOfCreatedServices;
    }

    public Integer getNumberOfUpdatedOrgs() {
        return numberOfUpdatedOrgs;
    }

    public DataImportReport numberOfUpdatedOrgs(Integer numberOfUpdatedOrgs) {
        this.numberOfUpdatedOrgs = numberOfUpdatedOrgs;
        return this;
    }

    public void setNumberOfUpdatedOrgs(Integer numberOfUpdatedOrgs) {
        this.numberOfUpdatedOrgs = numberOfUpdatedOrgs;
    }

    public Integer getNumberOfCreatedOrgs() {
        return numberOfCreatedOrgs;
    }

    public DataImportReport numberOfCreatedOrgs(Integer numberOfCreatedOrgs) {
        this.numberOfCreatedOrgs = numberOfCreatedOrgs;
        return this;
    }

    public void setNumberOfCreatedOrgs(Integer numberOfCreatedOrgs) {
        this.numberOfCreatedOrgs = numberOfCreatedOrgs;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public DataImportReport startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public DataImportReport endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public DocumentUpload getDocumentUpload() {
        return documentUpload;
    }

    public DataImportReport documentUpload(DocumentUpload documentUpload) {
        this.documentUpload = documentUpload;
        return this;
    }

    public void setDocumentUpload(DocumentUpload documentUpload) {
        this.documentUpload = documentUpload;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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

    @Override
    public String toString() {
        return "DataImportReport{" +
            "id=" + getId() +
            ", numberOfUpdatedServices=" + getNumberOfUpdatedServices() +
            ", numberOfCreatedServices=" + getNumberOfCreatedServices() +
            ", numberOfUpdatedOrgs=" + getNumberOfUpdatedOrgs() +
            ", numberOfCreatedOrgs=" + getNumberOfCreatedOrgs() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
