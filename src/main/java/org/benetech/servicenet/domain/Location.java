package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Location implements Serializable {

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

    @Column(name = "transportation")
    private String transportation;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToOne(mappedBy = "location")
    @JsonIgnore
    private PhysicalAddress physicalAddress;

    @OneToOne(mappedBy = "location")
    @JsonIgnore
    private PostalAddress postalAddress;

    @OneToOne(mappedBy = "location")
    @JsonIgnore
    private RegularSchedule regularSchedule;

    @OneToOne(mappedBy = "location")
    @JsonIgnore
    private HolidaySchedule holidaySchedule;

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Language> langs = new HashSet<>();

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AccessibilityForDisabilities> accessibilities = new HashSet<>();

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

    public Location name(String name) {
        this.name = name;
        return this;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public Location alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location description(String description) {
        this.description = description;
        return this;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public Location transportation(String transportation) {
        this.transportation = transportation;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Location latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Location longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public PhysicalAddress getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(PhysicalAddress physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public Location physicalAddress(PhysicalAddress physicalAddress) {
        this.physicalAddress = physicalAddress;
        return this;
    }

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }

    public Location postalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
        return this;
    }

    public RegularSchedule getRegularSchedule() {
        return regularSchedule;
    }

    public void setRegularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
    }

    public Location regularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
        return this;
    }

    public HolidaySchedule getHolidaySchedule() {
        return holidaySchedule;
    }

    public void setHolidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
    }

    public Location holidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
        return this;
    }

    public Set<Language> getLangs() {
        return langs;
    }

    public void setLangs(Set<Language> languages) {
        this.langs = languages;
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

    public Set<AccessibilityForDisabilities> getAccessibilities() {
        return accessibilities;
    }

    public void setAccessibilities(Set<AccessibilityForDisabilities> accessibilityForDisabilities) {
        this.accessibilities = accessibilityForDisabilities;
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
