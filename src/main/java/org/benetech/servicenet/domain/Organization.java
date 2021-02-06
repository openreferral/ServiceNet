package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.util.CompareUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Organization.
 */
@Data
@Entity
@Builder
@Table(name = "organization", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"external_db_id", "account_id"})
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("PMD.ExcessivePublicCount")
public class Organization extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "name", columnDefinition = "clob")
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "alternate_name", columnDefinition = "clob")
    private String alternateName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", columnDefinition = "clob")
    private String description;

    @Column(name = "email")
    @Size(max = 255, message = "Field value is too long.")
    private String email;

    @Column(name = "url")
    @Size(max = 255, message = "Field value is too long.")
    private String url;

    @Column(name = "tax_status")
    @Size(max = 255, message = "Field value is too long.")
    private String taxStatus;

    @Column(name = "tax_id")
    @Size(max = 255, message = "Field value is too long.")
    private String taxId;

    @Column(name = "year_incorporated")
    private LocalDate yearIncorporated;

    @Column(name = "legal_status")
    private String legalStatus;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @NotNull
    @Column(name = "needs_matching", nullable = false)
    private Boolean needsMatching = true;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "deactivated_at")
    private ZonedDateTime deactivatedAt;

    @Column(name = "external_db_id")
    @Size(max = 255, message = "Field value is too long.")
    private String externalDbId;

    @Column(name = "last_verified_on")
    private ZonedDateTime lastVerifiedOn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Organization replacedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private DocumentUpload sourceDocument;

    @Column(name = "only_remote")
    private boolean onlyRemote;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnoreProperties("")
    private SystemAccount account;

    @OneToOne(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Funding funding;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Program> programs = new HashSet<>();

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Phone> phones = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_profile_organizations",
        joinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "id"))
    private Set<UserProfile> userProfiles;

    @JsonIgnore
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "silo_organizations",
        joinColumns = @JoinColumn(name = "organization_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "silo_id", referencedColumnName = "id"))
    private Set<Silo> additionalSilos;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DailyUpdate> dailyUpdates = new HashSet<>();

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "covidProtocols", columnDefinition = "clob")
    private String covidProtocols;

    @Column(name = "facebookUrl")
    @Size(max = 255, message = "Field value is too long.")
    private String facebookUrl;

    @Column(name = "twitterUrl")
    @Size(max = 255, message = "Field value is too long.")
    private String twitterUrl;

    @Column(name = "instagramUrl")
    @Size(max = 255, message = "Field value is too long.")
    private String instagramUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Organization(Organization org) {
        this.name = org.name;
        this.alternateName = org.alternateName;
        this.description = org.description;
        this.email = org.email;
        this.url = org.url;
        this.taxStatus = org.taxStatus;
        this.taxId = org.taxId;
        this.yearIncorporated = org.yearIncorporated;
        this.legalStatus = org.legalStatus;
        this.active = org.active;
        this.updatedAt = org.updatedAt;
        this.externalDbId = org.externalDbId;
        this.lastVerifiedOn = org.lastVerifiedOn;
        this.userProfiles = org.userProfiles;
        this.dailyUpdates = org.dailyUpdates;
        this.covidProtocols = org.covidProtocols;
        this.facebookUrl = org.facebookUrl;
        this.twitterUrl = org.twitterUrl;
        this.instagramUrl = org.instagramUrl;
    }

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

    public Organization locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Organization addLocations(Location location) {
        this.locations.add(location);
        location.setOrganization(this);
        return this;
    }

    public Organization removeLocations(Location location) {
        this.locations.remove(location);
        location.setOrganization(null);
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

    public Organization contacts(Set<Contact> contacts) {
        this.contacts = contacts;
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

    public Organization lastVerifiedOn(ZonedDateTime lastVerifiedOn) {
        this.lastVerifiedOn = lastVerifiedOn;
        return this;
    }

    public Organization dailyUpdates(Set<DailyUpdate> dailyUpdates) {
        this.dailyUpdates = dailyUpdates;
        return this;
    }

    public Organization addDailyUpdates(DailyUpdate dailyUpdate) {
        this.dailyUpdates.add(dailyUpdate);
        dailyUpdate.setOrganization(this);
        return this;
    }

    public Organization removeDailyUpdates(DailyUpdate dailyUpdate) {
        this.dailyUpdates.remove(dailyUpdate);
        dailyUpdate.setOrganization(null);
        return this;
    }

    public Organization userProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
        return this;
    }

    public Set<DailyUpdate> getDailyUpdates() {
        return dailyUpdates;
    }

    public void setDailyUpdates(Set<DailyUpdate> dailyUpdates) {
        this.dailyUpdates = dailyUpdates;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public Organization users(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
        return this;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public Organization additionalSilos(Set<Silo> silos) {
        this.additionalSilos = silos;
        return this;
    }

    public Set<Silo> getAdditionalSilos() {
        return additionalSilos;
    }

    public void setAdditionalSilos(Set<Silo> additionalSilos) {
        this.additionalSilos = additionalSilos;
    }

    public Organization covidProtocols(String covidProtocols) {
        this.covidProtocols = covidProtocols;
        return this;
    }

    public String getCovidProtocols() {
        return covidProtocols;
    }

    public void setCovidProtocols(String covidProtocols) {
        this.covidProtocols = covidProtocols;
    }

    public Organization facebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
        return this;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Organization twitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
        return this;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Organization instagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
        return this;
    }

    public String getInstagramUrl(String instagramUrl) {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity", "checkstyle:booleanExpressionComplexity"})
    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Organization org = (Organization) o;
        if (org.lastVerifiedOn != null && this.lastVerifiedOn != null &&
            !org.lastVerifiedOn.isEqual(this.lastVerifiedOn)) {
            return false;
        }
        if (!(Objects.equals(org.name, this.name) &&
            Objects.equals(org.alternateName, this.alternateName) &&
            Objects.equals(org.description, this.description) &&
            Objects.equals(org.email, this.email) &&
            Objects.equals(org.url, this.url) &&
            Objects.equals(org.taxStatus, this.taxStatus) &&
            Objects.equals(org.taxId, this.taxId) &&
            Objects.equals(org.yearIncorporated, this.yearIncorporated) &&
            Objects.equals(org.legalStatus, this.legalStatus) &&
            Objects.equals(org.active, this.active) &&
            Objects.equals(org.externalDbId, this.externalDbId) &&
            Objects.equals(org.covidProtocols, this.covidProtocols) &&
            Objects.equals(org.facebookUrl, this.facebookUrl) &&
            Objects.equals(org.twitterUrl, this.twitterUrl) &&
            Objects.equals(org.instagramUrl, this.instagramUrl)
        )) {
            return false;
        }
        return CompareUtils.oneSidedDeepEquals(org.funding, this.funding) &&
            CompareUtils.oneSidedDeepEquals(org.locations, this.locations) &&
            CompareUtils.oneSidedDeepEquals(org.programs, this.programs) &&
            CompareUtils.oneSidedDeepEquals(org.services, this.services) &&
            CompareUtils.oneSidedDeepEquals(org.contacts, this.contacts);
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
            "}";
    }
}
