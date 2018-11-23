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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A Service.
 */
@Entity
@Table(name = "service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Service implements Serializable {

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

    @Column(name = "url")
    private String url;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @Lob
    @Column(name = "interpretation_services")
    private String interpretationServices;

    @Lob
    @Column(name = "application_process")
    private String applicationProcess;

    @Lob
    @Column(name = "wait_time")
    private String waitTime;

    @Lob
    @Column(name = "fees")
    private String fees;

    @Lob
    @Column(name = "accreditations")
    private String accreditations;

    @Lob
    @Column(name = "licenses")
    private String licenses;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToOne
    @JsonIgnoreProperties("services")
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties("services")
    private Program program;

    @OneToOne(mappedBy = "srvc")
    @JsonIgnore
    private ServiceAtLocation location;

    @OneToOne(mappedBy = "srvc")
    @JsonIgnore
    private RegularSchedule regularSchedule;

    @OneToOne(mappedBy = "srvc")
    @JsonIgnore
    private HolidaySchedule holidaySchedule;

    @OneToOne(mappedBy = "srvc")
    @JsonIgnore
    private Funding funding;

    @OneToOne(mappedBy = "srvc")
    @JsonIgnore
    private Eligibility eligibility;

    @OneToMany(mappedBy = "srvc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceArea> areas = new HashSet<>();

    @OneToMany(mappedBy = "srvc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RequiredDocument> docs = new HashSet<>();

    @OneToMany(mappedBy = "srvc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PaymentAccepted> paymentsAccepteds = new HashSet<>();

    @OneToMany(mappedBy = "srvc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Language> langs = new HashSet<>();

    @OneToMany(mappedBy = "srvc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceTaxonomy> taxonomies = new HashSet<>();

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

    public Service name(String name) {
        this.name = name;
        return this;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public Service alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Service description(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Service url(String url) {
        this.url = url;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Service email(String email) {
        this.email = email;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Service status(String status) {
        this.status = status;
        return this;
    }

    public String getInterpretationServices() {
        return interpretationServices;
    }

    public void setInterpretationServices(String interpretationServices) {
        this.interpretationServices = interpretationServices;
    }

    public Service interpretationServices(String interpretationServices) {
        this.interpretationServices = interpretationServices;
        return this;
    }

    public String getApplicationProcess() {
        return applicationProcess;
    }

    public void setApplicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
    }

    public Service applicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
        return this;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public Service waitTime(String waitTime) {
        this.waitTime = waitTime;
        return this;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public Service fees(String fees) {
        this.fees = fees;
        return this;
    }

    public String getAccreditations() {
        return accreditations;
    }

    public void setAccreditations(String accreditations) {
        this.accreditations = accreditations;
    }

    public Service accreditations(String accreditations) {
        this.accreditations = accreditations;
        return this;
    }

    public String getLicenses() {
        return licenses;
    }

    public void setLicenses(String licenses) {
        this.licenses = licenses;
    }

    public Service licenses(String licenses) {
        this.licenses = licenses;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Service type(String type) {
        this.type = type;
        return this;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Service updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Service organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Service program(Program program) {
        this.program = program;
        return this;
    }

    public ServiceAtLocation getLocation() {
        return location;
    }

    public void setLocation(ServiceAtLocation serviceAtLocation) {
        this.location = serviceAtLocation;
    }

    public Service location(ServiceAtLocation serviceAtLocation) {
        this.location = serviceAtLocation;
        return this;
    }

    public RegularSchedule getRegularSchedule() {
        return regularSchedule;
    }

    public void setRegularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
    }

    public Service regularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
        return this;
    }

    public HolidaySchedule getHolidaySchedule() {
        return holidaySchedule;
    }

    public void setHolidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
    }

    public Service holidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
        return this;
    }

    public Funding getFunding() {
        return funding;
    }

    public void setFunding(Funding funding) {
        this.funding = funding;
    }

    public Service funding(Funding funding) {
        this.funding = funding;
        return this;
    }

    public Eligibility getEligibility() {
        return eligibility;
    }

    public void setEligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
    }

    public Service eligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
        return this;
    }

    public Set<ServiceArea> getAreas() {
        return areas;
    }

    public void setAreas(Set<ServiceArea> serviceAreas) {
        this.areas = serviceAreas;
    }

    public Service areas(Set<ServiceArea> serviceAreas) {
        this.areas = serviceAreas;
        return this;
    }

    public Service addAreas(ServiceArea serviceArea) {
        this.areas.add(serviceArea);
        serviceArea.setSrvc(this);
        return this;
    }

    public Service removeAreas(ServiceArea serviceArea) {
        this.areas.remove(serviceArea);
        serviceArea.setSrvc(null);
        return this;
    }

    public Set<RequiredDocument> getDocs() {
        return docs;
    }

    public void setDocs(Set<RequiredDocument> requiredDocuments) {
        this.docs = requiredDocuments;
    }

    public Service docs(Set<RequiredDocument> requiredDocuments) {
        this.docs = requiredDocuments;
        return this;
    }

    public Service addDocs(RequiredDocument requiredDocument) {
        this.docs.add(requiredDocument);
        requiredDocument.setSrvc(this);
        return this;
    }

    public Service removeDocs(RequiredDocument requiredDocument) {
        this.docs.remove(requiredDocument);
        requiredDocument.setSrvc(null);
        return this;
    }

    public Set<PaymentAccepted> getPaymentsAccepteds() {
        return paymentsAccepteds;
    }

    public void setPaymentsAccepteds(Set<PaymentAccepted> paymentAccepteds) {
        this.paymentsAccepteds = paymentAccepteds;
    }

    public Service paymentsAccepteds(Set<PaymentAccepted> paymentAccepteds) {
        this.paymentsAccepteds = paymentAccepteds;
        return this;
    }

    public Service addPaymentsAccepted(PaymentAccepted paymentAccepted) {
        this.paymentsAccepteds.add(paymentAccepted);
        paymentAccepted.setSrvc(this);
        return this;
    }

    public Service removePaymentsAccepted(PaymentAccepted paymentAccepted) {
        this.paymentsAccepteds.remove(paymentAccepted);
        paymentAccepted.setSrvc(null);
        return this;
    }

    public Set<Language> getLangs() {
        return langs;
    }

    public void setLangs(Set<Language> languages) {
        this.langs = languages;
    }

    public Service langs(Set<Language> languages) {
        this.langs = languages;
        return this;
    }

    public Service addLangs(Language language) {
        this.langs.add(language);
        language.setSrvc(this);
        return this;
    }

    public Service removeLangs(Language language) {
        this.langs.remove(language);
        language.setSrvc(null);
        return this;
    }

    public Set<ServiceTaxonomy> getTaxonomies() {
        return taxonomies;
    }

    public void setTaxonomies(Set<ServiceTaxonomy> serviceTaxonomies) {
        this.taxonomies = serviceTaxonomies;
    }

    public Service taxonomies(Set<ServiceTaxonomy> serviceTaxonomies) {
        this.taxonomies = serviceTaxonomies;
        return this;
    }

    public Service addTaxonomies(ServiceTaxonomy serviceTaxonomy) {
        this.taxonomies.add(serviceTaxonomy);
        serviceTaxonomy.setSrvc(this);
        return this;
    }

    public Service removeTaxonomies(ServiceTaxonomy serviceTaxonomy) {
        this.taxonomies.remove(serviceTaxonomy);
        serviceTaxonomy.setSrvc(null);
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
        Service service = (Service) o;
        if (service.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), service.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Service{" +
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
            "}";
    }
}
