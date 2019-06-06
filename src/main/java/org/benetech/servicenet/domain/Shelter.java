package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import org.hibernate.annotations.Type;

/**
 * A Shelter.
 */
@Entity
@Table(name = "shelter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Shelter extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "agency_name", columnDefinition = "clob")
    private String agencyName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "program_name", columnDefinition = "clob")
    private String programName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "alternate_name", columnDefinition = "clob")
    private String alternateName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "website", columnDefinition = "clob")
    private String website;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "eligibility_details", columnDefinition = "clob")
    private String eligibilityDetails;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "documents_required", columnDefinition = "clob")
    private String documentsRequired;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "application_process", columnDefinition = "clob")
    private String applicationProcess;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "fees", columnDefinition = "clob")
    private String fees;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "program_hours", columnDefinition = "clob")
    private String programHours;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "holiday_schedule", columnDefinition = "clob")
    private String holidaySchedule;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "email", columnDefinition = "clob")
    @ElementCollection
    private List<String> emails = new ArrayList<String>();

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "address_1", columnDefinition = "clob")
    private String address1;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "address_2", columnDefinition = "clob")
    private String address2;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "city", columnDefinition = "clob")
    private String city;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "zipcode", columnDefinition = "clob")
    private String zipcode;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "location_description", columnDefinition = "clob")
    private String locationDescription;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "bus_service", columnDefinition = "clob")
    private String busService;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "transportation", columnDefinition = "clob")
    private String transportation;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "disability_access", columnDefinition = "clob")
    private String disabilityAccess;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "shelter")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties("shelter")
    private Set<Phone> phones = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("shelters")
    private Beds beds;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "shelter_tags",
               joinColumns = @JoinColumn(name = "shelter_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Option> tags = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "shelter_languages",
        joinColumns = @JoinColumn(name = "shelter_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "language_id", referencedColumnName = "id"))
    private Set<Option> languages;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "shelter_defined_coverage_areas",
        joinColumns = @JoinColumn(name = "shelter_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "defined_coverage_area_id", referencedColumnName = "id"))
    private Set<Option> definedCoverageAreas;

    public String getAgencyName() {
        return agencyName;
    }

    public Shelter agencyName(String agencyName) {
        this.agencyName = agencyName;
        return this;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getProgramName() {
        return programName;
    }

    public Shelter programName(String programName) {
        this.programName = programName;
        return this;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public Shelter alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getWebsite() {
        return website;
    }

    public Shelter website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEligibilityDetails() {
        return eligibilityDetails;
    }

    public Shelter eligibilityDetails(String eligibilityDetails) {
        this.eligibilityDetails = eligibilityDetails;
        return this;
    }

    public void setEligibilityDetails(String eligibilityDetails) {
        this.eligibilityDetails = eligibilityDetails;
    }

    public String getDocumentsRequired() {
        return documentsRequired;
    }

    public Shelter documentsRequired(String documentsRequired) {
        this.documentsRequired = documentsRequired;
        return this;
    }

    public void setDocumentsRequired(String documentsRequired) {
        this.documentsRequired = documentsRequired;
    }

    public String getApplicationProcess() {
        return applicationProcess;
    }

    public Shelter applicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
        return this;
    }

    public void setApplicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
    }

    public String getFees() {
        return fees;
    }

    public Shelter fees(String fees) {
        this.fees = fees;
        return this;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getProgramHours() {
        return programHours;
    }

    public Shelter programHours(String programHours) {
        this.programHours = programHours;
        return this;
    }

    public void setProgramHours(String programHours) {
        this.programHours = programHours;
    }

    public String getHolidaySchedule() {
        return holidaySchedule;
    }

    public Shelter holidaySchedule(String holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
        return this;
    }

    public void setHolidaySchedule(String holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
    }

    public List<String> getEmails() {
        return emails;
    }

    public Shelter emails(List<String> emails) {
        this.emails = emails;
        return this;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getAddress1() {
        return address1;
    }

    public Shelter address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public Shelter address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public Shelter city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public Shelter zipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public Shelter locationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
        return this;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getBusService() {
        return busService;
    }

    public Shelter busService(String busService) {
        this.busService = busService;
        return this;
    }

    public void setBusService(String busService) {
        this.busService = busService;
    }

    public String getTransportation() {
        return transportation;
    }

    public Shelter transportation(String transportation) {
        this.transportation = transportation;
        return this;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getDisabilityAccess() {
        return disabilityAccess;
    }

    public Shelter disabilityAccess(String disabilityAccess) {
        this.disabilityAccess = disabilityAccess;
        return this;
    }

    public void setDisabilityAccess(String disabilityAccess) {
        this.disabilityAccess = disabilityAccess;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public Shelter phones(Set<Phone> phones) {
        this.phones = phones;
        return this;
    }

    public Shelter addPhones(Phone phone) {
        this.phones.add(phone);
        phone.setShelter(this);
        return this;
    }

    public Shelter removePhones(Phone phone) {
        this.phones.remove(phone);
        phone.setShelter(null);
        return this;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public Beds getBeds() {
        return beds;
    }

    public Shelter beds(Beds beds) {
        this.beds = beds;
        return this;
    }

    public void setBeds(Beds beds) {
        this.beds = beds;
    }

    public Set<Option> getTags() {
        return tags;
    }

    public Shelter tags(Set<Option> options) {
        this.tags = options;
        return this;
    }

    public Shelter addTags(Option option) {
        this.tags.add(option);
        return this;
    }

    public Shelter removeTags(Option option) {
        this.tags.remove(option);
        return this;
    }

    public void setTags(Set<Option> options) {
        this.tags = options;
    }

    public Set<Option> getLanguages() {
        return languages;
    }

    public Shelter language(Set<Option> option) {
        this.languages = option;
        return this;
    }

    public void setLanguages(Set<Option> option) {
        this.languages = option;
    }

    public Set<Option> getDefinedCoverageAreas() {
        return definedCoverageAreas;
    }

    public Shelter definedCoverageArea(Set<Option> option) {
        this.definedCoverageAreas = option;
        return this;
    }

    public void setDefinedCoverageAreas(Set<Option> option) {
        this.definedCoverageAreas = option;
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
        Shelter shelter = (Shelter) o;
        if (shelter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shelter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Shelter{" +
            "id=" + getId() +
            ", agencyName='" + getAgencyName() + "'" +
            ", programName='" + getProgramName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            ", website='" + getWebsite() + "'" +
            ", eligibilityDetails='" + getEligibilityDetails() + "'" +
            ", documentsRequired='" + getDocumentsRequired() + "'" +
            ", applicationProcess='" + getApplicationProcess() + "'" +
            ", fees='" + getFees() + "'" +
            ", programHours='" + getProgramHours() + "'" +
            ", holidaySchedule='" + getHolidaySchedule() + "'" +
            ", emails='" + getEmails() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            ", locationDescription='" + getLocationDescription() + "'" +
            ", busService='" + getBusService() + "'" +
            ", transportation='" + getTransportation() + "'" +
            ", disabilityAccess='" + getDisabilityAccess() + "'" +
            ", languages='" + getLanguages() + "'" +
            "}";
    }
}
