package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 * A Service.
 */
@Data
@Entity
@Table(name = "service")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NoArgsConstructor
public class Service extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "external_db_id")
    private String externalDbId;

    @Column(name = "provider_name")
    private String providerName;

    @ManyToOne
    @JsonIgnoreProperties("services")
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties("services")
    private Program program;

    @OneToOne(mappedBy = "srvc")
    @JsonIgnore
    private ServiceAtLocation location;

    @OneToOne(mappedBy = "srvc", fetch = FetchType.LAZY)
    @JsonIgnore
    private RegularSchedule regularSchedule;

    @OneToOne(mappedBy = "srvc", fetch = FetchType.LAZY)
    @JsonIgnore
    private HolidaySchedule holidaySchedule;

    @OneToOne(mappedBy = "srvc", fetch = FetchType.LAZY)
    @JsonIgnore
    private Funding funding;

    @OneToOne(mappedBy = "srvc", fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "srvc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Phone> phones = new HashSet<>();

    @OneToMany(mappedBy = "srvc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Service(Service srvc) {
        this.name = srvc.name;
        this.alternateName = srvc.alternateName;
        this.description = srvc.description;
        this.url = srvc.url;
        this.email = srvc.email;
        this.status = srvc.status;
        this.interpretationServices = srvc.interpretationServices;
        this.applicationProcess = srvc.applicationProcess;
        this.waitTime = srvc.waitTime;
        this.fees = srvc.fees;
        this.accreditations = srvc.accreditations;
        this.licenses = srvc.licenses;
        this.type = srvc.type;
        this.updatedAt = srvc.updatedAt;
        this.externalDbId = srvc.externalDbId;
        this.providerName = srvc.providerName;
    }

    public Service name(String name) {
        this.name = name;
        return this;
    }

    public Service alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public Service description(String description) {
        this.description = description;
        return this;
    }

    public Service url(String url) {
        this.url = url;
        return this;
    }

    public Service email(String email) {
        this.email = email;
        return this;
    }

    public Service status(String status) {
        this.status = status;
        return this;
    }

    public Service interpretationServices(String interpretationServices) {
        this.interpretationServices = interpretationServices;
        return this;
    }

    public Service applicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
        return this;
    }

    public Service waitTime(String waitTime) {
        this.waitTime = waitTime;
        return this;
    }

    public Service fees(String fees) {
        this.fees = fees;
        return this;
    }

    public Service accreditations(String accreditations) {
        this.accreditations = accreditations;
        return this;
    }

    public Service licenses(String licenses) {
        this.licenses = licenses;
        return this;
    }

    public Service type(String type) {
        this.type = type;
        return this;
    }

    public Service updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Service organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Service program(Program program) {
        this.program = program;
        return this;
    }

    public Service location(ServiceAtLocation serviceAtLocation) {
        this.location = serviceAtLocation;
        return this;
    }

    public Service regularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
        return this;
    }

    public Service holidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
        return this;
    }

    public Service funding(Funding funding) {
        this.funding = funding;
        return this;
    }

    public Service eligibility(Eligibility eligibility) {
        this.eligibility = eligibility;
        return this;
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

    public Service taxonomies(Set<ServiceTaxonomy> serviceTaxonomies) {
        this.taxonomies = serviceTaxonomies;
        return this;
    }

    public Service phones(Set<Phone> phones) {
        this.phones = phones;
        return this;
    }

    public Service contacts(Set<Contact> contacts) {
        this.contacts = contacts;
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

    public Service externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public Service providerName(String providerName) {
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
