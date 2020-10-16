package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the CheckIn.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferralMadeFromUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID orgId;

    private String orgName;

    private Long referralCount;

    @Override
    public String toString() {
        return "ReferralMadeFromUserDTO{" +
            "orgId=" + getOrgId() +
            "orgName=" + getOrgName() +
            ", referralCount=" + getReferralCount().toString() +
            "}";
    }
}
