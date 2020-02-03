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

import org.benetech.servicenet.domain.enumeration.ServiceFields;
import org.hibernate.annotations.GenericGenerator;

/**
 * A ServiceFieldsValue.
 */
@Entity
@Table(name = "service_fields_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceFieldsValue implements Serializable {

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
    @Column(name = "service_field", nullable = false)
    private ServiceFields serviceField;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServiceFields getServiceField() {
        return serviceField;
    }

    public ServiceFieldsValue serviceField(ServiceFields serviceField) {
        this.serviceField = serviceField;
        return this;
    }

    public void setServiceField(ServiceFields serviceField) {
        this.serviceField = serviceField;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceFieldsValue)) {
            return false;
        }
        return id != null && id.equals(((ServiceFieldsValue) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServiceFieldsValue{" +
            "id=" + getId() +
            ", serviceField='" + getServiceField() + "'" +
            "}";
    }
}
