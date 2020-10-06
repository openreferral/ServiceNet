package org.benetech.servicenet.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import org.benetech.servicenet.util.CompareUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.Type;

/**
 * A RegularSchedule.
 */
@Entity
@Table(name = "regular_schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RegularSchedule extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(unique = true)
    private Service srvc;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToMany(mappedBy = "regularSchedule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OpeningHours> openingHours = new HashSet<>();

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "notes", columnDefinition = "clob")
    private String notes = "";

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Service getSrvc() {
        return srvc;
    }

    public RegularSchedule srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public Location getLocation() {
        return location;
    }

    public RegularSchedule location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public RegularSchedule openingHours(Set<OpeningHours> openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    public RegularSchedule addOpeningHours(OpeningHours openingHours) {
        this.openingHours.add(openingHours);
        openingHours.setRegularSchedule(this);
        return this;
    }

    public RegularSchedule removeOpeningHours(OpeningHours openingHours) {
        this.openingHours.remove(openingHours);
        openingHours.setRegularSchedule(null);
        return this;
    }

    public void setOpeningHours(Set<OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }

    public String getNotes() {
        return notes;
    }

    public RegularSchedule notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        RegularSchedule regularSchedule = (RegularSchedule) o;
        if (regularSchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), regularSchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegularSchedule{" +
            "id=" + getId() +
            "notes=" + getNotes() +
            "}";
    }

    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegularSchedule rs = (RegularSchedule) o;
        return Objects.equals(notes, rs.notes) &&
            CompareUtils.oneSidedDeepEquals(this.openingHours, rs.openingHours);
    }
}
