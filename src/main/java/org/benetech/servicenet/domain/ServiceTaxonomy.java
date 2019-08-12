package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import org.benetech.servicenet.util.CompareUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ServiceTaxonomy.
 */
@Entity
@Data
@Table(name = "service_taxonomy", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"external_db_id", "provider_name"})
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceTaxonomy extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "taxonomy_details", columnDefinition = "clob")
    private String taxonomyDetails;

    @ManyToOne
    @JsonIgnoreProperties("taxonomies")
    private Service srvc;

    @Column(name = "external_db_id")
    @Size(max = 255, message = "Field value is too long.")
    private String externalDbId;

    @Column(name = "provider_name")
    @Size(max = 255, message = "Field value is too long.")
    private String providerName;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Taxonomy taxonomy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

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

    public ServiceTaxonomy externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public ServiceTaxonomy providerName(String providerName) {
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

    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceTaxonomy st = (ServiceTaxonomy) o;
        return Objects.equals(taxonomyDetails, st.taxonomyDetails) &&
            Objects.equals(externalDbId, st.externalDbId) &&
            Objects.equals(providerName, st.providerName) &&
            CompareUtils.deepEquals(taxonomy, st.taxonomy);
    }
}
