package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.util.CompareUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
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
public class Service extends AbstractEntity implements Serializable, DeepComparable {

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

    @Column(name = "url")
    @Size(max = 255, message = "Field value is too long.")
    private String url;

    @Column(name = "email")
    @Size(max = 255, message = "Field value is too long.")
    private String email;

    @Column(name = "status")
    @Size(max = 255, message = "Field value is too long.")
    private String status;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "interpretation_services", columnDefinition = "clob")
    private String interpretationServices;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "application_process", columnDefinition = "clob")
    private String applicationProcess;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "wait_time", columnDefinition = "clob")
    private String waitTime;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fees", columnDefinition = "clob")
    private String fees;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "accreditations", columnDefinition = "clob")
    private String accreditations;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "licenses", columnDefinition = "clob")
    private String licenses;

    @Column(name = "jhi_type")
    @Size(max = 255, message = "Field value is too long.")
    private String type;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "external_db_id")
    @Size(max = 255, message = "Field value is too long.")
    private String externalDbId;

    @Column(name = "provider_name")
    @Size(max = 255, message = "Field value is too long.")
    private String providerName;

    @ManyToOne
    @JsonIgnoreProperties("services")
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties("services")
    private Program program;

    @OneToMany(mappedBy = "srvc")
    @JsonIgnore
    private Set<ServiceAtLocation> locations;

    @OneToOne(mappedBy = "srvc", fetch = FetchType.LAZY)
    @JsonIgnore
    private RegularSchedule regularSchedule;

    @OneToMany(mappedBy = "srvc", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<HolidaySchedule> holidaySchedules;

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
        this.organization = srvc.organization;
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

    public Service regularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
        return this;
    }

    public Service holidaySchedules(Set<HolidaySchedule> holidaySchedules) {
        this.holidaySchedules = holidaySchedules;
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

    @SuppressWarnings({"checkstyle:cyclomaticComplexity", "checkstyle:booleanExpressionComplexity"})
    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Service srvc = (Service) o;
        return (Objects.equals(this.name, srvc.name) &&
            Objects.equals(this.alternateName, srvc.alternateName) &&
            Objects.equals(this.description, srvc.description) &&
            Objects.equals(this.url, srvc.url) &&
            Objects.equals(this.email, srvc.email) &&
            Objects.equals(this.status, srvc.status) &&
            Objects.equals(this.interpretationServices, srvc.interpretationServices) &&
            Objects.equals(this.applicationProcess, srvc.applicationProcess) &&
            Objects.equals(this.waitTime, srvc.waitTime) &&
            Objects.equals(this.fees, srvc.fees) &&
            Objects.equals(this.accreditations, srvc.accreditations) &&
            Objects.equals(this.licenses, srvc.licenses) &&
            Objects.equals(this.type, srvc.type) &&
            Objects.equals(this.updatedAt, srvc.updatedAt) &&
            Objects.equals(this.externalDbId, srvc.externalDbId) &&
            Objects.equals(this.providerName, srvc.providerName) &&
            CompareUtils.deepEquals(locations, srvc.locations) &&
            CompareUtils.deepEquals(regularSchedule, srvc.regularSchedule) &&
            CompareUtils.deepEquals(holidaySchedules, srvc.holidaySchedules) &&
            CompareUtils.deepEquals(funding, srvc.funding) &&
            CompareUtils.deepEquals(eligibility, srvc.eligibility) &&
            CompareUtils.deepEquals(areas, srvc.areas) &&
            CompareUtils.deepEquals(docs, srvc.docs) &&
            CompareUtils.deepEquals(paymentsAccepteds, srvc.paymentsAccepteds) &&
            CompareUtils.deepEquals(langs, srvc.langs) &&
            CompareUtils.deepEquals(taxonomies, srvc.taxonomies) &&
            CompareUtils.deepEquals(phones, srvc.phones) &&
            CompareUtils.deepEquals(contacts, srvc.contacts));
    }
}
