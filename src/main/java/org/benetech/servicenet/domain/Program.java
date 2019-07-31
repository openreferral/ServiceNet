package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Program extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "name", nullable = false)
    @Size(max = 255, message = "Field value is too long.")
    private String name = "";

    @Column(name = "alternate_name")
    @Size(max = 255, message = "Field value is too long.")
    private String alternateName;

    @ManyToOne
    @JsonIgnoreProperties("programs")
    private Organization organization;

    @OneToMany(mappedBy = "program")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Service> services = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Program name(String name) {
        this.name = name;
        return this;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public Program alternateName(String alternateName) {
        this.alternateName = alternateName;
        return this;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Program organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Program services(Set<Service> services) {
        this.services = services;
        return this;
    }

    public Program addServices(Service service) {
        this.services.add(service);
        service.setProgram(this);
        return this;
    }

    public Program removeServices(Service service) {
        this.services.remove(service);
        service.setProgram(null);
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
        Program program = (Program) o;
        if (program.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), program.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            "}";
    }

    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Program program = (Program) o;
        return Objects.equals(name, program.getName()) &&
            Objects.equals(alternateName, program.getAlternateName());
    }
}
