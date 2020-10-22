package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.domain.Referral;

/**
 * A DTO for the CheckIn.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralMadeToUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID orgId;

    private String orgName;

    private ZonedDateTime fulfilledAt;

    public String getStatus() {
        if (this.fulfilledAt != null) {
            return Referral.ARRIVED;
        }
        return Referral.WAITING;
    }

    @Override
    public String toString() {
        return "ReferralMadeToUserDTO{" +
            "orgId=" + getOrgId() +
            "orgName=" + getOrgName() +
            "fulfilledAt=" + getFulfilledAt() +
            "}";
    }
}
