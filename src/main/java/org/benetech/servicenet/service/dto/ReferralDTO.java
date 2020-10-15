package org.benetech.servicenet.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.Referral;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.Referral} entity.
 */
public class ReferralDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String shortcode;

    private ZonedDateTime sentAt;

    private ZonedDateTime fulfilledAt;

    private UUID fromId;

    private String fromName;

    private UUID toId;

    private String toName;

    private UUID beneficiaryId;

    private String beneficiaryPhoneNumber;

    public String getBeneficiaryPhoneNumber() {
        return beneficiaryPhoneNumber;
    }

    public void setBeneficiaryPhoneNumber(String beneficiaryPhoneNumber) {
        this.beneficiaryPhoneNumber = beneficiaryPhoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public ZonedDateTime getFulfilledAt() {
        return fulfilledAt;
    }

    public void setFulfilledAt(ZonedDateTime fulfilledAt) {
        this.fulfilledAt = fulfilledAt;
    }

    public UUID getFromId() {
        return fromId;
    }

    public void setFromId(UUID organizationId) {
        this.fromId = organizationId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String organizationName) {
        this.fromName = organizationName;
    }

    public UUID getToId() {
        return toId;
    }

    public void setToId(UUID organizationId) {
        this.toId = organizationId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String organizationName) {
        this.toName = organizationName;
    }

    public UUID getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(UUID beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getStatus() {
        if (this.fulfilledAt != null) {
            return Referral.FULFILLED;
        }
        return Referral.SENT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReferralDTO referralDTO = (ReferralDTO) o;
        if (referralDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), referralDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReferralDTO{" +
            "id=" + getId() +
            ", shortcode='" + getShortcode() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", fulfilledAt='" + getFulfilledAt() + "'" +
            ", fromId=" + getFromId() +
            ", fromName='" + getFromName() + "'" +
            ", toId=" + getToId() +
            ", toName='" + getToName() + "'" +
            ", beneficiaryId=" + getBeneficiaryId() +
            "}";
    }
}
