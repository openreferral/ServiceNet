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
public class CheckInsAndReferralsMadeForRecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID orgId;

    private Integer checkInsCount;

    private Integer referralsToCount;

    private Integer referralsFromCount;

    @Override
    public String toString() {
        return "CheckInsAndReferralsMadeForRecordDTO{" +
            "orgId=" + getOrgId() +
            ", checkInsCount=" + getCheckInsCount().toString() +
            ", referralsToCount=" + getReferralsToCount().toString() +
            ", referralsFromCount=" + getReferralsFromCount().toString() +
            "}";
    }
}
