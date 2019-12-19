package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.maps.model.LatLng;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.UniqueConstraint;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Location.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "location", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"external_db_id", "provider_name"})
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Location extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "name", columnDefinition = "clob")
    private String name;

    @Column(name = "alternate_name")
    @Size(max = 255, message = "Field value is too long.")
    private String alternateName;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", columnDefinition = "clob")
    private String description;

    @Column(name = "transportation")
    @Size(max = 255, message = "Field value is too long.")
    private String transportation;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "external_db_id")
    @Size(max = 255, message = "Field value is too long.")
    private String externalDbId;

    @Column(name = "provider_name")
    @Size(max = 255, message = "Field value is too long.")
    private String providerName;

    @Column(name = "last_verified_on")
    private ZonedDateTime lastVerifiedOn;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private PhysicalAddress physicalAddress;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private RegularSchedule regularSchedule;

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<HolidaySchedule> holidaySchedules;

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Language> langs = new HashSet<>();

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Phone> phones = new HashSet<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AccessibilityForDisabilities> accessibilities = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "location_geocoding_results",
                       joinColumns = @JoinColumn(name = "location_id", referencedColumnName = "id"),
                       inverseJoinColumns = @JoinColumn(name = "geocoding_results_id", referencedColumnName = "id"))
    private List<GeocodingResult> geocodingResults = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties("locations")
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @PrePersist
    @PreUpdate
    public void addTimestamp() {
        updatedAt = ZonedDateTime.now();
    }

    public Location(Location loc) {
        this.name = loc.name;
        this.alternateName = loc.alternateName;
        this.description = loc.description;
        this.transportation = loc.transportation;
        this.latitude = loc.latitude;
        this.longitude = loc.longitude;
        this.externalDbId = loc.externalDbId;
        this.providerName = loc.providerName;
        this.organization = loc.organization;
        this.lastVerifiedOn = loc.lastVerifiedOn;
        this.updatedAt = loc.updatedAt;
    }

    public LatLng getCoordinates() {
        return new LatLng(latitude, longitude);
    }

    public Location organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Location name(String name) {
        this.name = name;
        return this;
    }

    public Location alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public Location description(String description) {
        this.description = description;
        return this;
    }

    public Location transportation(String transportation) {
        this.transportation = transportation;
        return this;
    }

    public Location latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Location longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Location externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public Location providerName(String providerName) {
        this.providerName = providerName;
        return this;
    }

    public Location physicalAddress(PhysicalAddress physicalAddress) {
        this.physicalAddress = physicalAddress;
        return this;
    }

    public Location postalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
        return this;
    }

    public Location regularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
        return this;
    }

    public Location holidaySchedules(Set<HolidaySchedule> holidaySchedules) {
        this.holidaySchedules = holidaySchedules;
        return this;
    }

    public Location langs(Set<Language> languages) {
        this.langs = languages;
        return this;
    }

    public Location geocodingResults(List<GeocodingResult> geocodingResults) {
        this.geocodingResults = geocodingResults;
        return this;
    }

    public Location addLangs(Language language) {
        this.langs.add(language);
        language.setLocation(this);
        return this;
    }

    public Location removeLangs(Language language) {
        this.langs.remove(language);
        language.setLocation(null);
        return this;
    }

    public Location accessibilities(Set<AccessibilityForDisabilities> accessibilityForDisabilities) {
        this.accessibilities = accessibilityForDisabilities;
        return this;
    }

    public Location addAccessibilities(AccessibilityForDisabilities accessibilityForDisabilities) {
        this.accessibilities.add(accessibilityForDisabilities);
        accessibilityForDisabilities.setLocation(this);
        return this;
    }

    public Location removeAccessibilities(AccessibilityForDisabilities accessibilityForDisabilities) {
        this.accessibilities.remove(accessibilityForDisabilities);
        accessibilityForDisabilities.setLocation(null);
        return this;
    }

    public Location phones(Set<Phone> phones) {
        this.phones = phones;
        return this;
    }

    public Location addPhones(Phone phone) {
        this.phones.add(phone);
        phone.setLocation(this);
        return this;
    }

    public Location removePhones(Phone phone) {
        this.phones.remove(phone);
        phone.setLocation(null);
        return this;
    }

    public Location lastVerifiedOn(ZonedDateTime lastVerifiedOn) {
        this.lastVerifiedOn = lastVerifiedOn;
        return this;
    }

    public Location updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            ", description='" + getDescription() + "'" +
            ", transportation='" + getTransportation() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }

    @SuppressWarnings({"checkstyle:cyclomaticComplexity", "checkstyle:booleanExpressionComplexity"})
    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location loc = (Location) o;

        return (Objects.equals(this.name, loc.name) &&
            Objects.equals(this.alternateName, loc.alternateName) &&
            Objects.equals(this.description, loc.description) &&
            Objects.equals(this.transportation, loc.transportation) &&
            Objects.equals(this.latitude, loc.latitude) &&
            Objects.equals(this.longitude, loc.longitude) &&
            Objects.equals(this.externalDbId, loc.externalDbId) &&
            Objects.equals(this.providerName, loc.providerName) &&
            Objects.equals(this.lastVerifiedOn, loc.lastVerifiedOn) &&
            CompareUtils.oneSidedDeepEquals(this.physicalAddress, loc.physicalAddress) &&
            CompareUtils.oneSidedDeepEquals(this.postalAddress, loc.postalAddress) &&
            CompareUtils.oneSidedDeepEquals(this.regularSchedule, loc.regularSchedule) &&
            CompareUtils.oneSidedDeepEquals(this.holidaySchedules, loc.holidaySchedules) &&
            CompareUtils.oneSidedDeepEquals(this.langs, loc.langs) &&
            CompareUtils.oneSidedDeepEquals(this.phones, loc.phones) &&
            CompareUtils.oneSidedDeepEquals(this.accessibilities, loc.accessibilities));
    }
}
