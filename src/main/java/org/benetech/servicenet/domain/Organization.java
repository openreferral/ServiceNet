package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
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
@Data
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

    @Column(name = "external_db_id")
    private String externalDbId;

    @Column(name = "provider_name")
    private String providerName;

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

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public Organization alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public Organization description(String description) {
        this.description = description;
        return this;
    }

    public Organization email(String email) {
        this.email = email;
        return this;
    }

    public Organization url(String url) {
        this.url = url;
        return this;
    }

    public Organization taxStatus(String taxStatus) {
        this.taxStatus = taxStatus;
        return this;
    }

    public Organization taxId(String taxId) {
        this.taxId = taxId;
        return this;
    }

    public Organization yearIncorporated(LocalDate yearIncorporated) {
        this.yearIncorporated = yearIncorporated;
        return this;
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

    public Organization updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Organization location(Location location) {
        this.location = location;
        return this;
    }

    public Organization replacedBy(Organization organization) {
        this.replacedBy = organization;
        return this;
    }

    public Organization sourceDocument(DocumentUpload documentUpload) {
        this.sourceDocument = documentUpload;
        return this;
    }

    public Organization account(SystemAccount systemAccount) {
        this.account = systemAccount;
        return this;
    }

    public Organization funding(Funding funding) {
        this.funding = funding;
        return this;
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

    public Organization externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public Organization providerName(String providerName) {
        this.providerName = providerName;
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
}
