package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PaymentAccepted.
 */
@Entity
@Table(name = "payment_accepted")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PaymentAccepted extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "payment")
    private String payment;

    @ManyToOne
    @JsonIgnoreProperties("paymentsAccepteds")
    private Service srvc;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public PaymentAccepted payment(String payment) {
        this.payment = payment;
        return this;
    }

    public Service getSrvc() {
        return srvc;
    }

    public void setSrvc(Service service) {
        this.srvc = service;
    }

    public PaymentAccepted srvc(Service service) {
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
        PaymentAccepted paymentAccepted = (PaymentAccepted) o;
        if (paymentAccepted.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentAccepted.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentAccepted{" +
            "id=" + getId() +
            ", payment='" + getPayment() + "'" +
            "}";
    }
}
