package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ServiceTaxonomy.
 */
@Entity
@Table(name = "service_taxonomy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceTaxonomy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "taxonomy_details")
    private String taxonomyDetails;

    @ManyToOne
    @JsonIgnoreProperties("taxonomies")
    private Service srvc;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Taxonomy taxonomy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxonomyDetails() {
        return taxonomyDetails;
    }

    public void setTaxonomyDetails(String taxonomyDetails) {
        this.taxonomyDetails = taxonomyDetails;
    }

    public ServiceTaxonomy taxonomyDetails(String taxonomyDetails) {
        this.taxonomyDetails = taxonomyDetails;
        return this;
    }

    public Service getSrvc() {
        return srvc;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public ServiceTaxonomy srvc(Service service) {
        this.srvc = service;
        return this;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }

    public ServiceTaxonomy taxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
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
        ServiceTaxonomy serviceTaxonomy = (ServiceTaxonomy) o;
        if (serviceTaxonomy.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceTaxonomy.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceTaxonomy{" +
            "id=" + getId() +
            ", taxonomyDetails='" + getTaxonomyDetails() + "'" +
            "}";
    }
}
