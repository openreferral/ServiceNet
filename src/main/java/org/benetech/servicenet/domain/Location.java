package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Location.
 */
@Data
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Location extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "alternate_name")
    private String alternateName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "transportation")
    private String transportation;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "external_db_id")
    private String externalDbId;

    @Column(name = "provider_name")
    private String providerName;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private PhysicalAddress physicalAddress;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private RegularSchedule regularSchedule;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    @JsonIgnore
    private HolidaySchedule holidaySchedule;

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Language> langs = new HashSet<>();

    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AccessibilityForDisabilities> accessibilities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("locations")
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

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

    public Location holidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
        return this;
    }

    public Location langs(Set<Language> languages) {
        this.langs = languages;
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
}
