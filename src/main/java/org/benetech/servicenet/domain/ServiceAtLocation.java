package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
 * A ServiceAtLocation.
 */
@Entity
@Data
@Table(name = "service_at_location")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceAtLocation extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description", columnDefinition = "clob")
    private String description;

    @ManyToOne
    @JoinColumn
    private Service srvc;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Location location;

    @Column(name = "external_db_id")
    @Size(max = 255, message = "Field value is too long.")
    private String externalDbId;

    @Column(name = "provider_name")
    @Size(max = 255, message = "Field value is too long.")
    private String providerName;

    @OneToOne(mappedBy = "serviceAtlocation")
    @JsonIgnore
    private RegularSchedule regularSchedule;

    @OneToOne(mappedBy = "serviceAtlocation")
    @JsonIgnore
    private HolidaySchedule holidaySchedule;

    @OneToMany(mappedBy = "serviceAtLocation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Phone> phones = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceAtLocation description(String description) {
        this.description = description;
        return this;
    }

    public Service getSrvc() {
        return srvc;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public ServiceAtLocation srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ServiceAtLocation location(Location location) {
        this.location = location;
        return this;
    }

    public RegularSchedule getRegularSchedule() {
        return regularSchedule;
    }

    public void setRegularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
    }

    public ServiceAtLocation regularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
        return this;
    }

    public HolidaySchedule getHolidaySchedule() {
        return holidaySchedule;
    }

    public void setHolidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
    }

    public ServiceAtLocation holidaySchedule(HolidaySchedule holidaySchedule) {
        this.holidaySchedule = holidaySchedule;
        return this;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public ServiceAtLocation phones(Set<Phone> phones) {
        this.phones = phones;
        return this;
    }

    public ServiceAtLocation addPhones(Phone phone) {
        this.phones.add(phone);
        phone.setServiceAtLocation(this);
        return this;
    }

    public ServiceAtLocation removePhones(Phone phone) {
        this.phones.remove(phone);
        phone.setServiceAtLocation(null);
        return this;
    }

    public ServiceAtLocation providerName(String providerName) {
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
        ServiceAtLocation serviceAtLocation = (ServiceAtLocation) o;
        if (serviceAtLocation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceAtLocation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceAtLocation{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
