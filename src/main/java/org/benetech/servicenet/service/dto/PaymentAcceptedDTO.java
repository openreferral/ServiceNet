package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PaymentAccepted entity.
 */
public class PaymentAcceptedDTO implements Serializable {

    private Long id;

    private String payment;

    private Long srvcId;

    private String srvcName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Long getSrvcId() {
        return srvcId;
    }

    public void setSrvcId(Long serviceId) {
        this.srvcId = serviceId;
    }

    public String getSrvcName() {
        return srvcName;
    }

    public void setSrvcName(String serviceName) {
        this.srvcName = serviceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentAcceptedDTO paymentAcceptedDTO = (PaymentAcceptedDTO) o;
        if (paymentAcceptedDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), paymentAcceptedDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PaymentAcceptedDTO{" +
            "id=" + getId() +
            ", payment='" + getPayment() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            "}";
    }
}
