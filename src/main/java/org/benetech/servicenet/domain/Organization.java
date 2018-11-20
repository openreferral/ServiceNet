package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "url")
    private String url;

    @Column(name = "tax_status")
    private String taxStatus;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "year_incorporated")
    private LocalDate yearIncorporated;

    @Column(name = "legal_status")
    private String legalStatus;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToOne
    @JoinColumn(unique = true)
    private Organization replacedBy;

    @ManyToOne
    @JsonIgnoreProperties("")
    private DocumentUpload sourceDocument;

    @ManyToOne
    @JsonIgnoreProperties("")
    private SystemAccount account;

    @OneToOne(mappedBy = "organization")
    @JsonIgnore
    private Funding funding;

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Program> programs = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Service> services = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public Organization alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organization description(String description) {
        this.description = description;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Organization email(String email) {
        this.email = email;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Organization url(String url) {
        this.url = url;
        return this;
    }

    public String getTaxStatus() {
        return taxStatus;
    }

    public void setTaxStatus(String taxStatus) {
        this.taxStatus = taxStatus;
    }

    public Organization taxStatus(String taxStatus) {
        this.taxStatus = taxStatus;
        return this;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public Organization taxId(String taxId) {
        this.taxId = taxId;
        return this;
    }

    public LocalDate getYearIncorporated() {
        return yearIncorporated;
    }

    public void setYearIncorporated(LocalDate yearIncorporated) {
        this.yearIncorporated = yearIncorporated;
    }

    public Organization yearIncorporated(LocalDate yearIncorporated) {
        this.yearIncorporated = yearIncorporated;
        return this;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public Organization legalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
        return this;
    }

    public Boolean isActive() {
        return active;
    }

    public Organization active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Organization updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Organization location(Location location) {
        this.location = location;
        return this;
    }

    public Organization getReplacedBy() {
        return replacedBy;
    }

    public void setReplacedBy(Organization organization) {
        this.replacedBy = organization;
    }

    public Organization replacedBy(Organization organization) {
        this.replacedBy = organization;
        return this;
    }

    public DocumentUpload getSourceDocument() {
        return sourceDocument;
    }

    public void setSourceDocument(DocumentUpload documentUpload) {
        this.sourceDocument = documentUpload;
    }

    public Organization sourceDocument(DocumentUpload documentUpload) {
        this.sourceDocument = documentUpload;
        return this;
    }

    public SystemAccount getAccount() {
        return account;
    }

    public void setAccount(SystemAccount systemAccount) {
        this.account = systemAccount;
    }

    public Organization account(SystemAccount systemAccount) {
        this.account = systemAccount;
        return this;
    }

    public Funding getFunding() {
        return funding;
    }

    public void setFunding(Funding funding) {
        this.funding = funding;
    }

    public Organization funding(Funding funding) {
        this.funding = funding;
        return this;
    }

    public Set<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }

    public Organization programs(Set<Program> programs) {
        this.programs = programs;
        return this;
    }

    public Organization addPrograms(Program program) {
        this.programs.add(program);
        program.setOrganization(this);
        return this;
    }

    public Organization removePrograms(Program program) {
        this.programs.remove(program);
        program.setOrganization(null);
        return this;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Organization services(Set<Service> services) {
        this.services = services;
        return this;
    }

    public Organization addServices(Service service) {
        this.services.add(service);
        service.setOrganization(this);
        return this;
    }

    public Organization removeServices(Service service) {
        this.services.remove(service);
        service.setOrganization(null);
        return this;
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
        Organization organization = (Organization) o;
        if (organization.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organization.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            ", description='" + getDescription() + "'" +
            ", email='" + getEmail() + "'" +
            ", url='" + getUrl() + "'" +
            ", taxStatus='" + getTaxStatus() + "'" +
            ", taxId='" + getTaxId() + "'" +
            ", yearIncorporated='" + getYearIncorporated() + "'" +
            ", legalStatus='" + getLegalStatus() + "'" +
            ", active='" + isActive() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
