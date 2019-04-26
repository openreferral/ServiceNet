package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Contact.
 */
@Entity
@Data
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contact extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "department")
    private String department;

    @Column(name = "email")
    private String email;

    @Column(name = "external_db_id")
    private String externalDbId;

    @Column(name = "provider_name")
    private String providerName;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Service srvc;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ServiceAtLocation serviceAtLocation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Contact name(String name) {
        this.name = name;
        return this;
    }

    public Contact title(String title) {
        this.title = title;
        return this;
    }

    public Contact department(String department) {
        this.department = department;
        return this;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    public Contact organization(Organization organization) {
        this.organization = organization;
        return this;
    }

    public Contact srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public Contact serviceAtLocation(ServiceAtLocation serviceAtLocation) {
        this.serviceAtLocation = serviceAtLocation;
        return this;
    }

    public Contact externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public Contact providerName(String providerName) {
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
        Contact contact = (Contact) o;
        if (contact.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contact.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", department='" + getDepartment() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
