package org.benetech.servicenet.domain;

import com.sun.istack.NotNull;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

import org.benetech.servicenet.domain.enumeration.ServiceTaxonomiesDetailsFields;
import org.hibernate.annotations.GenericGenerator;

/**
 * A ServiceTaxonomiesDetailsFieldsValue.
 */
@Entity
@Table(name = "service_taxonomies_details_fields_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceTaxonomiesDetailsFieldsValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "service_taxonomies_details_field", nullable = false)
    private ServiceTaxonomiesDetailsFields serviceTaxonomiesDetailsField;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServiceTaxonomiesDetailsFields getServiceTaxonomiesDetailsField() {
        return serviceTaxonomiesDetailsField;
    }

    public ServiceTaxonomiesDetailsFieldsValue serviceTaxonomiesDetailsField(
        ServiceTaxonomiesDetailsFields serviceTaxonomiesDetailsField
    ) {
        this.serviceTaxonomiesDetailsField = serviceTaxonomiesDetailsField;
        return this;
    }

    public void setServiceTaxonomiesDetailsField(ServiceTaxonomiesDetailsFields serviceTaxonomiesDetailsField) {
        this.serviceTaxonomiesDetailsField = serviceTaxonomiesDetailsField;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceTaxonomiesDetailsFieldsValue)) {
            return false;
        }
        return id != null && id.equals(((ServiceTaxonomiesDetailsFieldsValue) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceTaxonomiesDetailsFieldsValue{" +
            "id=" + getId() +
            ", serviceTaxonomiesDetailsField='" + getServiceTaxonomiesDetailsField() + "'" +
            "}";
    }
}
