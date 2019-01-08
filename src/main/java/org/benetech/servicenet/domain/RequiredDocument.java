package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A RequiredDocument.
 */
@Entity
@Table(name = "required_document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RequiredDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Column(name = "document", nullable = false)
    private String document;

    @ManyToOne
    @JsonIgnoreProperties("docs")
    private Service srvc;

    @Column(name = "external_db_id")
    private String externalDbId;

    @Column(name = "provider_name")
    private String providerName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public RequiredDocument document(String document) {
        this.document = document;
        return this;
    }

    public Service getSrvc() {
        return srvc;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public RequiredDocument srvc(Service service) {
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
        RequiredDocument requiredDocument = (RequiredDocument) o;
        if (requiredDocument.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requiredDocument.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequiredDocument{" +
            "id=" + getId() +
            ", document='" + getDocument() + "'" +
            "}";
    }
}
