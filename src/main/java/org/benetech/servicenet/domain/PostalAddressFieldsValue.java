package org.benetech.servicenet.domain;

import com.sun.istack.NotNull;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import org.benetech.servicenet.domain.enumeration.PostalAddressFields;
import org.hibernate.annotations.GenericGenerator;

/**
 * A PostalAddressFieldsValue.
 */
@Entity
@Table(name = "postal_address_fields_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostalAddressFieldsValue implements Serializable {

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
    @Column(name = "postal_address_field", nullable = false)
    private PostalAddressFields postalAddressField;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PostalAddressFields getPostalAddressField() {
        return postalAddressField;
    }

    public PostalAddressFieldsValue postalAddressField(PostalAddressFields postalAddressField) {
        this.postalAddressField = postalAddressField;
        return this;
    }

    public void setPostalAddressField(PostalAddressFields postalAddressField) {
        this.postalAddressField = postalAddressField;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostalAddressFieldsValue)) {
            return false;
        }
        return id != null && id.equals(((PostalAddressFieldsValue) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PostalAddressFieldsValue{" +
            "id=" + getId() +
            ", postalAddressField='" + getPostalAddressField() + "'" +
            "}";
    }
}
