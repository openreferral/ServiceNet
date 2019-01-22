package org.benetech.servicenet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Funding.
 */
@Entity
@Table(name = "funding")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Funding extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "source", nullable = false)
    private String source;

    @OneToOne
    @JoinColumn(unique = true)
    private Organization organization;

    @OneToOne
    @JoinColumn(unique = true)
    private Service srvc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Funding source(String source) {
        this.source = source;
        return this;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Funding organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Service getSrvc() {
        return srvc;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public Funding srvc(Service service) {
        this.srvc = service;
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
        Funding funding = (Funding) o;
        if (funding.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), funding.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Funding{" +
            "id=" + getId() +
            ", source='" + getSource() + "'" +
            "}";
    }
}
