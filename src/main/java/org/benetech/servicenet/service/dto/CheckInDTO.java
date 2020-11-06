package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.util.IdentifierUtils;

/**
 * A DTO for the CheckIn.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String beneficiaryId;

    private String phoneNumber;

    private UUID cboId;

    private UUID locationId;

    public void setBeneficiaryId(Integer beneficiaryId) {
        this.beneficiaryId = IdentifierUtils.toBase36(beneficiaryId);
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }
}
